package com.ui.master;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import com.ui.util.Environment;
import com.ui.util.SQLServerDataService;


public class Test {
	
	
	public static void main(String[] args) throws Exception {
		
	
		SQLServerDataService service=new SQLServerDataService();
		
		service.insertData("F:\\kishore\\CDC_wrkSpace\\CDCUI\\test.sql");

    }



}
