package vehiclemarketplace.dao;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import vehiclemarketplace.entities.Brand;
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

	public List<Model> getLazyModelsByBrandID(Map<String, String> sortBy, int id, int offset, int pageSize) {
		List<Model> list = null;

		String order = getOrderBy(sortBy);
		System.out.println(order);
		Query query = em.createQuery("SELECT m FROM Model m WHERE m.brand.idBrand = :id" + order).setFirstResult(offset)
				.setMaxResults(pageSize);
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
				orderBy = orderBy.concat("m." + field + " " + order);
			}
		}

		return orderBy;
	}

	public long countModelsByBrandID(int id) {
		long count = 0;

		Query query = em.createQuery("SELECT COUNT(m) FROM Model m WHERE m.brand.idBrand = :id");
		query.setParameter("id", id);

		try {
			count = (long) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return count;
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

	public Model getModelByNameAndBrandID(String name, int id) {
		Model model = null;

		Query query = em.createQuery("SELECT m FROM Model m WHERE m.name = :name AND m.brand.idBrand = :id");
		query.setParameter("name", name);
		query.setParameter("id", id);

		try {
			model = (Model) query.getSingleResult();
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		return model;
	}
}
