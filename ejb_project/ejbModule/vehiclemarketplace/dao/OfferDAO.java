package vehiclemarketplace.dao;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import vehiclemarketplace.classes.SelectFilter;
import vehiclemarketplace.classes.SelectUtilities;
import vehiclemarketplace.entities.Offer;

@Stateless
public class OfferDAO {
	private final static String UNIT_NAME = "vehicleMarketplacePU";

	@PersistenceContext(unitName = UNIT_NAME)
	protected EntityManager em;

	public void create(Offer offer) {
		em.persist(offer);
	}

	public Offer merge(Offer offer) {
		return em.merge(offer);
	}

	public void remove(Offer offer) {
		em.remove(em.merge(offer));
	}

	public Offer find(Object id) {
		return em.find(Offer.class, id);
	}

	public List<Offer> getFullList() {
		List<Offer> list = null;

		Query query = em.createQuery("SELECT o FROM Offer o");

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public List<Offer> getLazyList(Map<String, String> sortBy, List<SelectFilter> filter, int offset, int pageSize) {
		List<Offer> list = null;

		SelectUtilities selectUtilities = new SelectUtilities("o");
		String order = selectUtilities.getOrder(sortBy);
		String where = selectUtilities.getWhere(filter);

		Query query = em.createQuery("SELECT o FROM Offer o" + where + order).setFirstResult(offset)
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

		SelectUtilities selectUtilities = new SelectUtilities("o");
		String where = selectUtilities.getWhere(filter);

		Query query = em.createQuery("SELECT COUNT(o) FROM Offer o" + where);
		query = selectUtilities.getParameters(query, filter);

		try {
			count = (long) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return count;
	}

	public List<Offer> getOffersByBrandID(int id) {
		List<Offer> list = null;

		Query query = em.createQuery("SELECT o FROM Offer o WHERE o.brand.idBrand = :id");
		query.setParameter("id", id);

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public long countOffersByBrandID(int id) {
		long count = 0;

		Query query = em.createQuery("SELECT COUNT(o) FROM Offer o WHERE o.brand.idBrand = :id AND o.archived = false");
		query.setParameter("id", id);

		try {
			count = (long) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return count;
	}

	public List<Offer> getOffersByModelID(int id) {
		List<Offer> list = null;

		Query query = em.createQuery("SELECT o FROM Offer o WHERE o.model.idModel = :id");
		query.setParameter("id", id);

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public long countOffersByModelID(int id) {
		long count = 0;

		Query query = em.createQuery("SELECT COUNT(o) FROM Offer o WHERE o.model.idModel = :id AND o.archived = false");
		query.setParameter("id", id);

		try {
			count = (long) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return count;
	}

	public List<Offer> getOffersByGenerationID(int id) {
		List<Offer> list = null;

		Query query = em.createQuery("SELECT o FROM Offer o WHERE o.generation.idGeneration = :id");
		query.setParameter("id", id);

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public long countOffersByGenerationID(int id) {
		long count = 0;

		Query query = em.createQuery(
				"SELECT COUNT(o) FROM Offer o WHERE o.generation.idGeneration = :id AND o.archived = false");
		query.setParameter("id", id);

		try {
			count = (long) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return count;
	}

	public List<Offer> getOffersByUserID(int id) {
		List<Offer> list = null;

		Query query = em.createQuery("SELECT o FROM Offer o WHERE o.user.idUser = :id");
		query.setParameter("id", id);

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public List<Offer> getOffersByEquipmentID(int id) {
		List<Offer> list = null;

		Query query = em.createQuery("SELECT o FROM Offer o JOIN o.equipments e WHERE e.idEquipment = :id");
		query.setParameter("id", id);

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
}
