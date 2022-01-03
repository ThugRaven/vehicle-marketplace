package vehiclemarketplace.offer;

import java.io.IOException;
import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
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
}
