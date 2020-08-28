package core.xml.data;

import java.util.List;

import core.xml.model.Employee;

public interface EmployeeDAO {
	public int save(Employee employee);

	public void update(Employee employee);

	public void deleteEmpById(int empId);

	public List<Employee> findAllEmployees();
}