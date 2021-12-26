package vehiclemarketplace.classes;

public class SelectItemCount<T> {
	private T object;
	private long count;

	public SelectItemCount(T object, long count) {
		super();
		this.object = object;
		this.count = count;
	}

	public T getObject() {
		return object;
	}

	public void setObject(T object) {
		this.object = object;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}
}
