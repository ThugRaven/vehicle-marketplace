package vehiclemarketplace.register;

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

import vehiclemarketplace.dao.UserDAO;
import vehiclemarketplace.dao.UserRoleDAO;
import vehiclemarketplace.entities.User;
import vehiclemarketplace.entities.UserRole;

@Named
@RequestScoped
public class RegisterBB {
//	private static final String PAGE_PERSON_EDIT = "personEdit?faces-redirect=true";
	private static final String PAGE_MAIN = "/index?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;

	private User user = new User();

	public User getUser() {
		return user;
	}

	@Inject
	ExternalContext extcontext;

	@Inject
	Flash flash;

	@EJB
	UserDAO userDAO;
	
	@EJB
	UserRoleDAO userRoleDAO;

	public List<User> getFullList() {
		return userDAO.getFullList();
	}
	
	public String register(){
		User testUser = new User();
		testUser.setArchived((byte) 0);
		UserRole userRole = userRoleDAO.find(2);
		testUser.setUserRole(userRole);
		testUser.setName("test");
		testUser.setSurname("testowe");
		testUser.setPhoneNumber("48123456789");
		testUser.setLogin(user.getLogin());
		testUser.setPassword(user.getPassword());
		// https://howtodoinjava.com/java/java-security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/
		testUser.setEmail(user.getEmail());
		userDAO.create(testUser);
		return PAGE_STAY_AT_THE_SAME;
	}
}
