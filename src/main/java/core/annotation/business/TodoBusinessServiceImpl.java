package core.annotation.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import core.annotation.data.TodoDataService;
import core.annotation.model.Todo;

@Service
public class TodoBusinessServiceImpl implements TodoBusinessService {
	private static final Logger logger = Logger.getLogger(TodoBusinessServiceImpl.class);
	
	private static final int ONE_DAY_IN_MINISECONDS = 24 * 60 * 1000;
	@Autowired
	private TodoDataService dataService;// = new TodoDataServiceStub();

	@Override
	public List<Todo> retrieveTodosRelatedToSpring(String user) {
		List<Todo> todos = dataService.retrieveTodos(user);
		List<Todo> filteredTodos = filterTodoInTheNextWeek(todos);
		logger.info("Retrieve todos related to spring by user.");
		logger.info(user + todos);
		return filteredTodos;
	}

	private List<Todo> filterTodoInTheNextWeek(List<Todo> retrieveTodos) {
		Date today = new Date();
		long todayMs = today.getTime();

		Date oneWeekLater = new Date(todayMs + 7 * ONE_DAY_IN_MINISECONDS);
		List<Todo> filteredTodos = new ArrayList<Todo>();
		for (Todo todo : retrieveTodos) {
			if (todo.getDate().before(oneWeekLater)) {
				filteredTodos.add(todo);
			}
		}
		return filteredTodos;
	}
}
