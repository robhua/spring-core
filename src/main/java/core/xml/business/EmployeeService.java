package core.xml.business;

import java.util.List;
import java.util.Optional;

import core.xml.model.Employee;

public interface EmployeeService {

	public Employee save(Employee emp);

	public List<Employee> findAllEmployees();

	public Optional<Employee> findById(int empId);

	public boolean update(Employee employee);

	public boolean deleteById(int empId);

}
