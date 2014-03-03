import java.util.ArrayList;

public class AssemblyLine {
	public Production_Schedule unnamed_Production_Schedule_;
	public AssemblyProcedure unnamed_AssemblyProcedure_;
	public ProductionSchedule productionSchedule;
	public PerformAssemblyTaskController unnamed_PerformAssemblyTaskController_;
	public AdvanceAssemblyLineController unnamed_AdvanceAssemblyLineController_;
	public Workstation unnamed_Workstation_;
	public ArrayList<WorkPost> workposts = new ArrayList<WorkPost>();

	public AssemblyProcedure[] getActiveAssemblies() {
		throw new UnsupportedOperationException();
	}

	public AssemblyProcedure getCompleteAssembly() {
		throw new UnsupportedOperationException();
	}

	public WorkPostInfo[] getWorkPosts() {
		throw new UnsupportedOperationException();
	}

	public AssemblyTaskInfo[] getAssemblyTasksAtPost(Object int_1) {
		throw new UnsupportedOperationException();
	}

	public void completeWorkpostTask(Object intPost, Object intTask) {
		throw new UnsupportedOperationException();
	}

	public OrderContainer[] getActiveOrderContainers() {
		throw new UnsupportedOperationException();
	}
}