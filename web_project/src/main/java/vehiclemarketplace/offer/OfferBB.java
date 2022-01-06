package vehiclemarketplace.offer;

import java.io.IOException;
import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import vehiclemarketplace.dao.BodyStyleDAO;
import vehiclemarketplace.dao.BrandDAO;
import vehiclemarketplace.dao.EquipmentDAO;
import vehiclemarketplace.dao.GenerationDAO;
import vehiclemarketplace.dao.ModelDAO;
import vehiclemarketplace.dao.OfferDAO;
import vehiclemarketplace.dao.UserDAO;
import vehiclemarketplace.dao.UserRoleDAO;
import vehiclemarketplace.entities.BodyStyle;
import vehiclemarketplace.entities.Generation;
import vehiclemarketplace.entities.Model;
import vehiclemarketplace.entities.Offer;

@Named
@ViewScoped
public class OfferBB implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final String PAGE_STAY_AT_THE_SAME = null;
	private static final String PAGE_OFFERS = "/pages/public/offers?faces-redirect=true";

	private Offer offer = new Offer();

	public Offer getOffer() {
		return offer;
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

	@Inject
	Flash flash;

	public String onLoad() throws IOException {
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

			offer = loaded;
			offer.setEquipments(equipmentDAO.getEquipmentsByOfferID(offer.getIdOffer()));
		} else {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Brak podanej oferty!", null));
			extcontext.getFlash().setKeepMessages(true);
			return PAGE_OFFERS;
		}

		return PAGE_STAY_AT_THE_SAME;
	}

	public String endOffer() {
		offer.setArchived(true);
		offerDAO.merge(offer);

		ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Pomyślnie zakończono ogłoszenie!", null));
		extcontext.getFlash().setKeepMessages(true);

		return PAGE_OFFERS;
	}

	public String brandLink() {
		Offer offerFilter = new Offer();
		if (offer.getBrand() == null) {
			try {
				onLoad();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		offerFilter.setBrand(offer.getBrand());
		offerFilter.setModel(new Model());
		offerFilter.setGeneration(new Generation());
		offerFilter.setBodyStyle(new BodyStyle());
		flash.put("offerFilter", offerFilter);

		return PAGE_OFFERS;
	}

	public String modelLink() {
		Offer offerFilter = new Offer();
		if (offer.getModel() == null) {
			try {
				onLoad();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		offerFilter.setBrand(offer.getBrand());
		offerFilter.setModel(offer.getModel());
		offerFilter.setGeneration(new Generation());
		offerFilter.setBodyStyle(new BodyStyle());
		flash.put("offerFilter", offerFilter);

		return PAGE_OFFERS;
	}

	public String generationLink() {
		Offer offerFilter = new Offer();
		if (offer.getGeneration() == null) {
			try {
				onLoad();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		offerFilter.setBrand(offer.getBrand());
		offerFilter.setModel(offer.getModel());
		offerFilter.setGeneration(offer.getGeneration());
		offerFilter.setBodyStyle(new BodyStyle());
		flash.put("offerFilter", offerFilter);

		return PAGE_OFFERS;
	}

	public String fuelLink() {
		Offer offerFilter = new Offer();
		if (offer.getFuel() == null) {
			try {
				onLoad();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		offerFilter.setBrand(offer.getBrand());
		offerFilter.setModel(offer.getModel());
		offerFilter.setGeneration(offer.getGeneration());
		offerFilter.setBodyStyle(new BodyStyle());
		offerFilter.setFuel(offer.getFuel());
		flash.put("offerFilter", offerFilter);

		return PAGE_OFFERS;
	}

	public String bodyStyleLink() {
		Offer offerFilter = new Offer();
		if (offer.getBodyStyle() == null) {
			try {
				onLoad();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		offerFilter.setBrand(offer.getBrand());
		offerFilter.setModel(offer.getModel());
		offerFilter.setGeneration(offer.getGeneration());
		offerFilter.setBodyStyle(offer.getBodyStyle());
		flash.put("offerFilter", offerFilter);

		return PAGE_OFFERS;
	}
}
