package com.linkedin.databus.client.data_model;

public class DBRecord {
 
 
 String sourceId;
 String Operation;
 DBRecordInfo dbRecordInfo;
 
 
 public String getSourceId() {
  return sourceId;
 }
 public void setSourceId(String sourceId) {
  this.sourceId = sourceId;
 }
 public String getOperation() {
  return Operation;
 }
 public void setOperation(String operation) {
  Operation = operation;
 }
 public DBRecordInfo getDbRecordInfo() {
  return dbRecordInfo;
 }
 public void setDbRecordInfo(DBRecordInfo dbRecordInfo) {
  this.dbRecordInfo = dbRecordInfo;
 }
 
 
 @Override
 public String toString() {

  StringBuffer sb=new StringBuffer();
  
  sb.append("sourceId: "+this.sourceId);
  sb.append(" Operation: "+this.Operation);
  
  for(Field field:this.getDbRecordInfo().getDbFields()){
   sb.append(" Property Name: "+field.propertyName+" Column Name: "+field.getColumnName());
  }
  
  return sb.toString();
 } 

}