package vehiclemarketplace.classes;

public class SelectFilter {
	private String property;
	// e.g. name, surname, userRole.idUserRole
	private String parameter;
	// e.g. name, surname, idUserRole
	private Object value;
	private int searchType;

	public SelectFilter(String property, String parameter, Object value, int searchType) {
		super();
		this.property = property;
		this.parameter = parameter;
		this.value = value;
		this.searchType = searchType;
	}

	public SelectFilter(String parameter, Object value, int searchType) {
		super();
		this.property = parameter;
		this.parameter = parameter;
		this.value = value;
		this.searchType = searchType;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public int getSearchType() {
		return searchType;
	}

	public void setSearchType(int searchType) {
		this.searchType = searchType;
	}

	@Override
	public String toString() {
		return "SearchFilter [property=" + property + ", parameter=" + parameter + ", value=" + value + ", searchType="
				+ searchType + "]";
	}
}
