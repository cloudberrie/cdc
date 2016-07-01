package com.linkedin.databus.core.test;

/*
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
 */

//import com.linkedin.databus.client.DbusEventAvroDecoder;
import com.linkedin.databus.core.DbusEventBuffer;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.avro.generic.GenericRecord;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.linkedin.databus.core.DbusEventBuffer.AllocationPolicy;
import com.linkedin.databus.core.DbusEventBuffer.DbusEventIterator;
import com.linkedin.databus.core.DbusEventBuffer.QueuePolicy;
import com.linkedin.databus.core.DbusEventFactory;
import com.linkedin.databus.core.DbusEventInfo;
import com.linkedin.databus.core.DbusEventKey;
import com.linkedin.databus.core.DbusEventSerializable;
import com.linkedin.databus.core.DbusEventV1Factory;
import com.linkedin.databus.core.DbusEventKey.KeyType;
import com.linkedin.databus.core.DbusOpcode;
import com.linkedin.databus.core.Encoding;
import com.linkedin.databus.core.test.DbusEventCorrupter;
import com.linkedin.databus.core.test.DbusEventCorrupter.EventCorruptionType;
import com.linkedin.databus.core.test.DbusEventFactoryForTesting;
import com.linkedin.databus.core.util.Base64;
import com.linkedin.databus.core.util.InvalidConfigException;
import com.linkedin.databus.core.util.RngUtils;
import com.linkedin.databus.core.util.Utils;

public class TestDbusEvent {
	private static final long key = 12345L;
	private static final long timeStamp = 3456L;
	private static final short partitionId = 30;
	private static final short srcId = 15;
	private static final byte[] schemaId = "abcdefghijklmnop".getBytes(Charset
			.defaultCharset());

	private static final String DATA_ROOT_DIR_PROP_NAME = "test.datadir";
	private static final String DATA_DIR_NAME = "./test_data";
	private static final String OLD_JAVA_EVENT_FILE = "DbusEventUpsertByOldJava.evt";
	private static final DbusEventFactory _eventV1Factory = new DbusEventV1Factory();
	public static final Logger LOG = Logger.getLogger(TestDbusEvent.class
			.getName());

	public static void main(String args[]) throws Exception {

		String valueStr = "testvalue";
		ByteBuffer serializationBuffer = ByteBuffer.allocate(1000).order(
				_eventV1Factory.getByteOrder());

		DbusEventInfo eventInfo = new DbusEventInfo(DbusOpcode.INSERT, 2L,
				(short) 0, (short) 3, timeStamp, (short) 5, schemaId,
				valueStr.getBytes(Charset.defaultCharset()), false, true);
		eventInfo.setIsInsertEvent(false);
		// DbusEventInfo eventInfo1 = new DbusEventInfo(null, 0L, pPartitionId,
		// lPartitionId,
		// timeStamp, srcId, schemaId, value, enableTracing,
		// false);

		eventInfo.setEventSerializationVersion(DbusEventFactory.DBUS_EVENT_V1); // make
																				// this
																				// explicit
		DbusEventFactory.serializeEvent(new DbusEventKey(1L),
				serializationBuffer, eventInfo);
		com.linkedin.databus.core.DbusEventInternalReadable event1 = _eventV1Factory
				.createReadOnlyDbusEventFromBuffer(serializationBuffer, 0);
		// test JSON_PLAIN_VALUE
		ByteArrayOutputStream jsonOut = new ByteArrayOutputStream();
		WritableByteChannel jsonOutChannel = Channels.newChannel(jsonOut);
		event1.writeTo(jsonOutChannel, Encoding.JSON_PLAIN_VALUE);

		byte[] jsonBytes = jsonOut.toByteArray();
		String jsonString = new String(jsonBytes);
		System.out.println(jsonString);
		
//		DbusEventAvroDecoder eventDecoder=new DbusEventAvroDecoder();
		
//		GenericRecord decodedEvent = eventDecoder.getGenericRecord(event, null);

		DbusEventBuffer eventBuffer1 = new DbusEventBuffer(getConfig(100000,
				DbusEventBuffer.Config.DEFAULT_INDIVIDUAL_BUFFER_SIZE, 10000,
				1000, AllocationPolicy.HEAP_MEMORY,
				QueuePolicy.OVERWRITE_ON_WRITE));
		 eventBuffer1.startEvents();
		 
		 System.out.println(DbusEventSerializable.appendToEventBuffer(jsonString,
				 eventBuffer1,
				 null,
				 false));

		 eventBuffer1.endEvents(2);

	}

	public static DbusEventBuffer.StaticConfig getConfig(long maxEventBufferSize,
			int maxIndividualBufferSize, int maxIndexSize,
			int maxReadBufferSize, AllocationPolicy allocationPolicy,
			QueuePolicy policy) throws InvalidConfigException {
		DbusEventBuffer.Config config = new DbusEventBuffer.Config();
		config.setMaxSize(maxEventBufferSize);
		config.setMaxIndividualBufferSize(maxIndividualBufferSize);
		config.setScnIndexSize(maxIndexSize);
		config.setAverageEventSize(maxReadBufferSize);
		config.setAllocationPolicy(allocationPolicy.name());
		config.setQueuePolicy(policy.toString());
		return config.build();
	}

}
