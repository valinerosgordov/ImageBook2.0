package ru.minogin.core.client.bean;

public class BaseIdBean extends BaseBean implements IdBean {
	private static final long serialVersionUID = -8156842385633227586L;

	public static final String ID = "id";

	public BaseIdBean() {}

	public BaseIdBean(String id) {
		setId(id);
	}

	@Override
	public String getId() {
		return get(ID);
	}

	@Override
	public void setId(String id) {
		set(ID, id);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;

		if (!(obj instanceof BaseIdBean) || obj == null)
			return false;

		BaseIdBean bean = (BaseIdBean) obj;
		if (getId() == null)
			return false;

		return getId().equals(bean.getId());
	}

	@Override
	public int hashCode() {
		if (getId() == null)
			return super.hashCode();

		return getId().hashCode();
	}
}
