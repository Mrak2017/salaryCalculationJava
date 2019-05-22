package com.github.mrak2017.salarycalculation.repository.person2group;

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
}
