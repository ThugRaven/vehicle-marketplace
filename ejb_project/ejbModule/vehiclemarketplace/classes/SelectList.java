package vehiclemarketplace.classes;

import java.util.List;

public class SelectList {
	private String parameter;
	private List<Object> values;

	public SelectList(String parameter, List<Object> values) {
		super();
		this.parameter = parameter;
		this.values = values;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public List<Object> getValues() {
		return values;
	}

	public void setValues(List<Object> values) {
		this.values = values;
	}

	@Override
	public String toString() {
		return "SelectList [parameter=" + parameter + ", values=" + values + "]";
	}

}
