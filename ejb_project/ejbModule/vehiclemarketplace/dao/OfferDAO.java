package vehiclemarketplace.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import vehiclemarketplace.entities.Model;
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
}
