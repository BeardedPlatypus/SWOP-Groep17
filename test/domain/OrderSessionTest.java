package domain;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import domain.order.Order;
import exceptions.IllegalCarOptionCombinationException;
import exceptions.NoOptionCategoriesRemainingException;
import exceptions.OptionRestrictionException;
import exceptions.OrderDoesNotExistException;

public class OrderSessionTest {
	@Rule public ExpectedException exception = ExpectedException.none();
	
	@Mock Manufacturer mockMan;
	@Mock Model mockModel;
	@Mock OptionCategory mockCat1;
	@Mock OptionCategory mockCat2;
	@Mock Option mockOpt1;
	@Mock Option mockOpt2;
	@Mock Order mockOrder;
	@Mock DateTime mockTime;
	OrderSession session;
	List<Model> models;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		session = new OrderSession(mockMan);

	}

	@Test
	public void testConstructorNull() {
		exception.expect(IllegalArgumentException.class);
		session = new OrderSession(null);
	}
	
	@Test
	public void testAddOption() {
		session.addOption(mockOpt1);
	}

	@Test
	public void testAddOptionNull() {
		exception.expect(IllegalArgumentException.class);
		session.addOption(null);
	}
	
	@Test
	public void testHasUnfilledOptions() {
		Mockito.when(mockMan.isValidModel(mockModel)).thenReturn(true);
		session.chooseModel(mockModel);
		Mockito.when(mockModel.hasUnfilledOptions(new ArrayList<Option>())).thenReturn(true);
		assertTrue(session.hasUnfilledOptions());
	}
	
	@Test
	public void testHasUnfilledOptionsNoModelChosen() {
		exception.expect(IllegalStateException.class);
		session.hasUnfilledOptions();
	}

	@Test
	public void testChooseModelValidModel() {
		Mockito.when(mockMan.isValidModel(mockModel)).thenReturn(true);
		session.chooseModel(mockModel);
	}

	@Test
	public void testChooseModelNoValidModel() {
		exception.expect(IllegalArgumentException.class);
		Mockito.when(mockMan.isValidModel(mockModel)).thenReturn(false);
		session.chooseModel(mockModel);
	}
	
	@Test
	public void testChooseModelNullModel() {
		exception.expect(IllegalArgumentException.class);
		session.chooseModel(null);
	}
	
	
	@Test
	public void testChooseModelAlreadyChosen() {
		exception.expect(IllegalStateException.class);
		Mockito.when(mockMan.isValidModel(mockModel)).thenReturn(true);
		session.chooseModel(mockModel);
		session.chooseModel(mockModel);
	}

	@Test
	public void testGetCarModels() {
		models = new ArrayList<>();
		models.add(mockModel);
		Mockito.when(mockMan.getCarModels()).thenReturn(models);
		assertTrue(session.getCarModels().contains(mockModel));
		assertTrue(session.getCarModels().size() == 1);
	}

	@Test
	public void testGetNextOptionCategory()
			throws NoOptionCategoriesRemainingException {
		Mockito.when(mockMan.isValidModel(mockModel)).thenReturn(true);
		session.chooseModel(mockModel);
		Mockito.when(this.mockModel.getNextOptionCategory(new ArrayList<Option>())).thenReturn(mockCat1);
		assertTrue(session.getNextOptionCategory().equals(mockCat1));
	}
	
	@Test
	public void testGetNextOptionCategoryNoChosenModel()
			throws NoOptionCategoriesRemainingException {
		exception.expect(IllegalStateException.class);
		session.getNextOptionCategory();
	}


	@Test
	public void testSubmitOrder()
			throws IllegalArgumentException,
			IllegalCarOptionCombinationException,
			OptionRestrictionException
	{
		Mockito.when(mockMan.isValidModel(mockModel)).thenReturn(true);
		session.chooseModel(mockModel);
		Mockito.when(mockMan.submitStandardOrder(mockModel, new ArrayList<Option>())).thenReturn(mockOrder);
		session.submitOrder();
	}
	
	@Test
	public void testSubmitOrderNoModel()
			throws IllegalArgumentException,
			IllegalCarOptionCombinationException,
			OptionRestrictionException
	{
		exception.expect(IllegalStateException.class);
		session.submitOrder();
	}
	
	@Test
	public void testSubmitOrderAlreadySubmitted()
			throws IllegalArgumentException,
			IllegalCarOptionCombinationException,
			OptionRestrictionException
	{
		exception.expect(IllegalStateException.class);
		Mockito.when(mockMan.isValidModel(mockModel)).thenReturn(true);
		session.chooseModel(mockModel);
		Mockito.when(mockMan.submitStandardOrder(mockModel, new ArrayList<Option>())).thenReturn(mockOrder);
		session.submitOrder();
		session.submitOrder();
	}

	@Test
	public void testGetETA() 
			throws IllegalArgumentException, 
			IllegalCarOptionCombinationException, 
			OptionRestrictionException,
			OrderDoesNotExistException {
		Mockito.when(mockMan.isValidModel(mockModel)).thenReturn(true);
		session.chooseModel(mockModel);
		Mockito.when(mockMan.submitStandardOrder(mockModel, new ArrayList<Option>())).thenReturn(mockOrder);
		session.submitOrder();
		Mockito.when(mockMan.getEstimatedCompletionTime(mockOrder)).thenReturn(mockTime);
		assertTrue(session.getETA().equals(mockTime));
	}
	
	@Test
	public void testGetETANoOrderYet()
			throws OrderDoesNotExistException{
		exception.expect(IllegalStateException.class);
		session.getETA();
	}

}
