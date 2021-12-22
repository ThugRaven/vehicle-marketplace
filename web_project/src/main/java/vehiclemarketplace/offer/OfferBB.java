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

	@Inject
	FacesContext ctx;

	public void onLoad() throws IOException {
		if (!ctx.isPostback()) {
			Offer loaded = null;
			if (!ctx.isValidationFailed() && offer.getIdOffer() != 0) {
				loaded = offerDAO.find(offer.getIdOffer());
			}
			if (loaded != null) {
				offer = loaded;
			} else {
				ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Błędne użycie systemu", null));
			}
		}
	}
}
