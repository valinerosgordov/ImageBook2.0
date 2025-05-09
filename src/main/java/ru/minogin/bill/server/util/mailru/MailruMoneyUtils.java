package ru.minogin.bill.server.util.mailru;

import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Utility component for "MailRu Money" service
 * 
 * @see <a href="https://money.mail.ru/img/partners/dmr_light_v1.2.pdf">MailRu Money 'Light' scheme</a>  
 */
public class MailruMoneyUtils {

	private static final String ISSUER_ID_PARAM = "issuer_id";
	private static final String SIGNATURE_PARAM = "signature";
	
	/**
	 * Generate digital signature
	 * 
	 * @param formParams
	 * @param key - SHA-1 from MailRu Money secret key 
	 * @return signature
	 */
	public static String signFormData(Map<String, Object> formParams, String key) {
		String cryptKey = DigestUtils.shaHex(key);
		return signData(formParams, cryptKey);
	}
	
	/**
	 * Generate digital signature for MailRu Money 'Light' scheme 
	 * 
	 * @param params 
	 * @param key - SHA-1 from MailRu Money secret key 
	 * @return signature
	 */
	public static String signData(Map<String, Object> params, String key) {
		Map<String, Object> paramsSortedByName = new TreeMap<String, Object>(params);
		String paramsStr = StringUtils.join(paramsSortedByName.values(), "");
		return DigestUtils.shaHex(paramsStr + key);
	}
	
	/**
	 * Decode "Issuer_id" parameter from Base64 String 
	 * 
	 * @param params - input parameters
	 * @return issuer_id 
	 */
	public static String getAndDecodeIssuerId(Map<String, Object> params) {
		String billIdString = String.valueOf(params.get(ISSUER_ID_PARAM));
		return new String(Base64.decodeBase64(billIdString));
	}

	/**
	 * Validate signature store's notification
	 * 
	 * @param params - input parameters
	 * @param key - MailRu Money secret key
	 * @return true if signature is valid, else false
	 */
	public static boolean validateSignature(Map<String, Object> params, String key) {
		String inputSignature = String.valueOf(params.get(SIGNATURE_PARAM));	
		params.remove(SIGNATURE_PARAM);
		String validSignature = signData(params, key);		
		return StringUtils.isNotEmpty(inputSignature) && inputSignature.equalsIgnoreCase(validSignature); 
	}
	
	private MailruMoneyUtils() {
	}
}
