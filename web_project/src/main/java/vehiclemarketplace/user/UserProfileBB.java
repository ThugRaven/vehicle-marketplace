package vehiclemarketplace.user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import vehiclemarketplace.classes.SelectFilter;
import vehiclemarketplace.classes.SelectType;
import vehiclemarketplace.dao.OfferDAO;
import vehiclemarketplace.dao.UserDAO;
import vehiclemarketplace.entities.BodyStyle;
import vehiclemarketplace.entities.Brand;
import vehiclemarketplace.entities.Generation;
import vehiclemarketplace.entities.Model;
import vehiclemarketplace.entities.Offer;
import vehiclemarketplace.entities.User;

@Named
@RequestScoped
public class UserProfileBB {
	private static final String PAGE_STAY_AT_THE_SAME = null;

	private User user = new User();

	private LazyDataModel<Offer> lazyOffers;

	public User getUser() {
		return user;
	}

	public LazyDataModel<Offer> getLazyOffers() {
		return lazyOffers;
	}

	@Inject
	ExternalContext extcontext;

	@EJB
	UserDAO userDAO;

	@EJB
	OfferDAO offerDAO;

	@Inject
	FacesContext ctx;

	public void onLoad() throws IOException {
		if (!ctx.isPostback()) {
			User loaded = null;
			if (!ctx.isValidationFailed() && user.getIdUser() != 0) {
				loaded = userDAO.find(user.getIdUser());
			}
			if (loaded != null) {
				user = loaded;
			} else {
				ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Błędne użycie systemu", null));
			}
		}
	}

	@PostConstruct
	public void init() {
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
				filter.add(new SelectFilter("user.idUser", "idUser", user.getIdUser(), SelectType.NORMAL));

				offers = offerDAO.getLazyList(sortMap, filter, offset, pageSize);

				int rowCount = (int) offerDAO.countLazyList(filter);
				setRowCount(rowCount);

				return offers;
			}
		};
	}

	public int userCountOffers() {
		return offerDAO.getOffersByUserID(user.getIdUser()).size();
	}
}
