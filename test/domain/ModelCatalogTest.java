/**
 * 
 */
package domain;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import domain.car.Model;
import domain.car.ModelCatalog;

/**
 * @author Frederik Goovaerts
 */
public class ModelCatalogTest {
	@Rule public ExpectedException exception = ExpectedException.none();
	
	ModelCatalog testModelCatalog;
	@Mock Model mockModel1;
	@Mock Model mockModel2;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		List<Model> list = new ArrayList<Model>();
		list.add(mockModel1);
		list.add(mockModel2);
		this.testModelCatalog = new ModelCatalog(list);
	}

	@Test
	public void testConstructor() {
		assertTrue(testModelCatalog.getModels().contains(mockModel1));
		assertTrue(testModelCatalog.getModels().contains(mockModel2));
		assertTrue(testModelCatalog.getModels().size() == 2);
	}
	
	@Test
	public void testConstructorNullList() {
		exception.expect(IllegalArgumentException.class);
		testModelCatalog = new ModelCatalog(null);
	}
	
	@Test
	public void testConstructorNullInList() {
		exception.expect(IllegalArgumentException.class);
		List<Model> list = new ArrayList<Model>();
		list.add(null);
		testModelCatalog = new ModelCatalog(list);
	}

	@Test
	public void testContains() {
		assertTrue(testModelCatalog.contains(mockModel1));
		assertTrue(testModelCatalog.contains(mockModel2));
	}

}
