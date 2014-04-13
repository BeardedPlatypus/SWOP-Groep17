package domain;

/**
 * One single task that needs to be performed as part of the assembly process. Painting a car blue is an example
 * of an assembly task.
 */
public class AssemblyTask implements AssemblyTaskContainer {
	private TaskType type;
	public Option option;

	public void setCompleted() {
		throw new UnsupportedOperationException();
	}

	public TaskType getTaskType() {
		return this.taskType;
	}
}