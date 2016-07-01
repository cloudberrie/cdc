package com.ui.pane;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.ui.util.SQLServerDataService;

public class SQLServerDataDumpPane extends JPanel {

	private JTextField sourceFileTextField;
	private JButton souceFileChoose;

	private JButton submitBtn;
	private JLabel emptyLabel;

	private File sourceFile;

	final JLabel statusbar = new JLabel("");

	public SQLServerDataDumpPane() {
		// construct components
		souceFileChoose = new JButton("Choose File");
		submitBtn = new JButton("Submit");

		sourceFileTextField = new JTextField(10);

		emptyLabel = new JLabel("");

		souceFileChoose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
//				FileNameExtensionFilter filter = new FileNameExtensionFilter(
//						"json");
				JFileChooser chooser = new JFileChooser();
//				chooser.setFileFilter(filter);
				chooser.setMultiSelectionEnabled(false);
				int option = chooser.showOpenDialog(SQLServerDataDumpPane.this);
				if (option == JFileChooser.APPROVE_OPTION) {
					sourceFile = chooser.getSelectedFile();
					sourceFileTextField.setText(sourceFile.getAbsolutePath());
				}
			}
		});


		submitBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (sourceFile != null ) {
					statusbar.setText("Processing File...");
					try {
						
						SQLServerDataService service=new SQLServerDataService();						
						service.insertData(sourceFile.getAbsolutePath());
						statusbar.setText("Success...");
					} catch (Exception e) {
						statusbar.setText("Error in Processing:");
						e.printStackTrace();
					}
				} else {
					statusbar.setText("Select the file to Dump");
				}
			}
		});
		// adjust size and set layout
		setPreferredSize(new Dimension(600, 300));
		setLayout(new FlowLayout());

		JPanel panel = new JPanel();
		// panel.setBackground(Color.darkGray);
		panel.setSize(300, 300);
		panel.setAlignmentX(LEFT_ALIGNMENT);

		GridLayout layout = new GridLayout(2, 2);
		layout.setHgap(10);
		layout.setVgap(10);

		panel.setLayout(layout);

		// add components
		panel.add(sourceFileTextField);
		panel.add(souceFileChoose);

		panel.add(submitBtn);

		add(panel);

		JPanel panel1 = new JPanel();
		panel1.setSize(300, 300);
		panel1.add(statusbar);
		add(panel1);
	}
}
