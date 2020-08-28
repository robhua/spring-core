package core.annotation.business;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import core.annotation.data.FooRepository;

@Service
public class FooServiceImpl implements InitializingBean, DisposableBean, FooService{
	@Autowired
	private FooRepository fooRepository;

	public FooServiceImpl() {
		System.out.println("Constructor fooService");
	}
	
	@PostConstruct
    public void init() {
        System.out.println("1. fooService was initialized");
    }
    
	public void doStuff() {
		fooRepository.doStuff();
	}
	
	@PreDestroy
    public void pre_Destroy() {
        System.out.println("3. fooService was pre_destroyed");
    }

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("2. foodService run afterPropertiesSet() method.");
	}

	@Override
	public void destroy() throws Exception {
		System.out.println("4. food service run destroy() method.");
		
	}
}
