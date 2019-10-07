package com.github.mrak2017.salarycalculation.repository.person2group;

import com.github.mrak2017.salarycalculation.model.person.GroupType;
import com.github.mrak2017.salarycalculation.model.person.Person;
import com.github.mrak2017.salarycalculation.model.person.Person2Group;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Person2GroupRepositoryImpl implements Person2GroupRepositoryCustom {

	@PersistenceContext
	private EntityManager em;

	@Override
	public Optional<Person2Group> getPersonGroupOnDate(Person person, LocalDate onDate) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Person2Group> query = cb.createQuery(Person2Group.class);

		Root<Person2Group> root = query.from(Person2Group.class);
		List<Predicate> predicates = new ArrayList<>();

		predicates.add(cb.equal(root.get("person"), person));
		predicates.add(cb.lessThanOrEqualTo(root.get("periodStart"), onDate));
		predicates.add(cb.or(
				cb.isNull(root.get("periodEnd")),
				cb.greaterThanOrEqualTo(root.get("periodEnd"), onDate)
		));

		query.where(predicates.toArray(new Predicate[0]));
		query.orderBy(cb.desc(root.get("id")));
		List<Person2Group> list = em.createQuery(query)
										  .setFirstResult(0)
										  .setMaxResults(1)
										  .getResultList();
		return list.size() > 0 ? Optional.of(list.get(0)) : Optional.empty();
	}

	@Override
	public List<Person2Group> getExistingGroups(Person person, Person2Group p2g) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Person2Group> query = cb.createQuery(Person2Group.class);

		Root<Person2Group> root = query.from(Person2Group.class);
		List<Predicate> predicates = new ArrayList<>();

		predicates.add(cb.equal(root.get("person"), person));
		if (p2g.getId() != null) {
			predicates.add(cb.not(cb.equal(root.get("id"), p2g.getId())));
		}
		predicates.add(cb.lessThanOrEqualTo(root.get("periodStart"), p2g.getPeriodEnd()));
		predicates.add(cb.or(
				cb.isNull(root.get("periodEnd")),
				cb.greaterThanOrEqualTo(root.get("periodEnd"), p2g.getPeriodStart())
		));

		query.where(predicates.toArray(new Predicate[0]));
		return em.createQuery(query).getResultList();
	}

	/** Possible chiefs - all persons except:
	 * 1) Person itself
	 * 2) Person without group at this moment
	 * 3) Person group is different from Manager, Salesman
	 * 4) Person's subordinates of any level (subordinates removes in class PersonControllerImpl method getPossibleChiefs)
	 * 5) Person's current chief
	 * */
	@Override
	public List<Person> getPossibleChiefs(Person person, Optional<Person> currentChief) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Person> query = cb.createQuery(Person.class);

		Root<Person2Group> root = query.from(Person2Group.class);
		List<Predicate> predicates = new ArrayList<>();

		// Exclude person itself
		predicates.add(cb.notEqual(root.get("person").get("id"), person.getId()));

		// Only person with active group at this moment
		predicates.add(cb.lessThanOrEqualTo(root.get("periodStart"), LocalDate.now()));
		predicates.add(cb.or(
				cb.isNull(root.get("periodEnd")),
				cb.greaterThanOrEqualTo(root.get("periodEnd"), LocalDate.now())
		));

		// Only person with group Manager or Salesman
		predicates.add(root.get("groupType").in( Arrays.asList(GroupType.Manager, GroupType.Salesman)));

		// Exclude current chief
		currentChief.ifPresent(value -> predicates.add(cb.notEqual(root.get("person").get("id"), value.getId())));

		query.where(predicates.toArray(new Predicate[0]));
		query.distinct(true);
		query.select(root.get("person"));

		return em.createQuery(query).getResultList();
	}
}
