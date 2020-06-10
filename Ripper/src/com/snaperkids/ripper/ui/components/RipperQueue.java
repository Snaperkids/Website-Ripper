/*
 *
 */
package com.snaperkids.ripper.ui.components;

import java.awt.BorderLayout;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import com.snaperkids.ripper.config.InternalConfigurator;
import com.snaperkids.ripper.config.Setting;
import com.snaperkids.ripper.ui.components.logic.RipperQueueTable;
import com.snaperkids.ripper.utils.LoggerNames;

public class RipperQueue extends JPanel {
	private static final String[] colNames = { "URL", "Username", "Password", "Rip" };
	private static final Class<?>[] colTypes = { URL.class, String.class, String.class, Boolean.class };
	private transient static final Logger logger;
	/**
	 *
	 */
	private static final long serialVersionUID = 889060989407762535L;

	static {
		logger = Logger.getLogger(LoggerNames.RIPPER_QUEUE.name());
		logger.setParent(Logger.getLogger(LoggerNames.GUI.name()));
	}

	private final RipperQueueTable ripQueueTable;
	private volatile JTable table;

	/**
	 * Create the panel.
	 */
	public RipperQueue() {
		logger.info("Buiding Ripper Queue");
		setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane);

		table = new JTable();
		scrollPane.setViewportView(table);

		ripQueueTable = loadModel(Path.of(InternalConfigurator.getSetting(Setting.RIP_QUEUE_FILE_NAME)));

		table.setModel(ripQueueTable);

	}

	public RipperQueueTable getQueueTable() {
		return ripQueueTable;
	}
	
	synchronized void setTableModel(TableModel model) {
		table.setModel(model);
	}

	public static RipperQueueTable loadModel(Path file) {
		logger.info("Loading Ripper History.");
		RipperQueueTable tableModel;
		try (ObjectInputStream stream = new ObjectInputStream(new FileInputStream(file.toFile()))) {
			Object input = stream.readObject();
			if (input instanceof RipperQueueTable) {
				tableModel = (RipperQueueTable) input;
			} else {
				tableModel = new RipperQueueTable(colNames, colTypes, 0);
			}
		} catch (FileNotFoundException e) {
			tableModel = new RipperQueueTable(colNames, colTypes, 0);
			logger.log(Level.WARNING, "No Rip History Found.", e);
		} catch (IOException e) {
			tableModel = new RipperQueueTable(colNames, colTypes, 0);
			logger.log(Level.WARNING, "Rip History Could Not Be Loaded.", e);
		} catch (ClassNotFoundException e) {
			tableModel = new RipperQueueTable(colNames, colTypes, 0);
			logger.log(Level.WARNING, "History File Corrupted.", e);
		}
		return tableModel;
	}

	public void saveModel() {
		logger.info("Saving Ripper History.");
		try (ObjectOutputStream stream = new ObjectOutputStream(
				new FileOutputStream(InternalConfigurator.getSetting(Setting.RIP_QUEUE_FILE_NAME)))) {
			stream.writeObject(ripQueueTable);
		} catch (IOException e) {
			logger.log(Level.WARNING, "Rip History could not be saved.", e);
		}
	}

}
