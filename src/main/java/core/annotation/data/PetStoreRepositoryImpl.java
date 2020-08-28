package core.annotation.data;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class PetStoreRepositoryImpl implements PetStoreRepository{
	public List<String> getListPetName() {
		List<String> list = new ArrayList<String>();
		list.add("Petter");
		list.add("Sue");
		return list;
	}
}
