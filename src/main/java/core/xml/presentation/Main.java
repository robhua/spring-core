package core.xml.presentation;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import core.xml.business.EmployeeService;
import core.xml.model.Employee;

public class Main {
	private	static final Logger logger = Logger.getLogger(Main.class);
	public static void main(String[] args) {
		 logger.info("getting application context file...");
		// Create and configure beans
		AbstractApplicationContext context = new ClassPathXmlApplicationContext(
				"beans.xml");

		EmployeeService service = context.getBean("employeeService", EmployeeService.class);
//		Employee emp = new Employee();
//		emp.setEmpName("John");
//		emp.setAge(25);
//		
//		int status = service.save(emp);
//		System.out.println(status);

		System.out.println(" - List all employees: ");
		List<Employee> empList = service.findAllEmployees();
		for (int i = 0; i < empList.size(); i++) {
			System.out.println(empList.get(i));
		}
		
		logger.info("done...");
		// Retrieve configured instance
	}
}
