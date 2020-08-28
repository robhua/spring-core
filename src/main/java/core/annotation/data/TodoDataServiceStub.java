package core.annotation.data;

import java.util.List;

import core.annotation.model.Todo;

public class TodoDataServiceStub implements TodoDataService {
	private TodoDataBase dataBase;

	@Override
	public List<Todo> retrieveTodos(String user) {
		return dataBase.retrieveTodos(user);
	}
}