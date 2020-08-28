package core.annotation.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestBeanLifeCycle implements BeanNameAware, BeanFactoryAware,
		ApplicationContextAware, BeanPostProcessor, InitializingBean, DisposableBean {
	
	private ApplicationContext applicationContext;
	private BeanFactory beanFactory;
	private String beanName;
	
	public static void main(String[] args) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
				TestBeanLifeCycle.class);
		context.close();
	}
	
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {
		System.out.println("Called postProcessBeforeInitialization() for :"
				+ bean + " " + beanName);
		return bean;
	}
	
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {
		System.out.println("Called postProcessAfterInitialization() for :" + bean + " "
				+ beanName);
		return bean;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("Called afterPropertiesSet()");
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		System.out.println("Called setApplicationContext(): " + applicationContext);
		this.applicationContext = applicationContext;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		System.out.println("Called setBeanFactory(): " + beanFactory);
		this.beanFactory = beanFactory;
	}

	@Override
	public void setBeanName(String name) {
		System.out.println("Called setBeanName(): " + name);
		this.beanName = name;
	}

	@Override
	public void destroy() throws Exception {
		System.out.println("Called destroy()");
	}
}
