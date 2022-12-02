package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import model.CarBrand;
import util.RetrieveBrands;

public class RetrieveTests {

	@Test 
	void userLogin() throws InterruptedException {
		Set<CarBrand> set = new HashSet<>();
		Thread thr = new Thread(new RetrieveBrands(set));
		thr.start();
		thr.join();
//		assertTrue(set.stream().findFirst().filter(br->br.getName().equals("SUZUKI")).isPresent());
		assertEquals("SUZUKI", set.stream().findFirst().filter(br->br.getName().equals("SUZUKI")).get().toString());
//		assertTrue(set.stream().findFirst().filter(br->br.getName().equals("HYUNDAI")).isPresent());
//		assertTrue(set.stream().findFirst().filter(br->br.getName().equals("SKODA")).isPresent());		
//		assertEquals("SUZUKI");
	}	
}
 