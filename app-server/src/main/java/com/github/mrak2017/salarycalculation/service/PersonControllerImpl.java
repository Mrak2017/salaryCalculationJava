package com.github.mrak2017.salarycalculation.service;

import com.github.mrak2017.salarycalculation.controller.dto.Person2GroupDTO;
import com.github.mrak2017.salarycalculation.controller.dto.PersonDTO;
import com.github.mrak2017.salarycalculation.controller.dto.PersonJournalDTO;
import com.github.mrak2017.salarycalculation.core.Exception.ResourceNotFoundException;
import com.github.mrak2017.salarycalculation.core.Exception.UserErrorTemplate;
import com.github.mrak2017.salarycalculation.model.person.GroupType;
import com.github.mrak2017.salarycalculation.model.person.OrganizationStructure;
import com.github.mrak2017.salarycalculation.model.person.Person;
import com.github.mrak2017.salarycalculation.model.person.Person2Group;
import com.github.mrak2017.salarycalculation.repository.PersonRepository;
import com.github.mrak2017.salarycalculation.repository.orgStructure.OrganizationStructureRepository;
import com.github.mrak2017.salarycalculation.repository.person2group.Person2GroupRepository;
import com.github.mrak2017.salarycalculation.utils.CheckUtil;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PersonControllerImpl implements PersonController {

    private final SalaryCalculator calculator;

    private final PersonRepository repository;

    private final Person2GroupRepository groupRepository;

    private final OrganizationStructureRepository orgStructureRep;

    PersonControllerImpl(PersonRepository repository, Person2GroupRepository groupRepository,
                         OrganizationStructureRepository orgStructureRep, SalaryCalculator calculator) {
        this.repository = repository;
        this.groupRepository = groupRepository;
        this.orgStructureRep = orgStructureRep;
        this.calculator = calculator;
    }

    @Override
    public List<Person> findAll(String search) {
        return repository.findByLastNameContainingIgnoreCase(search);
    }

    @Override
    public Optional<Person2Group> getCurrentGroup(Person person) {
        return getGroupOnDate(person, LocalDate.now());
    }

    @Override
    public Optional<Person2Group> getGroupOnDate(Person person, LocalDate date) {
        return groupRepository.getPersonGroupOnDate(person, date);
    }

    @Override
    public BigDecimal getCurrentSalary(Person person) {
        return calculator.getSalaryOnDate(person, LocalDate.now());
    }

    @Override
    public Long create(PersonJournalDTO dto) {
        Person person = new Person();
        person.setFirstName(dto.firstName);
        person.setLastName(dto.lastName);
        person.setFirstDate(dto.startDate);
        person.setBaseSalaryPart(dto.baseSalaryPart);
        repository.save(person);

        Person2Group group = new Person2Group();
        group.setPerson(person);
        group.setPeriodStart(dto.startDate);
        group.setGroupType(dto.currentGroup);
        groupRepository.save(group);

        OrganizationStructure structure = new OrganizationStructure();
        structure.setPerson(person);
        orgStructureRep.save(structure);

        return person.getId();
    }

    @Override
    public Optional<Person> find(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Person> getFirstLevelSubordinates(Person person) {
        OrganizationStructure orgStructure = orgStructureRep.findByPerson(person);
        CheckUtil.AssertNotNull(orgStructure, "Unable to find hierarchy unit for Person with id:" + person.getId());
        return Optional.ofNullable(orgStructure.getSubordinates())
                .orElseGet(Collections::emptyList)
                .stream()
                .map(OrganizationStructure::getPerson)
                .collect(Collectors.toList());
    }

    @Override
    public List<Person2Group> getAllGroups(Person person) {
        return groupRepository.findByPersonOrderByPeriodStartAsc(person);
    }

    @Override
    public Optional<Person> getCurrentChief(Person person) {
        OrganizationStructure parentOS = orgStructureRep.findByPerson(person).getParentStructure();
        if (parentOS != null) {
            return Optional.of(parentOS.getPerson());
        }
        return Optional.empty();
    }

    /**
     * Possible chiefs - all persons except:
     * 1) Person itself
     * 2) Person without group at this moment
     * 3) Person group is different from Manager, Salesman
     * 4) Person's subordinates of any level
     * 5) Person's current chief
     */
    @Override
    public List<Person> getPossibleChiefs(Person person) {
        List<Person> personsToExclude = new ArrayList<>();
        personsToExclude.add(person);
        getCurrentChief(person).ifPresent(personsToExclude::add);

        List<Person> allSubordinates = getAllSubordinates(person);
        personsToExclude.addAll(allSubordinates);

        return groupRepository.getPersonListWithCurrentGroupExists(personsToExclude,
                Arrays.asList(GroupType.Manager, GroupType.Salesman));
    }

    @Override
    public List<Person> getAllSubordinates(Person person) {
        OrganizationStructure organizationStructure = orgStructureRep.findByPerson(person);
        return orgStructureRep.findAllByPath(ensureGetMaterializedPath(organizationStructure))
                .stream()
                .map(OrganizationStructure::getPerson)
                .collect(Collectors.toList());
    }

    /**
     * Possible subordinates - all persons except:
     * 1) Person itself
     * 2) Person without group at this moment
     * 3) First level subordinates
     * 4) Person's chiefs of any level of hierarchy
     */
    @Override
    public List<Person> getPossibleSubordinates(Person person) {
        List<Person> personsToExclude = new ArrayList<>();
        personsToExclude.add(person);

        List<Person> firstLevelSubordinates = getFirstLevelSubordinates(person);
        personsToExclude.addAll(firstLevelSubordinates);

        List<Person> allChiefs = getAllChiefsList(person, new ArrayList<>());
        personsToExclude.addAll(allChiefs);

        return groupRepository.getPersonListWithCurrentGroupExists(personsToExclude, Arrays.asList(GroupType.values()));
    }

    private List<Person> getAllChiefsList(Person person, List<Person> result) {
        OrganizationStructure os = orgStructureRep.findByPerson(person);
        OrganizationStructure parentOS = os.getParentStructure();
        if (parentOS != null) {
            result.add(parentOS.getPerson());
            return getAllChiefsList(parentOS.getPerson(), result);
        }

        return result;
    }

    @Override
    public Person2Group getGroupById(Long id) {
        return groupRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public void deleteGroup(Long id) {
        groupRepository.deleteById(id);
    }

    @Override
    public void updatePerson(PersonDTO dto) {
        Person person = repository.findById(dto.id).orElseThrow(ResourceNotFoundException::new);
        person.setFirstName(dto.firstName);
        person.setLastName(dto.lastName);
        person.setFirstDate(dto.startDate);
        person.setLastDate(dto.endDate);
        person.setBaseSalaryPart(dto.baseSalaryPart);
        repository.save(person);
    }

    @Override
    public Long addGroup(Long id, Person2GroupDTO dto) {
        Person person = find(id).orElseThrow(ResourceNotFoundException::new);
        Person2Group group = new Person2Group();
        group.setPerson(person);
        group.setPeriodStart(dto.periodStart);
        group.setPeriodEnd(dto.periodEnd);
        group.setGroupType(dto.groupType);
        checkGroupBeforeSave(person, group);
        groupRepository.save(group);

        return group.getId();
    }

    @Override
    public void updateGroup(Person2GroupDTO dto) {
        Person2Group group = groupRepository.findById(dto.id).orElseThrow(ResourceNotFoundException::new);
        group.setPeriodStart(dto.periodStart);
        group.setPeriodEnd(dto.periodEnd);
        group.setGroupType(dto.groupType);
        checkGroupBeforeSave(group.getPerson(), group);
        groupRepository.save(group);
    }

    @Override
    public void updateChief(Long id, Long chiefId) {
        Person person = find(id).orElseThrow(ResourceNotFoundException::new);
        if (chiefId > 0) {
            Person chief = find(chiefId).orElseThrow(ResourceNotFoundException::new);
            setNewChief(person, chief);
        } else {
            clearChief(person);
        }
    }

    private void setNewChief(Person person, Person chief) {
        OrganizationStructure personOS = orgStructureRep.findByPerson(person);
        OrganizationStructure chiefOS = orgStructureRep.findByPerson(chief);

        checkChiefBeforeSetNew(personOS, chiefOS);

        personOS.setParentStructure(chiefOS);
        orgStructureRep.save(personOS);

        recalculateMatPathDownRecursive(personOS);
    }

    private void checkChiefBeforeSetNew(OrganizationStructure personOS, OrganizationStructure chiefOS) {
        checkPersonInChiefHierarchy(personOS, chiefOS);
        checkPersonHasGroup(personOS, chiefOS);
        checkNewChiefGroupIsValid(personOS, chiefOS);
    }

    private void checkPersonInChiefHierarchy(OrganizationStructure personOS, OrganizationStructure chiefOS) {
        if (chiefOS.getMaterializedPath() != null
                && chiefOS.getMaterializedPath().contains(personOS.getPerson().getId().toString())) {
            String hierarchyContainsPersonError = String.format(
                    "Unable to set chief '%s' for person '%s'. Chief hierarchy contains person as chief.",
                    chiefOS.getPerson().getId(), personOS.getPerson().getId());
            throw new ValidationException(hierarchyContainsPersonError);
        }
    }

    private void checkPersonHasGroup(OrganizationStructure personOS, OrganizationStructure chiefOS) {
        if (!getCurrentGroup(personOS.getPerson()).isPresent()) {
            String noGroupError = String.format(
                    "Unable to set chief '%s' for person '%s'. Person must be in any group.",
                    chiefOS.getPerson().getId(), personOS.getPerson().getId());
            throw new ValidationException(noGroupError);
        }
    }

    private void checkNewChiefGroupIsValid(OrganizationStructure personOS, OrganizationStructure chiefOS) {
        Optional<Person2Group> group = getCurrentGroup(chiefOS.getPerson());
        if (!group.isPresent()
                || (!group.get().getGroupType().equals(GroupType.Manager)
                && !group.get().getGroupType().equals(GroupType.Salesman))
        ) {
            String invalidChiefGroupError = String.format(
                    "Unable to set chief '%s' for person '%s'. Chief group must be one of '%s' or '%s'.",
                    chiefOS.getPerson().getId(), personOS.getPerson().getId(),
                    GroupType.Manager.name(), GroupType.Salesman.name());
            throw new ValidationException(invalidChiefGroupError);
        }
    }

    private void recalculateMatPathDownRecursive(OrganizationStructure orgStructure) {
        OrganizationStructure parentOS = orgStructure.getParentStructure();
        if (parentOS != null) {
            String newMatPath = ensureGetMaterializedPath(parentOS);
            orgStructure.setMaterializedPath(newMatPath);
        } else {
            orgStructure.setMaterializedPath(null);
        }
        orgStructureRep.save(orgStructure);

        List<OrganizationStructure> firstLevel = orgStructureRep.findByParentStructure(orgStructure);
        for (OrganizationStructure child : firstLevel) {
            recalculateMatPathDownRecursive(child);
        }
    }

    private String ensureGetMaterializedPath(OrganizationStructure os) {
        if (os.getMaterializedPath() != null) {
            String personId = os.getPerson().getId().toString();
            return String.join(OrganizationStructure.MAT_PATH_DELIMITER, os.getMaterializedPath(), personId);
        } else {
            return os.getPerson().getId().toString();
        }
    }

    private void clearChief(Person person) {
        OrganizationStructure personOS = orgStructureRep.findByPerson(person);
        personOS.setParentStructure(null);
        orgStructureRep.save(personOS);

        recalculateMatPathDownRecursive(personOS);
    }

    private void checkGroupBeforeSave(Person person, Person2Group p2g) {
        List<Person2Group> existingGroups = groupRepository.getExistingGroups(person, p2g);
        if (existingGroups.size() > 0) {
            String identifiers = existingGroups.stream()
                    .map(Person2Group::getId)
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));
            String message = String.format(UserErrorTemplate.MORE_THAN_ONE_GROUP_ON_DATE_RANGE.getTemplate(),
                    p2g.getPeriodStart().toString(), p2g.getPeriodEnd().toString(), identifiers);
            throw new ValidationException(message);
        }
    }
}
