package com.example.courseFinder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CourseFinderApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void contextLoads() {
	}

	@Test
	void testSearchEndpointWithDirectUrl() throws Exception {
		String responseContent = mockMvc.perform(get("/api/search?q=Physics&page=0&size=5"))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(responseContent);

		int total = root.get("total").asInt();
		String title = root.get("courses").get(0).get("title").asText();

		// Check total is greater than 0
		assertThat(total).isGreaterThan(0);

		// Check that first course title contains "Physics"
		assertThat(title).contains("Physics");
	}
}
