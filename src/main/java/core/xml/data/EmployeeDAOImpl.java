package core.xml.data;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import core.xml.model.Employee;
import core.xml.model.EmployeeMapper;

public class EmployeeDAOImpl implements EmployeeDAO{
	private static final String INSERT_QUERY = "INSERT INTO employee (name, age) values (?, ?)";
	private static final String SELECT_ALL_QUERY = "SELECT * FROM employee";
	
	// the EmployeeDAOImpl has a dependency on a JdbcTemplate
	private JdbcTemplate jdbcTemplate;

	// a constructor so that the Spring container can inject a JdbcTemplate
	public EmployeeDAOImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public int save(Employee employee) {
		return jdbcTemplate.update(INSERT_QUERY, employee.getEmpName(), employee.getAge());
	}

	@Override
	public void update(Employee employee) {
		// TODO update employee
		
	}

	@Override
	public void deleteEmpById(int empId) {
		// TODO delete employee by Id
		
	}

	@Override
	public List<Employee> findAllEmployees() {
	    return jdbcTemplate.query(SELECT_ALL_QUERY, new EmployeeMapper());
	}
}

