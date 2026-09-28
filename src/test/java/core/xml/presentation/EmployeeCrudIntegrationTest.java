package core.xml.presentation;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = core.annotation.presentation.Main.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class EmployeeCrudIntegrationTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@BeforeEach
	public void clearEmployees() {
		jdbcTemplate.update("DELETE FROM employee");
	}

	@Test
	public void mvcScreensSupportEmployeeCrud() throws Exception {
		mockMvc.perform(get("/employees"))
				.andExpect(status().isOk())
				.andExpect(view().name("employees/list"));

		mockMvc.perform(post("/employees").param("empName", "Nguyễn Minh An").param("age", "32"))
				.andExpect(status().is3xxRedirection())
				.andExpect(header().string("Location", "/employees"));

		int employeeId = jdbcTemplate.queryForObject("SELECT id FROM employee", Integer.class);
		mockMvc.perform(get("/employees/{id}/edit", employeeId))
				.andExpect(status().isOk())
				.andExpect(view().name("employees/form"));

		mockMvc.perform(post("/employees/{id}", employeeId)
				.param("empName", "Nguyễn An")
				.param("age", "33"))
				.andExpect(status().is3xxRedirection());
		mockMvc.perform(get("/employees"))
				.andExpect(status().isOk())
				.andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.content()
						.string(containsString("Nguyễn An")));

		mockMvc.perform(post("/employees/{id}/delete", employeeId))
				.andExpect(status().is3xxRedirection());
		mockMvc.perform(get("/employees"))
				.andExpect(status().isOk())
				.andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.content()
						.string(containsString("Chưa có hồ sơ nào")));
	}

	@Test
	public void mvcEmployeeListPaginatesAndSortsByNameThenAge() throws Exception {
		jdbcTemplate.update("INSERT INTO employee (name, age) VALUES (?, ?)", "Beta Employee", 27);
		jdbcTemplate.update("INSERT INTO employee (name, age) VALUES (?, ?)", "Alpha Employee", 35);
		jdbcTemplate.update("INSERT INTO employee (name, age) VALUES (?, ?)", "Alpha Employee", 21);
		for (int index = 1; index <= 9; index++) {
			jdbcTemplate.update("INSERT INTO employee (name, age) VALUES (?, ?)", "Employee " + index, 20 + index);
		}

		MvcResult firstPageResult = mockMvc.perform(get("/employees").param("page", "0"))
				.andExpect(status().isOk())
				.andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.content()
						.string(containsString("12")))
				.andReturn();
		String firstPage = firstPageResult.getResponse().getContentAsString();
		int firstAlpha = firstPage.indexOf("Alpha Employee");
		int secondAlpha = firstPage.indexOf("Alpha Employee", firstAlpha + 1);
		int beta = firstPage.indexOf("Beta Employee");
		assertTrue(firstAlpha >= 0 && secondAlpha > firstAlpha && beta > secondAlpha);
		String firstAlphaRow = firstPage.substring(firstPage.lastIndexOf("<tr", firstAlpha), firstPage.indexOf("</tr>", firstAlpha));
		String secondAlphaRow = firstPage.substring(firstPage.lastIndexOf("<tr", secondAlpha), firstPage.indexOf("</tr>", secondAlpha));
		assertTrue(firstAlphaRow.contains(">21</span>"));
		assertTrue(secondAlphaRow.contains(">35</span>"));
		assertTrue(!firstPage.contains("Employee 9"));

		mockMvc.perform(get("/employees").param("page", "1"))
				.andExpect(status().isOk())
				.andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.content()
						.string(containsString("Employee 9")))
				.andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.content()
						.string(containsString("2 / 2")));
	}

	@Test
	public void restApiSupportsEmployeeCrudAndValidation() throws Exception {
		String employeeJson = "{\"empName\":\"Trần Bình\",\"age\":28}";
		mockMvc.perform(post("/api/employees").contentType(MediaType.APPLICATION_JSON).content(employeeJson))
				.andExpect(status().isCreated())
				.andExpect(header().exists("Location"))
				.andExpect(jsonPath("$.empId").isNumber());

		int employeeId = jdbcTemplate.queryForObject("SELECT id FROM employee", Integer.class);
		mockMvc.perform(get("/api/employees"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)));
		mockMvc.perform(get("/api/employees/{id}", employeeId))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.empName").value("Trần Bình"));

		mockMvc.perform(put("/api/employees/{id}", employeeId)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"empName\":\"Trần Bình An\",\"age\":29}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.empName").value("Trần Bình An"));
		mockMvc.perform(post("/api/employees")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"empName\":\" \",\"age\":-1}"))
				.andExpect(status().isBadRequest());

		mockMvc.perform(delete("/api/employees/{id}", employeeId))
				.andExpect(status().isNoContent());
		mockMvc.perform(get("/api/employees/{id}", employeeId))
				.andExpect(status().isNotFound());
	}
}