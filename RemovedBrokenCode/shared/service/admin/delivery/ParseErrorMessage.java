package ru.imagebook.shared.service.admin.delivery;

import ru.minogin.core.client.push.PushMessage;

public class ParseErrorMessage implements PushMessage{	
	private static final long serialVersionUID = 7577576681741976908L;
	
	private int fieldId;
	private int rowId;
	
	public ParseErrorMessage(){}
	
	public ParseErrorMessage(int rowId, int fieldId){
		this.fieldId = fieldId;
		this.rowId = rowId;	
	}
	
	public int getFieldId(){
		return fieldId;
	}
	
	public int getRowId(){
		return rowId;
	}	
}
