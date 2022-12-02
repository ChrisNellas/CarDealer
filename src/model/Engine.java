package model;

import java.util.HashSet;
import java.util.Set;

public class Engine {
	private Long id;
	private String name;
	private int power;
	private Set<Gearbox> availableGearboxes;
	private Type type;
	
	public Engine(Long id, String name, int power, Type type) {
		this.id = id;
		this.name = name;
		this.power = power;
		this.type = type;
		availableGearboxes = new HashSet<>();
	}
	
	public Long getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public int getPower() {
		return power;
	}
	
	public Set<Gearbox> getAvailableGearboxes() {
		return availableGearboxes;
	}
	
	public Type getType() {
		return type;
	}
	
	public void addGearbox(Gearbox gearbox) {
		availableGearboxes.add(gearbox);
	}
	
	public void removeExistedEngine(Gearbox gearbox) {
		if(availableGearboxes.contains(gearbox)) {
			availableGearboxes.remove(gearbox);
		}
	}

//	@Override
//	public String toString() {
//		return "Engine [id=" + id + ", name=" + name + ", power=" + power + ", availableGearboxes=" + availableGearboxes
//				+ ", type=" + type + "]";
//	}

	@Override
	public String toString() {
		return name + " | " + power+" hp";
	}
	
	
}
