package ru.minogin.core.client.i18n;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class Grammar {
	private Map<String, NounType> nounTypes;
	private Map<String, NounForm> nounForms;
	
	public Grammar() {
		nounTypes = new LinkedHashMap<String, NounType>();
		nounForms = new LinkedHashMap<String, NounForm>();
	}
	
	public Collection<String> getNounTypes() {
		return nounTypes.keySet();
	}
	
	public String getNounTypeName(String type) {
		return nounTypes.get(type).getName();
	}
	
	public String getNounTypeDescription(String type) {
		return nounTypes.get(type).getDescription();
	}
	
	public void addNounType(String type, String name, String description) {
		nounTypes.put(type, new NounType(name, description));
	}
	
	public Collection<String> getNounForms() {
		return nounForms.keySet();
	}
	
	public String getNounFormName(String form) {
		return nounForms.get(form).getName();
	}
	
	public String getNounFormDescription(String form) {
		return nounForms.get(form).getDescription();
	}
	
	public void addNounForm(String form, String name, String description) {
		nounForms.put(form, new NounForm(name, description));
	}
}
