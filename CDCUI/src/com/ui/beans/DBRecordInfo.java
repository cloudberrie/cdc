package com.ui.beans;

import java.util.List;


public class DBRecordInfo {
 
 String tableName;
 String primaryKey;
 List<Field> dbFields;
 List<FieldValues> fieldValues;
 
 public String getTableName() {
  return tableName;
 }
 public void setTableName(String tableName) {
  this.tableName = tableName;
 }
 public List<Field> getDbFields() {
  return dbFields;
 }
 public void setDbFields(List<Field> dbFields) {
  this.dbFields = dbFields;
 }
 public List<FieldValues> getFieldValues() {
  return fieldValues;
 }
 public void setFieldValues(List<FieldValues> fieldValues) {
  this.fieldValues = fieldValues;
 }
 public String getPrimaryKey() {
	return primaryKey;
}
public void setPrimaryKey(String primaryKey) {
	this.primaryKey = primaryKey;
}
 

}