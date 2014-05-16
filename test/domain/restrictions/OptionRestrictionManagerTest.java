package domain.restrictions;

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

import domain.car.Option;
import domain.car.Model;

public class OptionRestrictionManagerTest {
	@Rule public ExpectedException exception = ExpectedException.none();

	@Mock List<Option> mockListFail;
	@Mock List<Option> mockListPass;
	@Mock RequiredOptionSetRestriction mockReqRestr;
	@Mock Model mockModel;
	OptionRestrictionManager man;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		Mockito.when(mockReqRestr.isLegalOptionList(mockListPass)).thenReturn(true);
		Mockito.when(mockReqRestr.isLegalOptionList(mockListFail)).thenReturn(false);

	}

	@Test
	public void testConstructor() {
		ArrayList<Restriction> rests = new ArrayList<>();
		rests.add(mockReqRestr);
		man = new OptionRestrictionManager(rests);
	}
	
	@Test
	public void testConstructorNullList() {
		exception.expect(IllegalArgumentException.class);
		man = new OptionRestrictionManager(null);
	}
	
	@Test
	public void testConstructorNullInList() {
		exception.expect(IllegalArgumentException.class);
		ArrayList<Restriction> rests = new ArrayList<>();
		rests.add(null);
		man = new OptionRestrictionManager(rests);
	}

	@Test
	public void testCheckValidity() {
		testConstructor(); //Setup from constructor test
		assertTrue(man.checkValidity(mockModel, mockListPass));
		assertFalse(man.checkValidity(mockModel, mockListFail));
	}
	

	@Test
	public void testCheckValidityNullModel() {
		exception.expect(IllegalArgumentException.class);
		testConstructor(); //Setup from constructor test
		man.checkValidity(null, mockListPass);
	}
	
	@Test
	public void testCheckValidityNullList() {
		exception.expect(IllegalArgumentException.class);
		testConstructor(); //Setup from constructor test
		man.checkValidity(mockModel, null);
	}
	
	@Test
	public void testCheckValidityNullInList() {
		exception.expect(IllegalArgumentException.class);
		testConstructor(); //Setup from constructor test
		List<Option> opts = new ArrayList<>();
		opts.add(null);
		man.checkValidity(mockModel, opts);
	}
}
