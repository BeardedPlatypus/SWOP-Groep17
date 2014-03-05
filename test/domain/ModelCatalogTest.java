/**
 * 
 */
package domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import domain.ModelCatalog;

/**
 * @author Month
 *
 */
public class ModelCatalogTest {
	ModelCatalog modelCatalog;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.modelCatalog = new ModelCatalog();
	}

	@Test
	public void test_getModels() {
		fail();
	}

}
