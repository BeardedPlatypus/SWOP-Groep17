package domain;

import java.util.ArrayList;
import java.util.List;

public class AdvanceAssemblyLineHandler {
	private AssemblyLine assemblyLine;

	public AdvanceAssemblyLineHandler(AssemblyLine assemblyLine){
		if(assemblyLine == null)
			throw new IllegalArgumentException("AssemblyLine should not be null.");
		this.assemblyLine = assemblyLine;
	}
	
	public List<WorkPostContainer> getWorkPostLayout(){
		return assemblyLine.getWorkPosts();
	}
	
	public List<AssemblyProcedureContainer> getCurrentActiveAssemblies() {
		return assemblyLine.getCurrentActiveAssemblies();
	}
	
	public List<AssemblyProcedureContainer> getFutureActiveAssemblies() {
		return assemblyLine.getFutureActiveAssemblies();
	}
	
	public void tryAdvance(int Time) {
		assemblyLine.tryAdvance(Time);
	}
}