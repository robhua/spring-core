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
import org.springframework.mock.web.MockHttpSession;
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
				.andExpect(view().name("employees/list"))
				.andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.content()
						.string(containsString("site-footer")));
		mockMvc.perform(post("/employees").param("empName", " ").param("age", "-1"))
				.andExpect(status().isOk())
				.andExpect(view().name("employees/form"))
				.andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.content()
						.string(containsString("site-footer")))
				.andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.content()
						.string(containsString("Tên nhân viên không được để trống")))
				.andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.content()
						.string(containsString("Tuổi không được là số âm")));

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
	public void mvcRequiresAgeForCreateAndUpdate() throws Exception {
		mockMvc.perform(post("/employees").param("empName", "Missing Age"))
				.andExpect(status().isOk())
				.andExpect(view().name("employees/form"))
				.andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.content()
						.string(containsString("Tuổi là bắt buộc")));
		assertTrue(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM employee", Integer.class) == 0);

		jdbcTemplate.update("INSERT INTO employee (name, age) VALUES (?, ?)", "Existing Employee", 42);
		int employeeId = jdbcTemplate.queryForObject("SELECT id FROM employee", Integer.class);
		mockMvc.perform(post("/employees/{id}", employeeId).param("empName", "Changed Name"))
				.andExpect(status().isOk())
				.andExpect(view().name("employees/form"))
				.andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.content()
						.string(containsString("Tuổi là bắt buộc")));
		assertTrue(jdbcTemplate.queryForObject("SELECT age FROM employee WHERE id = ?", Integer.class, employeeId) == 42);
	}

	@Test
	public void languageSwitchChangesMessagesAndPersistsInSession() throws Exception {
		jdbcTemplate.update("INSERT INTO employee (name, age) VALUES (?, ?)", "Locale Test Employee", 29);
		MvcResult englishResult = mockMvc.perform(get("/employees").param("lang", "en"))
				.andExpect(status().isOk())
				.andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.content()
						.string(containsString("Employee directory")))
				.andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.content()
						.string(containsString("Select all on this page")))
				.andReturn();
		MockHttpSession localeSession = (MockHttpSession) englishResult.getRequest().getSession(false);
		assertTrue(localeSession != null);

		mockMvc.perform(get("/employees").session(localeSession))
				.andExpect(status().isOk())
				.andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.content()
						.string(containsString("Employee directory")));

		mockMvc.perform(get("/employees").session(localeSession).param("lang", "vi"))
				.andExpect(status().isOk())
				.andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.content()
						.string(containsString("Danh bạ nhân viên")));
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
		String alphaNameCell = "<span class=\"person-name\">Alpha Employee</span>";
		int firstAlpha = firstPage.indexOf(alphaNameCell);
		int secondAlpha = firstPage.indexOf(alphaNameCell, firstAlpha + alphaNameCell.length());
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
	public void mvcCanDeleteSeveralSelectedEmployees() throws Exception {
		jdbcTemplate.update("INSERT INTO employee (name, age) VALUES (?, ?)", "Remove One", 24);
		jdbcTemplate.update("INSERT INTO employee (name, age) VALUES (?, ?)", "Remove Two", 31);
		jdbcTemplate.update("INSERT INTO employee (name, age) VALUES (?, ?)", "Keep This", 42);
		Integer[] employeeIds = jdbcTemplate.queryForList("SELECT id FROM employee WHERE name LIKE 'Remove %'", Integer.class)
				.toArray(new Integer[0]);

		mockMvc.perform(post("/employees/delete").param("ids",
					java.util.Arrays.stream(employeeIds).map(String::valueOf).toArray(String[]::new)))
				.andExpect(status().is3xxRedirection());

		assertTrue(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM employee", Integer.class) == 1);
		assertTrue(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM employee WHERE name = 'Keep This'", Integer.class) == 1);
	}

	@Test
	public void mvcBulkDeleteWithoutSelectionKeepsEmployees() throws Exception {
		jdbcTemplate.update("INSERT INTO employee (name, age) VALUES (?, ?)", "Keep This", 42);

		mockMvc.perform(post("/employees/delete").param("page", "1"))
				.andExpect(status().is3xxRedirection())
				.andExpect(header().string("Location", "/employees?page=1"));

		assertTrue(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM employee", Integer.class) == 1);
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