package ru.minogin.core.client.serialization;

import java.math.BigDecimal;
import java.util.*;

import ru.minogin.core.client.common.Builder;

/**
 * @author Andrey Minogin
 *   
 * At the moment only maps with primitive keys can be [de]serialized.
 * Arrays are not allowed, use ArrayList instead.
 * 
 */
public abstract class Serializer {
	public static final String DATE_PATTERN = "dd.MM.yyyy HH:mm:ss";
	
	private Map<String, Builder<?>> builders = new HashMap<String, Builder<?>>();

	public void registerBuilder(String typeName, Builder<?> builder) {
		builders.put(typeName, builder);
	}
	
	public abstract String serialize(Object serializable);

	public abstract Object deserialize(String source);

	protected XSerializable create(String typeName) {
		Builder<?> builder = builders.get(typeName);
		if (builder == null)
			throw new SerializationError("Cannot instantiate \"" + typeName
					+ "\". Builder is not registered.");
		return (XSerializable) builder.newInstance();
	}

	protected String serializePrimitive(Object primitive) {
		if (primitive == null)
			return "null";
		else if (primitive instanceof String)
			return "s:" + primitive;
		else if (primitive instanceof Integer)
			return "i:" + primitive;
		else if (primitive instanceof Double)
			return "q:" + primitive;
		else if (primitive instanceof Boolean)
			return "b:" + primitive;
		else if (primitive instanceof BigDecimal)
			return "n:" + primitive;
		else if (primitive instanceof Date)
			return "d:" + serializeDate((Date) primitive);
		else
			throw new SerializationError("Cannot serialize primitive: " + primitive);
	}

	protected abstract String serializeDate(Date date);

	protected abstract Date deserializeDate(String s);

	protected Object deserializePrimitive(String s) {
		if (s == null)
			return null;

		if (s.equals("null"))
			return null;

		char t = s.charAt(0);
		String v = s.substring(2);
		switch (t) {
			case 's':
				return v;
			case 'i':
				return new Integer(v);
			case 'q':
				return new Double(v);
			case 'b':
				return new Boolean(v);
			case 'n':
				return new BigDecimal(v);
			case 'd':
				return deserializeDate(v);
			default:
				throw new SerializationError("Cannot deserialize primitive: " + s);
		}
	}
}
