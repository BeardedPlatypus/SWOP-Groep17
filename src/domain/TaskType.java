package domain;

/**
 * A representation of all possible types of work posts.
 */
public enum TaskType {
	BODY("Body Post"),
	DRIVETRAIN("Drivetrain Post"),
	ACCESSORIES("Accessories Post");
	
	private String name;
	
	/**
	 * Construct a new TaskType with the specified name.
	 * 
	 * @param name
	 * 		The name of the new TaskType.
	 */
	TaskType(String name) {
		this.name = name;
	}

	/**
	 * Get the name of this TaskType.
	 * 
	 * @return the name of this TaskTyp.
	 */
	public String getName() {
		return this.name;
	}
}