package domain;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import domain.car.Option;
import domain.car.OptionCategory;
import domain.order.SingleTaskCatalog;
/**
 * @author Simon Slangen
 *
 */
@RunWith(MockitoJUnitRunner.class )
public class SingleTaskCatalogTest {
	
	@Mock Option option1;
	@Mock Option option2;
	
	SingleTaskCatalog singleTaskCatalog;
	ArrayList<OptionCategory> possibleTasks;
	
	@Before
	public void setUp() {
		
		ArrayList<Option> optionCatList = new ArrayList<>();
		optionCatList.add(option1);
		optionCatList.add(option2);
		
		OptionCategory optionCat = new OptionCategory(optionCatList, "testcat");
		
		possibleTasks = new ArrayList<>();
		possibleTasks.add(optionCat);
		
		singleTaskCatalog = new SingleTaskCatalog(possibleTasks);
		
	}
	
	@Test
	public void test_getPossibleTasks() {
		assertEquals(singleTaskCatalog.getPossibleTasks(), possibleTasks);
	}
	
	@Test
	public void test_contains() {
		Option opt = option1;
		assertTrue(singleTaskCatalog.contains(opt));
	}
}
