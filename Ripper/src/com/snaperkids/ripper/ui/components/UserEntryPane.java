/*
 *
 */
package com.snaperkids.ripper.ui.components;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.RootPaneContainer;
import javax.swing.SwingWorker;

import com.snaperkids.ripper.Ripper;
import com.snaperkids.ripper.ui.components.logic.Download;
import com.snaperkids.ripper.ui.components.logic.RipperQueueTable;
import com.snaperkids.ripper.utils.LoggerNames;
import com.snaperkids.services.Protocol;

import javax.swing.JComboBox;

public class UserEntryPane extends JPanel {

	private static final Logger logger;
	/**
	 *
	 */
	private static final long serialVersionUID = 6370503783684032426L;
	static {
		logger = Logger.getLogger(LoggerNames.USER_ENTRY_PANE.name());
		logger.setParent(Logger.getLogger(LoggerNames.GUI.name()));
	}

	private final JPasswordField passwordField;

	private final JTextField hostEntryField;

	private final JTextField usernameEntryField;
	private JTextField pageEntryField;

	/**
	 * Create the panel.
	 */
	public UserEntryPane() {
		logger.info("Building Entry Pane");
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 49, 84, 43, 113, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, 0.0, 1.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		logger.finer("Adding Url to Rip Label");

		logger.finer("Adding Url Entry Field");
		
		JLabel lblProtocol = new JLabel("Protocol:");
		GridBagConstraints gbc_lblProtocol = new GridBagConstraints();
		gbc_lblProtocol.anchor = GridBagConstraints.EAST;
		gbc_lblProtocol.insets = new Insets(0, 0, 5, 5);
		gbc_lblProtocol.gridx = 0;
		gbc_lblProtocol.gridy = 0;
		add(lblProtocol, gbc_lblProtocol);
		
		JComboBox<Protocol> protocolMenu = new JComboBox<Protocol>(new Vector<Protocol>(Ripper.SUPPORTED_PROTOCOLS));
		protocolMenu.setEditable(true);
		GridBagConstraints gbc_protocolMenu = new GridBagConstraints();
		gbc_protocolMenu.insets = new Insets(0, 0, 5, 5);
		gbc_protocolMenu.fill = GridBagConstraints.HORIZONTAL;
		gbc_protocolMenu.gridx = 1;
		gbc_protocolMenu.gridy = 0;
		add(protocolMenu, gbc_protocolMenu);
		
		JLabel lblHost = new JLabel("Host:");
		GridBagConstraints gbc_lblHost = new GridBagConstraints();
		gbc_lblHost.insets = new Insets(0, 0, 5, 5);
		gbc_lblHost.gridx = 2;
		gbc_lblHost.gridy = 0;
		add(lblHost, gbc_lblHost);
		hostEntryField = new JTextField();
		GridBagConstraints gbc_hostEntryField = new GridBagConstraints();
		gbc_hostEntryField.insets = new Insets(0, 0, 5, 5);
		gbc_hostEntryField.fill = GridBagConstraints.HORIZONTAL;
		gbc_hostEntryField.gridx = 3;
		gbc_hostEntryField.gridy = 0;
		add(hostEntryField, gbc_hostEntryField);
		hostEntryField.setColumns(10);

		logger.finer("Adding Username Label");

		logger.finer("Adding Username Entry Field");
		JLabel lblUrlToRip = new JLabel("Page to Rip:");
		GridBagConstraints gbc_lblUrlToRip = new GridBagConstraints();
		gbc_lblUrlToRip.insets = new Insets(0, 0, 5, 5);
		gbc_lblUrlToRip.anchor = GridBagConstraints.EAST;
		gbc_lblUrlToRip.gridx = 0;
		gbc_lblUrlToRip.gridy = 1;
		add(lblUrlToRip, gbc_lblUrlToRip);
		
		pageEntryField = new JTextField();
		GridBagConstraints gbc_pageEntryField = new GridBagConstraints();
		gbc_pageEntryField.gridwidth = 3;
		gbc_pageEntryField.insets = new Insets(0, 0, 5, 5);
		gbc_pageEntryField.fill = GridBagConstraints.HORIZONTAL;
		gbc_pageEntryField.gridx = 1;
		gbc_pageEntryField.gridy = 1;
		add(pageEntryField, gbc_pageEntryField);
		pageEntryField.setColumns(10);

		logger.finer("Adding Password Field");
		JLabel lblUsername = new JLabel("Username:");
		GridBagConstraints gbc_lblUsername = new GridBagConstraints();
		gbc_lblUsername.insets = new Insets(0, 0, 0, 5);
		gbc_lblUsername.anchor = GridBagConstraints.EAST;
		gbc_lblUsername.gridx = 0;
		gbc_lblUsername.gridy = 2;
		add(lblUsername, gbc_lblUsername);
		usernameEntryField = new JTextField();
		GridBagConstraints gbc_usernameEntryField = new GridBagConstraints();
		gbc_usernameEntryField.insets = new Insets(0, 0, 0, 5);
		gbc_usernameEntryField.fill = GridBagConstraints.HORIZONTAL;
		gbc_usernameEntryField.gridx = 1;
		gbc_usernameEntryField.gridy = 2;
		add(usernameEntryField, gbc_usernameEntryField);
		usernameEntryField.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password:");
		GridBagConstraints gbc_lblPassword = new GridBagConstraints();
		gbc_lblPassword.insets = new Insets(0, 0, 0, 5);
		gbc_lblPassword.anchor = GridBagConstraints.EAST;
		gbc_lblPassword.gridx = 2;
		gbc_lblPassword.gridy = 2;
		add(lblPassword, gbc_lblPassword);
		passwordField = new JPasswordField();
		GridBagConstraints gbc_passwordField = new GridBagConstraints();
		gbc_passwordField.insets = new Insets(0, 0, 0, 5);
		gbc_passwordField.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordField.gridx = 3;
		gbc_passwordField.gridy = 2;
		add(passwordField, gbc_passwordField);

		logger.finer("Adding Password Label");

		logger.finer("Adding Start Ripper Button");
		JButton btnStartRipper = new JButton("Start Ripper");
		GridBagConstraints gbc_btnStartRipper = new GridBagConstraints();
		gbc_btnStartRipper.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnStartRipper.insets = new Insets(0, 0, 5, 0);
		gbc_btnStartRipper.gridx = 4;
		gbc_btnStartRipper.gridy = 0;
		btnStartRipper.addActionListener(new RipperStartButtonListener());
		add(btnStartRipper, gbc_btnStartRipper);

		logger.finer("Adding Submit to Rip Button");
		JButton btnSubmitToRip = new JButton("Submit to Rip Queue");
		logger.finer("Adding Button Listener");
		GridBagConstraints gbc_btnSubmitToRip = new GridBagConstraints();
		gbc_btnSubmitToRip.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnSubmitToRip.insets = new Insets(0, 0, 5, 0);
		gbc_btnSubmitToRip.gridx = 4;
		gbc_btnSubmitToRip.gridy = 1;
		add(btnSubmitToRip, gbc_btnSubmitToRip);
		btnSubmitToRip
				.addActionListener(new QueueSubmitButtonListener(protocolMenu, hostEntryField, pageEntryField, usernameEntryField, passwordField));
		

		logger.finer("Adding Submit and Start Button");
		JButton btnSubmitAndStart = new JButton("Submit & Start Ripper");
		logger.finer("Adding Button Listener");
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton.gridx = 4;
		gbc_btnNewButton.gridy = 2;
		add(btnSubmitAndStart, gbc_btnNewButton);
		btnSubmitAndStart.addActionListener(
				new QueueSubmitRipperStartButtonListener(protocolMenu, hostEntryField, pageEntryField, usernameEntryField, passwordField));
	}
	
