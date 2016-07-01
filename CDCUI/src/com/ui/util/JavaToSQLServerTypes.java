package com.ui.util;

public enum JavaToSQLServerTypes {
	
	;

	
	  private final String _avroType;
	  
	  private JavaToSQLServerTypes(String avroType)
	  {
	    _avroType = avroType;
	  }
	  public String getAvroType()
	  {
	    return _avroType;
	  }

}
