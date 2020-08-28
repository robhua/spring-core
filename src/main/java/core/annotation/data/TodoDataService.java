package core.annotation.data;

import java.util.List;

import core.annotation.model.Todo;

public interface TodoDataService {
	List<Todo> retrieveTodos(String userName);
}
