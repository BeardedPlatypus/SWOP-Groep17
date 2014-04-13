package domain;

/**
 * Coordinates with the outside world in order to complete assembly tasks of a single workpost.
 */
public class PerformAssemblyTaskHandler {
	private AssemblyLine attribute;
	public Manufacturer manufacturer;

	public void getWorkPosts() {
		throw new UnsupportedOperationException();
	}

	public void getTasksAtWorkPost(int postNum) {
		throw new UnsupportedOperationException();
	}

	public void completeTask(Object parameter, Object parameter2, Object parameter3) {
		throw new UnsupportedOperationException();
	}
}