package core.annotation.presentation;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages="core.annotation")
//@ImportResource(locations="classpath:properties-config.xml")
public class AppConfig {
/*	@Bean
	BazRepository bazRepository() {
		return new BazRepository();
	}
	
	@Bean 
	BarRepository barRepository() {
		return new BarRepository();
	}
	@Bean
	public FooService fooService(){
		return new FooService();
	}*/
}
