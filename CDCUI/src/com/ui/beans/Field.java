package com.ui.beans;

public class Field {
 
 String  propertyName;
 String  columnName;
 Integer dbColumnIndex;
 String propertyType;
 String javaObjectType;
 
 
 public String getPropertyType() {
	return propertyType;
}

public void setPropertyType(String propertyType) {
	this.propertyType = propertyType;
}

public Field(){
  
 }
 
 public Field(String propertyName,String columnName,Integer dbColumnIndex,String propertyType,String javaObjectType){
  
  this.propertyName=propertyName;
  this.columnName=columnName;
  this.dbColumnIndex=dbColumnIndex;
  this.propertyType=propertyType;
  this.javaObjectType=javaObjectType;
  
 }
 
 public String getPropertyName() {
  return propertyName;
 }
 public void setPropertyName(String propertyName) {
  this.propertyName = propertyName;
 }
 public String getColumnName() {
  return columnName;
 }
 public void setColumnName(String columnName) {
  this.columnName = columnName;
 }
 public Integer getDbColumnIndex() {
  return dbColumnIndex;
 }
 public void setDbColumnIndex(Integer dbColumnIndex) {
  this.dbColumnIndex = dbColumnIndex;
 }

public String getJavaObjectType() {
	return javaObjectType;
}

public void setJavaObjectType(String javaObjectType) {
	this.javaObjectType = javaObjectType;
}
 }