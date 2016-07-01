package com.ui.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class SQLServerDataService {
	
	private final static  String SQLSERVER_DRIVER_KEY="SQLServer.jdbc.driver";
	private final static  String SQLSERVER_URL_KEY="SQLServer.jdbc.url";
	
	static{		
		try{
			Class.forName(Environment.getProperty(SQLSERVER_DRIVER_KEY));
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	private Connection createConnection() throws Exception{		
		return DriverManager.getConnection(Environment.getProperty(SQLSERVER_URL_KEY));
	}
	
	public void insertData(String filePath)throws Exception{
		
		System.out.println("kishore: "+filePath);
		
		Connection con=createConnection();
		
		Statement sta = con.createStatement();
		
		try{
			File file=new File(filePath);
			
			if(file.exists()){
				FileReader in=new FileReader(file);
				BufferedReader br = new BufferedReader(in);

				String query;
				
				final int batchSize = 1000;
				int count = 0;
				con.setAutoCommit(false);
				 while ((query = br.readLine()) != null) {
					 System.out.println(query);
					 sta.addBatch(query);
					 if(++count % batchSize == 0) {
						 sta.executeBatch();
						}				 
				 }
				 sta.executeBatch();
//				String Sql = "INSERT INTO PERSON(FIRST_NAME,LAST_NAME,DOB) VALUES('KISHORE','KIMAR',GETDATE())";			
			}	
			con.commit();
		}catch(Exception e){
			con.rollback();
			e.printStackTrace();
		}	
		con.close();
	}

}
