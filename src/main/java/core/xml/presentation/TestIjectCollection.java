package core.xml.presentation;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import core.xml.business.ComplexObjectService;

public class TestIjectCollection {
	public static void main(String[] args) {
		// Create and configure beans
		// Create and configure beans
		AbstractApplicationContext context = new ClassPathXmlApplicationContext(
				"beans.xml");
		// Use configured instance
		ComplexObjectService complexObject = context.getBean("complexObject",
				ComplexObjectService.class);
		System.out.println("Properties: " + complexObject.getAdminEmails());
		System.out.println("List: " + complexObject.getSomeList());
		System.out.println("Map : " + complexObject.getSomeMap());
		System.out.println("Set : " + complexObject.getSomeSet());
		System.out.println("List-user : " + complexObject.getListUser());

		context.close();
	}
}
