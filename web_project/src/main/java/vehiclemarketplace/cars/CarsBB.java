package vehiclemarketplace.cars;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

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

	private LazyDataModel<Brand> lazyBrands;

	private Brand selectedBrand;

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

	public LazyDataModel<Brand> getLazyBrands() {
		return lazyBrands;
	}

	public Brand getSelectedBrand() {
		return selectedBrand;
	}

	public void setSelectedBrand(Brand selectedBrand) {
		this.selectedBrand = selectedBrand;
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
		lazyBrands = new LazyDataModel<Brand>() {
			private static final long serialVersionUID = 1L;

			private List<Brand> brands;

			@Override
			public Brand getRowData(String rowKey) {
				for (Brand brand : brands) {
					if (brand.getIdBrand() == Integer.parseInt(rowKey)) {
						System.out.println(brand.getName());
						return brand;
					}
				}
				return null;
			}

			@Override
			public String getRowKey(Brand brand) {
				return String.valueOf(brand.getIdBrand());
			}

			@Override
			public List<Brand> load(int offset, int pageSize, Map<String, SortMeta> sortBy,
					Map<String, FilterMeta> filterBy) {
				brands = brandDAO.getLazyFullList(offset, pageSize);

				int rowCount = (int) brandDAO.countFullList();
				setRowCount(rowCount);

				return brands;
			}
		};
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

	public String editBrand() {
		Brand brandOld = brandDAO.find(selectedBrand.getIdBrand());
		Brand brandDB = brandDAO.getBrandByName(brandOld.getName());
		System.out.println("edit: " + selectedBrand.getName() + " old: " + brandOld.getName() + " db: "
				+ brandDB.getName() + " " + selectedBrand.getIdBrand());
		if (brandDB != null && selectedBrand.getName().equals(brandDB.getName())) {
			System.out.println("Same, no changes");
			return PAGE_STAY_AT_THE_SAME;
		}

		Brand brandCheck = brandDAO.getBrandByName(selectedBrand.getName());
		if (brandCheck != null) {
			ctx.addMessage("brandDialogForm",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Marka o podanej nazwie już istnieje!", null));
			selectedBrand = brandOld;
			return PAGE_STAY_AT_THE_SAME;
		}
		brandDAO.merge(selectedBrand);
		brands = getBrandList();
		PrimeFaces.current().executeScript("PF('brandDialog').hide()");
		return PAGE_STAY_AT_THE_SAME;
	}

	public void deleteBrand() {
		System.out.println("delete: " + selectedBrand.getName() + " " + selectedBrand.getIdBrand());
		brandDAO.remove(selectedBrand);
		brands = getBrandList();
		selectedBrand = null;
	}

	public int brandCountModels() {
		return modelDAO.getModelsByBrandID(selectedBrand.getIdBrand()).size();
	}

	public void info() {
		System.out.println("info: " + selectedBrand.getName() + " " + selectedBrand.getIdBrand());
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
