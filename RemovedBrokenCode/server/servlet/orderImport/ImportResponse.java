package ru.imagebook.server.servlet.orderImport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "result")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportResponse {
	public static final String STATUS_OK = "OK";
	public static final String STATUS_ERROR = "Error";
	
	@XmlElement(required = true)
	private String status;
	private String errorCode;
	private String errorData;
	private String errorMessage;
	
	public ImportResponse() {		
	}
	
	public ImportResponse(String status, String errorCode, String errorData,
			String errorMessage) {
		super();
		this.status = status;
		this.errorCode = errorCode;
		this.errorData = errorData;
		this.errorMessage = errorMessage;
	}

	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getErrorCode() {
		return errorCode;
	}
	
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
	public String getErrorData() {
		return errorData;
	}
	
	public void setErrorData(String errorData) {
		this.errorData = errorData;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
