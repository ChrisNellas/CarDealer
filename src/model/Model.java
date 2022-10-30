package model;
import java.util.HashSet;
import java.util.Set;

public class Model {
	private Long id;
	private String name;
	private Set<ModelVersion> modelVersions;

	public Model(Long id, String name) {
		this.id = id;
		this.name = name;
		this.modelVersions = new HashSet<>();
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}	

	public void setName(String name) {
		this.name = name;
	}

	public Set<ModelVersion> getModelVersions() {
		return modelVersions;
	}

	public void addModelVersion(ModelVersion mv) {
		modelVersions.add(mv);
	}
	
	public void removeExistedModelVersion(ModelVersion mv) {
		if(modelVersions.contains(mv)) {
			modelVersions.remove(mv);
		}
	}

	@Override
	public String toString() {
		return "Model [id=" + id + ", name=" + name + ", modelVersions=" + modelVersions + "]";
	}
}
