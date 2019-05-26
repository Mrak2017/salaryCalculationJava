package com.github.mrak2017.salarycalculation.model.person;

import com.github.mrak2017.salarycalculation.model.BaseEntity;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Hierarchical structure of organization. <br/>
 * {@link #materializedPath} contains ordered ids of all chiefs for {@link #person}, from top to immediate.
 */
@Entity
@Table(name = "sc_organization_structure")
public class OrganizationStructure extends BaseEntity {

	public static String MAT_PATH_DELIMITER = ".";

	@ManyToOne
	@JoinColumn(name = "person_id", nullable = false)
	@NotNull
	private Person person;

	@Column(columnDefinition = "ltree")
	@Type(type = "com.github.mrak2017.salarycalculation.model.customtypes.LTreeType")
	private String materializedPath;

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public String getMaterializedPath() {
		return materializedPath;
	}

	public void setMaterializedPath(String materializedPath) {
		this.materializedPath = materializedPath;
	}
}
