package com.ui.querygen;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.ui.beans.DBRecord;
import com.ui.beans.DBRecordInfo;
import com.ui.beans.Field;
import com.ui.beans.FieldValues;


public class SQLServerQueryGenerator implements QueryGenerator {
	
	HashMap<String,Field> propertyColumnMap=new HashMap<String,Field>();
	HashMap<String,String> propertyValueMap=new HashMap<String,String>();

	@Override
	public String createQuery(DBRecord dbRecord) {

		prepareQueryMap(dbRecord);
		
		if(OPERATION_INSERT.equalsIgnoreCase(dbRecord.Operation)){
			return createInsert(dbRecord);
		}else if(OPERATION_UPDATE.equalsIgnoreCase(dbRecord.Operation)){
			return createUpdate(dbRecord);
		}else if(OPERATION_DELETE.equalsIgnoreCase(dbRecord.Operation)){
			return createDelete(dbRecord);
		}
		return null;
	}
	
	public String createInsert(DBRecord dbRecord){
		 StringBuffer sb=new StringBuffer();
		 
		if(dbRecord!=null){			
	      
	       sb.append("INSERT INTO ");
	       sb.append(dbRecord.getDbRecordInfo().getTableName());
	       StringBuffer columns=new StringBuffer();
	       StringBuffer colValues=new StringBuffer();

	       for(Entry<String,Field> entry:propertyColumnMap.entrySet()){
	    	   
	    	   String key=entry.getKey();
	    	   Field field=entry.getValue();
	    	   
	    	   columns.append(field.getColumnName());
	    	   columns.append(",");
	    	   
	    	   if("java.lang.String".equalsIgnoreCase(field.getJavaObjectType())){
	    		   colValues.append("'"+propertyValueMap.get(key)+"'");
	    	   }else if("java.util.Date".equalsIgnoreCase(field.getJavaObjectType())){
	    		   Date date=new Date(Long.valueOf(propertyValueMap.get(key)));
	    		   SimpleDateFormat df2 = new SimpleDateFormat("MM-dd-yyyy");
	    	        String dateText = df2.format(date);

	    		   colValues.append("'"+dateText+"'");
	    	   }else{
	    		   colValues.append(propertyValueMap.get(key));
	    	   }
	    	   colValues.append(",");
	       }
	       sb.append("(");
	       sb.append(columns.substring(0, columns.length()-1));
	       sb.append(") VALUES");
	       sb.append("(");
	       sb.append(colValues.substring(0, colValues.length()-1));
	       sb.append(");");
		}
			
		return sb.toString();
	}
	
	public void prepareQueryMap(DBRecord dbRecord){
		
		DBRecordInfo recordInfo=dbRecord.getDbRecordInfo();
		List<Field> fields=recordInfo.getDbFields();
		List<FieldValues> fieldValues=recordInfo.getFieldValues();
		
		for(Field field:fields){				
			propertyColumnMap.put(field.getPropertyName(),field);				
		}
		for(FieldValues fieldValue:fieldValues){				
			propertyValueMap.put(fieldValue.getPropertyName(), fieldValue.getPropertyValue());				
		}
	}
	
	public String createUpdate(DBRecord dbRecord){
		
		StringBuffer sb=new StringBuffer();
		DBRecordInfo recordInfo=dbRecord.getDbRecordInfo();
		sb.append("UPDATE ");
		sb.append(recordInfo.getTableName());
		sb.append(" SET ");
		
		StringBuffer colVal=new StringBuffer();
		for(Entry<String,Field> entry:propertyColumnMap.entrySet()){
			Field field=entry.getValue();
			String property=field.getColumnName();
			colVal.append(property+"=");
			
			System.out.println("kisihoe: "+propertyValueMap.get(entry.getKey()));
			if("utf8".equalsIgnoreCase(field.getPropertyType())){
				colVal.append("'"+propertyValueMap.get(entry.getKey())+"'");
		 	   }else if("java.util.Date".equalsIgnoreCase(field.getJavaObjectType())){
	    		   Date date=new Date(Long.valueOf(propertyValueMap.get(entry.getKey())));
	    		   SimpleDateFormat df2 = new SimpleDateFormat("MM-dd-yyyy");
	    	        String dateText = df2.format(date);

	    	        colVal.append("'"+dateText+"'");
	    	   }
			else{
		 		  colVal.append(propertyValueMap.get(entry.getKey()));
		 	   }	
			colVal.append(",");
		}
		sb.append(colVal.substring(0, colVal.length()-1));
		sb.append(getWhereConditionForPk(dbRecord));
		sb.append(";");
		return sb.toString();
	}
	
	public String createDelete(DBRecord dbRecord){
		
		StringBuffer sb=new StringBuffer();
		sb.append("DELETE FROM ");
		sb.append(dbRecord.getDbRecordInfo().getTableName());		
		sb.append(getWhereConditionForPk(dbRecord));
		sb.append(";");
		return sb.toString();
	}
	
	private String getWhereConditionForPk(DBRecord dbRecord){
		StringBuffer sb=new StringBuffer();
		sb.append(" WHERE ");
		String primarykey=dbRecord.getDbRecordInfo().getPrimaryKey();
		sb.append(primarykey);
		sb.append("=");
		Field field=propertyColumnMap.get(dbRecord.getDbRecordInfo().getPrimaryKey());
		
		if("utf8".equalsIgnoreCase(field.getPropertyType())){
			sb.append("'"+propertyValueMap.get(primarykey)+"'");
	 	   }else{
	 		  sb.append(propertyValueMap.get(primarykey));
	 	   }
		
		return sb.toString();
	}

}
