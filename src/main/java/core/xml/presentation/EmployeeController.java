package core.xml.presentation;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import core.xml.business.EmployeeService;
import core.xml.model.Employee;

@Controller
@RequestMapping("/employees")
public class EmployeeController {
	private static final int PAGE_SIZE = 10;
	private static final int PAGE_WINDOW = 2;
	private final EmployeeService employeeService;

	public EmployeeController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	@GetMapping
	public String listEmployees(@RequestParam(defaultValue = "0") int page, Model model) {
		long totalEmployees = employeeService.countEmployees();
		int totalPages = (int) ((totalEmployees + PAGE_SIZE - 1) / PAGE_SIZE);
		int currentPage = Math.max(0, Math.min(page, Math.max(0, totalPages - 1)));
		int firstPage = Math.max(0, currentPage - PAGE_WINDOW);
		int lastPage = Math.min(totalPages - 1, currentPage + PAGE_WINDOW);

		model.addAttribute("employees", employeeService.findEmployees(PAGE_SIZE, currentPage * PAGE_SIZE));
		model.addAttribute("totalEmployees", totalEmployees);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("firstPage", firstPage);
		model.addAttribute("lastPage", lastPage);
		model.addAttribute("hasPrevious", currentPage > 0);
		model.addAttribute("hasNext", currentPage + 1 < totalPages);
		return "employees/list";
	}

	@GetMapping("/new")
	public String newEmployee(Model model) {
		return employeeForm(model, new Employee(), "/employees", "Thêm nhân viên", "Tạo hồ sơ");
	}

	@PostMapping
	public String createEmployee(@Valid @ModelAttribute("employee") Employee employee,
			BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			return employeeForm(model, employee, "/employees", "Thêm nhân viên", "Tạo hồ sơ");
		}

		employeeService.save(employee);
		redirectAttributes.addFlashAttribute("successMessage", "Đã thêm nhân viên.");
		return "redirect:/employees";
	}

	@GetMapping("/{id}/edit")
	public String editEmployee(@PathVariable int id, Model model) {
		Employee employee = employeeService.findById(id).orElseThrow(() -> notFound(id));
		return employeeForm(model, employee, "/employees/" + id, "Chỉnh sửa hồ sơ", "Lưu thay đổi");
	}

	@PostMapping("/{id}")
	public String updateEmployee(@PathVariable int id, @Valid @ModelAttribute("employee") Employee employee,
			BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
		if (employeeService.findById(id).isEmpty()) {
			throw notFound(id);
		}
		employee.setEmpId(id);
		if (bindingResult.hasErrors()) {
			return employeeForm(model, employee, "/employees/" + id, "Chỉnh sửa hồ sơ", "Lưu thay đổi");
		}
		if (!employeeService.update(employee)) {
			throw notFound(id);
		}

		redirectAttributes.addFlashAttribute("successMessage", "Đã cập nhật hồ sơ.");
		return "redirect:/employees";
	}

	@PostMapping("/{id}/delete")
	public String deleteEmployee(@PathVariable int id, @RequestParam(defaultValue = "0") int page,
			RedirectAttributes redirectAttributes) {
		if (!employeeService.deleteById(id)) {
			throw notFound(id);
		}
		redirectAttributes.addFlashAttribute("successMessage", "Đã xóa nhân viên.");
		return employeeListPage(page);
	}

	@PostMapping("/delete")
	public String deleteEmployees(@RequestParam(name = "ids", required = false) List<Integer> employeeIds,
			@RequestParam(defaultValue = "0") int page, RedirectAttributes redirectAttributes) {
		if (employeeIds == null || employeeIds.isEmpty()) {
			redirectAttributes.addFlashAttribute("warningMessage", "Chọn ít nhất một nhân viên để xóa.");
			return employeeListPage(page);
		}

		int deletedCount = employeeService.deleteByIds(employeeIds);
		redirectAttributes.addFlashAttribute("successMessage", "Đã xóa " + deletedCount + " nhân viên.");
		return employeeListPage(page);
	}

	private String employeeListPage(int page) {
		return "redirect:/employees?page=" + Math.max(page, 0);
	}

	private String employeeForm(Model model, Employee employee, String formAction, String title, String submitLabel) {
		model.addAttribute("employee", employee);
		model.addAttribute("formAction", formAction);
		model.addAttribute("pageTitle", title);
		model.addAttribute("submitLabel", submitLabel);
		return "employees/form";
	}

	private ResponseStatusException notFound(int id) {
		return new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy nhân viên " + id);
	}
}
