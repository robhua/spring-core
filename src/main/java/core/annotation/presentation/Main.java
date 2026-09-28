package core.annotation.presentation;

import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import core.annotation.business.FooService;
import core.annotation.business.PetStoreService;

@SpringBootApplication(
		scanBasePackages = "core.annotation")
public class Main {

	public static void main(String[] args) {
		try (ConfigurableApplicationContext context = SpringApplication.run(Main.class, args)) {

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
		}
	}
}
