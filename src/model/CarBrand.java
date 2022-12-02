package model;
import java.util.HashSet;
import java.util.Set;

public class CarBrand {
	private Long id; 
	private String name, logo;
	private Set<Model> models;
	
	public CarBrand(Long id, String name, String logo) {
		this.id = id;
		this.name = name;
		this.logo = logo;
		this.models = new HashSet<>();
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

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getLogo() {
		return logo;
	}

	public Set<Model> getModels() {
		return models;
	}
	
//	public void setModels(Set<Model> models) {
//		this.models =models; 
//	}

	public void addNewModel(Model model) {
		models.add(model);
	}
	
	public void removeExistedModel(Model model) {
		if(models.contains(model)) {
			models.remove(model);
		}
	}

	@Override
	public String toString() {
//		return "CarBrand [id=" + id + ", name=" + name + ", logo=" + logo + ", models=" + models + "]";
		return getName();
	}
}
