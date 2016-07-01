package com.linkedin.databus.client.example;
/*
 *
 * Copyright 2013 LinkedIn Corp. All rights reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
*/


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.util.Properties;

import com.linkedin.databus.client.DatabusHttpClientImpl;

public class PersonClientMain
{
  static final String PERSON_SOURCE = "com.linkedin.events.example.or_test.Person";
  
  static Properties props=new Properties();

  public static void main(String[] args) throws Exception
  {
	  loadProperties();
	  createPidFile();
    DatabusHttpClientImpl.Config configBuilder = new DatabusHttpClientImpl.Config();
    String sourcesStr=props.getProperty("source.events");
    
    String[] sources=sourcesStr.split(",");
    
    //Try to connect to a relay on localhost
    configBuilder.getRuntime().getRelay("1").setHost("localhost");
    configBuilder.getRuntime().getRelay("1").setPort(11115);
    configBuilder.getRuntime().getRelay("1").setSources(sourcesStr);

    //Instantiate a client using command-line parameters if any
    DatabusHttpClientImpl client = DatabusHttpClientImpl.createFromCli(args, configBuilder);
    
    

    //register callbacks
    PersonConsumer personConsumer = new PersonConsumer(props.getProperty("databus.json.changeEvents.file.path"));
    client.registerDatabusStreamListener(personConsumer, null, sources);
    client.registerDatabusBootstrapListener(personConsumer, null, sources);

    //fire off the Databus client
    client.startAndBlock();
  }

	public static void loadProperties() {

		try {
			File file=new File("./conf/client.properties");
			InputStream in=new FileInputStream(file);
//			InputStream in = PersonClientMain.class.getResourceAsStream("conf/client.properties");
			props.load(in);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public static void createPidFile() {
		try {
			String processName = ManagementFactory.getRuntimeMXBean().getName();
			String pidStr = ManagementFactory.getRuntimeMXBean().getName();

			Integer pid = 0;
			if (pidStr.contains("@")) {
				pid = Integer.parseInt(pidStr.split("@")[0]);
			}

			File file = new File("ClientRelay_Pid.txt");
			if (file.exists()) {
				FileReader reader = new FileReader("ClientRelay_Pid.txt");
				BufferedReader br=new BufferedReader(reader);
				String prevPid = br.readLine();
				Runtime.getRuntime().exec("taskkill /F /PID " + prevPid);
				file.delete();
				reader.close();
			} 
				FileWriter writer = new FileWriter(file);
				BufferedWriter br=new BufferedWriter(writer);
				br.write(String.valueOf(pid));
				br.close();
				writer.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
  
}
