package core.annotation.data;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import core.annotation.model.Todo;

@Repository
public class TodoDataServiceRealImp implements TodoDataService {
	@Autowired
	private TodoDataBase dataBase;

	public List<Todo> retrieveTodos(String user) {
		return dataBase.retrieveTodos(user);
	}
}