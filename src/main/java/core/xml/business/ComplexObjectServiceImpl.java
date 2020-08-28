package core.xml.business;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import core.xml.data.ComplexObjectDao;
import core.xml.model.User;

public class ComplexObjectServiceImpl implements ComplexObjectService {
	private ComplexObjectDao complexObjectDao;
	
	@Override
	public Properties getAdminEmails() {
		return complexObjectDao.getAdminEmails();
	}

	public ComplexObjectDao getComplexObjectDao() {
		return complexObjectDao;
	}

	public void setComplexObjectDao(ComplexObjectDao complexObjectDao) {
		this.complexObjectDao = complexObjectDao;
	}

	@Override
	public List<String> getSomeList() {
		return complexObjectDao.getSomeList();
	}

	@Override
	public Map<String, String> getSomeMap() {
		return complexObjectDao.getSomeMap();
	}

	@Override
	public Set<String> getSomeSet() {
		return complexObjectDao.getSomeSet();
	}

	@Override
	public List<User> getListUser() {
		return complexObjectDao.getListUser();
	}
}
