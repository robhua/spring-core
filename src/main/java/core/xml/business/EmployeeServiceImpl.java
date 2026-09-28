package core.xml.business;

import java.util.List;
import java.util.Optional;

import core.xml.data.EmployeeDAO;
import core.xml.model.Employee;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {
	private final EmployeeDAO employeeDAO;

	public EmployeeServiceImpl(EmployeeDAO employeeDAO) {
		this.employeeDAO = employeeDAO;
	}

	@Override
	public Employee save(Employee employee) {
		return employeeDAO.save(employee);
	}

	@Override
	public List<Employee> findAllEmployees() {
		return employeeDAO.findAllEmployees();
	}

	@Override
	public List<Employee> findEmployees(int limit, int offset) {
		return employeeDAO.findEmployees(limit, offset);
	}

	@Override
	public long countEmployees() {
		return employeeDAO.countEmployees();
	}

	@Override
	public Optional<Employee> findById(int empId) {
		return employeeDAO.findById(empId);
	}

	@Override
	public boolean update(Employee employee) {
		return employeeDAO.update(employee) > 0;
	}

	@Override
	public boolean deleteById(int empId) {
		return employeeDAO.deleteById(empId) > 0;
	}

	@Override
	public int deleteByIds(List<Integer> employeeIds) {
		return employeeDAO.deleteByIds(employeeIds);
	}
}
