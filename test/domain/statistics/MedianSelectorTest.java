package domain.statistics;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.javatuples.Pair;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import domain.statistics.MedianSelector;
import domain.statistics.WorkingDay;

public class MedianSelectorTest {
	
	MedianSelector select = new MedianSelector();
	double epsilon = 1E-14;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void findMedian_oddNumElements() {
		List<Pair<Integer, WorkingDay>> data = new ArrayList<Pair<Integer, WorkingDay>>();
		data.add(new Pair<Integer, WorkingDay>(3, new WorkingDay(0)));
		data.add(new Pair<Integer, WorkingDay>(2, new WorkingDay(1)));
		data.add(new Pair<Integer, WorkingDay>(5, new WorkingDay(2)));
		data.add(new Pair<Integer, WorkingDay>(4, new WorkingDay(3)));
		data.add(new Pair<Integer, WorkingDay>(1, new WorkingDay(4)));
		
		double val = select.findMedian(data);
		assertEquals(3, val, epsilon);
	}
	
	@Test
	public void findMedian_evenNumElements() {
		List<Pair<Integer, WorkingDay>> data = new ArrayList<Pair<Integer, WorkingDay>>();
		data.add(new Pair<Integer, WorkingDay>(3, new WorkingDay(0)));
		data.add(new Pair<Integer, WorkingDay>(2, new WorkingDay(1)));
		data.add(new Pair<Integer, WorkingDay>(6, new WorkingDay(2)));
		data.add(new Pair<Integer, WorkingDay>(1, new WorkingDay(3)));
		data.add(new Pair<Integer, WorkingDay>(4, new WorkingDay(4)));
		data.add(new Pair<Integer, WorkingDay>(5, new WorkingDay(5)));
		
		double val = select.findMedian(data);
		assertEquals(3.5, val, epsilon);
	}
	
	@Test
	public void speedTest() {
		List<Pair<Integer, WorkingDay>> data = new ArrayList<Pair<Integer, WorkingDay>>();
		int numElems = 1000000;
		for (int i = 0; i < numElems; i++) {
			int newVal = (int) Math.floor(Math.random() * 100);
			data.add(new Pair<Integer, WorkingDay>(newVal, new WorkingDay(i)));
		}
		long startTime = System.currentTimeMillis();
		double val = select.findMedian(data);
		long endTime = System.currentTimeMillis() - startTime;
		System.out.println("Finding median of " + (numElems + 1) + " elements took "
				+ (double) endTime / 1000 + " seconds; median was " + val);
	}

}
