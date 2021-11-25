package vehiclemarketplace.cars;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Query;
import javax.enterprise.context.RequestScoped;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;

import vehiclemarketplace.dao.BrandDAO;
import vehiclemarketplace.dao.GenerationDAO;
import vehiclemarketplace.dao.ModelDAO;
import vehiclemarketplace.entities.Brand;
import vehiclemarketplace.entities.Generation;
import vehiclemarketplace.entities.Model;

@Named
@RequestScoped
public class CarsBB {
	private static final String PAGE_STAY_AT_THE_SAME = null;

	private Brand brand = new Brand();
	private Model model = new Model();
	private Generation generation = new Generation();

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public Generation getGeneration() {
		return generation;
	}

	public void setGeneration(Generation generation) {
		this.generation = generation;
	}

	@Inject
	ExternalContext extcontext;

	@Inject
	Flash flash;

	@EJB
	BrandDAO brandDAO;

	@EJB
	ModelDAO modelDAO;

	@EJB
	GenerationDAO generationDAO;

	@Inject
	FacesContext ctx;

	public void addBrand() {
		brandDAO.create(brand);
	}

	public List<Brand> getBrandList() {
		return brandDAO.getFullList();
	}

	public Map<String, Integer> getBrandsMap() {
		List<Brand> brandList = getBrandList();
		Map<String, Integer> brands = new HashMap<>();
		for (Brand brand : brandList) {
			brands.put(brand.getName(), brand.getIdBrand());
		}
		return brands;
	}

	public String addModel() {
		if (brand.getIdBrand() == 0) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Błąd przy podaniu marki!", null));
			return PAGE_STAY_AT_THE_SAME;
		}

		System.out.println(brand.getIdBrand());
		Brand brandDB = brandDAO.find(brand.getIdBrand());

		if (brandDB == null) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Brak marki o podanym ID!", null));
			return PAGE_STAY_AT_THE_SAME;
		}

		model.setBrand(brandDB);
		modelDAO.create(model);
		return PAGE_STAY_AT_THE_SAME;
	}

	public List<Model> getModelList() {
		return modelDAO.getFullList();
	}

	public List<Model> getModelsByBrand() {
		List<Model> list = null;

		if (brand.getIdBrand() != 0) {
			System.out.println(brand.getIdBrand());
			list = modelDAO.getModelsByBrandID(brand.getIdBrand());
			System.out.println(list);
		}

		return list;
	}

	public Map<String, Integer> getModelsMap() {
		List<Model> modelList = getModelsByBrand();
		Map<String, Integer> models = new HashMap<>();
		if (modelList != null) {
			for (Model model : modelList) {
				models.put(model.getName(), model.getIdModel());
			}
			System.out.println(models.toString());
		}
		return models;
	}

	public String addGeneration() {
		if (model.getIdModel() == 0) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Błąd przy podaniu modelu!", null));
			return PAGE_STAY_AT_THE_SAME;
		}

		System.out.println(model.getIdModel());
		Model modelDB = modelDAO.find(model.getIdModel());

		if (modelDB == null) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Brak modelu o podanym ID!", null));
			return PAGE_STAY_AT_THE_SAME;
		}

		generation.setModel(modelDB);
		generationDAO.create(generation);
		return PAGE_STAY_AT_THE_SAME;
	}

	public List<Generation> getGenerationsByModel() {
		List<Generation> list = null;

		if (model.getIdModel() != 0) {
			System.out.println(model.getIdModel());
			list = generationDAO.getGenerationsByModelID(model.getIdModel());
			System.out.println(list);
		}

		return list;
	}
}
