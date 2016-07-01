package com.linkedin.databus.relay.example;

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
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.util.Map;

import org.apache.log4j.Logger;

import com.linkedin.databus.container.netty.HttpRelay;
import com.linkedin.databus.core.data_model.PhysicalPartition;
import com.linkedin.databus.core.util.InvalidConfigException;
import com.linkedin.databus2.core.DatabusException;
import com.linkedin.databus2.core.seq.MultiServerSequenceNumberHandler;
import com.linkedin.databus2.producers.EventProducer;
import com.linkedin.databus2.relay.DatabusRelayMain;
import com.linkedin.databus2.relay.config.PhysicalSourceStaticConfig;

public class RelayServer extends DatabusRelayMain {
	public static final String MODULE = RelayServer.class.getName();
	public static final Logger LOG = Logger.getLogger(MODULE);
	// static final String FULLY_QUALIFIED_PERSON_EVENT_NAME =
	// "com.linkedin.events.example.person.Person";
	// static final int PERSON_SRC_ID = 40;

	static String sourceFilePath = "conf/sources_database.json";

	MultiServerSequenceNumberHandler _maxScnReaderWriters;
	protected Map<PhysicalPartition, EventProducer> _producers;

	public RelayServer() throws IOException, InvalidConfigException,
			DatabusException {
		this(new HttpRelay.Config(), null);
	}

	public RelayServer(String sourceFilePath) throws IOException,
			InvalidConfigException, DatabusException {
		this(new HttpRelay.Config(), null);
	}

	public RelayServer(HttpRelay.Config config,
			PhysicalSourceStaticConfig[] pConfigs) throws IOException,
			InvalidConfigException, DatabusException {
		this(config.build(), pConfigs);
	}

	public RelayServer(HttpRelay.StaticConfig config,
			PhysicalSourceStaticConfig[] pConfigs) throws IOException,
			InvalidConfigException, DatabusException {
		super(config, pConfigs);

	}

	public static void main(String[] args) throws Exception {

		createPidFile();
		Cli cli = new Cli();
		cli.setDefaultPhysicalSrcConfigFiles(sourceFilePath);
		cli.processCommandLineArgs(args);
		cli.parseRelayConfig();
		// Process the startup properties and load configuration
		PhysicalSourceStaticConfig[] pStaticConfigs = cli
				.getPhysicalSourceStaticConfigs();
		HttpRelay.StaticConfig staticConfig = cli.getRelayConfigBuilder()
				.build();

		// Create and initialize the server instance
		DatabusRelayMain serverContainer = new DatabusRelayMain(staticConfig,
				pStaticConfigs);

		serverContainer.initProducers();
		serverContainer.registerShutdownHook();
		serverContainer.startAndBlock();
	}

	public void startServer(String[] args) throws Exception {
		createPidFile();
		Cli cli = new Cli();
		cli.setDefaultPhysicalSrcConfigFiles(sourceFilePath);
		cli.processCommandLineArgs(args);
		cli.parseRelayConfig();
		// Process the startup properties and load configuration
		PhysicalSourceStaticConfig[] pStaticConfigs = cli
				.getPhysicalSourceStaticConfigs();
		HttpRelay.StaticConfig staticConfig = cli.getRelayConfigBuilder()
				.build();

		// Create and initialize the server instance
		DatabusRelayMain serverContainer = new DatabusRelayMain(staticConfig,
				pStaticConfigs);

		serverContainer.initProducers();
		serverContainer.registerShutdownHook();
		serverContainer.startAndBlock();
	}

	public String getSourceFilePath() {
		return sourceFilePath;
	}

	public void setSourceFilePath(String sourceFilePath) {
		this.sourceFilePath = sourceFilePath;
	}

	public static void createPidFile() {
		try {
			String processName = ManagementFactory.getRuntimeMXBean().getName();
			String pidStr = ManagementFactory.getRuntimeMXBean().getName();

			Integer pid = 0;
			if (pidStr.contains("@")) {
				pid = Integer.parseInt(pidStr.split("@")[0]);
			}
			File file = new File("Relay_Pid.txt");
			if (file.exists()) {
				FileReader reader = new FileReader("Relay_Pid.txt");
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
