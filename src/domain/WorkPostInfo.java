package domain;

public class WorkPostInfo {
	
	private int workPostNumber;
	private String name;
	private TaskType workPostType;
	
	public WorkPostInfo(int workPostNumber, String name, TaskType workPostType) {
		this.workPostNumber = workPostNumber;
		this.name = name;
		this.workPostType = workPostType;
	}
	
	public int getWorkPostNumber() {
		return this.workPostNumber;
	}
	
	public String getName() {
		return this.name;
	}
	
	public TaskType getWorkPostType() {
		return this.workPostType;
	}
}