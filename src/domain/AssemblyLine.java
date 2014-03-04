package domain;

import java.util.ArrayList;
import java.util.List;

public class AssemblyLine {
	public ProductionSchedule productionSchedule;
	public ArrayList<WorkPost> workPosts = new ArrayList<WorkPost>();

	public List<AssemblyProcedure> getActiveAssemblies() {
		List<AssemblyProcedure> toReturn = new ArrayList<AssemblyProcedure>();
		for (WorkPost workPost : this.workPosts) {
			AssemblyProcedure activeAssembly = workPost.getAssemblyProcedure();
			if (activeAssembly != null) {
				toReturn.add(activeAssembly);
			}
		}
		return toReturn;
	}

	public AssemblyProcedure getCompleteAssembly() {
		throw new UnsupportedOperationException();
	}

	public List<WorkPostInfo> getWorkPosts() {
		List<WorkPostInfo> toReturn = new ArrayList<WorkPostInfo>();
		int counter = 1;
		for (WorkPost workPost : this.workPosts) {
			toReturn.add(new WorkPostInfo(counter++, workPost.getName(), workPost.getTaskType()));
		}
		return toReturn;
	}

	public List<AssemblyTaskInfo> getAssemblyTasksAtPost(int workPostNumber) throws IllegalArgumentException {
		return this.workPosts.get(workPostNumber).getAssemblyTasks();
	}

	public void completeWorkpostTask(int workPostNumber, int taskNumber) throws IllegalArgumentException {
		this.workPosts.get(workPostNumber).completeTask(taskNumber);
	}

	public List<OrderContainer> getActiveOrderContainers() {
		throw new UnsupportedOperationException();
	}
}