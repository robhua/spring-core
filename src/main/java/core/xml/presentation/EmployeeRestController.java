package core.xml.presentation;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import core.xml.business.EmployeeService;
import core.xml.model.Employee;

@RestController
@RequestMapping("/api/employees")
public class EmployeeRestController {
	private final EmployeeService employeeService;

	public EmployeeRestController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	@GetMapping
	public List<Employee> findAll() {
		return employeeService.findAllEmployees();
	}

	@GetMapping("/{id}")
	public Employee findById(@PathVariable int id) {
		return employeeService.findById(id).orElseThrow(() -> notFound(id));
	}

	@PostMapping
	public ResponseEntity<Employee> create(@Valid @RequestBody Employee employee) {
		Employee saved = employeeService.save(employee);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(saved.getEmpId())
				.toUri();
		return ResponseEntity.created(location).body(saved);
	}

	@PutMapping("/{id}")
	public Employee update(@PathVariable int id, @Valid @RequestBody Employee employee) {
		if (employeeService.findById(id).isEmpty()) {
			throw notFound(id);
		}
		employee.setEmpId(id);
		if (!employeeService.update(employee)) {
			throw notFound(id);
		}
		return employee;
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable int id) {
		if (!employeeService.deleteById(id)) {
			throw notFound(id);
		}
		return ResponseEntity.noContent().build();
	}

	private ResponseStatusException notFound(int id) {
		return new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy nhân viên " + id);
	}
}