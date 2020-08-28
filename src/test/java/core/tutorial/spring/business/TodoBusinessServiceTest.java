package core.tutorial.spring.business;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import core.annotation.business.TodoBusinessService;
import core.annotation.model.Todo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/BusinessApplicationContext.xml" })
public class TodoBusinessServiceTest {
	@Autowired
	TodoBusinessService businessService;

	@Test
	public void testGetToDosAboutSpring() {
		List<Todo> todos = businessService.retrieveTodosRelatedToSpring("Ranga");
		assertEquals(1, todos.size());
		assertEquals("Complete Spring Tutorial", todos.get((0)).getDesc());
	}
}

