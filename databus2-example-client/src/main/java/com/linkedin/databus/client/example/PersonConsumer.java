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


import org.apache.avro.generic.GenericRecord;
import org.apache.avro.util.Utf8;
import org.apache.log4j.Logger;
import java.io.FileOutputStream;

import java.io.IOException; 
import java.io.OutputStream; 
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;



import com.linkedin.databus.client.consumer.AbstractDatabusCombinedConsumer;
import com.linkedin.databus.client.pub.ConsumerCallbackResult;
import com.linkedin.databus.client.pub.DbusEventDecoder;
import com.linkedin.databus.client.DbusEventAvroDecoder;
import com.linkedin.databus.core.DbusEvent;

public class PersonConsumer extends AbstractDatabusCombinedConsumer
{
  public static final Logger LOG = Logger.getLogger(PersonConsumer.class.getName());
  
  OutputStream output=null;
  public PersonConsumer(String jsonFilePath){
	  
	  try{
		      String fileName=getFileName();
		  	  File file =new File(fileName);
			  if(file.exists()){
				   file.delete();
			  }
			  output = new FileOutputStream(fileName,true);	
		}catch (Exception e) {
      LOG.error("error decoding event ", e);
     } 	
	  
  }
  


  @Override
  public ConsumerCallbackResult onDataEvent(DbusEvent event,
                                            DbusEventDecoder eventDecoder)
  {
    return processEvent(event, eventDecoder);
  }

  @Override
  public ConsumerCallbackResult onBootstrapEvent(DbusEvent event,
                                                 DbusEventDecoder eventDecoder)
  {
    return processEvent(event, eventDecoder);
  }

  private ConsumerCallbackResult processEvent(DbusEvent event,
                                              DbusEventDecoder eventDecoder)
  {
      GenericRecord decodedEvent = eventDecoder.getGenericRecord(event, null);

 
//      Utf8 firstName = (Utf8)decodedEvent.get("firstName");
//      Utf8 lastName = (Utf8)decodedEvent.get("lastName");
//      Long birthDate = (Long)decodedEvent.get("birthDate");
//      Utf8 deleted = (Utf8)decodedEvent.get("deleted");   
    try {

//      LOG.info("firstName: " + firstName.toString() +
//               ", lastName: " + lastName.toString() +
//               ", birthDate: " + birthDate +
//               ", deleted: " + deleted.toString());
//			   



    DbusEventAvroDecoder avroDecoder=(DbusEventAvroDecoder)eventDecoder;
	avroDecoder.dumpEventValueInJSON(event,output);
			   
    } catch (Exception e) {
      LOG.error("error decoding event ", e);
      return ConsumerCallbackResult.ERROR;
    }

    return ConsumerCallbackResult.SUCCESS;
  }
  
  
  public  static String getFileName(){
	  
	  Date date=new Date();
	  SimpleDateFormat df2 = new SimpleDateFormat("dd_MM_yy");
      String dateText = df2.format(date);
	  
	  String fileName="output/CDC_Json_output_"+dateText+"_"+date.getTime()+".json";
	  return fileName;
  }

}
