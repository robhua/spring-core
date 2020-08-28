package core.annotation.presentation;

import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import core.annotation.business.FooService;
import core.annotation.business.PetStoreService;

public class Main {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
				AppConfig.class);

		FooService fooService = context.getBean(FooService.class);
		fooService.doStuff();

		BeanDefinition definition = context.getBeanFactory().getBeanDefinition(
				"fooServiceImpl");
		System.out.println("fooService is " + definition.getScope());

		System.out.println("---------------------");
		PetStoreService petStore = context.getBean(PetStoreService.class);
		List<String> list = petStore.getUsernameList();
		System.out.println(list);
		System.out.println("---------------------");

		context.close();
	}
}
