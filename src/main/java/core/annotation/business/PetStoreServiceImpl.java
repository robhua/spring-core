package core.annotation.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import core.annotation.data.PetStoreRepository;

@Service
public class PetStoreServiceImpl implements PetStoreService {
	private PetStoreRepository accountDao;

	public PetStoreServiceImpl() {
	}

	@Autowired
	public PetStoreServiceImpl(PetStoreRepository accountDao) {
		this.accountDao = accountDao;
	}

	@Override
	public List<String> getUsernameList() {
		return accountDao.getListPetName();
	}
}
