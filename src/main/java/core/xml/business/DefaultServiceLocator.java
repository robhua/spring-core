package core.xml.business;

import core.annotation.business.PetStoreService;
import core.annotation.business.PetStoreServiceImpl;

public class DefaultServiceLocator {
	private static PetStoreService petStore = new PetStoreServiceImpl();
	private static ComplexObjectService complexObject = new ComplexObjectServiceImpl();

	private DefaultServiceLocator() {
	}

	public PetStoreService createPetStoreService() {
		System.out
				.println("Instantiation PetStoreService with a Static Factory Method");
		return petStore;
	}

	public ComplexObjectService createComplexObjectServiceImp() {
		System.out
				.println("Instantiation ComplexObjectService with a Static Factory Method");
		return complexObject;
	}
}
