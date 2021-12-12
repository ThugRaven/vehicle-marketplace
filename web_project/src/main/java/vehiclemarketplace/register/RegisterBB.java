package vehiclemarketplace.register;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.New;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;

import vehiclemarketplace.dao.UserDAO;
import vehiclemarketplace.dao.UserRoleDAO;
import vehiclemarketplace.entities.User;
import vehiclemarketplace.entities.UserRole;

@Named
@RequestScoped
public class RegisterBB {
//	private static final String PAGE_PERSON_EDIT = "personEdit?faces-redirect=true";
	private static final String PAGE_MAIN = "/pages/public/main?faces-redirect=true";
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

	@Inject
	FacesContext ctx;

	public String register() {
		if (userDAO.getUserByLogin(user.getLogin()) != null) {
			ctx.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Użytkownik o podanym loginie już istnieje!", null));
			return PAGE_STAY_AT_THE_SAME;
		}

		UserRole userRole = userRoleDAO.find(1);
		user.setUserRole(userRole);

		try {
			user.setPassword(generatePasswordHash(user.getPassword()));
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Błąd przy rejestracji konta!", null));
			return PAGE_STAY_AT_THE_SAME;
		}

		user.setArchived(false);
		userDAO.create(user);
		return PAGE_MAIN;
	}

	private String generatePasswordHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
		int iterations = 10000;
		char[] chars = password.toCharArray();
		byte[] salt = getSalt();
		int keyLength = 64; // In characters

		PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, keyLength * 4);
		SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");

		byte[] hash = skf.generateSecret(spec).getEncoded();
		System.out.println(toHex(salt) + ":" + toHex(hash));
		return toHex(salt) + ":" + toHex(hash);
	}

	private byte[] getSalt() throws NoSuchAlgorithmException {
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		int saltLength = 8; // In characters
		byte[] salt = new byte[saltLength / 2];
		sr.nextBytes(salt);
		return salt;
	}

	private String toHex(byte[] array) throws NoSuchAlgorithmException {
		BigInteger bi = new BigInteger(1, array);
		String hex = bi.toString(16);

		int paddingLength = (array.length * 2) - hex.length();
		if (paddingLength > 0) {
			return String.format("%0" + paddingLength + "d", 0) + hex;
		} else {
			return hex;
		}
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
