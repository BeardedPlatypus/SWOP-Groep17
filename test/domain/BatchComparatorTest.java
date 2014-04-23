package domain;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import domain.order.Order;

/**
 * @author SimSla
 *
 */
public class BatchComparatorTest {

	@Mock Option option1;
	@Mock Option option2;
	@Mock Option option3;
	@Mock Option option4;
	
	@Mock Specification spec1;
	@Mock Specification spec2;
	
	@Mock Order order1;
	@Mock Order order2;
	@Mock Order order3;
	@Mock Order order4;
	@Mock Order order5;
	
	Specification order1Specs;
	Specification order2Specs;
	Specification order3Specs;
	Specification order4Specs;
	
	DateTime order1Sub;
	DateTime order2Sub;
	DateTime order3Sub;
	DateTime order4Sub;
	
	Specification batch;
	
	BatchComparator comp;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		Mockito.when(option1.getName()).thenReturn("Annie");
		Mockito.when(option2.getName()).thenReturn("Are");
		Mockito.when(option3.getName()).thenReturn("You");
		Mockito.when(option4.getName()).thenReturn("Okay");
		
		Mockito.when(option1.getType()).thenReturn(TaskType.BODY);
		Mockito.when(option2.getType()).thenReturn(TaskType.DRIVETRAIN);
		Mockito.when(option3.getType()).thenReturn(TaskType.ACCESSORIES);
		Mockito.when(option4.getType()).thenReturn(TaskType.ACCESSORIES);
		
		order1Specs = new Specification(new ArrayList<Option>(Arrays.asList(option1, option2)));
		order2Specs = new Specification(new ArrayList<Option>(Arrays.asList(option2, option1)));
		order3Specs = new Specification(new ArrayList<Option>(Arrays.asList(option3)));
		order4Specs = new Specification(new ArrayList<Option>(Arrays.asList(option4)));
		
		batch = new Specification(order2Specs.getOptions());
		
		order1Sub = new DateTime(1, 0, 0);
		order2Sub = new DateTime(2, 0, 0);
		order3Sub = new DateTime(3, 0, 0);
		order4Sub = new DateTime(4, 0, 0);
		
		Mockito.when(order1.getSubmissionTime()).thenReturn(order1Sub);
		Mockito.when(order2.getSubmissionTime()).thenReturn(order2Sub);
		Mockito.when(order3.getSubmissionTime()).thenReturn(order3Sub);
		Mockito.when(order4.getSubmissionTime()).thenReturn(order4Sub);
		
		Mockito.when(order1.getSpecifications()).thenReturn(order1Specs);
		Mockito.when(order2.getSpecifications()).thenReturn(order2Specs);
		Mockito.when(order3.getSpecifications()).thenReturn(order3Specs);
		Mockito.when(order4.getSpecifications()).thenReturn(order4Specs);
		
		comp = new BatchComparator(batch);
	}

	@Test
	public void compare_OneMatches_SmallerThan() {
		int result = comp.compare(order1, order3);
		assertTrue(result < 0);
	}
	
	@Test
	public void compare_OneMatches_GreaterThan() {
		int result = comp.compare(order4, order2);
		assertTrue(result > 0);
	}
	
	@Test
	public void compare_NeitherMatches_SmallerThan() {
		int result = comp.compare(order3, order4);
		assertTrue(result < 0);
	}
	
	@Test
	public void compare_NeitherMatches_GreaterThan() {
		int result = comp.compare(order4, order3);
		assertTrue(result > 0);
	}
	
	@Test
	public void compare_BothMatch_SmallerThan() {
		int result = comp.compare(order1, order2);
		assertTrue(result < 0);
	}
	
	@Test
	public void compare_BothMatch_GreaterThan() {
		int result = comp.compare(order2, order1);
		assertTrue(result > 0);
	}
	
	@Test
	public void compare_NeitherMatches_equals() {
		int result = comp.compare(order3, order3);
		assertEquals(result, 0);
	}
	
	@Test
	public void compare_BothMatch_equals() {
		int result = comp.compare(order1, order1);
		assertEquals(result, 0);
	}
}
