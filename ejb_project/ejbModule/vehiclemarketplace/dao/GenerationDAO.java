package vehiclemarketplace.dao;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import vehiclemarketplace.entities.Generation;
import vehiclemarketplace.entities.Model;

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

	public List<Generation> getLazyGenerationsByModelID(Map<String, String> sortBy, int id, int offset, int pageSize) {
		List<Generation> list = null;

		String order = getOrderBy(sortBy);
		System.out.println(order);
		Query query = em.createQuery("SELECT g FROM Generation g WHERE g.model.idModel = :id" + order)
				.setFirstResult(offset).setMaxResults(pageSize);
		query.setParameter("id", id);

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public String getOrderBy(Map<String, String> sortBy) {
		String orderBy = "";

		if (sortBy != null) {
			for (Map.Entry<String, String> entry : sortBy.entrySet()) {
				String field = entry.getKey();
				String order = entry.getValue();

				order = order.equals("ASCENDING") ? "ASC" : "DESC";

				if (orderBy.isEmpty()) {
					orderBy = " ORDER BY ";
				} else {
					orderBy = orderBy.concat(", ");
				}
				orderBy = orderBy.concat("g." + field + " " + order);
			}
		}

		return orderBy;
	}

	public long countGenerationsByModelID(int id) {
		long count = 0;

		Query query = em.createQuery("SELECT COUNT(g) FROM Generation g WHERE g.model.idModel = :id");
		query.setParameter("id", id);

		try {
			count = (long) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return count;
	}

	public List<Generation> getGenerationsByModelID(int id) {
		List<Generation> list = null;

		Query query = em.createQuery("SELECT g FROM Generation g WHERE g.model.idModel = :id");
		query.setParameter("id", id);

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public Generation getGenerationByNameAndModelID(String name, int id) {
		Generation generation = null;

		Query query = em.createQuery("SELECT g FROM Generation g WHERE g.name = :name AND g.model.idModel = :id");
		query.setParameter("name", name);
		query.setParameter("id", id);

		try {
			generation = (Generation) query.getSingleResult();
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		return generation;
	}
}
