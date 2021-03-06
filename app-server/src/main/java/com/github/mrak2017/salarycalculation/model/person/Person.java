package com.github.mrak2017.salarycalculation.model.person;

import com.github.mrak2017.salarycalculation.model.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

/**
 * Class contains main information about employee
 */
@Entity
@Table(name = "sc_person")
public class Person extends BaseEntity {

    @Column(name = "first_name")
    @Size(max = 100)
    @NotNull
    private String firstName;

    @Column(name = "last_name")
    @Size(max = 100)
    @NotNull
    private String lastName;

    /**
     * of employment
     */
    @Column(name = "first_date")
    @NotNull
    private LocalDate firstDate;

    /**
     * of employment
     */
    @Column(name = "last_date")
    private LocalDate lastDate;

    /**
     * per month
     */
    @Column(name = "base_salary_part")
    @Digits(integer = 10, fraction = 4)
    private BigDecimal baseSalaryPart;

    @OneToMany(mappedBy = "person")
    private Set<Person2Group> groups;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public LocalDate getFirstDate() {
        return firstDate;
    }

    public void setFirstDate(LocalDate firstDate) {
        this.firstDate = firstDate;
    }

    public LocalDate getLastDate() {
        return lastDate;
    }

    public void setLastDate(LocalDate lastDate) {
        this.lastDate = lastDate;
    }

    public BigDecimal getBaseSalaryPart() {
        return baseSalaryPart;
    }

    public void setBaseSalaryPart(BigDecimal baseSalaryPart) {
        this.baseSalaryPart = baseSalaryPart;
    }
}
