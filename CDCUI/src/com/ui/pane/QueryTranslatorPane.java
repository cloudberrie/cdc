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

import com.ui.parser.JSONToQueryParser;

public class QueryTranslatorPane extends JPanel {

	private JTextField sourceFileTextField;
	private JTextField targetFileTextField;
	private JButton souceFileChoose;
	private JButton targetFileChoose;
	private JButton translateBtn;
	private JLabel emptyLabel;

	private File sourceFile;
	private File targetFile;

	final JLabel statusbar = new JLabel("");

	public QueryTranslatorPane() {
		// construct components
		souceFileChoose = new JButton("Source File");
		targetFileChoose = new JButton("Target File");
		translateBtn = new JButton("Translate");

		sourceFileTextField = new JTextField(10);
		targetFileTextField = new JTextField(10);

		emptyLabel = new JLabel("");

		souceFileChoose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
//				FileNameExtensionFilter filter = new FileNameExtensionFilter(
//						"json");
				JFileChooser chooser = new JFileChooser();
//				chooser.setFileFilter(filter);
				chooser.setMultiSelectionEnabled(false);
				int option = chooser.showOpenDialog(QueryTranslatorPane.this);
				if (option == JFileChooser.APPROVE_OPTION) {
					sourceFile = chooser.getSelectedFile();
					sourceFileTextField.setText(sourceFile.getAbsolutePath());
				}
			}
		});

		targetFileChoose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JFileChooser chooser = new JFileChooser();
//				FileNameExtensionFilter filter = new FileNameExtensionFilter(
//						"txt");
//				chooser.setFileFilter(filter);
				chooser.setMultiSelectionEnabled(false);
				int option = chooser.showOpenDialog(QueryTranslatorPane.this);
				if (option == JFileChooser.APPROVE_OPTION) {
					targetFile = chooser.getSelectedFile();
					targetFileTextField.setText(targetFile.getAbsolutePath());
				}
			}
		});

		translateBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (sourceFile != null && targetFile != null) {
					statusbar.setText("Transalating JSON File...");
					try {
						JSONToQueryParser.convert(sourceFile, targetFile);
						statusbar.setText("Successfully Translated to: "
								+ targetFile.getAbsolutePath());
					} catch (Exception e) {
						statusbar.setText("Error in Translation:");
						e.printStackTrace();
					}
				} else {
					statusbar.setText("Select the file For Translation");
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

		GridLayout layout = new GridLayout(3, 2);
		layout.setHgap(10);
		layout.setVgap(10);

		panel.setLayout(layout);

		// add components
		panel.add(sourceFileTextField);
		panel.add(souceFileChoose);
		panel.add(targetFileTextField);
		panel.add(targetFileChoose);

		panel.add(translateBtn);

		add(panel);

		JPanel panel1 = new JPanel();
		panel1.setSize(300, 300);
		panel1.add(statusbar);
		add(panel1);
	}
}
