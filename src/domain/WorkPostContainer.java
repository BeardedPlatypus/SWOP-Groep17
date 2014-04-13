package domain;

public interface WorkPostContainer {

	public TaskType getWorkPostType();

	public AssemblyTaskContainer[] toMatchingAssemblyTasksArray();
}