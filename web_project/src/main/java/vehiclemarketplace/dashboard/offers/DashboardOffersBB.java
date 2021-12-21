package vehiclemarketplace.dashboard.offers;

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
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import vehiclemarketplace.classes.SelectFilter;
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

@Named("dashOffersBB")
@ViewScoped
public class DashboardOffersBB implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final String PAGE_STAY_AT_THE_SAME = null;
	private static final String PAGE_OFFER = "/pages/public/offer?faces-redirect=true";

	private LazyDataModel<Offer> lazyOffers;

	private Offer selectedOffer;

	private Offer offerFilter = new Offer();

	private List<Brand> brands;
	private List<Model> models;
	private List<Generation> generations;
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

	public Offer getOfferFilter() {
		return offerFilter;
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
	FacesContext ctx;

	@PostConstruct
	public void init() {
		offerFilter.setUser(new User());
		offerFilter.setBrand(new Brand());
		offerFilter.setModel(new Model());
		offerFilter.setGeneration(new Generation());
		offerFilter.setBodyStyle(new BodyStyle());

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
				for (SortMeta sortMeta : sortBy.values()) {
					sortMap.put(sortMeta.getField(), sortMeta.getOrder().toString());
				}

				List<SelectFilter> filter = new ArrayList<>();
				if (offerFilter.getIdOffer() != 0) {
					filter.add(new SelectFilter("idOffer", offerFilter.getIdOffer(), SelectType.NORMAL));
				}
				if (offerFilter.getUser() != null && offerFilter.getUser().getIdUser() != 0) {
					filter.add(new SelectFilter("user.idUser", "idUser", offerFilter.getUser().getIdUser(),
							SelectType.NORMAL));
				}
				if (offerFilter.getCity() != null && !offerFilter.getCity().isEmpty()) {
					filter.add(new SelectFilter("city", offerFilter.getCity(), SelectType.LIKE_FULL));
				}
				if (offerFilter.getVin() != null && !offerFilter.getVin().isEmpty()) {
					filter.add(new SelectFilter("vin", offerFilter.getVin(), SelectType.LIKE_FULL));
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
				if (offerFilter.getBodyStyle() != null && offerFilter.getBodyStyle().getIdBodyStyle() != 0) {
					filter.add(new SelectFilter("bodyStyle.idBodyStyle", "idBodyStyle",
							offerFilter.getBodyStyle().getIdBodyStyle(), SelectType.NORMAL));
				}
				if (offerFilter.getArchived() != null) {
					filter.add(new SelectFilter("archived", offerFilter.getArchived(), SelectType.NORMAL));
				}

				offers = offerDAO.getLazyList(sortMap, filter, offset, pageSize);

				int rowCount = (int) offerDAO.countLazyList(filter);
				setRowCount(rowCount);

				return offers;
			}
		};
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

	public void clearFilters() {
		offerFilter = new Offer();
		offerFilter.setUser(new User());
		offerFilter.setBrand(new Brand());
		offerFilter.setModel(new Model());
		offerFilter.setGeneration(new Generation());
		offerFilter.setBodyStyle(new BodyStyle());
	}

	public String offer() {
		String offerUrl = PAGE_OFFER + "c=" + selectedOffer.getBrand().getName() + " "
				+ selectedOffer.getModel().getName() + " " + selectedOffer.getTitle() + "&o="
				+ selectedOffer.getIdOffer();
		System.out.println(offerUrl);
		return offerUrl;
	}
}
