package vehiclemarketplace.dao;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import vehiclemarketplace.classes.SelectUtilities;
import vehiclemarketplace.entities.Brand;
import vehiclemarketplace.entities.Model;
import vehiclemarketplace.entities.User;

@Stateless
public class BrandDAO {
	private final static String UNIT_NAME = "vehicleMarketplacePU";

	@PersistenceContext(unitName = UNIT_NAME)
	protected EntityManager em;

	public void create(Brand brand) {
		em.persist(brand);
	}

	public Brand merge(Brand brand) {
		return em.merge(brand);
	}

	public void remove(Brand brand) {
		em.remove(em.merge(brand));
	}

	public Brand find(Object id) {
		return em.find(Brand.class, id);
	}

	public List<Brand> getFullList() {
		List<Brand> list = null;

		Query query = em.createQuery("SELECT b FROM Brand b");

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public long countFullList() {
		long count = 0;

		Query query = em.createQuery("SELECT COUNT(b) FROM Brand b");

		try {
			count = (long) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return count;
	}

	public List<Brand> getLazyFullList(Map<String, String> sortBy, int offset, int pageSize) {
		List<Brand> list = null;

		SelectUtilities selectUtilities = new SelectUtilities("b");
		String order = selectUtilities.getOrder(sortBy);
		Query query = em.createQuery("SELECT b FROM Brand b" + order).setFirstResult(offset).setMaxResults(pageSize);

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public Brand getBrandByName(String name) {
		Brand brand = null;

		Query query = em.createQuery("SELECT b FROM Brand b WHERE b.name = :name");
		query.setParameter("name", name);

		try {
			brand = (Brand) query.getSingleResult();
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		return brand;
	}
}
