package vehiclemarketplace.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import vehiclemarketplace.entities.UserRole;

@Stateless
public class UserRoleDAO {
	private final static String UNIT_NAME = "vehicleMarketplacePU";

	@PersistenceContext(unitName = UNIT_NAME)
	protected EntityManager em;

	public void create(UserRole userRole) {
		em.persist(userRole);
	}

	public UserRole merge(UserRole userRole) {
		return em.merge(userRole);
	}

	public void remove(UserRole userRole) {
		em.remove(em.merge(userRole));
	}

	public UserRole find(Object id) {
		return em.find(UserRole.class, id);
	}

	public List<UserRole> getFullList() {
		List<UserRole> list = null;

		Query query = em.createQuery("SELECT u FROM UserRole u");

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
}
