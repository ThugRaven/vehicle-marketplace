package vehiclemarketplace.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import vehiclemarketplace.entities.Model;
import vehiclemarketplace.entities.User;

@Stateless
public class ModelDAO {
	private final static String UNIT_NAME = "vehicleMarketplacePU";

	@PersistenceContext(unitName = UNIT_NAME)
	protected EntityManager em;

	public void create(Model model) {
		em.persist(model);
	}

	public Model merge(Model model) {
		return em.merge(model);
	}

	public void remove(Model model) {
		em.remove(em.merge(model));
	}

	public Model find(Object id) {
		return em.find(Model.class, id);
	}

	public List<Model> getFullList() {
		List<Model> list = null;

		Query query = em.createQuery("SELECT m FROM Model m");

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
	
	public List<Model> getModelsByBrandID(int id) {
		List<Model> list = null;

		Query query = em.createQuery("SELECT m FROM Model m WHERE m.brand.idBrand = :id");
		query.setParameter("id", id);

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
}
