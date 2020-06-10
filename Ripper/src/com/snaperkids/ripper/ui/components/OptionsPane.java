/*
 *
 */
package com.snaperkids.ripper.ui.components;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.snaperkids.ripper.config.InternalConfigurator;
import com.snaperkids.ripper.config.Setting;
import com.snaperkids.ripper.utils.LoggerNames;

public class OptionsPane extends JPanel {

	private static final Logger logger;

	/**
	 *
	 */
	private static final long serialVersionUID = 5629679671203483113L;

	static {
		logger = Logger.getLogger(LoggerNames.OPTIONS_PANE.name());
		logger.setParent(Logger.getLogger(LoggerNames.GUI.name()));
	}
	private final JTextField logsDirectoryField;
	private final JTextField ripQueueSaveFileField;
	private final JComboBox<String> debugLevelSelector;
	private final JTextField saveDirectoryField;
	private final JSlider threadsSlider;
	private final RipperQueue ripperQueue;

	/**
	 * Create the panel.
	 * @param ripperQueue 
	 */
	public OptionsPane(RipperQueue ripperQueue) {
		this.ripperQueue = ripperQueue;
		logger.info("Building Options Pane.");
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 33, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		logger.finer("Adding processor label.");
		JLabel lblProcessorsToBe = new JLabel("Processors to be used for downloads:");
		GridBagConstraints gbc_lblProcessorsToBe = new GridBagConstraints();
		gbc_lblProcessorsToBe.insets = new Insets(0, 0, 5, 5);
		gbc_lblProcessorsToBe.gridx = 0;
		gbc_lblProcessorsToBe.gridy = 0;
		add(lblProcessorsToBe, gbc_lblProcessorsToBe);

		logger.finer("Adding processor slider.");
		threadsSlider = new JSlider();
		threadsSlider.setValue(Integer.parseInt(InternalConfigurator.getSetting(Setting.MAX_THREADS)));
		threadsSlider.setSnapToTicks(true);
		threadsSlider.setPaintTicks(true);
		threadsSlider.setPaintLabels(true);
		threadsSlider.setMajorTickSpacing(1);
		threadsSlider.setMinimum(1);
		threadsSlider.setMaximum(Runtime.getRuntime().availableProcessors());
		ThreadsSelectorListener threadsSelectorListener = new ThreadsSelectorListener();
		threadsSlider.addChangeListener(threadsSelectorListener);

		GridBagConstraints gbc_threadsSlider = new GridBagConstraints();
		gbc_threadsSlider.fill = GridBagConstraints.HORIZONTAL;
		gbc_threadsSlider.gridwidth = 2;
		gbc_threadsSlider.insets = new Insets(0, 0, 5, 0);
		gbc_threadsSlider.gridx = 1;
		gbc_threadsSlider.gridy = 0;
		add(threadsSlider, gbc_threadsSlider);

		logger.finer("Adding Save directory label.");
		JLabel lblSaveDirectory = new JLabel("Save Directory:");
		GridBagConstraints gbc_lblSaveDirectory = new GridBagConstraints();
		gbc_lblSaveDirectory.anchor = GridBagConstraints.EAST;
		gbc_lblSaveDirectory.insets = new Insets(0, 0, 5, 5);
		gbc_lblSaveDirectory.gridx = 0;
		gbc_lblSaveDirectory.gridy = 1;
		add(lblSaveDirectory, gbc_lblSaveDirectory);

		logger.finer("Adding save directory field.");
		saveDirectoryField = new JTextField(InternalConfigurator.getSetting(Setting.SAVE_DIRECTORY));
		DirectoryListener saveDirectoryListener = new DirectoryListener(saveDirectoryField, Setting.SAVE_DIRECTORY);
		saveDirectoryField.addActionListener(saveDirectoryListener);

		GridBagConstraints gbc_saveDirectoryField = new GridBagConstraints();
		gbc_saveDirectoryField.insets = new Insets(0, 0, 5, 5);
		gbc_saveDirectoryField.fill = GridBagConstraints.HORIZONTAL;
		gbc_saveDirectoryField.gridx = 1;
		gbc_saveDirectoryField.gridy = 1;
		add(saveDirectoryField, gbc_saveDirectoryField);
		saveDirectoryField.setColumns(10);

		logger.finer("Adding save directory button.");
		JButton btnSaveDirectory = new JButton("Browse...");
		btnSaveDirectory.addActionListener(saveDirectoryListener);

		GridBagConstraints gbc_btnSaveDirectory = new GridBagConstraints();
		gbc_btnSaveDirectory.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnSaveDirectory.insets = new Insets(0, 0, 5, 0);
		gbc_btnSaveDirectory.gridx = 2;
		gbc_btnSaveDirectory.gridy = 1;
		add(btnSaveDirectory, gbc_btnSaveDirectory);

		logger.finer("Adding log directory label.");
		JLabel lblLogsDirectory = new JLabel("Logs Directory:");
		GridBagConstraints gbc_lblLogsDirectory = new GridBagConstraints();
		gbc_lblLogsDirectory.insets = new Insets(0, 0, 5, 5);
		gbc_lblLogsDirectory.anchor = GridBagConstraints.EAST;
		gbc_lblLogsDirectory.gridx = 0;
		gbc_lblLogsDirectory.gridy = 2;
		add(lblLogsDirectory, gbc_lblLogsDirectory);

		logger.finer("Adding log directory field.");
		logsDirectoryField = new JTextField(InternalConfigurator.getSetting(Setting.LOGS_DIRECTORY));
		DirectoryListener logsDirectoryListener = new DirectoryListener(logsDirectoryField, Setting.LOGS_DIRECTORY);
		logsDirectoryField.addActionListener(logsDirectoryListener);

		GridBagConstraints gbc_logsDirectoryField = new GridBagConstraints();
		gbc_logsDirectoryField.insets = new Insets(0, 0, 5, 5);
		gbc_logsDirectoryField.fill = GridBagConstraints.HORIZONTAL;
		gbc_logsDirectoryField.gridx = 1;
		gbc_logsDirectoryField.gridy = 2;
		add(logsDirectoryField, gbc_logsDirectoryField);
		logsDirectoryField.setColumns(10);

		logger.finer("Adding logs directory button.");
		JButton btnLogsDirectory = new JButton("Browse...");
		btnLogsDirectory.addActionListener(logsDirectoryListener);

		GridBagConstraints gbc_btnLogsDirectory = new GridBagConstraints();
		gbc_btnLogsDirectory.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnLogsDirectory.insets = new Insets(0, 0, 5, 0);
		gbc_btnLogsDirectory.gridx = 2;
		gbc_btnLogsDirectory.gridy = 2;
		add(btnLogsDirectory, gbc_btnLogsDirectory);

		logger.finer("Adding queue save file label.");
		JLabel lblRipQueueSave = new JLabel("Rip Queue Save File:");
		GridBagConstraints gbc_lblRipQueueSave = new GridBagConstraints();
		gbc_lblRipQueueSave.insets = new Insets(0, 0, 5, 5);
		gbc_lblRipQueueSave.anchor = GridBagConstraints.EAST;
		gbc_lblRipQueueSave.gridx = 0;
		gbc_lblRipQueueSave.gridy = 3;
		add(lblRipQueueSave, gbc_lblRipQueueSave);

		logger.finer("Adding queue save file field.");
		ripQueueSaveFileField = new JTextField(InternalConfigurator.getSetting(Setting.RIP_QUEUE_FILE_NAME));
		SaveFileListener saveFileListener = new SaveFileListener();
		ripQueueSaveFileField.addActionListener(saveFileListener);
		GridBagConstraints gbc_ripQueueSaveFileField = new GridBagConstraints();
		gbc_ripQueueSaveFileField.insets = new Insets(0, 0, 5, 5);
		gbc_ripQueueSaveFileField.fill = GridBagConstraints.HORIZONTAL;
		gbc_ripQueueSaveFileField.gridx = 1;
		gbc_ripQueueSaveFileField.gridy = 3;
		add(ripQueueSaveFileField, gbc_ripQueueSaveFileField);
		ripQueueSaveFileField.setColumns(10);

		logger.finer("Adding model save button.");
		JButton btnModelSave = new JButton("Browse...");
		btnModelSave.addActionListener(saveFileListener);

		GridBagConstraints gbc_btnModelSave = new GridBagConstraints();
		gbc_btnModelSave.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnModelSave.insets = new Insets(0, 0, 5, 0);
		gbc_btnModelSave.gridx = 2;
		gbc_btnModelSave.gridy = 3;
		add(btnModelSave, gbc_btnModelSave);

		logger.finer("Adding debug label.");
		JLabel lblDebugLevel = new JLabel("Debug Level:");
		GridBagConstraints gbc_lblDebugLevel = new GridBagConstraints();
		gbc_lblDebugLevel.anchor = GridBagConstraints.EAST;
		gbc_lblDebugLevel.insets = new Insets(0, 0, 0, 5);
		gbc_lblDebugLevel.gridx = 0;
		gbc_lblDebugLevel.gridy = 4;
		add(lblDebugLevel, gbc_lblDebugLevel);

		logger.finer("Adding debug level selector.");
		Field[] fields = Level.class.getFields();
		String[] names = new String[fields.length];
		for (int i = 0; i < fields.length; i++) {
			names[i] = fields[i].getName();
		}
		debugLevelSelector = new JComboBox<>();
		debugLevelSelector.setModel(new DefaultComboBoxModel<>(names));
		String selectedLevel = InternalConfigurator.getSetting(Setting.DEBUG_LEVEL);
		DebugLevelListener debugLevelListener = new DebugLevelListener();
		debugLevelSelector.setSelectedItem(selectedLevel);
		debugLevelSelector.addActionListener(debugLevelListener);

		GridBagConstraints gbc_debugLevelSelector = new GridBagConstraints();
		gbc_debugLevelSelector.gridwidth = 2;
		gbc_debugLevelSelector.insets = new Insets(0, 0, 0, 5);
		gbc_debugLevelSelector.fill = GridBagConstraints.HORIZONTAL;
		gbc_debugLevelSelector.gridx = 1;
		gbc_debugLevelSelector.gridy = 4;
		add(debugLevelSelector, gbc_debugLevelSelector);

	}
	
