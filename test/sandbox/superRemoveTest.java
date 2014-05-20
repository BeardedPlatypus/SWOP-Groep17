package sandbox;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import domain.assemblyLine.WorkPost;
import domain.assemblyLine.WorkPostView;

public class superRemoveTest {
	
	@Mock WorkPost wp;
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void test() {
		List<WorkPost> list = new ArrayList<>();
		list.add(wp);
		WorkPostView wpc = wp;
		list.remove(wpc);
		assertEquals(0, list.size());
	}

}
