package core.annotation.data;

import org.springframework.stereotype.Repository;

@Repository
public class FooRepsitoryImpl implements FooRepository {
	public void doStuff() {
		System.out.println("This is foo repository Bean");
	}
}
