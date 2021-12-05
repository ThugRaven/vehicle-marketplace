package vehiclemarketplace.dashboard.cars;

import java.io.Serializable;
import java.util.HashMap;
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
import vehiclemarketplace.dao.OfferDAO;
import vehiclemarketplace.entities.Brand;
import vehiclemarketplace.entities.Generation;
import vehiclemarketplace.entities.Model;

@Named("dashCarsBB")
@ViewScoped
public class DashboardCarsBB implements Serializable {
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
	private LazyDataModel<Model> lazyModels;
	private LazyDataModel<Generation> lazyGenerations;

	private Brand selectedBrand;
	private Model selectedModel;
	private Generation selectedGeneration;

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

	public LazyDataModel<Model> getLazyModels() {
		return lazyModels;
	}

	public LazyDataModel<Generation> getLazyGenerations() {
		return lazyGenerations;
	}

	public Brand getSelectedBrand() {
		return selectedBrand;
	}

	public void setSelectedBrand(Brand selectedBrand) {
		this.selectedBrand = selectedBrand;
	}

	public Model getSelectedModel() {
		return selectedModel;
	}

	public void setSelectedModel(Model selectedModel) {
		this.selectedModel = selectedModel;
	}

	public Generation getSelectedGeneration() {
		return selectedGeneration;
	}

