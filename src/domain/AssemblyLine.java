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

	public List<WorkPostContainer> getWorkPosts() {
		return new ArrayList<WorkPostContainer>(workPosts);
	}

	public List<AssemblyTaskContainer> getAssemblyTasksAtPost(int workPostNumber) throws IllegalArgumentException {
		return this.workPosts.get(workPostNumber).getAssemblyTasks();
	}

	public void completeWorkpostTask(int workPostNumber, int taskNumber) throws IllegalArgumentException {
		this.workPosts.get(workPostNumber).completeTask(taskNumber);
	}

	public List<OrderContainer> getActiveOrderContainers() {
		throw new UnsupportedOperationException();
	}

	public ArrayList<AssemblyProcedureContainer> getCurrentAssembly() {
		// TODO Auto-generated method stub
		return null;
	}

	public ArrayList<AssemblyProcedureContainer> getFutureAssembly() {
		// TODO Auto-generated method stub
		return null;
	}

	public void tryAdvance(int time) {
		// TODO Auto-generated method stub
		
	}
	
	public int getSize() {
		// TODO Auto-generated method stub
		return -1;
	}
}