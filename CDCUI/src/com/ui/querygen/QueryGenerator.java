package com.ui.querygen;

import com.ui.beans.DBRecord;


public interface QueryGenerator {
	
	public String OPERATION_INSERT="INSERT";
	public String OPERATION_DELETE="DELETE";
	public String OPERATION_UPDATE="UPSERT";
	
	public String createQuery(DBRecord dbRecord);
}