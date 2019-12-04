package com.github.mrak2017.salarycalculation.model.person;

public enum GroupType {
    Employee("EmployeeWorkExperienceRatio", "EmployeeMaxWorkExperienceRatio", null),
    Manager("ManagerWorkExperienceRatio", "ManagerMaxWorkExperienceRatio", "ManagerSubordinatesRatio"),
    Salesman("SalesmanWorkExperienceRatio", "SalesmanMaxWorkExperienceRatio", "SalesmanSubordinatesRatio");

    public final String workExperienceRatioSetting;
    public final String maxWorkExperienceRatioSetting;
    public final String subordinatesRatioSetting;

    GroupType(String workExpRatio, String maxWorkExpRatio, String subRatioSetting) {
        this.workExperienceRatioSetting = workExpRatio;
        this.maxWorkExperienceRatioSetting = maxWorkExpRatio;
        this.subordinatesRatioSetting = subRatioSetting;
    }
}
