package domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * @author SimSla
 *
 */
public class BatchComparatorTest {
	
	@Mock Specification spec1;
	@Mock Specification spec2;
	
	@Mock Order order1;
	@Mock Order order2;
	@Mock Order order3;
	@Mock Order order4;
	@Mock Order order5;
	
	DateTime time1;
	DateTime time2;
	DateTime time3;
	DateTime time4;
	DateTime time5;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		time1 = new DateTime(1, 0, 0);
		time2 = new DateTime(2, 0, 0);
		time3 = new DateTime(3, 0, 0);
		time4 = new DateTime(4, 0, 0);
		time5 = new DateTime(5, 0, 0);
		
		Mockito.when(spec1.equals(spec2)).thenReturn(false);
		Mockito.when(spec1.equals(spec1)).thenReturn(true);
		Mockito.when(spec2.equals(spec1)).thenReturn(false);
		Mockito.when(spec2.equals(spec2)).thenReturn(true);
		
		Mockito.when(order1.getSubmissionTime()).thenReturn(time1);
		Mockito.when(order2.getSubmissionTime()).thenReturn(time2);
		Mockito.when(order3.getSubmissionTime()).thenReturn(time3);
		Mockito.when(order4.getSubmissionTime()).thenReturn(time4);
		Mockito.when(order5.getSubmissionTime()).thenReturn(time5);
		
		Mockito.when(order1.getSpecifications()).thenReturn(spec1);
		Mockito.when(order2.getSpecifications()).thenReturn(spec2);
		Mockito.when(order3.getSpecifications()).thenReturn(spec1);
		Mockito.when(order4.getSpecifications()).thenReturn(spec2);
		Mockito.when(order5.getSpecifications()).thenReturn(spec1);
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
