package core.xml.data;

import java.util.List;
import java.util.Optional;

import core.xml.model.Employee;

public interface EmployeeDAO {
	public Employee save(Employee employee);

	public Optional<Employee> findById(int empId);

	public int update(Employee employee);

	public int deleteById(int empId);

	public int deleteByIds(List<Integer> employeeIds);

	public List<Employee> findAllEmployees();

	public List<Employee> findEmployees(int limit, int offset);

	public long countEmployees();
}