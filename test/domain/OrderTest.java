package domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class OrderTest {
	@Mock DateTime dt;
	@Mock Specifications spectacles;
	@Mock Model mockSuperModel;
	
	Order order1;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test 
	public void test_constructor1() {
				
	}
	
	@Test 
	public void test_constructorNullPointerException() {
		
	}
	
	@Test
	public void test_isCompleted() {
		;
	}

}
