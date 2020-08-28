package core.xml.business;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import core.xml.model.User;

public interface ComplexObjectService {

	public Properties getAdminEmails();

	public List<String> getSomeList();

	public Map<String, String> getSomeMap();

	public Set<String> getSomeSet();

	public List<User> getListUser();
}
