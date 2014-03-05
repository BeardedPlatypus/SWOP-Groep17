package domain;

/**
 * 
 * A representation of all possible types of work posts.
 *
 */

public enum TaskType implements Comparable<TaskType> {
	
	BODY("Body Post"),
	DRIVETRAIN("Drivetrain Post"),
	ACCESSORIES("Accessories Post");
	
	private String name;
	
	TaskType(String name) {
		this.name = name;
	}

	/**
	 * Getter for this task type's name.
	 */
	public String getName() {
		return this.name;
	}
}