	private abstract class SubmitButtonListener implements ActionListener {

		private final Logger logger;

		{
			logger = Logger.getLogger(LoggerNames.SUBMIT_BUTTON_LISTENTER.name());
			logger.setParent(Logger.getLogger(LoggerNames.USER_ENTRY_PANE.name()));
		}

		RipperQueue findRipperQueue(JComponent source) {
			logger.finer("Locating ripper queue in the window hierarchy.");
			RipperQueue ripperQueue = null;
			Stack<Component> components = new Stack<>();
			components.addAll(Arrays.asList(source.getTopLevelAncestor().getComponents()));
			while ((ripperQueue == null) && !components.isEmpty()) {
				Component currentComponent = components.pop();
				Container currentContainer = null;
				if (currentComponent instanceof RipperQueue) {
					ripperQueue = (RipperQueue) currentComponent;
				} else {
					if (currentComponent instanceof RootPaneContainer) {
						currentContainer = ((RootPaneContainer) currentComponent).getContentPane();
					} else if (currentComponent instanceof Container) {
						currentContainer = (Container) currentComponent;
					}
					if (currentContainer != null) {
						Component[] parts = currentContainer.getComponents();
						components.addAll(Arrays.asList(parts));
					}
				}
			}
			return ripperQueue;
		}
	}
	
