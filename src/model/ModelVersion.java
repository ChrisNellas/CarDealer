package model;

import java.util.HashSet;
import java.util.Set;

public class ModelVersion {

	private Long id;
	private String name, image;
	private Set<Engine> engineVersions;
	private boolean availableWith3, availableWith5;
	private Set<String> availablecolors;
	
	public ModelVersion(Long id, String name, String image, boolean availableWith3, boolean availableWith5, Set<String> colors) {
		this.id = id;
		this.name = name;
		this.image = image;
		this.availableWith3 = availableWith3;
		this.availableWith5 = availableWith5;
		this.engineVersions = new HashSet<>();
		this.availablecolors = colors;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getImage() {
		return image;
	}

	public Set<Engine> getEngineVersions() {
		return engineVersions;
	}

	public boolean isAvailableWith3() {
		return availableWith3;
	}

	public boolean isAvailableWith5() {
		return availableWith5;
	}

	public Set<String> getAvailablecolors() {
		return availablecolors;
	}
	
	public void addEngineVersion(Engine engine) {
		engineVersions.add(engine);
	}
	
	public void removeExistedEngine(Engine engine) {
		if(engineVersions.contains(engine)) {
			engineVersions.remove(engine);
		}
	}

	@Override
	public String toString() {
//		return "ModelVersion [id=" + id + ", name=" + name + ", image=" + image + ", engineVersions=" + engineVersions
//				+ ", availableWith3=" + availableWith3 + ", availableWith5=" + availableWith5 + ", availablecolors="
//				+ availablecolors + "]";
		return getName();
	}	
}
