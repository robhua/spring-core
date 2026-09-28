package core.xml.data;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import core.xml.model.Employee;
import core.xml.model.EmployeeMapper;

@Repository
public class EmployeeDAOImpl implements EmployeeDAO {
	private static final String INSERT_QUERY = "INSERT INTO employee (name, age) values (?, ?)";
	private static final String SORTED_EMPLOYEES_QUERY = "SELECT id, name, age FROM employee ORDER BY LOWER(name), age, id";
	private static final String SELECT_PAGE_QUERY = SORTED_EMPLOYEES_QUERY + " LIMIT ? OFFSET ?";
	private static final String SELECT_BY_ID_QUERY = "SELECT id, name, age FROM employee WHERE id = ?";
	private static final String UPDATE_QUERY = "UPDATE employee SET name = ?, age = ? WHERE id = ?";
	private static final String DELETE_QUERY = "DELETE FROM employee WHERE id = ?";
	private static final String COUNT_QUERY = "SELECT COUNT(*) FROM employee";
	
	// the EmployeeDAOImpl has a dependency on a JdbcTemplate
	private JdbcTemplate jdbcTemplate;

	// a constructor so that the Spring container can inject a JdbcTemplate
	public EmployeeDAOImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Employee save(Employee employee) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update((PreparedStatementCreator) connection -> {
			PreparedStatement statement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, employee.getEmpName());
			statement.setInt(2, employee.getAge());
			return statement;
		}, keyHolder);
		Number generatedId = keyHolder.getKey();
		if (generatedId == null) {
			throw new IllegalStateException("PostgreSQL did not return an employee ID");
		}
		employee.setEmpId(generatedId.intValue());
		return employee;
	}

	@Override
	public Optional<Employee> findById(int empId) {
		return jdbcTemplate.query(SELECT_BY_ID_QUERY, new EmployeeMapper(), empId)
				.stream()
				.findFirst();
	}

	@Override
	public int update(Employee employee) {
		return jdbcTemplate.update(UPDATE_QUERY, employee.getEmpName(), employee.getAge(), employee.getEmpId());
	}

	@Override
	public int deleteById(int empId) {
		return jdbcTemplate.update(DELETE_QUERY, empId);
	}

	@Override
	public List<Employee> findAllEmployees() {
		return jdbcTemplate.query(SORTED_EMPLOYEES_QUERY, new EmployeeMapper());
	}

	@Override
	public List<Employee> findEmployees(int limit, int offset) {
		return jdbcTemplate.query(SELECT_PAGE_QUERY, new EmployeeMapper(), limit, offset);
	}

	@Override
	public long countEmployees() {
		Long count = jdbcTemplate.queryForObject(COUNT_QUERY, Long.class);
		return count == null ? 0 : count;
	}
}

