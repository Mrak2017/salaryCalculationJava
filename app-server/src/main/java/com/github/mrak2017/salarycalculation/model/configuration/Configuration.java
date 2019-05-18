package com.github.mrak2017.salarycalculation.model.configuration;

import com.github.mrak2017.salarycalculation.model.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Stored system settings
 */
@Entity
@Table(name = "sc_configuration")
public class Configuration extends BaseEntity {

    @Column(unique = true)
    @NotNull
    private String code;

    @Column
    @NotNull
    private String value;

    @Column
    private String description;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
