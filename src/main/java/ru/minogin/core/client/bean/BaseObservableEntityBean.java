package ru.minogin.core.client.bean;

public class BaseObservableEntityBean extends BaseEntityBean implements ObservableEntityBean {
	private static final long serialVersionUID = -4511130289011518791L;

	private transient ObservableSupport observableSupport;

	public BaseObservableEntityBean() {
		observableSupport = new ObservableSupport(this);
	}

	@Override
	public <X> X set(String name, X value) {
		X oldValue = super.set(name, value);

		observableSupport.notifyPropertyChange(name, oldValue, value);

		return oldValue;
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		observableSupport.addPropertyChangeListener(listener);
	}
	
	@Override
	public <X> X directSet(String name, X value) {
		return super.set(name, value);
	}
}
