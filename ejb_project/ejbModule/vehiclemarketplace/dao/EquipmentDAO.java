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
import vehiclemarketplace.entities.Equipment;

@Stateless
public class EquipmentDAO {
	private final static String UNIT_NAME = "vehicleMarketplacePU";

	@PersistenceContext(unitName = UNIT_NAME)
	protected EntityManager em;

	public void create(Equipment equipment) {
		em.persist(equipment);
	}

	public Equipment merge(Equipment equipment) {
		return em.merge(equipment);
	}

	public void remove(Equipment equipment) {
		em.remove(em.merge(equipment));
	}

	public Equipment find(Object id) {
		return em.find(Equipment.class, id);
	}

	public List<Equipment> getFullList() {
		List<Equipment> list = null;

		Query query = em.createQuery("SELECT e FROM Equipment e");

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public List<Equipment> getLazyList(Map<String, String> sortBy, List<SelectFilter> filter, int offset,
			int pageSize) {
		List<Equipment> list = null;

		SelectUtilities selectUtilities = new SelectUtilities("e");
		String order = selectUtilities.getOrder(sortBy);
		String where = selectUtilities.getWhere(filter);

		Query query = em.createQuery("SELECT e FROM Equipment e" + where + order).setFirstResult(offset)
				.setMaxResults(pageSize);
		query = selectUtilities.getParameters(query, filter);

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public long countLazyList(List<SelectFilter> filter) {
		long count = 0;

		SelectUtilities selectUtilities = new SelectUtilities("e");
		String where = selectUtilities.getWhere(filter);

		Query query = em.createQuery("SELECT COUNT(e) FROM Equipment e" + where);
		query = selectUtilities.getParameters(query, filter);

		try {
			count = (long) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return count;
	}

	public Equipment getEquipmentByName(String name) {
		Equipment equipment = null;

		Query query = em.createQuery("SELECT e FROM Equipment e WHERE e.name = :name");
		query.setParameter("name", name);

		try {
			equipment = (Equipment) query.getSingleResult();
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		return equipment;
	}
}
