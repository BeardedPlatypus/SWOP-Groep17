package domain;

import java.util.Collection;
import java.util.List;

public interface WorkPostContainer {
	
	public int getWorkPostNumber();
	
	public String getName();
	
	public TaskType getWorkPostType();

	public List<AssemblyTaskContainer> getMatchingAssemblyTasks();
	
}