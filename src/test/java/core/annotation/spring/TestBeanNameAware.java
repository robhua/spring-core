package core.annotation.spring;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

public class TestBeanNameAware {
	@Test
	public void test() {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
				Config.class);

		SomeService service = context.getBean(SomeService.class);
		service.getAnotherBean();
		service.doSomethingWithChilds();
	}
}

abstract class MessageService implements BeanNameAware {
	String beanName;

	@Override
	public void setBeanName(String name) {
		beanName = name;
	}

	abstract public void send(String message, String recepient);
}

@Component
class EmailService extends MessageService {
	private static final Logger LOG = Logger.getLogger(EmailService.class);

	@Override
	public void send(String message, String recepient) {
		LOG.info("message sent with context " + message + "\t recepient "
				+ recepient);
	}
}

@Component
class SMSService extends MessageService {
	private static final Logger LOG = Logger.getLogger(SMSService.class);

	@Override
	public void send(String message, String recepient) {
		LOG.info("message sent with context " + message + "\t recepient "
				+ recepient);
	}

}

@Service
class SomeService implements BeanFactoryAware{
	private static final Logger LOG = Logger.getLogger(SomeService.class);
	
	@Autowired
	MessageService[] childs; // injecting all Child*

	private BeanFactory beanFactory;

	void doSomethingWithChilds() {
		for (MessageService child : childs) {
			child.send("Test message", "test@gmail.com");;

			String currentChildName = child.beanName;

			if (currentChildName.equals("emailService")) {
				LOG.info("Email service do something special");
			}
		}
	}

	public void getAnotherBean() {
		Config bean = beanFactory.getBean(Config.class);
		bean.print();
	}
	
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}
}

@Configuration
@ComponentScan
class Config {
	public void print() {
		System.out.println("Config bean");
	}
}