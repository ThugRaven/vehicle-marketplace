package vehiclemarketplace.user;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.Flash;
import javax.inject.Inject;
import javax.inject.Named;

import vehiclemarketplace.dao.UserDAO;
import vehiclemarketplace.entities.User;

@Named
@RequestScoped
public class UserListBB {
//	private static final String PAGE_PERSON_EDIT = "personEdit?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;

	private String surname;

	@Inject
	ExternalContext extcontext;

	@Inject
	Flash flash;

	@EJB
	UserDAO userDAO;

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public List<User> getFullList() {
		return userDAO.getFullList();
	}
}
