package vehiclemarketplace.cars;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Query;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.view.ViewScoped;

import vehiclemarketplace.dao.BrandDAO;
import vehiclemarketplace.dao.GenerationDAO;
import vehiclemarketplace.dao.ModelDAO;
import vehiclemarketplace.entities.Brand;
import vehiclemarketplace.entities.Generation;
import vehiclemarketplace.entities.Model;

@Named
@ViewScoped
public class CarsBB implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final String PAGE_STAY_AT_THE_SAME = null;

	private Brand brandBrand = new Brand();
	private Brand brandModel = new Brand();
	private Brand brandGeneration = new Brand();
	private Model modelModel = new Model();
	private Model modelGeneration = new Model();
	private Generation generation = new Generation();

	private List<Brand> brands;
	private List<Model> models;

	public Brand getBrandBrand() {
		return brandBrand;
	}

	public void setBrandBrand(Brand brandBrand) {
		this.brandBrand = brandBrand;
	}

	public Brand getBrandModel() {
		return brandModel;
	}

	public void setBrandModel(Brand brandModel) {
		this.brandModel = brandModel;
	}

	public Brand getBrandGeneration() {
		return brandGeneration;
	}

	public void setBrandGeneration(Brand brandGeneration) {
		this.brandGeneration = brandGeneration;
	}

	public Model getModelModel() {
		return modelModel;
	}

	public void setModelModel(Model modelModel) {
		this.modelModel = modelModel;
	}

	public Model getModelGeneration() {
		return modelGeneration;
	}

	public void setModelGeneration(Model modelGeneration) {
		this.modelGeneration = modelGeneration;
	}

	public Generation getGeneration() {
		return generation;
	}

	public void setGeneration(Generation generation) {
		this.generation = generation;
	}

	public List<Brand> getBrands() {
		return brands;
	}

	public List<Model> getModels() {
		return models;
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

	@PostConstruct
	public void init() {
		brands = getBrandList();
	}

	public String addBrand() {
		if (brandDAO.getBrandByName(brandBrand.getName()) != null) {
			ctx.addMessage("brandForm",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Marka o podanej nazwie już istnieje!", null));
			return PAGE_STAY_AT_THE_SAME;
		}
		brandDAO.create(brandBrand);
		brandBrand = new Brand();
		brands = getBrandList();
		return PAGE_STAY_AT_THE_SAME;
	}

	public List<Brand> getBrandList() {
		return brandDAO.getFullList();
	}

	public String addModel() {
		Model modelDB = modelDAO.getModelByNameAndID(modelModel.getName(), brandModel.getIdBrand());
		if (modelDB != null) {
			ctx.addMessage("modelForm",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Marka o podanej nazwie już istnieje!", null));
			return PAGE_STAY_AT_THE_SAME;
		}

		Brand brandDB = brandDAO.find(brandModel.getIdBrand());
		if (brandDB == null) {
			ctx.addMessage("modelForm",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Brak marki o podanym ID!", null));
			return PAGE_STAY_AT_THE_SAME;
		}

		modelModel.setBrand(brandDB);
		modelDAO.create(modelModel);
		modelModel = new Model();
		return PAGE_STAY_AT_THE_SAME;
	}

	public List<Model> getModelList() {
		return modelDAO.getFullList();
	}

	public List<Model> getModelsByBrand() {
		List<Model> list = null;

		if (brandModel.getIdBrand() != 0) {
			list = modelDAO.getModelsByBrandID(brandModel.getIdBrand());
		}

		return list;
	}

	public void changeBrand() {
		if (brandGeneration != null && brandGeneration.getIdBrand() != 0) {
			models = modelDAO.getModelsByBrandID(brandGeneration.getIdBrand());
		} else {
			models = null;
		}
		modelGeneration = new Model();
	}

	public String addGeneration() {
		Generation generationDB = generationDAO.getGenerationByNameAndID(generation.getName(),
				modelGeneration.getIdModel());
		if (generationDB != null) {
			ctx.addMessage("generationForm",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Generacja o podanej nazwie już istnieje!", null));
			return PAGE_STAY_AT_THE_SAME;
		}

		Model modelDB = modelDAO.find(modelGeneration.getIdModel());
		if (modelDB == null) {
			ctx.addMessage("generationForm",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Brak modelu o podanym ID!", null));
			return PAGE_STAY_AT_THE_SAME;
		}

		generation.setModel(modelDB);
		generationDAO.create(generation);
		generation = new Generation();
		return PAGE_STAY_AT_THE_SAME;
	}

	public List<Generation> getGenerationsByModel() {
		List<Generation> list = null;

		if (modelGeneration.getIdModel() != 0) {
			list = generationDAO.getGenerationsByModelID(modelGeneration.getIdModel());
		}

		return list;
	}
}
