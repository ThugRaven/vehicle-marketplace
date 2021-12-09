package vehiclemarketplace.dao;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import vehiclemarketplace.classes.SelectFilter;
import vehiclemarketplace.classes.SelectUtilities;
import vehiclemarketplace.entities.Brand;
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

	public long countFullList() {
		long count = 0;

		Query query = em.createQuery("SELECT COUNT(u) FROM User u");

		try {
			count = (long) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return count;
	}

	public List<User> getLazyFullList(Map<String, String> sortBy, List<SelectFilter> filter, int offset, int pageSize) {
		List<User> list = null;

		SelectUtilities selectUtilities = new SelectUtilities("u");
		String order = selectUtilities.getOrderBy(sortBy);
		String where = selectUtilities.getWhere(filter);

		Query query = em.createQuery("SELECT u FROM User u" + where + order).setFirstResult(offset)
				.setMaxResults(pageSize);
		query = selectUtilities.setParameters(query, filter);

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
