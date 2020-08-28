package core.annotation.business;

import java.util.List;

import core.annotation.model.Todo;

public interface TodoBusinessService {
	public List<Todo> retrieveTodosRelatedToSpring(String user);
}
