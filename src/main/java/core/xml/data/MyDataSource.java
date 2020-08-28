package core.xml.data;

import java.util.ArrayList;
import java.util.List;

import core.xml.model.User;

public class MyDataSource {
	private static List<String> listString;
	private static ArrayList<User> list; 
	static {
		list = new ArrayList<User>();
		list.add(new User("support", "support@example.org"));
		list.add(new User("administrator", "administrator@example.org"));
		list.add(new User("development", "development@example.org"));
		
		listString = new ArrayList<String>();
		listString.add("support");
		listString.add("administrator");
		
	}
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void sayHello() {
		System.out.println("Hello " + name);
	}
	
	public ArrayList<User> getListUser(){
		return list;
	}

	public List<String> getListString() {
		return listString;
	}
}
