package com.github.mrak2017.salarycalculation;

import com.github.mrak2017.salarycalculation.model.person.Person;
import com.github.mrak2017.salarycalculation.service.PersonController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.event.EventListener;

import java.util.List;

@SpringBootApplication
public class Application extends SpringBootServletInitializer {

	@Autowired
	PersonController controller;

	public static void main(String args[]) {
		SpringApplication.run(Application.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void test() {
        /*List<Person> data = controller.getTestData();
        for (Person person : data) {
            System.out.println(String.format("firstName: %s lastName: %s", person.getFirstName(), person.getLastName()));
        }*/
	}
}