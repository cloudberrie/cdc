package com.ui.beans;

public class FieldValues {
 
 String propertyName;
 
 String propertyValue;
 
 
 public FieldValues(){
  
 }
 
 public FieldValues(String propertyName,String propertyValue){
  this.propertyName=propertyName;
  this.propertyValue=propertyValue;
 }
 
 
 public String getPropertyName() {
  return propertyName;
 }

 public void setPropertyName(String propertyName) {
  this.propertyName = propertyName;
 }

 public String getPropertyValue() {
  return propertyValue;
 }

 public void setPropertyValue(String propertyValue) {
  this.propertyValue = propertyValue;
 }
 
 

}