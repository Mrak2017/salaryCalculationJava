package com.github.mrak2017.salarycalculation.model.person;

import com.github.mrak2017.salarycalculation.model.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Hierarchical structure of organization. <br/>
 * {@link #materializedPath} contains ordered ids of all chiefs for {@link #person}, from top to immediate.
 */
@Entity
@Table(name = "sc_organization_structure")
public class OrganizationStructure extends BaseEntity {

	@ManyToOne
	@JoinColumn(name = "person_id", nullable = false)
	@NotNull
	private Person person;

	@ManyToOne(cascade = {CascadeType.ALL})
	@JoinColumn(name = "parent_id")
	private OrganizationStructure parent;

	@OneToMany(mappedBy = "parent")
	private Set<OrganizationStructure> subordinates = new HashSet<OrganizationStructure>();

	/*@Column
	private List<Long> materializedPath;*/

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public OrganizationStructure getParent() {
		return parent;
	}

	public void setParent(OrganizationStructure parent) {
		this.parent = parent;
	}

	/*public List<Long> getMaterializedPath() {
		return materializedPath;
	}

	public void setMaterializedPath(List<Long> materializedPath) {
		this.materializedPath = materializedPath;
	}*/
}
