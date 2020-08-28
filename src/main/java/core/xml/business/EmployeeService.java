package core.xml.business;

import java.util.List;

import core.xml.model.Employee;

public interface EmployeeService {

	public int save(Employee emp);

	public List<Employee> findAllEmployees();

}
