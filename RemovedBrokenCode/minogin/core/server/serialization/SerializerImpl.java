package ru.minogin.core.server.serialization;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ru.minogin.core.client.exception.Exceptions;
import ru.minogin.core.client.model.IdMap;
import ru.minogin.core.client.model.Identifiable;
import ru.minogin.core.client.model.Named;
import ru.minogin.core.client.model.NamedMap;
import ru.minogin.core.client.serialization.SerializationError;
import ru.minogin.core.client.serialization.Serializer;
import ru.minogin.core.client.serialization.XSerializable;

@SuppressWarnings("unchecked")
public class SerializerImpl extends Serializer {
	private static final String ARRAY_LIST = "ArrayList";
	private static final String HASH_MAP = "HashMap";
	private static final String LINKED_HASH_MAP = "LinkedHashMap";
	private static final String HASH_SET = "HashSet";
	private static final String LINKED_HASH_SET = "LinkedHashSet";
	private static final String ID_LIST = "IdentifiableMap";
	private static final String NAMED_MAP = "NamedMap";

	@Override
	public String serialize(Object serializable) {
		return toJSON(serializable).toString();
	}

	@SuppressWarnings("rawtypes")
	private Object toJSON(Object object) {
		try {
			if (object == null || object instanceof String || object instanceof Integer
					|| object instanceof Double || object instanceof Boolean || object instanceof Date
					|| object instanceof BigDecimal)
				return serializePrimitive(object);
			else if (object instanceof Object[])
				throw new SerializationError("Arrays (Object[]) are not supported. Use ArrayList instead.");
			else if (object instanceof ArrayList) {
				JSONObject image = new JSONObject();
				image.put("class", ARRAY_LIST);
				JSONArray values = new JSONArray();
				for (Object o : (ArrayList) object) {
					values.put(toJSON(o));
				}
				image.put("values", values);
				return image;
			}
			else if (object instanceof LinkedHashMap) {
				HashMap map = (LinkedHashMap) object;
				JSONObject image = new JSONObject(new LinkedHashMap<String, Object>());
				image.put("class", LINKED_HASH_MAP);
				for (Object key : map.keySet()) {
					image.put(serializePrimitive(key), toJSON(map.get(key)));
				}
				return image;
			}
			else if (object instanceof HashMap) {
				HashMap map = (HashMap) object;
				JSONObject image = new JSONObject();
				image.put("class", HASH_MAP);
				for (Object key : map.keySet()) {
					image.put(serializePrimitive(key), toJSON(map.get(key)));
				}
				return image;
			}
			else if (object instanceof LinkedHashSet) {
				JSONObject image = new JSONObject();
				image.put("class", LINKED_HASH_SET);
				JSONArray values = new JSONArray();
				for (Object o : (LinkedHashSet) object) {
					values.put(toJSON(o));
				}
				image.put("values", values);
				return image;
			}
			else if (object instanceof HashSet) {
				JSONObject image = new JSONObject();
				image.put("class", HASH_SET);
				JSONArray values = new JSONArray();
				for (Object o : (HashSet) object) {
					values.put(toJSON(o));
				}
				image.put("values", values);
				return image;
			}
			else if (object instanceof NamedMap) {
				JSONObject image = new JSONObject();
				image.put("class", NAMED_MAP);
				JSONArray values = new JSONArray();
				for (Object o : (NamedMap) object) {
					values.put(toJSON(o));
				}
				image.put("values", values);
				return image;
			}
			else if (object instanceof IdMap) {
				JSONObject image = new JSONObject();
				image.put("class", ID_LIST);
				JSONArray values = new JSONArray();
				for (Object o : (IdMap) object) {
					values.put(toJSON(o));
				}
				image.put("values", values);
				return image;
			}
			else if (object instanceof XSerializable) {
				XSerializable serializable = (XSerializable) object;
				Map<String, Object> image = new HashMap<String, Object>();
				serializable.saveTo(image);
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("class", serializable.getTypeName());
				for (String key : image.keySet()) {
					jsonObject.put(key, toJSON(image.get(key)));
				}
				return jsonObject;
			}
			else
				throw new SerializationError("Cannot serialize: " + object + "; c = " + object.getClass());
		}
		catch (JSONException e) {
			throw new SerializationError(e);
		}
	}

