package vehiclemarketplace.dashboard.users;

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
import javax.faces.context.Flash;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import vehiclemarketplace.classes.SelectFilter;
import vehiclemarketplace.classes.SelectType;
import vehiclemarketplace.dao.BrandDAO;
import vehiclemarketplace.dao.OfferDAO;
import vehiclemarketplace.dao.UserDAO;
import vehiclemarketplace.dao.UserRoleDAO;
import vehiclemarketplace.entities.Brand;
import vehiclemarketplace.entities.User;
import vehiclemarketplace.entities.UserRole;

@Named("dashUsersBB")
@ViewScoped
public class DashboardUsersBB implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final String PAGE_STAY_AT_THE_SAME = null;

	private LazyDataModel<User> lazyUsers;

	private User selectedUser;

	private User userFilter = new User();
	private List<UserRole> roles;

	public LazyDataModel<User> getLazyUsers() {
		return lazyUsers;
	}

	public User getSelectedUser() {
		return selectedUser;
	}

	public void setSelectedUser(User selectedUser) {
		this.selectedUser = selectedUser;
	}

	public User getUserFilter() {
		return userFilter;
	}

	public List<UserRole> getRoles() {
		return roles;
	}

	@Inject
	ExternalContext extcontext;

	@Inject
	Flash flash;

	@EJB
	UserDAO userDAO;

	@EJB
	OfferDAO offerDAO;

	@EJB
	UserRoleDAO userRoleDAO;

	@Inject
	FacesContext ctx;

	@PostConstruct
	public void init() {
		userFilter.setUserRole(new UserRole());
		roles = getRolesList();
		lazyUsers = new LazyDataModel<User>() {
			private static final long serialVersionUID = 1L;

			private List<User> users;

			@Override
			public User getRowData(String rowKey) {
				for (User user : users) {
					if (user.getIdUser() == Integer.parseInt(rowKey)) {
						System.out.println(user.getLogin());
						return user;
					}
				}
				return null;
			}

			@Override
			public String getRowKey(User user) {
				return String.valueOf(user.getIdUser());
			}

			@Override
			public List<User> load(int offset, int pageSize, Map<String, SortMeta> sortBy,
					Map<String, FilterMeta> filterBy) {

				Map<String, String> sortMap = new HashMap<String, String>();
				for (SortMeta sortMeta : sortBy.values()) {
					sortMap.put(sortMeta.getField(), sortMeta.getOrder().toString());
				}

				List<SelectFilter> filter = new ArrayList<>();
				if (userFilter.getName() != null && !userFilter.getName().isEmpty()) {
					filter.add(new SelectFilter("name", userFilter.getName(), SelectType.LIKE));
				}
				if (userFilter.getSurname() != null && !userFilter.getSurname().isEmpty()) {
					filter.add(new SelectFilter("surname", userFilter.getSurname(), SelectType.LIKE));
				}
				if (userFilter.getUserRole() != null && userFilter.getUserRole().getIdUserRole() != 0) {
					filter.add(new SelectFilter("userRole.idUserRole", "idUserRole",
							userFilter.getUserRole().getIdUserRole(), SelectType.NORMAL));
				}
				if (userFilter.getArchived() != null) {
					filter.add(new SelectFilter("archived", userFilter.getArchived(), SelectType.NORMAL));
				}

				users = userDAO.getLazyFullList(sortMap, filter, offset, pageSize);

				int rowCount = (int) userDAO.countFullList();
				setRowCount(rowCount);

				return users;
			}
		};
	}

	public String blockUser() {
		System.out.println("block: " + selectedUser.getIdUser() + " " + selectedUser.getLogin());

		if (selectedUser.getArchived()) {
			ctx.addMessage("userTable",
					new FacesMessage(FacesMessage.SEVERITY_WARN, "Użytkownik jest już zablokowany!", null));
			return PAGE_STAY_AT_THE_SAME;
		}

		User user = selectedUser;
		user.setArchived(true);
		userDAO.merge(user);
		selectedUser = null;
		ctx.addMessage("userTable",
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Pomyślnie zablokowano użytkownika!", null));
		return PAGE_STAY_AT_THE_SAME;
	}

	public String unblockUser() {
		System.out.println("unblock: " + selectedUser.getIdUser() + " " + selectedUser.getLogin());

		if (!selectedUser.getArchived()) {
			ctx.addMessage("userTable",
					new FacesMessage(FacesMessage.SEVERITY_WARN, "Użytkownik jest już odblokowany!", null));
			return PAGE_STAY_AT_THE_SAME;
		}

		User user = selectedUser;
		user.setArchived(false);
		userDAO.merge(user);
		selectedUser = null;
		ctx.addMessage("userTable",
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Pomyślnie odblokowano użytkownika!", null));
		return PAGE_STAY_AT_THE_SAME;
	}

	public int userCountOffers() {
		return offerDAO.getOffersByUserID(selectedUser.getIdUser()).size();
	}

	public List<UserRole> getRolesList() {
		return userRoleDAO.getFullList();
	}
}
