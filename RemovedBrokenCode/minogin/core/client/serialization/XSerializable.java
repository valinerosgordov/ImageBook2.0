package ru.minogin.core.client.serialization;

import java.util.Map;

public interface XSerializable {
	public String getTypeName();
	
	public void loadFrom(Map<String, Object> image);
	
	public void saveTo(Map<String, Object> image);
}