	@Override
	public Object deserialize(String source) {
		try {
			return fromJSON(new JSONObject(source));
		}
		catch (JSONException e) {
			throw new SerializationError(e);
		}
	}

	@SuppressWarnings("rawtypes")
	private Object fromJSON(Object object) throws JSONException {
		if (object instanceof JSONObject) {
			JSONObject jsonObject = (JSONObject) object;
			String amlTypeName = (String) jsonObject.remove("class");

			if (amlTypeName.equals(ARRAY_LIST)) {
				ArrayList list = new ArrayList();
				JSONArray values = jsonObject.getJSONArray("values");
				for (int i = 0; i < values.length(); i++) {
					list.add(fromJSON(values.get(i)));
				}
				return list;
			}
			else if (amlTypeName.equals(HASH_MAP)) {
				HashMap map = new HashMap();
				String[] names = JSONObject.getNames(jsonObject);
				if (names != null) {
					for (String key : names) {
						map.put(deserializePrimitive(key), fromJSON(jsonObject.get(key)));
					}
				}
				return map;
			}
			else if (amlTypeName.equals(LINKED_HASH_MAP)) {
				LinkedHashMap map = new LinkedHashMap();
				String[] names = JSONObject.getNames(jsonObject);
				if (names != null) {
					for (String key : names) {
						map.put(deserializePrimitive(key), fromJSON(jsonObject.get(key)));
					}
				}
				return map;
			}
			else if (amlTypeName.equals(HASH_SET)) {
				HashSet set = new HashSet();
				JSONArray values = jsonObject.getJSONArray("values");
				for (int i = 0; i < values.length(); i++) {
					set.add(fromJSON(values.get(i)));
				}
				return set;
			}
			else if (amlTypeName.equals(LINKED_HASH_SET)) {
				HashSet set = new LinkedHashSet();
				JSONArray values = jsonObject.getJSONArray("values");
				for (int i = 0; i < values.length(); i++) {
					set.add(fromJSON(values.get(i)));
				}
				return set;
			}
			else if (amlTypeName.equals(NAMED_MAP)) {
				NamedMap map = new NamedMap();
				JSONArray values = jsonObject.getJSONArray("values");
				for (int i = 0; i < values.length(); i++) {
					map.add((Named) fromJSON(values.get(i)));
				}
				return map;
			}
			else if (amlTypeName.equals(ID_LIST)) {
				IdMap map = new IdMap();
				JSONArray values = jsonObject.getJSONArray("values");
				for (int i = 0; i < values.length(); i++) {
					map.add((Identifiable) fromJSON(values.get(i)));
				}
				return map;
			}
			else {
				XSerializable serializable = create(amlTypeName);
				Map<String, Object> image = new HashMap<String, Object>();
				String[] names = JSONObject.getNames(jsonObject);
				if (names != null) {
					for (String key : names) {
						image.put(key, fromJSON(jsonObject.get(key)));
					}
				}
				serializable.loadFrom(image);
				return serializable;
			}
		}
		else {
			if (object == null || object.equals(JSONObject.NULL))
				return null;
			else if (object instanceof String)
				return deserializePrimitive((String) object);
			else if (object instanceof JSONObject)
				return fromJSON((JSONObject) object);
			else
				throw new SerializationError("Cannot deserialize: " + object);
		}
	}

	@Override
	protected String serializeDate(Date date) {
		return new SimpleDateFormat(DATE_PATTERN).format(date);
	}

	@Override
	protected Date deserializeDate(String s) {
		try {
			return new SimpleDateFormat(DATE_PATTERN).parse(s);
		}
		catch (ParseException e) {
			return Exceptions.rethrow(e);
		}
	}
}
