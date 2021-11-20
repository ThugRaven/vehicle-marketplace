package vehiclemarketplace.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import vehiclemarketplace.entities.Generation;

@Stateless
public class GenerationDAO {
	private final static String UNIT_NAME = "vehicleMarketplacePU";

	@PersistenceContext(unitName = UNIT_NAME)
	protected EntityManager em;

	public void create(Generation generation) {
		em.persist(generation);
	}

	public Generation merge(Generation generation) {
		return em.merge(generation);
	}

	public void remove(Generation generation) {
		em.remove(em.merge(generation));
	}

	public Generation find(Object id) {
		return em.find(Generation.class, id);
	}

	public List<Generation> getFullList() {
		List<Generation> list = null;

		Query query = em.createQuery("SELECT g FROM Generation g");

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
}
