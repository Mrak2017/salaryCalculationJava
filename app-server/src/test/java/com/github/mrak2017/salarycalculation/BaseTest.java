package com.github.mrak2017.salarycalculation;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.io.IOException;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BaseTest {

	@Autowired
	protected MockMvc mockMvc;

	@Autowired
	protected ObjectMapper objectMapper;

	protected String getStringUUID() {
		return UUID.randomUUID().toString();
	}

	protected <T> T getResult(MockHttpServletRequestBuilder request, TypeReference typeKey) throws Exception {
		String resultString = mockMvc.perform(request)
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();
		return objectMapper.readValue(resultString, typeKey);
	}
}
