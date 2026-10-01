package core.xml.presentation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.context.MessageSource;
import org.springframework.ui.ExtendedModelMap;

import core.xml.business.EmployeeService;

class EmployeeControllerTest {
	private final EmployeeService employeeService = mock(EmployeeService.class);
	private final EmployeeController controller = new EmployeeController(employeeService, mock(MessageSource.class));

	@Test
	void listEmployeesCapsPageAtLargestRepresentableOffset() {
		when(employeeService.countEmployees()).thenReturn(Long.MAX_VALUE);
		ExtendedModelMap model = new ExtendedModelMap();

		assertEquals("employees/list", controller.listEmployees(Integer.MAX_VALUE, model));

		assertEquals(214748365, model.get("totalPages"));
		assertEquals(214748364, model.get("currentPage"));
		assertEquals(214748362, model.get("firstPage"));
		assertEquals(214748364, model.get("lastPage"));
		assertTrue((boolean) model.get("hasPrevious"));
		assertFalse((boolean) model.get("hasNext"));
		verify(employeeService).findEmployees(10, 2147483640);
	}

	@Test
	void listEmployeesHandlesEmptyDirectory() {
		ExtendedModelMap model = new ExtendedModelMap();

		assertEquals("employees/list", controller.listEmployees(Integer.MAX_VALUE, model));

		assertEquals(0, model.get("totalPages"));
		assertEquals(0, model.get("currentPage"));
		assertFalse((boolean) model.get("hasPrevious"));
		assertFalse((boolean) model.get("hasNext"));
		verify(employeeService).findEmployees(10, 0);
	}
}
