package com.ui.master;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.ui.pane.QueryTranslatorPane;
import com.ui.pane.SQLServerDataDumpPane;
import com.ui.pane.SchemaGeneratorPane;
import com.ui.util.Environment;

public class Master {
	
	public final static String DATABUS_RELAY_CLIENT_PATH = "databus.relay.client.startfile.path";
	public final static String DATABUS_RELAY_PATH = "databus.relay.startfile.path";
	public final static String DATABUS_RELAY_START_FILE = "databus.relay.startfile.fileName";
	public final static String DATABUS_RELAY_CLIENT_START_FILE = "databus.relay.client.startfile.name";
	public final static String DATABUS_RELAY_DRIVE = "databus.project.drive";
	
	
	private JButton openButton;
	private JButton startDatabusRelayBtn;
	private JButton startDatabusClientBtn;
	private JButton schemaGenerator;
	private JButton sqlServerDataDumpBtn;
	private JLabel relayStatusField;
	private JLabel clientRelaytatusField;
	private JLabel ProgressLField;

	private boolean isRelayStarted;
	private boolean isClientStarted;

	private Process relayProcess;
	private Process clientProcess;
//	Properties props = new Properties();
	Thread relayThread;
	JPanel grid;
	
	
	public Master() {

//		loadProperties();
		relayStatusField = new JLabel("--- Relay Not Started");
		clientRelaytatusField = new JLabel("--- Client Not Started");
		ProgressLField = new JLabel("Progress:");

//		openButton = new JButton("QueryGenerator");
		openButton=createSimpleButton("Query Generator");
		startDatabusRelayBtn = createSimpleButton("Start Relay");
		startDatabusClientBtn = createSimpleButton("Start Client");
		schemaGenerator=createSimpleButton("Schema Generator");
		sqlServerDataDumpBtn=createSimpleButton("SQL Server Data Dump");
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
		sqlServerDataDumpBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame frame = new JFrame("SQL Server Service");
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.getContentPane().add(new SQLServerDataDumpPane());
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
						
					   
						String[] command = {"cmd.exe", "/C", "Start", "start-relay.bat"};
						
						relayProcess = Runtime.getRuntime().exec(command); 
						
						startDatabusRelayBtn.setText("Stop Relay");
						relayStatusField.setText("--- Started Relay");
					} else {
						int pid=killProcess("databus2-relay\\Relay_Pid.txt");
						if (relayProcess != null) {
							relayProcess.destroy();
							
							isRelayStarted = false;
							startDatabusRelayBtn.setText("Start Relay");
							relayStatusField.setText("--- Relay Stopped.. PID-"+pid);
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

						String[] command = {"cmd.exe", "/C", "Start", "start-client.bat"};
						clientProcess = Runtime.getRuntime().exec(command); 
						startDatabusClientBtn.setText("Stop Client");
						clientRelaytatusField.setText("--- Started Client");


					} else {
						isClientStarted = false;
//						clientProcess.destroy();
						int pid=killProcess("databus2-client\\ClientRelay_Pid.txt");
						startDatabusClientBtn.setText("Start Client");
						clientRelaytatusField.setText("--- Stopped Client: PID-"+pid);
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

		panel.add(schemaGenerator);	
		panel.add(openButton);			
		panel.add(startDatabusRelayBtn);		
		panel.add(startDatabusClientBtn);
		panel.add(sqlServerDataDumpBtn);

		panel.setBackground(Color.gray);
		panel.setVisible(true);
		
		JFrame frame = new JFrame("CDC Master");
		frame.setBackground(Color.LIGHT_GRAY);
		
		grid = new JPanel() {
			@Override
			// arbitrary placeholder size
			public Dimension getPreferredSize() {
				return new Dimension(600, 400);
			}
		};
		
		Box panel1 = new Box(BoxLayout.Y_AXIS);
		
		frame.add(panel, BorderLayout.WEST);
		grid.setBackground(Color.red);
		panel1.add(ProgressLField);
		panel1.add(relayStatusField);
		panel1.add(clientRelaytatusField);
		grid.add(panel1,BorderLayout.WEST);
		frame.add(grid, BorderLayout.CENTER);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	

	public static void main(String args[]) {
		new Master();
	}

//	public void loadProperties() {
//
//		try {
////			File file= new File(this.getClass().getClassLoader().getResourceAsStream("conf.properties"));
////			File file=new File("conf.properties");
////			InputStream in=this.getClass().getResourceAsStream("../conf/client_conf.properties");
//			File file=new File("./resource/conf/client_conf.properties");
//			InputStream in=new FileInputStream(file);
//			props.load(in);
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new RuntimeException(e);
//		}
//	}
	
	public static int killProcess(String filePath) {
		int pid=0;
		
		File currentDirectory = new File(new File(".").getAbsolutePath());
		String pidFile=currentDirectory.getParentFile().getParentFile().getAbsolutePath()+"\\"+filePath;
		System.out.println(pidFile);
		try{
			  File file=new File(pidFile);
			  if(file.exists()){
				  FileReader reader=new FileReader(file);
				  BufferedReader br=new BufferedReader(reader);
				   String pidStr=br.readLine();
				  pid=Integer.valueOf(pidStr);
				  Runtime.getRuntime().exec("taskkill /F /PID "+pidStr);
				  br.close();
				  reader.close();
			  }	  
		}catch(Exception e){
			e.printStackTrace();
		}
		return pid;
	}
  
	private static JButton createSimpleButton(String text) {
		  JButton button = new JButton(text);
		  button.setForeground(Color.BLUE);
		  button.setBackground(Color.LIGHT_GRAY);
		  Border line = new LineBorder(Color.WHITE);
		  Border margin = new EmptyBorder(5, 15, 5, 15);
		  Border compound = new CompoundBorder(line, margin);
		  button.setBorder(compound);
		  return button;
		}



}
