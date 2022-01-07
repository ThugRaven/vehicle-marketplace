package vehiclemarketplace.main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.LazyDataModel;

import vehiclemarketplace.ConstantsBB;
import vehiclemarketplace.classes.SelectFilter;
import vehiclemarketplace.classes.SelectItemCount;
import vehiclemarketplace.classes.SelectType;
import vehiclemarketplace.dao.BodyStyleDAO;
import vehiclemarketplace.dao.BrandDAO;
import vehiclemarketplace.dao.GenerationDAO;
import vehiclemarketplace.dao.ModelDAO;
import vehiclemarketplace.dao.OfferDAO;
import vehiclemarketplace.dao.UserDAO;
import vehiclemarketplace.dao.UserRoleDAO;
import vehiclemarketplace.entities.BodyStyle;
import vehiclemarketplace.entities.Brand;
import vehiclemarketplace.entities.Generation;
import vehiclemarketplace.entities.Model;
import vehiclemarketplace.entities.Offer;
import vehiclemarketplace.entities.User;

@Named
@ViewScoped
public class MainBB implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final String PAGE_STAY_AT_THE_SAME = null;
	private static final String PAGE_OFFERS = "/pages/public/offers?faces-redirect=true";

	private LazyDataModel<Offer> lazyOffers;
	private int countList;

	private Offer selectedOffer;

	private Offer offerFilter = new Offer();
	private Offer offerFilterFrom = new Offer();
	private Offer offerFilterTo = new Offer();

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

	@Inject
	ConstantsBB constantsBB;

	@Inject
	FacesContext ctx;

	@Inject
	Flash flash;

	@PostConstruct
	public void init() {
		System.out.println("INIT");
		offerFilter.setUser(new User());
		offerFilter.setBrand(new Brand());
		offerFilter.setModel(new Model());
		offerFilter.setGeneration(new Generation());
		offerFilter.setBodyStyle(new BodyStyle());

		countLazyList();

		brands = getBrandList();

		bodyStyles = getBodyStyleList();
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
		countLazyList();
	}

	public List<Offer> mainViewList() {
		List<Offer> offers;

		Map<String, String> sortMap = new HashMap<String, String>();
		sortMap.put("createTime", "DESCENDING");

		List<SelectFilter> filter = new ArrayList<>();
		filter.add(new SelectFilter("archived", false, SelectType.NORMAL));

		offers = offerDAO.getLazyList(sortMap, filter, 0, 10);

		return offers;
	}

	public String showOffers() {
		flash.put("offerFilter", offerFilter);
		flash.put("offerFilterFrom", offerFilterFrom);
		flash.put("offerFilterTo", offerFilterTo);

		return PAGE_OFFERS;
	}
}
