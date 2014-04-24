package domain.restrictions;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import domain.assemblyLine.TaskType;
import domain.car.Option;

public class RestrictionTest {
	@Rule public ExpectedException exception = ExpectedException.none();

	Option option1a;
	Option option1b;
	Option option2a;
	Option option2b;
	RequiredOptionSetRestriction testRestr;
	
	@Before
	public void setUp() {
		option1a = new Option(TaskType.BODY, "1a", "1a desc");
		option1b = new Option(TaskType.BODY, "1b", "1b desc");
		Set<Option> req = new HashSet<Option>();
		req.add(option1a);
		req.add(option1b);
		testRestr = new RequiredOptionSetRestriction(req);
	}

	@Test
	public void isLegalNullList() {
		exception.expect(IllegalArgumentException.class);
		testRestr.isLegalOptionList(null);
	}
	
	@Test
	public void isLegalNullInList() {
		exception.expect(IllegalArgumentException.class);
		ArrayList<Option> opts = new ArrayList<>();
		opts.add(null);
		opts.add(option1a);
		testRestr.isLegalOptionList(opts);
	}

}
