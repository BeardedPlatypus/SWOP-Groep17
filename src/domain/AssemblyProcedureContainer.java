package domain;

public interface AssemblyProcedureContainer {

	public OrderContainer getOrder();

	public AssemblyTaskContainer[] toAssemblyTasksArray();
}