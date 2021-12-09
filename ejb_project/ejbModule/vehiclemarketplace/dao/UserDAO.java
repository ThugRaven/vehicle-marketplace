package vehiclemarketplace.dao;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import vehiclemarketplace.classes.SearchFilter;
import vehiclemarketplace.classes.SearchType;
import vehiclemarketplace.entities.Brand;
import vehiclemarketplace.entities.User;

@Stateless
public class UserDAO {
	private final static String UNIT_NAME = "vehicleMarketplacePU";

	@PersistenceContext(unitName = UNIT_NAME)
	protected EntityManager em;

	public void create(User user) {
		em.persist(user);
	}

	public User merge(User user) {
		return em.merge(user);
	}

	public void remove(User user) {
		em.remove(em.merge(user));
	}

	public User find(Object id) {
		return em.find(User.class, id);
	}

	public List<User> getFullList() {
		List<User> list = null;

		Query query = em.createQuery("SELECT u FROM User u");

		try {
			list = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public long countFullList() {
		long count = 0;

		Query query = em.createQuery("SELECT COUNT(u) FROM User u");

		try {
			count = (long) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return count;
	}

	public List<User> getLazyFullList(Map<String, String> sortBy, List<SearchFilter> userFilter, int offset,
			int pageSize) {
		List<User> list = null;

		String order = getOrderBy(sortBy);
		String where = getWhere(userFilter);

		System.out.println(order);
		System.out.println(where);
		if (userFilter != null) {
			System.out.println(userFilter.toString());
		}

		Query query = em.createQuery("SELECT u FROM User u" + where + order).setFirstResult(offset)
				.setMaxResults(pageSize);
		setParameters(query, userFilter);

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
				orderBy = orderBy.concat("u." + field + " " + order);
			}
		}

		return orderBy;
	}

//	public String getWhere(User filter) {
//		String where = "";
//
//		if (filter.getName() != null && !filter.getName().isEmpty()) {
//			if (where.isEmpty()) {
//				where = " WHERE ";
//			} else {
//				where = where.concat(" AND ");
//			}
//
//			if (filter.getName() != null && !filter.getName().isEmpty()) {
//				where = where.concat(createWhere("name", true));
//			}
//		}
//
//		return where;
//	}
//
//	public String createWhere(String field, boolean like) {
//		String where = "";
//
//		if (like) {
//			where = "u." + field + " LIKE :" + field;
//		} else {
//			where = "u." + field + " = :" + field;
//		}
//
//		return where;
//	}
//
//	public void setParameters(Query query, User filter) {
//		if (filter.getName() == null || filter.getName().isEmpty()) {
//			return;
//		}
//
//		if (filter.getName() != null && !filter.getName().isEmpty()) {
//			query.setParameter("name", filter.getName() + "%");
//		}
//	}

	public String getWhere(List<SearchFilter> filter) {
		String where = "";

		if (filter.size() == 0) {
			return where;
		}

		for (SearchFilter searchFilter : filter) {
			if (where.isEmpty()) {
				where = " WHERE ";
			} else {
				where = where.concat(" AND ");
			}

			where = where.concat(
					createWhere(searchFilter.getProperty(), searchFilter.getParameter(), searchFilter.getSearchType()));
		}

		return where;
	}

	public String createWhere(String property, String field, int type) {
		String where = "";

		if (type == SearchType.LIKE || type == SearchType.LIKE_FULL) {
			where = "u." + property + " LIKE :" + field;
		} else if (type == SearchType.NORMAL) {
			where = "u." + property + " = :" + field;
		}

		return where;
	}

	public void setParameters(Query query, List<SearchFilter> filter) {
		if (filter.size() == 0) {
			return;
		}

		for (SearchFilter searchFilter : filter) {
			switch (searchFilter.getSearchType()) {
			case SearchType.NORMAL:
				query.setParameter(searchFilter.getParameter(), searchFilter.getValue());
				break;
			case SearchType.LIKE:
				query.setParameter(searchFilter.getParameter(), searchFilter.getValue() + "%");
				break;
			case SearchType.LIKE_FULL:
				query.setParameter(searchFilter.getParameter(), "%" + searchFilter.getValue() + "%");
				break;
			default:
				break;
			}
		}
	}

	public User getUserByLogin(String login) {
		User user = null;

		Query query = em.createQuery("SELECT u FROM User u WHERE u.login = :login");
		query.setParameter("login", login);

		try {
			user = (User) query.getSingleResult();
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		return user;
	}
}
