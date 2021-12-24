package vehiclemarketplace.classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

public class SelectUtilities {
	private String table;

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public SelectUtilities(String table) {
		super();
		this.table = table;
	}

	public String getOrder(Map<String, String> sortBy) {
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
				orderBy = orderBy.concat(table + "." + field + " " + order);
			}
		}

		System.out.println("Order: " + orderBy);
		return orderBy;
	}

	public String getWhere(List<SelectFilter> filter) {
		String where = "";

		if (filter.size() == 0) {
			return where;
		}

		List<SelectFilter> filterRange = new ArrayList<>();

		for (SelectFilter searchFilter : filter) {
			if (searchFilter.getSearchType() == SelectType.BETWEEN) {
				filterRange.add(searchFilter);
				continue;
			}

			if (where.isEmpty()) {
				where = " WHERE ";
			} else {
				where = where.concat(" AND ");
			}

			where = where.concat(
					createWhere(searchFilter.getProperty(), searchFilter.getParameter(), searchFilter.getSearchType()));
		}

		for (int i = 0; i < filterRange.size(); i++) {
			SelectFilter searchFilter = filterRange.get(i);
			if ((i + 1) % 2 == 0) {
				continue;
			}

			if (where.isEmpty()) {
				where = " WHERE ";
			} else {
				where = where.concat(" AND ");
			}

			where = where.concat(createWhereBetween(searchFilter.getProperty(), searchFilter.getParameter(),
					filterRange.get(i + 1).getParameter(), searchFilter.getSearchType()));
		}

		System.out.println("Where: " + where);
		System.out.println("Filter: " + filter.toString());
		System.out.println("FilterRange: " + filterRange.toString());
		return where;
	}

	public String createWhere(String property, String field, int type) {
		String where = "";

		if (type == SelectType.LIKE || type == SelectType.LIKE_FULL) {
			where = table + "." + property + " LIKE :" + field;
		} else if (type == SelectType.NORMAL) {
			where = table + "." + property + " = :" + field;
		} else if (type == SelectType.LESS_EQUAL_THAN) {
			where = table + "." + property + " <= :" + field;
		} else if (type == SelectType.GREATER_EQUAL_THAN) {
			where = table + "." + property + " >= :" + field;
		}

		return where;
	}

	public String createWhereBetween(String property, String fieldFrom, String fieldTo, int type) {
		String where = "";

		if (type == SelectType.BETWEEN) {
			where = table + "." + property + " BETWEEN :" + fieldFrom + " AND :" + fieldTo;
		}

		return where;
	}

	public Query getParameters(Query query, List<SelectFilter> filter) {
		if (filter.size() == 0) {
			return query;
		}

		for (SelectFilter searchFilter : filter) {
			switch (searchFilter.getSearchType()) {
			case SelectType.NORMAL, SelectType.BETWEEN, SelectType.LESS_EQUAL_THAN, SelectType.GREATER_EQUAL_THAN:
				query.setParameter(searchFilter.getParameter(), searchFilter.getValue());
				break;
			case SelectType.LIKE:
				query.setParameter(searchFilter.getParameter(), searchFilter.getValue() + "%");
				break;
			case SelectType.LIKE_FULL:
				query.setParameter(searchFilter.getParameter(), "%" + searchFilter.getValue() + "%");
				break;
			default:
				break;
			}
		}

		return query;
	}
}
