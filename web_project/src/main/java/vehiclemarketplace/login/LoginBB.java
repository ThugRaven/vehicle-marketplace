package vehiclemarketplace.login;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.simplesecurity.RemoteClient;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import vehiclemarketplace.dao.UserDAO;
import vehiclemarketplace.dao.UserRoleDAO;
import vehiclemarketplace.entities.User;

@Named
@RequestScoped
public class LoginBB {
	private static final String PAGE_MAIN = "/pages/public/main?faces-redirect=true";
	private static final String PAGE_LOGIN = "/pages/public/main?faces-redirect=true";
	private static final String PAGE_STAY_AT_THE_SAME = null;

	private String login;
	private String password;

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Inject
	ExternalContext extcontext;

	@Inject
	Flash flash;

	@EJB
	UserDAO userDAO;

	@EJB
	UserRoleDAO userRoleDAO;

	@Inject
	FacesContext ctx;

	public String doLogin() {
		FacesContext ctx = FacesContext.getCurrentInstance();

		User user = userDAO.getUserByLogin(login);

		if (user == null) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Niepoprawny login lub hasło", null));
			return PAGE_STAY_AT_THE_SAME;
		}

		boolean matched = false;
		try {
			matched = validatePassword(password, user.getPassword());
			System.out.println(matched);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
			ctx.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, "Błąd przy przetwarzaniu logowania!", null));
			return PAGE_STAY_AT_THE_SAME;
		}

		if (!matched) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Niepoprawny login lub hasło", null));
			return PAGE_STAY_AT_THE_SAME;
		}

		RemoteClient<User> client = new RemoteClient<User>();
		client.setDetails(user);

		String role = user.getUserRole().getRoleName();

		if (role != null) {
			client.getRoles().add(role);
		}

		HttpServletRequest request = (HttpServletRequest) ctx.getExternalContext().getRequest();
		client.store(request);

		return PAGE_MAIN;
	}

	public String doLogout() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		session.invalidate();
		return PAGE_LOGIN;
	}

	private boolean validatePassword(String originalPassword, String storedPassword)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		String[] parts = storedPassword.split(":");
		int iterations = 10000;

		byte[] salt = fromHex(parts[0]);
		byte[] hash = fromHex(parts[1]);

		PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(), salt, iterations, hash.length * 8);
		SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
		byte[] testHash = skf.generateSecret(spec).getEncoded();

		int diff = hash.length ^ testHash.length;
		for (int i = 0; i < hash.length && i < testHash.length; i++) {
			diff |= hash[i] ^ testHash[i];
		}
		return diff == 0;
	}

	private byte[] fromHex(String hex) throws NoSuchAlgorithmException {
		byte[] bytes = new byte[hex.length() / 2];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
		}
		return bytes;
	}
}
