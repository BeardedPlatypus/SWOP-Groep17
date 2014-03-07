package domain;

import java.util.List;

public interface AssemblyProcedureContainer {
	
	public OrderContainer getOrder();
	
	public List<AssemblyTaskContainer> getAssemblyTasks();
	
	
}
