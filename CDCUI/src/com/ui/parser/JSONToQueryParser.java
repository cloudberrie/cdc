package com.ui.parser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import org.codehaus.jackson.map.ObjectMapper;

import com.ui.beans.DBRecord;
import com.ui.querygen.QueryGenerator;
import com.ui.querygen.SQLServerQueryGenerator;


public class JSONToQueryParser {
	

	public static void convert(File sourceFile,File targetFile) throws Exception{
		
			FileReader fileReader=new FileReader(sourceFile);
			FileWriter fileWriter=new FileWriter(targetFile);
			
			BufferedReader bufferedReader = new BufferedReader(fileReader);			
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			
			QueryGenerator queryGenerator=new SQLServerQueryGenerator();
			ObjectMapper mapperObj = new ObjectMapper();
			String jsonStrLine;
			while ((jsonStrLine = bufferedReader.readLine()) != null) {					

				DBRecord dbRecord = mapperObj.readValue(jsonStrLine, DBRecord.class);
				String query=queryGenerator.createQuery(dbRecord);
				
				System.out.println(jsonStrLine);
				
				bufferedWriter.write(query);
				bufferedWriter.write("\n");
			}
			fileReader.close();
			bufferedWriter.close();	
	}

}
