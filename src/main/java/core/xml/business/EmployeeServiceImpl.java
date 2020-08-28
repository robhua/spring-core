package core.xml.business;

import java.util.List;

import core.xml.data.EmployeeDAO;
import core.xml.model.Employee;

public class EmployeeServiceImpl implements EmployeeService {
	// the EmployeeServiceImpl has a dependency on the EmployeeDAO
	private EmployeeDAO employeeDAO;

	@Override
	public int save(Employee emp) {
		// TODO Auto-generated method stub
		return 0;
	}

	// a setter method so that the Spring container can inject a EmployeeDAO
	public void setEmployeeDAO(EmployeeDAO employeeDAO) {
		this.employeeDAO = employeeDAO;
	}

	@Override
	public List<Employee> findAllEmployees() {
		return employeeDAO.findAllEmployees();
	}
}
