package com.github.mrak2017.salarycalculation.core.Exception;

public enum UserErrorTemplate {
	MORE_THAN_ONE_GROUP_ON_DATE_RANGE("У сотрудника не может быть больше 1 группы за период c '%s' по '%s'. Список идентификаторов: %s");

	private String template;

	UserErrorTemplate(String template) {
		this.template = template;
	}

	public String getTemplate() {
		return template;
	}
}
