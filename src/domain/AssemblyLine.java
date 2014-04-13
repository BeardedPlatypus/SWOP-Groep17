package domain;

import java.util.ArrayList;
import domain.WorkPost;

/**
 * A class depicting an assembly line in the system. An assembly line is composed of a number of workposts.
 * It is linked to a production schedule, which supplies it with new orders when it needs a new order to fill
 * the first workpost when it becomes vacant.
 * It also has a space to store one completed assembly which just came of the assembly line completed, until
 * it is collected. If no assembly is waiting to be collected, this contains a null object.
 * 
 * @author Thomas Vochten, Frederik Goovaerts, Maarten Tegelaers
 */
public class AssemblyLine {
	public Manufacturer manufacturer;
	private ProductionSchedule attribute;
	private ArrayList<WorkPost> workPosts = new ArrayList<WorkPost>();
	private AssemblyProcedure finishedAssembly;
	public StatisticsLogger statisticsLogger;

	public void getActiveOrders() {
		throw new UnsupportedOperationException();
	}

	public void getWorkPostContainers() {
		throw new UnsupportedOperationException();
	}

	public void getTaskContainersAtWorkPost(Object parameter) {
		throw new UnsupportedOperationException();
	}

	public void getWorkPost(Object parameter) {
		throw new UnsupportedOperationException();
	}

	public void completeTask(Object parameter, Object parameter2, Object parameter3) {
		throw new UnsupportedOperationException();
	}

	public void completeAssemblyAtPost() {
		throw new UnsupportedOperationException();
	}
}