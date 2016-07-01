package com.ui.pane;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.ui.schemaGen.MYSQLSchemaGenerator;


public class SchemaGeneratorPane extends JPanel {

	private JTextField databaseField;
	private JTextField avroTextField;
	private JTextField avroJavaTextField;
	private JButton avroFileDirectoryChoose;
	private JButton avroJavaFileDirChoose;
	private JButton generateSchemaField;
	private JButton translateBtn;
	private JLabel emptyLabel;
	private JLabel databaseLabel;
	private JLabel avroFileLabel;
	private JLabel avroJavaFileLabel;
	private JLabel dataBaseTypeLabel;

	private File avroFile;
	private File javaFile;
	private JComboBox databaseTypeField;
	String selectedDatabaseType;
	private JLabel status;

	final JLabel statusbar = new JLabel("");

	public SchemaGeneratorPane() {
		// construct components
		avroFileDirectoryChoose = new JButton("Avro Folder");
		avroJavaFileDirChoose = new JButton("Java Folder");
		translateBtn = new JButton("Translate");

		databaseField = new JTextField(10);
		avroTextField=new JTextField(10);
		avroJavaTextField=new JTextField(10);
		emptyLabel = new JLabel("");
		avroFileLabel=new JLabel("Select the Folder to Avro File");
		avroJavaFileLabel=new JLabel("Select the Folder to Java Avro File");
		dataBaseTypeLabel=new JLabel("Database");
		status=new JLabel("");
		
		
		databaseLabel=new JLabel("Enter Database Name");
		databaseTypeField= new JComboBox();
		databaseTypeField.addItem(new String("MySql"));
		
		databaseTypeField.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent e) {
		    	  selectedDatabaseType=((JComboBox) e.getSource()).getSelectedItem().toString();
		      }
		    });

		
		avroFileDirectoryChoose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
//				FileNameExtensionFilter filter = new FileNameExtensionFilter(
//						"json");
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//				chooser.setFileFilter(filter);
				chooser.setMultiSelectionEnabled(false);
				int option = chooser.showOpenDialog(SchemaGeneratorPane.this);
				if (option == JFileChooser.APPROVE_OPTION) {
				     
				      avroFile=chooser.getSelectedFile();
				      avroTextField.setText(avroFile.getAbsolutePath());
				}
			}
		});

		avroJavaFileDirChoose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//				FileNameExtensionFilter filter = new FileNameExtensionFilter(
//						"txt");
//				chooser.setFileFilter(filter);
				chooser.setMultiSelectionEnabled(false);
				int option = chooser.showOpenDialog(SchemaGeneratorPane.this);
				if (option == JFileChooser.APPROVE_OPTION) {
					javaFile=chooser.getSelectedFile();
					avroJavaTextField.setText(avroFile.getAbsolutePath());
				}
			}
		});

		translateBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
//				System.out.println(avroFile.getAbsoluteFile());
				
				String dataBaseName=databaseField.getText();
				String dataType=selectedDatabaseType;
				status.setText("");
				if ( dataBaseName != null
						&& dataType != null && avroFile != null && javaFile != null) {
					try{
						MYSQLSchemaGenerator sg = new MYSQLSchemaGenerator(
								dataType, dataBaseName,avroFile.getAbsolutePath(), javaFile
										.getAbsolutePath());
						sg.generateSchema();
						status.setText("schema: generated Successfully");
					}catch(Exception e){
						e.printStackTrace();
						status.setText("Error in Schema Generation");
					}
				}else{
					status.setText("Enter All the Fields");
				}
				
				}
		});
		// adjust size and set layout
		setPreferredSize(new Dimension(600, 300));
		setLayout(new FlowLayout());

		JPanel panel = new JPanel();
		// panel.setBackground(Color.darkGray);
		panel.setSize(400, 600);
		panel.setAlignmentX(LEFT_ALIGNMENT);

		GridLayout layout = new GridLayout(7, 2);
		layout.setHgap(10);
		layout.setVgap(10);

		panel.setLayout(layout);

		// add components
		panel.add(dataBaseTypeLabel);
		panel.add(databaseTypeField);
		panel.add(databaseLabel);
		panel.add(databaseField);

		panel.add(databaseLabel);
		panel.add(databaseField);

		
		panel.add(avroTextField);
		panel.add(avroFileDirectoryChoose);

		panel.add(avroJavaTextField);
		panel.add(avroJavaFileDirChoose);
		
		panel.add(translateBtn);
		panel.add(status);

		add(panel);

		JPanel panel1 = new JPanel();
		panel1.setSize(300, 300);
		panel1.add(statusbar);
		add(panel1);
	}
}
