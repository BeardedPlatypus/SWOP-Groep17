package domain;

/**
 * A class for workposts that are part of an assembly line.
 * A workpost has a type, and a name, derived from its type.
 * It also has a number, which depicts it's relative order in the assemblyline.
 */
public class WorkPost implements WorkPostContainer {
	private TaskType workPostType;
	private AssemblyProcedure activeAssembly;

	public void getTaskContainers() {
		throw new UnsupportedOperationException();
	}

	public void completeTask(Object parameter, Object parameter2) {
		throw new UnsupportedOperationException();
	}

	public void getActiveAssembly() {
		throw new UnsupportedOperationException();
	}

	public void incrementTime(Object parameter) {
		throw new UnsupportedOperationException();
	}

	public TaskType getWorkPostType() {
		return this.workPostType;
	}

	public AssemblyTaskContainer[] toMatchingAssemblyTasksArray() {
		AssemblyTaskContainer[] lMatchingAssemblyTasks_Temp = new AssemblyTaskContainer[this.matchingAssemblyTasks.size()];
		this.matchingAssemblyTasks.toArray(lMatchingAssemblyTasks_Temp);
		return lMatchingAssemblyTasks_Temp;
	}
}