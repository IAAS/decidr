package main.java.de.decidr.modelingtool.client.model;

public enum VariableType {

	// TODO: Internationalization
	STRING("String"),
	INTEGER("Integer"),
	FLOAT("Float"),
	BOOLEAN("Boolean"),
	FILE("File"),
	DATE("Date"),
	ROLE("Role");

	private final String name;

	private VariableType(String name) {
		this.name = name;		
	}
	
	public String getName() {
		return name;
	}
}
