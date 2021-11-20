package vehiclemarketplace.offer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.servlet.http.HttpSession;

import vehiclemarketplace.dao.OfferDAO;
import vehiclemarketplace.entities.Offer;

@Named
@RequestScoped
public class OfferListBB {
//	private static final String PAGE_PERSON_EDIT = "personEdit?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;

	@Inject
	ExternalContext extcontext;

	@Inject
	Flash flash;

	@EJB
	OfferDAO offerDAO;

	public List<Offer> getFullList() {
		return offerDAO.getFullList();
	}
}
