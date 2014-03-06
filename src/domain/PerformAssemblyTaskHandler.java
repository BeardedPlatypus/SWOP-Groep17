package domain;

import java.util.List;

public class PerformAssemblyTaskHandler {
	public AssemblyLine assemblyLine;

	public PerformAssemblyTaskHandler(AssemblyLine assemblyLine) throws IllegalArgumentException {
		if (assemblyLine == null) {
			throw new IllegalArgumentException("Cannot initialise perform assembly task handler with non-existent assembly line.");
		}
		this.assemblyLine = assemblyLine;
	}
	
	public List<WorkPostContainer> getWorkPosts() {
		return this.assemblyLine.getWorkPosts();
	}

	public List<AssemblyTaskContainer> getAssemblyTasksAtPost(int workPostNumber) {
		return this.assemblyLine.getAssemblyTasksAtPost(workPostNumber);
	}

	public void completeWorkpostTask(int workPostNumber, int taskNumber) {
		this.assemblyLine.completeWorkpostTask(workPostNumber, taskNumber);
	}
}