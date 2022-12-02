package model;

public record Gearbox(Long id, String name, GearboxType type, int gears) {

	@Override
	public String toString() {
		return name + "| " + type + " | " + gears;
	}
	
	
}
