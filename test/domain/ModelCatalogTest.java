/**
 * 
 */
package domain;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

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
		List<Model> list = new ArrayList<Model>(); 
		this.modelCatalog = new ModelCatalog(list);
	}

//	@Test
//	public void test_getModels() {
//		fail();
//	}

}
