package vehiclemarketplace.offer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import vehiclemarketplace.classes.SelectFilter;
import vehiclemarketplace.classes.SelectList;
import vehiclemarketplace.classes.SelectType;
import vehiclemarketplace.dao.BodyStyleDAO;
import vehiclemarketplace.dao.BrandDAO;
import vehiclemarketplace.dao.EquipmentDAO;
import vehiclemarketplace.dao.GenerationDAO;
import vehiclemarketplace.dao.ModelDAO;
import vehiclemarketplace.dao.OfferDAO;
import vehiclemarketplace.dao.UserDAO;
import vehiclemarketplace.dao.UserRoleDAO;
import vehiclemarketplace.entities.BodyStyle;
import vehiclemarketplace.entities.Brand;
import vehiclemarketplace.entities.Equipment;
import vehiclemarketplace.entities.Generation;
import vehiclemarketplace.entities.Model;
import vehiclemarketplace.entities.Offer;
import vehiclemarketplace.entities.User;

@Named
@ViewScoped
public class OfferListBB implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final String PAGE_STAY_AT_THE_SAME = null;

	private LazyDataModel<Offer> lazyOffers;

	private Offer selectedOffer;

	private Offer offerFilter = new Offer();
	private Offer offerFilterFrom = new Offer();
	private Offer offerFilterTo = new Offer();
	private List<SelectList> offerFilterList = new ArrayList<>();

	private List<Brand> brands;
	private List<Model> models;
	private List<Generation> generations;
	private List<String> fuels = new ArrayList<>();
	private List<String> transmissions = new ArrayList<>();
	private List<BodyStyle> bodyStyles;
	private List<SelectItem> drives = new ArrayList<>();
	private List<Byte> doors = new ArrayList<>();
	private List<Byte> seats = new ArrayList<>();
	private List<String> colors = new ArrayList<>();
	private List<String> colorTypes = new ArrayList<>();
	private List<Equipment> equipments;

	public Offer getSelectedOffer() {
		return selectedOffer;
	}

	public void setSelectedOffer(Offer selectedOffer) {
		this.selectedOffer = selectedOffer;
	}

	public LazyDataModel<Offer> getLazyOffers() {
		return lazyOffers;
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

	public List<Brand> getBrands() {
		return brands;
	}

	public List<Model> getModels() {
		return models;
	}

	public List<Generation> getGenerations() {
		return generations;
	}

	public List<String> getFuels() {
		return fuels;
	}

	public List<String> getTransmissions() {
		return transmissions;
	}

	public List<BodyStyle> getBodyStyles() {
		return bodyStyles;
	}

	public List<SelectItem> getDrives() {
		return drives;
	}

	public List<Byte> getDoors() {
		return doors;
	}

	public List<Byte> getSeats() {
		return seats;
	}

	public List<String> getColors() {
		return colors;
	}

	public List<String> getColorTypes() {
		return colorTypes;
	}

	public List<Equipment> getEquipments() {
		return equipments;
	}

	@Inject
	ExternalContext extcontext;

	@EJB
	UserDAO userDAO;

	@EJB
	UserRoleDAO userRoleDAO;

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

	@EJB
	EquipmentDAO equipmentDAO;

	@Inject
	FacesContext ctx;

	@PostConstruct
	public void init() {
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

		brands = getBrandList();

		fuels.add("Benzyna");
		fuels.add("Benzyna + LPG");
		fuels.add("Diesel");
		fuels.add("Elektryczny");
		fuels.add("Hybryda");

		transmissions.add("Manualna");
		transmissions.add("Automatyczna");

		bodyStyles = getBodyStyleList();

		drives.add(new SelectItem("FWD", "Na przednie koła"));
		drives.add(new SelectItem("RWD", "Na tylne koła"));
		drives.add(new SelectItem("AWD", "Na wszystkie koła 4x4"));

		for (int i = 2; i <= 6; i++) {
			doors.add((byte) i);
		}

		for (int i = 1; i <= 9; i++) {
			seats.add((byte) i);
		}

		colors.add("Beżowy");
		colors.add("Biały");
		colors.add("Bordowy");
		colors.add("Brązowy");
		colors.add("Czarny");
		colors.add("Czerwony");
		colors.add("Fioletowy");
		colors.add("Niebieski");
		colors.add("Srebrny");
		colors.add("Szary");
		colors.add("Zielony");
		colors.add("Złoty");
		colors.add("Żółty");

		colorTypes.add("Matowy");
		colorTypes.add("Metalik");
		colorTypes.add("Perłowy");

		equipments = getEquipmentList();

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
				for (SortMeta sortMeta : sortBy.values()) {
					sortMap.put(sortMeta.getField(), sortMeta.getOrder().toString());
				}

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
					filter.add(new SelectFilter("price", "priceFrom", offerFilterFrom.getPrice(),
							SelectType.GREATER_EQUAL_THAN));
				}
				if (offerFilterTo.getPrice() != null) {
					filter.add(
							new SelectFilter("price", "priceTo", offerFilterTo.getPrice(), SelectType.LESS_EQUAL_THAN));
				}
				if (offerFilterFrom.getProductionYear() != null) {
					filter.add(new SelectFilter("productionYear", "productionYearFrom",
							offerFilterFrom.getProductionYear(), SelectType.GREATER_EQUAL_THAN));
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
					filter.add(new SelectFilter("mileage", "mileageTo", offerFilterTo.getMileage(),
							SelectType.LESS_EQUAL_THAN));
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
					filter.add(
							new SelectFilter("isRightHandDrive", offerFilter.getIsRightHandDrive(), SelectType.NORMAL));
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

				offers = offerDAO.getLazyList(sortMap, filter, offset, pageSize);

				int rowCount = (int) offerDAO.countLazyList(filter);
				setRowCount(rowCount);

				return offers;
			}
		};
	}

	public List<Offer> getFullList() {
		return offerDAO.getFullList();
	}

	public String archiveOffer() {
		System.out.println("archive: " + selectedOffer.getIdOffer());

		if (selectedOffer.getArchived()) {
			ctx.addMessage("offerTable",
					new FacesMessage(FacesMessage.SEVERITY_WARN, "Ogłoszenie jest już zakończone!", null));
			return PAGE_STAY_AT_THE_SAME;
		}

		Offer offer = selectedOffer;
		offer.setArchived(true);
		offerDAO.merge(offer);
		selectedOffer = null;
		ctx.addMessage("offerTable",
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Pomyślnie zakończono ogłoszenie!", null));
		return PAGE_STAY_AT_THE_SAME;
	}

	public List<Brand> getBrandList() {
		return brandDAO.getFullList();
	}

	public void changeBrand() {
		if (offerFilter.getBrand() != null && offerFilter.getBrand().getIdBrand() != 0) {
			models = modelDAO.getModelsByBrandID(offerFilter.getBrand().getIdBrand());
		} else {
			models = null;
			generations = null;
		}
	}

	public void changeModel() {
		if (offerFilter.getModel() != null && offerFilter.getModel().getIdModel() != 0) {
			generations = generationDAO.getGenerationsByModelID(offerFilter.getModel().getIdModel());
		} else {
			generations = null;
		}
	}

	public List<BodyStyle> getBodyStyleList() {
		return bodyStyleDAO.getFullList();
	}

	public List<Equipment> getEquipmentList() {
		return equipmentDAO.getFullList();
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
	}
}