	private abstract class OptionsListener {

		private final Logger logger;
		{
			logger = Logger.getLogger(LoggerNames.OPTIONS_LISTENER.name());
			logger.setParent(Logger.getLogger(LoggerNames.OPTIONS_PANE.name()));
		}

	}
	
	private class DebugLevelListener extends OptionsListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			Object selected = debugLevelSelector.getModel().getSelectedItem();
			if (selected instanceof String) {
				InternalConfigurator.changeSetting(Setting.DEBUG_LEVEL, (String) selected);
				Logger.getGlobal().setLevel(Level.parse((String) selected));
			}
		}

	}
	
	private class DirectoryListener extends OptionsListener implements ActionListener {

		private final JTextField directoryField;
		private final Setting settingToModify;

		private DirectoryListener(JTextField directoryField, Setting setting) {
			this.directoryField = directoryField;
			this.settingToModify = setting;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() instanceof JTextField) {
				InternalConfigurator.changeSetting(settingToModify, directoryField.getText());
			}
			if (e.getSource() instanceof JButton) {
				JFileChooser fileChooser = new JFileChooser(InternalConfigurator.getSetting(settingToModify));
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = fileChooser.showDialog((JButton) e.getSource(), "Select");
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File selectedLocation = fileChooser.getSelectedFile();
					try {
						directoryField.setText(selectedLocation.getCanonicalPath());
						InternalConfigurator.changeSetting(settingToModify, selectedLocation.getAbsolutePath());
					} catch (IOException e1) {
						logger.log(Level.WARNING, "Unable to set directory to new location.", e1);
					}
				}
			}
		}

	}

	public class SaveFileListener extends OptionsListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() instanceof JTextField) {
				InternalConfigurator.changeSetting(Setting.RIP_QUEUE_FILE_NAME, ripQueueSaveFileField.getText());
			} else if (e.getSource() instanceof JButton) {
				JFileChooser fileChooser = new JFileChooser(InternalConfigurator.getSetting(Setting.RIP_QUEUE_FILE_NAME));
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int returnVal = fileChooser.showDialog((JButton) e.getSource(), "Select");
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File selectedLocation = fileChooser.getSelectedFile();
					try {
						ripQueueSaveFileField.setText(selectedLocation.getCanonicalPath());
						InternalConfigurator.changeSetting(Setting.RIP_QUEUE_FILE_NAME, selectedLocation.getAbsolutePath());
					} catch (IOException e1) {
						logger.log(Level.WARNING, "Unable to set save file to new location.", e1);
					}
					
					ripperQueue.setTableModel(RipperQueue.loadModel(selectedLocation.toPath()));
				}
			}
		}

	}
	
	private class ThreadsSelectorListener extends OptionsListener implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent e) {
			if (e.getSource() instanceof JSlider) {
				InternalConfigurator.changeSetting(Setting.MAX_THREADS, Integer.toString(threadsSlider.getValue()));
			}
		}

	}
	
}