	public void setSelectedGeneration(Generation selectedGeneration) {
		this.selectedGeneration = selectedGeneration;
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

	@EJB
	OfferDAO offerDAO;

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

				Map<String, String> sortMap = new HashMap<String, String>();
				for (SortMeta sortMeta : sortBy.values()) {
					sortMap.put(sortMeta.getField(), sortMeta.getOrder().toString());
				}

				brands = brandDAO.getLazyFullList(sortMap, offset, pageSize);

				int rowCount = (int) brandDAO.countFullList();
				setRowCount(rowCount);

				return brands;
			}
		};

		lazyModels = new LazyDataModel<Model>() {
			private static final long serialVersionUID = 1L;

			private List<Model> models;

			@Override
			public Model getRowData(String rowKey) {
				for (Model model : models) {
					if (model.getIdModel() == Integer.parseInt(rowKey)) {
						System.out.println(model.getName());
						return model;
					}
				}
				return null;
			}

			@Override
			public String getRowKey(Model model) {
				return String.valueOf(model.getIdModel());
			}

			@Override
			public List<Model> load(int offset, int pageSize, Map<String, SortMeta> sortBy,
					Map<String, FilterMeta> filterBy) {

				Map<String, String> sortMap = new HashMap<String, String>();
				for (SortMeta sortMeta : sortBy.values()) {
					sortMap.put(sortMeta.getField(), sortMeta.getOrder().toString());
				}

				models = modelDAO.getLazyModelsByBrandID(sortMap, brandModel.getIdBrand(), offset, pageSize);

				int rowCount = (int) modelDAO.countModelsByBrandID(brandModel.getIdBrand());
				setRowCount(rowCount);

				return models;
			}
		};

		lazyGenerations = new LazyDataModel<Generation>() {
			private static final long serialVersionUID = 1L;

			private List<Generation> generations;

			@Override
			public Generation getRowData(String rowKey) {
				for (Generation generation : generations) {
					if (generation.getIdGeneration() == Integer.parseInt(rowKey)) {
						System.out.println(generation.getName());
						return generation;
					}
				}
				return null;
			}

			@Override
			public String getRowKey(Generation generation) {
				return String.valueOf(generation.getIdGeneration());
			}

			@Override
			public List<Generation> load(int offset, int pageSize, Map<String, SortMeta> sortBy,
					Map<String, FilterMeta> filterBy) {

				Map<String, String> sortMap = new HashMap<String, String>();
				for (SortMeta sortMeta : sortBy.values()) {
					sortMap.put(sortMeta.getField(), sortMeta.getOrder().toString());
				}

				generations = generationDAO.getLazyGenerationsByModelID(sortMap, modelGeneration.getIdModel(), offset,
						pageSize);

				int rowCount = (int) generationDAO.countGenerationsByModelID(modelGeneration.getIdModel());
				setRowCount(rowCount);

				return generations;
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
		PrimeFaces.current().executeScript("PF('brandEditDialog').hide()");
		return PAGE_STAY_AT_THE_SAME;
	}

	public String deleteBrand() {
		System.out.println("delete: " + selectedBrand.getName() + " " + selectedBrand.getIdBrand());

		if (brandCountModels() > 0) {
			ctx.addMessage("brandTable", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Błąd podczas usuwania rekordu",
					"Marka nie może zostać usunięta ponieważ zawiera ona conajmniej jeden model!"));
			return PAGE_STAY_AT_THE_SAME;
		}

		brandDAO.remove(selectedBrand);
		brands = getBrandList();
		selectedBrand = null;
		ctx.addMessage("brandTable", new FacesMessage(FacesMessage.SEVERITY_INFO, "Pomyślnie usunięto rekord!", null));
		return PAGE_STAY_AT_THE_SAME;
	}

	public int brandCountModels() {
		return modelDAO.getModelsByBrandID(selectedBrand.getIdBrand()).size();
	}

	public int brandCountOffers() {
		return offerDAO.getOffersByBrandID(selectedBrand.getIdBrand()).size();
	}

	public List<Brand> getBrandList() {
		return brandDAO.getFullList();
	}

	public String addModel() {
		Model modelDB = modelDAO.getModelByNameAndBrandID(modelModel.getName(), brandModel.getIdBrand());
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
		models = getModelsByBrand();
		return PAGE_STAY_AT_THE_SAME;
	}

	public String editModel() {
		Model modelOld = modelDAO.find(selectedModel.getIdModel());
		Model modelDB = modelDAO.getModelByNameAndBrandID(modelOld.getName(), modelOld.getBrand().getIdBrand());
		System.out.println("edit: " + selectedModel.getName() + " old: " + modelOld.getName() + " db: "
				+ modelDB.getName() + " " + selectedModel.getIdModel());
		if (modelDB != null && selectedModel.getName().equals(modelDB.getName())) {
			System.out.println("Same, no changes");
			return PAGE_STAY_AT_THE_SAME;
		}

		Model modelCheck = modelDAO.getModelByNameAndBrandID(selectedModel.getName(),
				selectedModel.getBrand().getIdBrand());
		if (modelCheck != null) {
			ctx.addMessage("modelDialogForm",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Model o podanej nazwie już istnieje!", null));
			selectedModel = modelOld;
			return PAGE_STAY_AT_THE_SAME;
		}
		modelDAO.merge(selectedModel);
		models = getModelsByBrand();
		PrimeFaces.current().executeScript("PF('modelEditDialog').hide()");
		return PAGE_STAY_AT_THE_SAME;
	}

	public String deleteModel() {
		System.out.println("delete: " + selectedModel.getName() + " " + selectedModel.getIdModel());

		if (modelCountGenerations() > 0) {
			ctx.addMessage("modelTable", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Błąd podczas usuwania rekordu",
					"Model nie może zostać usunięty ponieważ zawiera ona conajmniej jedną generację!"));
			return PAGE_STAY_AT_THE_SAME;
		}

		modelDAO.remove(selectedModel);
		models = getModelsByBrand();
		selectedModel = null;
		ctx.addMessage("modelTable", new FacesMessage(FacesMessage.SEVERITY_INFO, "Pomyślnie usunięto rekord!", null));
		return PAGE_STAY_AT_THE_SAME;
	}

	public int modelCountGenerations() {
		return generationDAO.getGenerationsByModelID(selectedModel.getIdModel()).size();
	}

	public int modelCountOffers() {
		return offerDAO.getOffersByModelID(selectedModel.getIdModel()).size();
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
		Generation generationDB = generationDAO.getGenerationByNameAndModelID(generation.getName(),
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

	public String editGeneration() {
		Generation generationOld = generationDAO.find(selectedGeneration.getIdGeneration());
		Generation generationDB = generationDAO.getGenerationByNameAndModelID(generationOld.getName(),
				generationOld.getModel().getIdModel());
		System.out.println("edit: " + selectedGeneration.getName() + " old: " + generationOld.getName() + " db: "
				+ generationDB.getName() + " " + selectedGeneration.getIdGeneration());
		if (generationDB != null && selectedGeneration.getName().equals(generationDB.getName())
				&& selectedGeneration.getProductionStart().equals(generationDB.getProductionStart())
				&& selectedGeneration.getProductionEnd().equals(generationDB.getProductionEnd())) {
			System.out.println("Same, no changes");
			return PAGE_STAY_AT_THE_SAME;
		}

		Generation generationCheck = generationDAO.getGenerationByNameAndModelID(selectedGeneration.getName(),
				selectedGeneration.getModel().getIdModel());
		if (generationCheck != null && selectedGeneration.getProductionStart().equals(generationDB.getProductionStart())
				&& selectedGeneration.getProductionEnd().equals(generationDB.getProductionEnd())) {
			ctx.addMessage("genDialogForm",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Generacja o podanej nazwie już istnieje!", null));
			selectedGeneration = generationOld;
			return PAGE_STAY_AT_THE_SAME;
		}
		generationDAO.merge(selectedGeneration);
		PrimeFaces.current().executeScript("PF('genEditDialog').hide()");
		return PAGE_STAY_AT_THE_SAME;
	}

	public String deleteGeneration() {
		System.out.println("delete: " + selectedGeneration.getName() + " " + selectedGeneration.getIdGeneration());

		if (generationCountOffers() > 0) {
			ctx.addMessage("genTable", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Błąd podczas usuwania rekordu",
					"Generacja nie może zostać usunięta ponieważ zawiera ona conajmniej jedną ofertę!"));
			return PAGE_STAY_AT_THE_SAME;
		}

		generationDAO.remove(selectedGeneration);
		selectedGeneration = null;
		ctx.addMessage("genTable", new FacesMessage(FacesMessage.SEVERITY_INFO, "Pomyślnie usunięto rekord!", null));
		return PAGE_STAY_AT_THE_SAME;
	}

	public int generationCountOffers() {
		return offerDAO.getOffersByGenerationID(selectedGeneration.getIdGeneration()).size();
	}
}
