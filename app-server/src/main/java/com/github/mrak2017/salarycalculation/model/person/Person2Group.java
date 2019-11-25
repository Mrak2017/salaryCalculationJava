package com.github.mrak2017.salarycalculation.model.person;

import com.github.mrak2017.salarycalculation.model.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * Link person to specific group for period of time
 */
@Entity
@Table(name = "sc_person2group")
public class Person2Group extends BaseEntity {

    @ManyToOne
    @JoinColumn(name="person_id", nullable=false)
    @NotNull
    private Person person;

    @Column(name = "group_type")
    @Enumerated(EnumType.STRING)
    @NotNull
    private GroupType groupType;

    @Column(name = "period_start")
    @NotNull
    private LocalDate periodStart;

    @Column(name = "period_end")
    private LocalDate periodEnd;

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public GroupType getGroupType() {
        return groupType;
    }

    public void setGroupType(GroupType groupType) {
        this.groupType = groupType;
    }

    public LocalDate getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(LocalDate periodStart) {
        this.periodStart = periodStart;
    }

    public LocalDate getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(LocalDate periodEnd) {
        this.periodEnd = periodEnd;
    }
}
