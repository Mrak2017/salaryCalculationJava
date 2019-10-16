package com.github.mrak2017.salarycalculation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mrak2017.salarycalculation.controller.dto.PersonJournalDTO;
import com.github.mrak2017.salarycalculation.model.person.GroupType;
import com.github.mrak2017.salarycalculation.model.person.Person;
import com.github.mrak2017.salarycalculation.repository.orgStructure.OrganizationStructureRepository;
import com.github.mrak2017.salarycalculation.service.PersonController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment  = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class BaseIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected PersonController controller;

    @Autowired
    protected OrganizationStructureRepository orgStructureRepository;

    protected String getStringUUID() {
        return UUID.randomUUID().toString();
    }

    protected <T> T getResult(MockHttpServletRequestBuilder request, Class<T> typeKey) throws Exception {
        String resultString = getResultString(request);
        return objectMapper.readValue(resultString, typeKey);
    }

    protected <T> List<T> getResultList(MockHttpServletRequestBuilder request, Class<T> typeKey) throws Exception {
        String resultString = getResultString(request);
        return objectMapper.readValue(resultString,
                objectMapper.getTypeFactory().constructCollectionType(List.class, typeKey));
    }

    private String getResultString(MockHttpServletRequestBuilder request) throws Exception {
        return mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    protected Person createEmployee() {
        return createEmployee(new BigDecimal(100), LocalDate.now());
    }

    protected Person createEmployee(BigDecimal baseSalaryPart, LocalDate startDate) {
        PersonJournalDTO dtoEmployee = new PersonJournalDTO();
        dtoEmployee.firstName = getStringUUID();
        dtoEmployee.lastName = getStringUUID();
        dtoEmployee.baseSalaryPart = baseSalaryPart;
        dtoEmployee.startDate = startDate;
        dtoEmployee.currentGroup = GroupType.Employee;

        Long id = controller.create(dtoEmployee);
        Person result = controller.find(id).orElse(null);
        assertNotNull(result);
        return result;
    }

    protected Person createManager() {
        return createManager(new BigDecimal(100), LocalDate.now());
    }

    protected Person createManager(BigDecimal baseSalaryPart, LocalDate startDate) {
        PersonJournalDTO dtoManager = new PersonJournalDTO();
        dtoManager.firstName = getStringUUID();
        dtoManager.lastName = getStringUUID();
        dtoManager.baseSalaryPart = baseSalaryPart;
        dtoManager.startDate = startDate;
        dtoManager.currentGroup = GroupType.Manager;

        Long id = controller.create(dtoManager);
        Person result = controller.find(id).orElse(null);
        assertNotNull(result);
        return result;
    }

    protected Person createSalesman() {
        return createSalesman(new BigDecimal(100), LocalDate.now());
    }

    protected Person createSalesman(BigDecimal baseSalaryPart, LocalDate startDate) {
        PersonJournalDTO dtoSalesman = new PersonJournalDTO();
        dtoSalesman.firstName = getStringUUID();
        dtoSalesman.lastName = getStringUUID();
        dtoSalesman.baseSalaryPart = baseSalaryPart;
        dtoSalesman.startDate = startDate;
        dtoSalesman.currentGroup = GroupType.Salesman;

        Long id = controller.create(dtoSalesman);
        Person result = controller.find(id).orElse(null);
        assertNotNull(result);
        return result;
    }
}
