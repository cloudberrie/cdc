package com.ui.master;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.util.Properties;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.ui.pane.QueryTranslatorPane;
import com.ui.pane.SchemaGeneratorPane;

public class CDCMaster extends JPanel {

	public final static String DATABUS_RELAY_CLIENT_PATH = "databus.relay.client.startfile.path";
	public final static String DATABUS_RELAY_PATH = "databus.relay.startfile.path";
	public final static String DATABUS_RELAY_START_FILE = "databus.relay.startfile.fileName";
	public final static String DATABUS_RELAY_CLIENT_START_FILE = "databus.relay.client.startfile.name";
	public final static String DATABUS_RELAY_DRIVE = "databus.project.drive";

	private JButton openButton;
	private JButton startDatabusRelayBtn;
	private JButton startDatabusClientBtn;
	private JButton schemaGenerator;
	private JLabel relayStatusField;
	private JLabel clientRelaytatusField;
	private JLabel ProgressLField;

	private boolean isRelayStarted;
	private boolean isClientStarted;

	private Process relayProcess;
	private Process clientProcess;
	Properties props = new Properties();
	Thread relayThread;

	public CDCMaster() {

		loadProperties();
		relayStatusField = new JLabel("--- Relay Not Started");
		clientRelaytatusField = new JLabel("--- Client Not Started");
		ProgressLField = new JLabel("Progress:");

		openButton = new JButton("QueryGenerator");
		startDatabusRelayBtn = new JButton("Start Relay");
		startDatabusClientBtn = new JButton("Start Client");
		schemaGenerator=new JButton("Schema Generator");
		openButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame frame = new JFrame("Query Translator");
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.getContentPane().add(new QueryTranslatorPane());
				frame.pack();
				frame.setLocation(100, 100);
				frame.setVisible(true);
			}
		});

		startDatabusRelayBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				try {
					if (!isRelayStarted) {
						isRelayStarted = true;

						ProcessBuilder builder = new ProcessBuilder(
								"cmd.exe",
								"/c",
								"cd "
										+ props.getProperty(DATABUS_RELAY_PATH)
										+ " && "
										+ props.getProperty(DATABUS_RELAY_DRIVE)
										+ " && "
										+ props.getProperty(DATABUS_RELAY_START_FILE));
						builder.redirectErrorStream(true);
						relayProcess = builder.start();
						
						startDatabusRelayBtn.setText("Stop Relay");
						relayStatusField.setText("--- Started Relay");
					} else {
						if (relayProcess != null) {
							relayProcess.destroy();
							killProcess("Relay_Pid.txt");
							isRelayStarted = false;
							startDatabusRelayBtn.setText("Start Relay");
							relayStatusField.setText("--- Relay Stopped..");
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});

		startDatabusClientBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if (!isClientStarted) {
						isClientStarted = true;
						
						System.out.println("ishore: "+props.getProperty(DATABUS_RELAY_CLIENT_PATH));

						ProcessBuilder builder = new ProcessBuilder("cmd.exe",
								"/c", "cd " + props.getProperty(DATABUS_RELAY_CLIENT_PATH)
										+ " && " + props.getProperty(DATABUS_RELAY_DRIVE) + " && "
										+ props.getProperty(DATABUS_RELAY_CLIENT_START_FILE));
						builder.redirectErrorStream(true);
						clientProcess = builder.start();
						startDatabusClientBtn.setText("Stop Client");
						clientRelaytatusField.setText("--- Started Client");

//						BufferedReader r = new BufferedReader(
//								new InputStreamReader(relayProcess
//										.getInputStream()));
//						String line;
//						while (true) {
//							line = r.readLine();
//							if (line == null) {
//								break;
//							}
//							System.out.println(line);
//						}
					} else {
						isClientStarted = false;
//						clientProcess.destroy();
						killProcess("ClientRelay_Pid.txt");
						startDatabusClientBtn.setText("Start Client");
						clientRelaytatusField.setText("--- Stopped Client");
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		
		schemaGenerator.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame frame = new JFrame("Schema Generator");
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.getContentPane().add(new SchemaGeneratorPane());
				frame.pack();
				frame.setLocation(100, 100);
				frame.setVisible(true);				
			}
		});

		Box panel = new Box(BoxLayout.Y_AXIS);
		setLayout(new FlowLayout());
		// adjust size and set layout
		setPreferredSize(new Dimension(700, 500));
//		JPanel panel = new JPanel();
		// panel.setBackground(Color.darkGray);
//		panel.setSize(300, 300);
//		panel.setAlignmentX(LEFT_ALIGNMENT);

//		GridLayout layout = new GridLayout(4, 2);
//		layout.setHgap(10);
//		layout.setVgap(10);
//
//		panel.setLayout(layout);

		panel.add(openButton);
		panel.add(ProgressLField);
		panel.add(startDatabusRelayBtn);
		panel.add(relayStatusField);
		panel.add(startDatabusClientBtn);
		panel.add(clientRelaytatusField);
		panel.add(schemaGenerator);				

		add(panel, BorderLayout.WEST);
	}

	public static void main(String args[]) {
		JFrame frame = new JFrame("CDC Master");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(new CDCMaster());
		frame.pack();
		frame.setVisible(true);
	}

	public void loadProperties() {

		try {
//			File file= new File(this.getClass().getClassLoader().getResourceAsStream("conf.properties"));
//			File file=new File("conf.properties");
//			InputStream in=this.getClass().getResourceAsStream("../conf/client_conf.properties");
			File file=new File("./resource/conf/client_conf.properties");
			InputStream in=new FileInputStream(file);
			props.load(in);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public static void killProcess(String filePath) {
		try{
			  File file=new File(filePath);
			  if(file.exists()){
				  InputStream in=new FileInputStream(file);
				  int pid=in.read();
				  Runtime.getRuntime().exec("taskkill /F /PID "+pid);
				  in.close();
			  }	  
		}catch(Exception e){
			e.printStackTrace();
		}
	}
  

}