	private class RipperStartButtonListener extends SubmitButtonListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() instanceof JComponent) {
				ArrayList<Download> data = findRipperQueue((JComponent) e.getSource()).getQueueTable().getTableEntries();
				SwingWorker<Void, Void> ripper = new Ripper(data);
				logger.info("Running Ripper.");
				ripper.execute();
			}
		}

	}
	
	private class QueueSubmitRipperStartButtonListener extends SubmitButtonListener {

		private final JPasswordField passwordField;
		private final JTextField hostEntryField;
		private final JTextField usernameEntryField;
		private final JTextField pageEntryField;
		private final JComboBox<Protocol> protocolMenu;

		private QueueSubmitRipperStartButtonListener(JComboBox<Protocol> protocolMenu, JTextField hostEntryField, JTextField pageEntryField,
				JTextField usernameEntryField, JPasswordField passwordField) {
			this.protocolMenu = protocolMenu;
			this.passwordField = passwordField;
			this.hostEntryField = hostEntryField;
			this.pageEntryField = pageEntryField;
			this.usernameEntryField = usernameEntryField;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() instanceof JComponent) {
				logger.finer("Attempting to add entry to rip queue.");
				JComponent src = (JComponent) e.getSource();
				RipperQueue ripperQueue = findRipperQueue(src);
				RipperQueueTable table = ripperQueue.getQueueTable();
				String urlString = generateURLString();
				URL url = null;
				try {
					url = new URL(urlString);
				} catch (MalformedURLException e1) {
					JOptionPane.showMessageDialog(src, e1.getMessage(), "Invalid URL", JOptionPane.ERROR_MESSAGE);
					return;
				}
				String username = usernameEntryField.getText();
				char[] password = passwordField.getPassword();
				Boolean shouldDownload = true;
				Download download = new Download(url, username, password, shouldDownload);
				table.addRow(download);
				ArrayList<Download> data = table.getTableEntries();
				SwingWorker<Void, Void> ripper = new Ripper(data);
				logger.info("Running Ripper.");
				ripper.execute();
			}
		}

		private String generateURLString() {
			StringBuilder urlString = new StringBuilder();
			urlString.append(protocolMenu.getItemAt(protocolMenu.getSelectedIndex()).name().toLowerCase());
			urlString.append("://");
			String hostEntry = hostEntryField.getText();
			if(!hostEntry.startsWith("www.")) {
				urlString.append("www.");
			}
			urlString.append(hostEntry);
			String pageEntry = pageEntryField.getText();
			if(!pageEntry.startsWith("/")) {
				urlString.append("/");
			}
			urlString.append(pageEntry);
			return urlString.toString();
		}

	}
	
	private class QueueSubmitButtonListener extends SubmitButtonListener {

		private final JComboBox<Protocol> protocolMenu;
		private final JPasswordField passwordField;
		private final JTextField hostEntryField;
		private final JTextField usernameEntryField;
		private final JTextField pageEntryField;

		private QueueSubmitButtonListener(JComboBox<Protocol> protocolMenu, JTextField hostEntryField, JTextField pageEntryField,
				JTextField usernameEntryField, JPasswordField passwordField) {
			this.protocolMenu = protocolMenu;
			this.passwordField = passwordField;
			this.hostEntryField = hostEntryField;
			this.pageEntryField = pageEntryField;
			this.usernameEntryField = usernameEntryField;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() instanceof JButton) {
				logger.finer("Attempting to add value into queue.");
				JButton src = (JButton) e.getSource();
				RipperQueue ripperQueue = findRipperQueue(src);
				RipperQueueTable table = ripperQueue.getQueueTable();
				String urlString = generateURLString();
				URL url = null;
				try {
					url = new URL(urlString);
				} catch (MalformedURLException e1) {
					if (hostEntryField.getText().isBlank()) {
						JOptionPane.showMessageDialog(src, "No URL Provided", "Invalid URL", JOptionPane.ERROR_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(src, e1.getMessage(), "Invalid URL", JOptionPane.ERROR_MESSAGE);
					}
					return;
				}
				String username = usernameEntryField.getText();
				char[] password = passwordField.getPassword();
				Boolean shouldDownload = true;
				Download download = new Download(url, username, password, shouldDownload);
				table.addRow(download);
			}
		}
		
		private String generateURLString() {
			StringBuilder urlString = new StringBuilder();
			urlString.append(protocolMenu.getItemAt(protocolMenu.getSelectedIndex()).name().toLowerCase());
			urlString.append("://");
			String hostEntry = hostEntryField.getText();
			if(!hostEntry.startsWith("www.")) {
				urlString.append("www.");
			}
			urlString.append(hostEntry);
			String pageEntry = pageEntryField.getText();
			if(!pageEntry.startsWith("/")) {
				urlString.append("/");
			}
			urlString.append(pageEntry);
			return urlString.toString();
		}

	}

}
