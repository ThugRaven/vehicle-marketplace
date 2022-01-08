package vehiclemarketplace.offer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.simplesecurity.RemoteClient;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import vehiclemarketplace.dao.BodyStyleDAO;
import vehiclemarketplace.dao.BrandDAO;
import vehiclemarketplace.dao.EquipmentDAO;
import vehiclemarketplace.dao.GenerationDAO;
import vehiclemarketplace.dao.ModelDAO;
import vehiclemarketplace.dao.OfferDAO;
import vehiclemarketplace.dao.UserDAO;
import vehiclemarketplace.entities.BodyStyle;
import vehiclemarketplace.entities.Brand;
import vehiclemarketplace.entities.Equipment;
import vehiclemarketplace.entities.Generation;
import vehiclemarketplace.entities.Model;
import vehiclemarketplace.entities.Offer;
import vehiclemarketplace.entities.User;

@Named
@ViewScoped
public class OfferEditBB implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final String PAGE_STAY_AT_THE_SAME = null;
	private static final String PAGE_OFFER = "/pages/public/offer?faces-redirect=true";
	private static final String PAGE_OFFERS = "/pages/public/offers?faces-redirect=true";

	private Offer offer = new Offer();

	private List<Brand> brands;
	private List<Model> models;
	private List<Generation> generations;
	private List<BodyStyle> bodyStyles;
	private List<Equipment> equipments;

	public Offer getOffer() {
		return offer;
	}

	public void setOffer(Offer offer) {
		this.offer = offer;
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

	public List<Equipment> getEquipments() {
		return equipments;
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

	@EJB
	EquipmentDAO equipmentDAO;

	@EJB
	UserDAO userDAO;

	@Inject
	FacesContext ctx;

	@Inject
	UserFileManager userFileManager;

	public String onLoad() {
		Offer loaded = null;
		if (offer.getIdOffer() != 0) {
			loaded = offerDAO.find(offer.getIdOffer());
		}
		if (loaded != null) {
			if (loaded.getArchived()) {
				System.out.println("Offer archived!");

				ctx.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_WARN, "Podana oferta została już zakończona!", null));
				extcontext.getFlash().setKeepMessages(true);
				return PAGE_OFFERS;
			}

			HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
			RemoteClient<User> client = (RemoteClient<User>) RemoteClient.load(session);
			System.out.println("Client: " + client.getDetails().getLogin() + " " + client.getDetails().getIdUser());

			if (loaded.getUser().getIdUser() != client.getDetails().getIdUser() && !client.isInRole("admin")) {
				System.out.println("No permission!");

				String offerUrl = PAGE_OFFER + "c=" + loaded.getBrand().getName() + " " + loaded.getModel().getName()
						+ " " + loaded.getTitle() + "&o=" + loaded.getIdOffer();
				ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Brak uprawnień!", null));
				extcontext.getFlash().setKeepMessages(true);
				return offerUrl;
			}

			offer = loaded;
			changeBrand();
			changeModel();
			offer.setEquipments(equipmentDAO.getEquipmentsByOfferID(offer.getIdOffer()));
		} else {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Brak podanej oferty!", null));
			extcontext.getFlash().setKeepMessages(true);
			return PAGE_OFFERS;
		}

		return PAGE_STAY_AT_THE_SAME;
	}

	@PostConstruct
	public void init() {
		offer.setBrand(new Brand());
		offer.setModel(new Model());
		offer.setGeneration(new Generation());
		offer.setBodyStyle(new BodyStyle());
		offer.setEquipments(new ArrayList<>());

		brands = getBrandList();

		bodyStyles = getBodyStyleList();

		equipments = getEquipmentList();
		System.out.println(equipments.toString());
	}

	public List<Brand> getBrandList() {
		return brandDAO.getFullList();
	}

	public void changeBrand() {
		if (offer.getBrand() != null && offer.getBrand().getIdBrand() != 0) {
			models = modelDAO.getModelsByBrandID(offer.getBrand().getIdBrand());
		} else {
			models = null;
			generations = null;
		}
	}

	public void changeModel() {
		if (offer.getModel() != null && offer.getModel().getIdModel() != 0) {
			generations = generationDAO.getGenerationsByModelID(offer.getModel().getIdModel());
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

	public String editOffer() {
		String offerUrl = PAGE_OFFER + "c=" + offer.getBrand().getName() + " " + offer.getModel().getName() + " "
				+ offer.getTitle() + "&o=" + offer.getIdOffer();

		if (offer.getLicensePlate().isEmpty()) {
			offer.setLicensePlate(null);
		}
		offerDAO.merge(offer);

		ctx.addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Pomyślnie zaktualizowano ogłoszenie!", null));
		extcontext.getFlash().setKeepMessages(true);

		return offerUrl;
	}
}
