package vehiclemarketplace.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
}
