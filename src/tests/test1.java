package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import model.Customer;

class test1 {

	@Test
	void test() {
		assertEquals(12, 6+7-1);
	}
	
	@Test 
	void createCustomer() {
		Customer c = new Customer(98L, "name", "address", 201);
		assertEquals(c.getDept(), 201);
		assertEquals(c.getId(), 98);
	}

//	@Test
//	void testLogoutBtn() throws IOException {
//		LongTermController lTController = new AdminPanelController();
//		lTController.logoutBtnOnClick(new ActionEvent());
//	}
}
