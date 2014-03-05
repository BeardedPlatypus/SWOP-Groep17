package domain;

/**
 * A view of an assembly task. Use this class to expose a task to the outside world.
 */

public class AssemblyTaskInfo {
	
	private boolean isCompleted;
	private String name;
	private int taskNb;
	private String actionInfo;
	public TaskType type;
	
	/**
	 * Initialises a new task view.
	 * @param isCompleted
	 * 		The new task view's completion state.
	 * @param name
	 * 		The new task view's name.
	 * @param actionInfo
	 * 		The new task view's action info.
	 * @param taskNb
	 * @param type
	 * @throws IllegalArgumentException
	 */
	public AssemblyTaskInfo(boolean isCompleted, String name, String actionInfo, int taskNb, TaskType type) throws IllegalArgumentException {
		if (name == null) {
			throw new IllegalArgumentException("Cannot initialise task info with non-existent name.");
		}
		if (actionInfo == null)  {
			throw new IllegalArgumentException("Cannot initialise task info with non-existent action info.");
		}
		if (taskNb < 0) {
			throw new IllegalArgumentException("Cannot initialise task info with negative task number.");
		}
		if (type == null) {
			throw new IllegalArgumentException("Cannot initialise task with non-existent task type.");
		}
		this.isCompleted = isCompleted;
		this.name = name;
		this.actionInfo = actionInfo;
		this.taskNb = taskNb;
		this.type = type;
	}
	
	public TaskType getTaskType() {
		return this.type;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getTaskNumber() {
		return this.taskNb;
	}
	
	public String getActionInfo() {
		return this.actionInfo;
	}
	
	public boolean isCompleted() {
		return this.isCompleted;
	}
}