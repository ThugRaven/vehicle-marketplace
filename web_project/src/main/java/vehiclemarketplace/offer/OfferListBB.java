package vehiclemarketplace.offer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import vehiclemarketplace.ConstantsBB;
import vehiclemarketplace.classes.SelectFilter;
import vehiclemarketplace.classes.SelectItemCount;
import vehiclemarketplace.classes.SelectList;
import vehiclemarketplace.classes.SelectSort;
import vehiclemarketplace.classes.SelectType;
import vehiclemarketplace.dao.BodyStyleDAO;
import vehiclemarketplace.dao.BrandDAO;
import vehiclemarketplace.dao.GenerationDAO;
import vehiclemarketplace.dao.ModelDAO;
import vehiclemarketplace.dao.OfferDAO;
import vehiclemarketplace.entities.BodyStyle;
import vehiclemarketplace.entities.Brand;
import vehiclemarketplace.entities.Generation;
import vehiclemarketplace.entities.Model;
import vehiclemarketplace.entities.Offer;
import vehiclemarketplace.entities.User;

@Named
@SessionScoped
public class OfferListBB implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final String PAGE_STAY_AT_THE_SAME = null;

	private LazyDataModel<Offer> lazyOffers;
	private int countList;

	private Offer selectedOffer;

	private Offer offerFilter = new Offer();
	private Offer offerFilterFrom = new Offer();
	private Offer offerFilterTo = new Offer();
	private List<SelectList> offerFilterList = new ArrayList<>();
	private Integer offerSort;

	private List<SelectItemCount<Brand>> brands;
	private List<SelectItemCount<Model>> models = new ArrayList<>();
	private List<SelectItemCount<Generation>> generations = new ArrayList<>();
	private List<BodyStyle> bodyStyles;

	public Offer getSelectedOffer() {
		return selectedOffer;
	}

	public void setSelectedOffer(Offer selectedOffer) {
		this.selectedOffer = selectedOffer;
	}

	public LazyDataModel<Offer> getLazyOffers() {
		return lazyOffers;
	}

	public int getCountList() {
		return countList;
	}

	public Offer getOfferFilter() {
		return offerFilter;
	}

	public Offer getOfferFilterFrom() {
		return offerFilterFrom;
	}

	public Offer getOfferFilterTo() {
		return offerFilterTo;
	}

	public List<SelectList> getOfferFilterList() {
		return offerFilterList;
	}

	public void setOfferSort(Integer offerSort) {
		this.offerSort = offerSort;
	}

	public Integer getOfferSort() {
		return offerSort;
	}

	public List<SelectItemCount<Brand>> getBrands() {
		return brands;
	}

	public List<SelectItemCount<Model>> getModels() {
		return models;
	}

	public List<SelectItemCount<Generation>> getGenerations() {
		return generations;
	}

	public List<BodyStyle> getBodyStyles() {
		return bodyStyles;
	}

	@Inject
	ExternalContext extcontext;

	@EJB
	OfferDAO offerDAO;

	@EJB
	BrandDAO brandDAO;

	@EJB
	ModelDAO modelDAO;

	@EJB
	GenerationDAO generationDAO;

	@EJB
	BodyStyleDAO bodyStyleDAO;

	@Inject
	ConstantsBB constantsBB;

	@Inject
	FacesContext ctx;

	@Inject
	Flash flash;

	public void onLoad() {
		System.out.println("ON LOAD");
		Offer loadedFilter = null;
		Offer loadedFilterFrom = null;
		Offer loadedFilterTo = null;

		loadedFilter = (Offer) flash.get("offerFilter");
		loadedFilterFrom = (Offer) flash.get("offerFilterFrom");
		loadedFilterTo = (Offer) flash.get("offerFilterTo");

		if (loadedFilter != null) {
			offerFilter = loadedFilter;
		}
		if (loadedFilterFrom != null) {
			offerFilterFrom = loadedFilterFrom;
		}
		if (loadedFilterTo != null) {
			offerFilterTo = loadedFilterTo;
		}

		loadBrand();
		loadModel();
	}

	@PostConstruct
	public void init() {
		System.out.println("INIT");
		offerFilter.setUser(new User());
		offerFilter.setBrand(new Brand());
		offerFilter.setModel(new Model());
		offerFilter.setGeneration(new Generation());
		offerFilter.setBodyStyle(new BodyStyle());
		offerFilterList.add(new SelectList("transmission", new ArrayList<>()));
		offerFilterList.add(new SelectList("drive", new ArrayList<>()));
		offerFilterList.add(new SelectList("doors", new ArrayList<>()));
		offerFilterList.add(new SelectList("seats", new ArrayList<>()));
		offerFilterList.add(new SelectList("color", new ArrayList<>()));
		offerFilterList.add(new SelectList("colorType", new ArrayList<>()));

		countLazyList();

		brands = getBrandList();

		bodyStyles = getBodyStyleList();

		lazyOffers = new LazyDataModel<Offer>() {
			private static final long serialVersionUID = 1L;

			private List<Offer> offers;

			@Override
			public Offer getRowData(String rowKey) {
				for (Offer offer : offers) {
					if (offer.getIdOffer() == Integer.parseInt(rowKey)) {
						System.out.println(offer.getTitle());
						return offer;
					}
				}
				return null;
			}

			@Override
			public String getRowKey(Offer offer) {
				return String.valueOf(offer.getIdOffer());
			}

			@Override
			public List<Offer> load(int offset, int pageSize, Map<String, SortMeta> sortBy,
					Map<String, FilterMeta> filterBy) {

				Map<String, String> sortMap = new HashMap<String, String>();
				if (offerSort != null) {
					SelectSort sort = constantsBB.getSorts().get(offerSort);
					sortMap.put(sort.getParameter(), sort.getOrder());
				}
				System.out.println("LAZY OFFERS LOAD");
				List<SelectFilter> filter = new ArrayList<>();
				filter = addFilters();

				offers = offerDAO.getLazyList(sortMap, filter, offset, pageSize);

				int rowCount = (int) offerDAO.countLazyList(filter);
				countList = rowCount;
				setRowCount(rowCount);

				return offers;
			}
		};
	}

	public List<SelectFilter> addFilters() {
		List<SelectFilter> filter = new ArrayList<>();

		if (offerFilter.getBodyStyle() != null && offerFilter.getBodyStyle().getIdBodyStyle() != 0) {
			filter.add(new SelectFilter("bodyStyle.idBodyStyle", "idBodyStyle",
					offerFilter.getBodyStyle().getIdBodyStyle(), SelectType.NORMAL));
		}
		if (offerFilter.getBrand() != null && offerFilter.getBrand().getIdBrand() != 0) {
			filter.add(new SelectFilter("brand.idBrand", "idBrand", offerFilter.getBrand().getIdBrand(),
					SelectType.NORMAL));
		}
		if (offerFilter.getModel() != null && offerFilter.getModel().getIdModel() != 0) {
			filter.add(new SelectFilter("model.idModel", "idModel", offerFilter.getModel().getIdModel(),
					SelectType.NORMAL));
		}
		if (offerFilter.getGeneration() != null && offerFilter.getGeneration().getIdGeneration() != 0) {
			filter.add(new SelectFilter("generation.idGeneration", "idGeneration",
					offerFilter.getGeneration().getIdGeneration(), SelectType.NORMAL));
		}
		if (offerFilterFrom.getPrice() != null) {
			filter.add(
					new SelectFilter("price", "priceFrom", offerFilterFrom.getPrice(), SelectType.GREATER_EQUAL_THAN));
		}
		if (offerFilterTo.getPrice() != null) {
			filter.add(new SelectFilter("price", "priceTo", offerFilterTo.getPrice(), SelectType.LESS_EQUAL_THAN));
		}
		if (offerFilterFrom.getProductionYear() != null) {
			filter.add(new SelectFilter("productionYear", "productionYearFrom", offerFilterFrom.getProductionYear(),
					SelectType.GREATER_EQUAL_THAN));
		}
		if (offerFilterTo.getProductionYear() != null) {
			filter.add(new SelectFilter("productionYear", "productionYearTo", offerFilterTo.getProductionYear(),
					SelectType.LESS_EQUAL_THAN));
		}
		if (offerFilter.getFuel() != null && !offerFilter.getFuel().isEmpty()) {
			filter.add(new SelectFilter("fuel", offerFilter.getFuel(), SelectType.NORMAL));
		}
		if (offerFilterFrom.getMileage() != null) {
			filter.add(new SelectFilter("mileage", "mileageFrom", offerFilterFrom.getMileage(),
					SelectType.GREATER_EQUAL_THAN));
		}
		if (offerFilterTo.getMileage() != null) {
			filter.add(
					new SelectFilter("mileage", "mileageTo", offerFilterTo.getMileage(), SelectType.LESS_EQUAL_THAN));
		}
		if (offerFilter.getLicensePlate() != null
				&& Boolean.parseBoolean(String.valueOf(offerFilter.getLicensePlate()))) {
			filter.add(new SelectFilter("licensePlate", offerFilter.getLicensePlate(), SelectType.IS_NOT_NULL));
		}
		if (offerFilter.getIsDamaged() != null) {
			filter.add(new SelectFilter("isDamaged", offerFilter.getIsDamaged(), SelectType.NORMAL));
		}
		if (offerFilter.getIsAccidentFree() != null && offerFilter.getIsAccidentFree()) {
			filter.add(new SelectFilter("isAccidentFree", offerFilter.getIsAccidentFree(), SelectType.NORMAL));
		}
		if (offerFilter.getIsFirstOwner() != null && offerFilter.getIsFirstOwner()) {
			filter.add(new SelectFilter("isFirstOwner", offerFilter.getIsFirstOwner(), SelectType.NORMAL));
		}
		if (offerFilter.getIsRegistered() != null && offerFilter.getIsRegistered()) {
			filter.add(new SelectFilter("isRegistered", offerFilter.getIsRegistered(), SelectType.NORMAL));
		}
		if (offerFilter.getIsRightHandDrive() != null && offerFilter.getIsRightHandDrive()) {
			filter.add(new SelectFilter("isRightHandDrive", offerFilter.getIsRightHandDrive(), SelectType.NORMAL));
		}
		if (offerFilterFrom.getDisplacement() != null) {
			filter.add(new SelectFilter("displacement", "displacementFrom", offerFilterFrom.getDisplacement(),
					SelectType.GREATER_EQUAL_THAN));
		}
		if (offerFilterTo.getDisplacement() != null) {
			filter.add(new SelectFilter("displacement", "displacementTo", offerFilterTo.getDisplacement(),
					SelectType.LESS_EQUAL_THAN));
		}
		if (offerFilterFrom.getPower() != null) {
			filter.add(
					new SelectFilter("power", "powerFrom", offerFilterFrom.getPower(), SelectType.GREATER_EQUAL_THAN));
		}
		if (offerFilterTo.getPower() != null) {
			filter.add(new SelectFilter("power", "powerTo", offerFilterTo.getPower(), SelectType.LESS_EQUAL_THAN));
		}
		if (offerFilterList != null && offerFilterList.size() > 0) {
			for (SelectList selectList : offerFilterList) {
				System.out.println("selectList: " + selectList.toString());

				if (selectList.getValues() != null && selectList.getValues().size() > 0) {
					System.out.println("add to where");
					filter.add(new SelectFilter(selectList.getParameter(), selectList.getParameter(),
							selectList.getValues(), SelectType.LIST));
				}
			}
		}

		filter.add(new SelectFilter("archived", false, SelectType.NORMAL));

		return filter;
	}

	public void countLazyList() {
		countList = (int) offerDAO.countLazyList(addFilters());
	}

	public String getCountText(Integer count) {
		if (count == null) {
			count = lazyOffers.getRowCount();
		}
		String text = ((count == 1) ? "ogłoszenie" : ((count >= 2 && count <= 5) ? "ogłoszenia" : "ogłoszeń"));
		return text;
	}

	public String countListText() {
		return countList + " " + getCountText(countList);
	}

	public List<String> completePrices(String query) {
		List<String> list = new ArrayList<>();

		for (String price : constantsBB.getPrices()) {
			if (price.startsWith(query)) {
				list.add(price);
			}
		}

		return list;
	}

	public List<String> completeYears(String query) {
		List<String> list = new ArrayList<>();

		for (String year : constantsBB.getYears()) {
			if (year.startsWith(query)) {
				list.add(year);
			}
		}

		return list;
	}

	public List<String> completeMileages(String query) {
		List<String> list = new ArrayList<>();

		for (String mileage : constantsBB.getMileages()) {
			if (mileage.startsWith(query)) {
				list.add(mileage);
			}
		}

		return list;
	}

	public List<Offer> getFullList() {
		return offerDAO.getFullList();
	}

	public long countOffersByBrandID(int id) {
		return offerDAO.countOffersByBrandID(id);
	}

	public long countOffersByModelID(int id) {
		return offerDAO.countOffersByModelID(id);
	}

	public long countOffersByGenerationID(int id) {
		return offerDAO.countOffersByGenerationID(id);
	}

	public List<SelectItemCount<Brand>> getBrandList() {
		List<SelectItemCount<Brand>> selectItemCounts = new ArrayList<>();

		List<Brand> brandsList = brandDAO.getFullList();
		for (Brand brand : brandsList) {
			selectItemCounts.add(new SelectItemCount<Brand>(brand, countOffersByBrandID(brand.getIdBrand())));
		}

		return selectItemCounts;
	}

	public void changeBrand() {
		if (offerFilter.getBrand() != null && offerFilter.getBrand().getIdBrand() != 0) {
			models = new ArrayList<>();
			generations = new ArrayList<>();
			List<Model> modelsList = modelDAO.getModelsByBrandID(offerFilter.getBrand().getIdBrand());
			for (Model model : modelsList) {
				models.add(new SelectItemCount<Model>(model, countOffersByModelID(model.getIdModel())));
			}
		} else {
			models = null;
			generations = null;
		}
		offerFilter.setModel(new Model());
		offerFilter.setGeneration(new Generation());
		countLazyList();
	}

	public void loadBrand() {
		if (offerFilter.getBrand() != null && offerFilter.getBrand().getIdBrand() != 0) {
			models = new ArrayList<>();
			generations = new ArrayList<>();
			List<Model> modelsList = modelDAO.getModelsByBrandID(offerFilter.getBrand().getIdBrand());
			for (Model model : modelsList) {
				models.add(new SelectItemCount<Model>(model, countOffersByModelID(model.getIdModel())));
			}
		} else {
			models = null;
			generations = null;
		}
	}

	public void changeModel() {
		if (offerFilter.getModel() != null && offerFilter.getModel().getIdModel() != 0) {
			generations = new ArrayList<>();
			List<Generation> generationsList = generationDAO
					.getGenerationsByModelID(offerFilter.getModel().getIdModel());
			for (Generation generation : generationsList) {
				generations.add(new SelectItemCount<Generation>(generation,
						countOffersByGenerationID(generation.getIdGeneration())));
			}
		} else {
			generations = null;
		}
		offerFilter.setGeneration(new Generation());
		countLazyList();
	}

	public void loadModel() {
		if (offerFilter.getModel() != null && offerFilter.getModel().getIdModel() != 0) {
			generations = new ArrayList<>();
			List<Generation> generationsList = generationDAO
					.getGenerationsByModelID(offerFilter.getModel().getIdModel());
			for (Generation generation : generationsList) {
				generations.add(new SelectItemCount<Generation>(generation,
						countOffersByGenerationID(generation.getIdGeneration())));
			}
		} else {
			generations = null;
		}
	}

	public List<BodyStyle> getBodyStyleList() {
		return bodyStyleDAO.getFullList();
	}

	public void clearFilters() {
		offerFilter = new Offer();
		offerFilter.setUser(new User());
		offerFilter.setBrand(new Brand());
		offerFilter.setModel(new Model());
		offerFilter.setGeneration(new Generation());
		offerFilter.setBodyStyle(new BodyStyle());
		offerFilterFrom = new Offer();
		offerFilterTo = new Offer();
		offerFilterList.clear();
		offerFilterList.add(new SelectList("transmission", new ArrayList<>()));
		offerFilterList.add(new SelectList("drive", new ArrayList<>()));
		offerFilterList.add(new SelectList("doors", new ArrayList<>()));
		offerFilterList.add(new SelectList("seats", new ArrayList<>()));
		offerFilterList.add(new SelectList("color", new ArrayList<>()));
		offerFilterList.add(new SelectList("colorType", new ArrayList<>()));
		offerSort = null;
	}

	public void clearVehicleStatus() {
		offerFilter.setLicensePlate(null);
		offerFilter.setIsDamaged(null);
		offerFilter.setIsAccidentFree(null);
		offerFilter.setIsFirstOwner(null);
		offerFilter.setIsRegistered(null);
		offerFilter.setIsRightHandDrive(null);
	}

	public void clearEngineDrive() {
		offerFilterFrom.setDisplacement(null);
		offerFilterTo.setDisplacement(null);
		offerFilterFrom.setPower(null);
		offerFilterTo.setPower(null);
		offerFilterList.set(0, new SelectList("transmission", new ArrayList<>()));
		offerFilterList.set(1, new SelectList("drive", new ArrayList<>()));
	}

	public void clearBody() {
		offerFilterList.set(2, new SelectList("doors", new ArrayList<>()));
		offerFilterList.set(3, new SelectList("seats", new ArrayList<>()));
		offerFilterList.set(4, new SelectList("color", new ArrayList<>()));
		offerFilterList.set(5, new SelectList("colorType", new ArrayList<>()));
	}
}
