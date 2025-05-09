package ru.minogin.core.client.serialization;

@SuppressWarnings("unchecked")
public class EnumHelper {
	@SuppressWarnings("rawtypes")
	public static String save(Enum e) {
		return e != null ? e.name() : null; 
	}
	
	@SuppressWarnings("rawtypes")
	public static <E extends Enum> E load(Class e, String name) {
		return (E) (name != null ? Enum.valueOf(e, name) : null);
	}
}
