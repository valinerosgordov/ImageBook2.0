package ru.imagebook.server.service2.admin;

public class ZoneParseException extends  RuntimeException{
	private static final long serialVersionUID = -434495555001405984L;
	
	private int fieldId;
	private int rowId;
	
	public ZoneParseException(){}
	
	public ZoneParseException(int rowId, int fieldId){
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
