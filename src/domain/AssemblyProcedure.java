package domain;

import java.util.ArrayList;
import domain.AssemblyTask;

/**
 * An assembly procedure specifies a sequence of tasks that must be completed in order to fulfill an order.
 * 
 * @invariant getAssemblyTasks() != null
 * @invariant getOrder() != null && getOrderContainer != null
 */
public class AssemblyProcedure implements AssemblyProcedureContainer {
	private ArrayList<AssemblyTask> attribute = new ArrayList<AssemblyTask>();
	private Order attribute2;

	public void getTaskContainers() {
		throw new UnsupportedOperationException();
	}

	public void completeTask(Object parameter, Object parameter2) {
		throw new UnsupportedOperationException();
	}

	public void getTask() {
		throw new UnsupportedOperationException();
	}

	public void isFinished() {
		throw new UnsupportedOperationException();
	}

	public void getStatistics() {
		throw new UnsupportedOperationException();
	}
}