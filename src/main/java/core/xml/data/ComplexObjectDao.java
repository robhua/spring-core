package core.xml.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import core.xml.model.User;

public class ComplexObjectDao {
	private Properties adminEmails;
	private List<String> someList = new ArrayList<String>();
	private Map<String, String> someMap;
	private Set<String> someSet;
	private List<User> listUser;

	public Properties getAdminEmails() {
		return adminEmails;
	}

	public void setAdminEmails(Properties adminEmails) {
		this.adminEmails = adminEmails;
	}

	public List<String> getSomeList() {
		return someList;
	}

	public void setSomeList(MyDataSource dataSource) {
		this.someList.addAll(dataSource.getListString());
	}

	public Map<String, String> getSomeMap() {
		return someMap;
	}

	public void setSomeMap(Map<String, String> someMap) {
		this.someMap = someMap;
	}

	public Set<String> getSomeSet() {
		return someSet;
	}

	public void setSomeSet(Set<String> someSet) {
		this.someSet = someSet;
	}

	public void setListUser(MyDataSource dataSource) {
		this.listUser = dataSource.getListUser();
	}

	public List<User> getListUser() {
		return listUser;
	}
}
