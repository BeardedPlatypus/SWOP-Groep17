package domain;

import java.util.List;

import org.javatuples.Pair;

public class AdvanceAssemblyLineHandler {
	private AssemblyLine assemblyLine;

	public AdvanceAssemblyLineHandler(AssemblyLine assemblyLine){
		if(assemblyLine == null)
			throw new IllegalArgumentException("AssemblyLine should not be null.");
		this.assemblyLine = assemblyLine;
	}
	
	/**
	 * Returns the current active assemblies on the {@link AssemblyLine}, coupled with the {@link WorkPost}s they currently belong to, as containers.
	 * {@link WorkPost}s without an active assembly are coupled with null.
	 * 
	 * @return
	 * 		A list all of {@link AssemblyProcedureContainer}s of the active assemblies on the {@link AssemblyLine}, coupled with their respective {@link WorkPostContainer}s.
	 */
	public List<Pair<AssemblyProcedureContainer,WorkPostContainer>> getCurrentWorkpostsAndActiveAssemblies() {
		return assemblyLine.getCurrentWorkPostsAndActiveAssemblies();
	}
	
	/**
	 * Takes a list of the {@link AssemblyProcedure}s on the {@link AssemblyLine}, in the hypothetical situation 
	 * where the {@link AssemblyLine} would advance one post, thus shifting all assemblies one post forwards,
	 * removing the current last assembly, and scheduling a new assembly on the first post if the {@link ProductionSchedule}
	 * has a new order that can still be fully assembled on time according to the schedule. This list is then
	 * coupled in order with their respective {@link WorkPostContainer}s in the hypothetical situation. This resulting
	 * list of pairs is then returned.
	 * {@link WorkPost}s without a future active assembly are coupled with null.
	 * 
	 * @return
	 * 		The list of active {@link AssemblyProcedure}s with their {@link WorkPostContainer}s in the hypothetical situation
	 */
	public List<Pair<AssemblyProcedureContainer,WorkPostContainer>> getFutureWorkpostsAndActiveAssemblies() {
		return assemblyLine.getFutureWorkPostsAndActiveAssemblies();
	}
	
	public void tryAdvance(int Time) {
		assemblyLine.tryAdvance(Time);
	}
	
	/**
	 * Queries the assembly line for workposts with assemblyprocedures where all matching tasks haven't been completed yet.
	 * 
	 * @return
	 * 		The list of workposts.
	 */
	public List<WorkPostContainer> getUnfinishedWorkPosts(){
		return assemblyLine.getUnfinishedWorkPosts();
	}
}
