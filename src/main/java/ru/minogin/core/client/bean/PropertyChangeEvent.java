package ru.minogin.core.client.bean;

public class PropertyChangeEvent {
	private ObservableBean bean;
	private String name;
	private Object oldValue;
	private Object newValue;
	private Object modifiedValue;
	
	public PropertyChangeEvent(ObservableBean bean, String name, Object oldValue, Object newValue) {
		this.bean = bean;
		this.name = name;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}
	
	@SuppressWarnings("unchecked")
	public <B extends ObservableBean> B getBean() {
		return (B) bean;
	}

	public String getName() {
		return name;
	}
	
	@SuppressWarnings("unchecked")
	public <X> X getOldValue() {
		return (X) oldValue;
	}
	
	@SuppressWarnings("unchecked")
	public <X> X getNewValue() {
		return (X) newValue;
	}
	
	@SuppressWarnings("unchecked")
	public <X> X getModifiedValue() {
		return (X) modifiedValue;
	}
	
	public void modifyNewValue(Object value) {
		this.modifiedValue = value;
	}
}
