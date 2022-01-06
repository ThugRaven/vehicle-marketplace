package vehiclemarketplace.classes;

public class SelectSort {
	private String label;
	private String parameter;
	private String order;

	public SelectSort(String label, String parameter, String order) {
		super();
		this.label = label;
		this.parameter = parameter;
		this.order = order;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

}
