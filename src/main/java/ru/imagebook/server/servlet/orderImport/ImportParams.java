package ru.imagebook.server.servlet.orderImport;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public enum ImportParams {
	VENDOR_KEY ("vendor", "key", false),
	VENDOR_PWD ("vendor", "password", false),
	
	USER_LOGIN ("user", "username", false),
	USER_PWD ("user", "password", false),
	USER_NAME ("user", "name", true),
	USER_EMAIL ("user", "email", false),
	
	PRODUCT_TYPE ("product", "type", false),
	PRODUCT_NUMBER ("product", "number", false),
	PRODUCT_NPAGES ("product", "nPages", false),
	PRODUCT_PRICE ("product", "price", false),	
	
	FTP_HOST ("layout", "ftpHost", false),
	FTP_USER ("layout", "ftpUser", false),
	FTP_PWD ("layout", "ftpPassword", false),
	FTP_FOLDER_PATH ("layout", "ftpFolderPath", false);
	
	private final String parent;
	private final String value;
	private final Boolean nullable;
	
	ImportParams(String parent, String value, Boolean nullable) {
		this.parent = parent;
		this.value = value;
		this.nullable = nullable;
	}
	
	public static Map<String, List<ImportParams>> getGroupedParamsMap() {
		Map<String, List<ImportParams>> paramsMap = Maps.newHashMap();
		for (ImportParams param : ImportParams.values()) {
			if (paramsMap.containsKey(param.parent)) {
				List<ImportParams> paramsList= paramsMap.get(param.parent);
				paramsList.add(param);
			} else {
				paramsMap.put(param.parent, Lists.newArrayList(param));
			}	
		}
		return paramsMap;
	}
	
	public String getValue() {
		return value;
	}
	
	public Boolean isNullable() {
		return nullable;
	}
}
