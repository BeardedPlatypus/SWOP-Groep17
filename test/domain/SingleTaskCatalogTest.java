package domain;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
/**
 * @author Simon Slangen
 *
 */
@RunWith(MockitoJUnitRunner.class )
public class SingleTaskCatalogTest {
	
	@Mock OptionCategory optionCategory;
	
	@Test
	public void test_getPossibleTasks() {
		ArrayList<OptionCategory> possibleTasks = new ArrayList<>();
		possibleTasks.add(optionCategory);
		
		SingleTaskCatalog singleTaskCatalog = new SingleTaskCatalog(possibleTasks);
		
		assertEquals(singleTaskCatalog.getPossibleTasks(), possibleTasks);
	}
}
