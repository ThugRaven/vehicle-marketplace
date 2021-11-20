package vehiclemarketplace.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import vehiclemarketplace.entities.BodyStyle;

@Stateless
public class BodyStyleDAO {
	private final static String UNIT_NAME = "vehicleMarketplacePU";

	@PersistenceContext(unitName = UNIT_NAME)
	protected EntityManager em;

	public void create(BodyStyle bodyStyle) {
		em.persist(bodyStyle);
	}

	public BodyStyle merge(BodyStyle bodyStyle) {
		return em.merge(bodyStyle);
	}

	public void remove(BodyStyle bodyStyle) {
		em.remove(em.merge(bodyStyle));
	}

	public BodyStyle find(Object id) {
		return em.find(BodyStyle.class, id);
	}

	public List<BodyStyle> getFullList() {
		List<BodyStyle> list = null;

		Query query = em.createQuery("SELECT b FROM BodyStyle b");

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
}
