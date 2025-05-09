package ru.minogin.core.client.serialization;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

// TODO
public class GWTSerializer extends Serializer {
	@Override
	public Object deserialize(String source) {
		return fromJSON((JSONObject) JSONParser.parseLenient(source));
	}

	private Object fromJSON(JSONObject jsonObject) {
		return null;
		// String type = (String) jsonObject.keySet().iterator().next();
		// if (type.equals(Null))
		// return null;
		// else if (type.equals(String))
		// return ((JSONString) jsonObject.get(type)).stringValue();
		// else if (type.equals(Integer))
		// return (int) ((JSONNumber) jsonObject.get(type)).doubleValue();
		// else if (type.equals(int))
		// return (int) ((JSONNumber) jsonObject.get(type)).doubleValue();
		// else if (type.equals(Boolean))
		// return ((JSONBoolean) jsonObject.get(type)).booleanValue();
		// else if (type.equals(ArrayList)) {
		// Collection c = new ArrayList();
		// JSONArray image = (JSONArray) jsonObject.get(type);
		// for (int i = 0; i < image.size(); i++) {
		// c.add(fromJSON((JSONObject) image.get(i)));
		// }
		// return c;
		// }
		// else if (type.equals(LinkedHashSet)) {
		// Set set = new LinkedHashSet();
		// JSONArray image = (JSONArray) jsonObject.get(type);
		// for (int i = 0; i < image.size(); i++) {
		// set.add(fromJSON((JSONObject) image.get(i)));
		// }
		// return set;
		// }
		// else if (type.equals(LinkedHashMap)) {
		// Map map = new LinkedHashMap();
		// JSONArray image = (JSONArray) jsonObject.get(type);
		// for (int i = 0; i < image.size(); i++) {
		// JSONArray pair = (JSONArray) image.get(i);
		// map.put(fromJSON((JSONObject) pair.get(0)), fromJSON((JSONObject)
		// pair.get(1)));
		// }
		// return map;
		// }
		// else if (type.equals(XNamedCollection)) {
		// NamedMapImpl c = XFactory.getCore().newNamedMap();
		// JSONArray image = (JSONArray) jsonObject.get(type);
		// for (int i = 0; i < image.size(); i++) {
		// c.add((Named) fromJSON((JSONObject) image.get(i)));
		// }
		// return c;
		// }
		// else if (type.equals(XIdentifiableCollection)) {
		// IdentifiableMapImpl c = XFactory.getCore().newIdentifiableMap();
		// JSONArray image = (JSONArray) jsonObject.get(type);
		// for (int i = 0; i < image.size(); i++) {
		// c.add((Identifiable) fromJSON((JSONObject) image.get(i)));
		// }
		// return c;
		// }
		// else {
		// XSerializable serializable =
		// XFactory.getSerializationService().createInstance(type);
		// Map image = (Map) fromJSON((JSONObject) jsonObject.get(type));
		// serializable.loadFrom(image);
		// return serializable;
		// }
	}

	@Override
	public String serialize(Object serializable) {
		return toJSON(serializable).toString();
	}

	private JSONValue toJSON(Object object) {
		return null;
		// JSONObject jsonObject = new JSONObject();
		// if (object == null)
		// jsonObject.put(Null, JSONNull.getInstance());
		// else if (object instanceof String)
		// jsonObject.put(String, new JSONString((String) object));
		// else if (object instanceof Integer)
		// jsonObject.put(Integer, new JSONNumber((Integer) object));
		// else if (object instanceof int)
		// jsonObject.put(int, new JSONNumber((int) object));
		// else if (object instanceof Boolean)
		// jsonObject.put(Boolean, JSONBoolean.getInstance((Boolean) object));
		// else if (object instanceof ArrayList) {
		// JSONArray image = new JSONArray();
		// int i = 0;
		// for (Object o : (ArrayList) object) {
		// image.set(i, toJSON(o));
		// i++;
		// }
		// jsonObject.put(ArrayList, image);
		// }
		// else if (object instanceof HashSet) {
		// JSONArray image = new JSONArray();
		// int i = 0;
		// for (Object o : (HashSet) object) {
		// image.set(i, toJSON(o));
		// i++;
		// }
		// jsonObject.put(LinkedHashSet, image);
		// }
		// else if (object instanceof HashMap) {
		// Map map = (HashMap) object;
		// JSONArray image = new JSONArray();
		// int i = 0;
		// for (Object key : map.keySet()) {
		// JSONArray pair = new JSONArray();
		// pair.set(0, toJSON(key));
		// pair.set(1, toJSON(map.get(key)));
		// image.set(i, pair);
		// i++;
		// }
		// jsonObject.put(LinkedHashMap, image);
		// }
		// else if (object instanceof NamedMapImpl) {
		// JSONArray image = new JSONArray();
		// int i = 0;
		// for (Object o : (NamedMapImpl) object) {
		// image.set(i, toJSON(o));
		// i++;
		// }
		// jsonObject.put(XNamedCollection, image);
		// }
		// else if (object instanceof IdentifiableMapImpl) {
		// JSONArray image = new JSONArray();
		// int i = 0;
		// for (Object o : (IdentifiableMapImpl) object) {
		// image.set(i, toJSON(o));
		// i++;
		// }
		// jsonObject.put(XIdentifiableCollection, image);
		// }
		// else if (object instanceof XSerializable) {
		// XSerializable serializable = (XSerializable) object;
		// Map image = serializable.save();
		// jsonObject.put(serializable.getAMLTypeName(), toJSON(image));
		// }
		// else
		// throw new SerializationRuntimeException("Cannot serialize: " + object +
		// " ("
		// + object.getClass().getName() + ")");
		//
		// return jsonObject;
	}

	@Override
	protected String serializeDate(Date date) {
		return DateTimeFormat.getFormat(DATE_PATTERN).format(date);
	}

	@Override
	protected Date deserializeDate(String s) {
		return DateTimeFormat.getFormat(DATE_PATTERN).parse(s);
	}
}
