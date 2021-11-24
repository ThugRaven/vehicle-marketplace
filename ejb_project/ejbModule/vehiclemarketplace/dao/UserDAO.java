package vehiclemarketplace.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import vehiclemarketplace.entities.User;

@Stateless
public class UserDAO {
	private final static String UNIT_NAME = "vehicleMarketplacePU";

	@PersistenceContext(unitName = UNIT_NAME)
	protected EntityManager em;

	public void create(User user) {
		em.persist(user);
	}

	public User merge(User user) {
		return em.merge(user);
	}

	public void remove(User user) {
		em.remove(em.merge(user));
	}

	public User find(Object id) {
		return em.find(User.class, id);
	}

	public List<User> getFullList() {
		List<User> list = null;

		Query query = em.createQuery("SELECT u FROM User u");

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public User getUserByLogin(String login) {
		User user = null;

		Query query = em.createQuery("SELECT u FROM User u WHERE u.login = :login");
		query.setParameter("login", login);

		try {
			user = (User) query.getSingleResult();
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		return user;
	}
}
