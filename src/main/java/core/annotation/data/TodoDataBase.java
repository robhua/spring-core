package core.annotation.data;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import core.annotation.model.Todo;

@Component
public class TodoDataBase {
	private static final long ONE_DAY_IN_MILLISECONDS = 24 * 60 * 60 * 1000;

	public List<Todo> retrieveTodos(String user) {
		Date today = new Date();
		
		long todayMs = today.getTime();
		Date tomorrow = new Date(todayMs + ONE_DAY_IN_MILLISECONDS);
		Date tenDaysAfter = new Date(todayMs + 10 * ONE_DAY_IN_MILLISECONDS);
		// throw new RuntimeException("Database Down");
		return Arrays.asList(
				new Todo("Complete Spring Tutorial", today, false), 
				new Todo("Complete Spring MVC Tutorial", tomorrow, false),
				new Todo("Complete All Tutorials", tenDaysAfter, false));
	}
}
