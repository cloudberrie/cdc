package com.ui.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class Environment {
	private static Properties props=null;
	
	public static String getProperty(String propertyName){
		
		if(props==null){
			synchronized (Environment.class) {
				createProperties();
			}
		}		
		return props.getProperty(propertyName);
	}
	
	private static void createProperties(){
		
		try{			
			props=new Properties();
			File file=new File("./resource/conf/client_conf.properties");
			InputStream in=new FileInputStream(file);
			props.load(in);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}

}
