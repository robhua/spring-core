package core.xml.presentation;

import jakarta.validation.Valid;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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
	private static final Logger log = LoggerFactory.getLogger(EmployeeController.class);
	private static final int PAGE_SIZE = 10;
	private static final int PAGE_WINDOW = 2;
	private static final int MAX_PAGE_INDEX = Integer.MAX_VALUE / PAGE_SIZE;
	private final EmployeeService employeeService;
	private final MessageSource messageSource;

	public EmployeeController(EmployeeService employeeService, MessageSource messageSource) {
		this.employeeService = employeeService;
		this.messageSource = messageSource;
	}

	@GetMapping
	public String listEmployees(@RequestParam(defaultValue = "0") int page, Model model) {
		long totalEmployees = employeeService.countEmployees();
		long pageCount = totalEmployees / PAGE_SIZE + (totalEmployees % PAGE_SIZE == 0 ? 0 : 1);
		int totalPages = (int) Math.min(pageCount, (long) MAX_PAGE_INDEX + 1);
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
		log.debug("Employee list viewed: page={}, totalEmployees={}", currentPage, totalEmployees);
		return "employees/list";
	}

	@GetMapping("/new")
	public String newEmployee(Model model) {
		log.debug("Employee create form viewed");
		return employeeForm(model, new Employee(), "/employees", "employee.form.create.title", "employee.form.create.submit");
	}

	@PostMapping
	public String createEmployee(@Valid @ModelAttribute("employee") Employee employee,
			BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			log.warn("Employee create rejected: validationErrors={}", bindingResult.getErrorCount());
			return employeeForm(model, employee, "/employees", "employee.form.create.title", "employee.form.create.submit");
		}

		Employee saved = employeeService.save(employee);
		log.info("Employee created: employeeId={}", saved.getEmpId());
		redirectAttributes.addFlashAttribute("successMessage", message("employee.flash.created"));
		return "redirect:/employees";
	}

	@GetMapping("/{id}/edit")
	public String editEmployee(@PathVariable int id, Model model) {
		Employee employee = employeeService.findById(id).orElseThrow(() -> notFound(id));
		log.debug("Employee edit form viewed: employeeId={}", id);
		return employeeForm(model, employee, "/employees/" + id, "employee.form.edit.title", "employee.form.edit.submit");
	}

	@PostMapping("/{id}")
	public String updateEmployee(@PathVariable int id, @Valid @ModelAttribute("employee") Employee employee,
			BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
		if (employeeService.findById(id).isEmpty()) {
			throw notFound(id);
		}
		employee.setEmpId(id);
		if (bindingResult.hasErrors()) {
			log.warn("Employee update rejected: employeeId={}, validationErrors={}", id, bindingResult.getErrorCount());
			return employeeForm(model, employee, "/employees/" + id, "employee.form.edit.title", "employee.form.edit.submit");
		}
		if (!employeeService.update(employee)) {
			throw notFound(id);
		}

		log.info("Employee updated: employeeId={}", id);
		redirectAttributes.addFlashAttribute("successMessage", message("employee.flash.updated"));
		return "redirect:/employees";
	}

	@PostMapping("/{id}/delete")
	public String deleteEmployee(@PathVariable int id, @RequestParam(defaultValue = "0") int page,
			RedirectAttributes redirectAttributes) {
		if (!employeeService.deleteById(id)) {
			throw notFound(id);
		}
		log.info("Employee deleted: employeeId={}", id);
		redirectAttributes.addFlashAttribute("successMessage", message("employee.flash.deleted.single"));
		return employeeListPage(page);
	}

	@PostMapping("/delete")
	public String deleteEmployees(@RequestParam(name = "ids", required = false) List<Integer> employeeIds,
			@RequestParam(defaultValue = "0") int page, RedirectAttributes redirectAttributes) {
		if (employeeIds == null || employeeIds.isEmpty()) {
			log.warn("Employee bulk delete skipped: no employees selected");
			redirectAttributes.addFlashAttribute("warningMessage", message("employee.flash.delete.noneSelected"));
			return employeeListPage(page);
		}

		int deletedCount = employeeService.deleteByIds(employeeIds);
		log.info("Employees deleted in bulk: requestedCount={}, deletedCount={}", employeeIds.size(), deletedCount);
		redirectAttributes.addFlashAttribute("successMessage", message("employee.flash.deleted.multiple", deletedCount));
		return employeeListPage(page);
	}

	private String employeeListPage(int page) {
		return "redirect:/employees?page=" + Math.max(page, 0);
	}

	private String employeeForm(Model model, Employee employee, String formAction, String title, String submitLabel) {
		model.addAttribute("employee", employee);
		model.addAttribute("formAction", formAction);
		model.addAttribute("pageTitle", message(title));
		model.addAttribute("submitLabel", message(submitLabel));
		return "employees/form";
	}

	private ResponseStatusException notFound(int id) {
		log.warn("Employee not found: employeeId={}", id);
		return new ResponseStatusException(HttpStatus.NOT_FOUND, message("employee.error.notFound", id));
	}

	private String message(String code, Object... arguments) {
		return messageSource.getMessage(code, arguments, LocaleContextHolder.getLocale());
	}
}
