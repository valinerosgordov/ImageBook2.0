package ru.minogin.core.client.bean;

public class BaseObservableBean extends BaseBean implements ObservableBean {
	private static final long serialVersionUID = 5342955921641973590L;

	private transient ObservableSupport observableSupport;

	public BaseObservableBean() {
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
