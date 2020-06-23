/*
 *
 */
package com.snaperkids.ripper.ui.components.logic;

import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.swing.table.AbstractTableModel;

import com.snaperkids.ripper.utils.LoggerNames;

// TODO: Write Javadocs
/**
 * The Class RipperQueueTable.
 */
public class RipperQueueTable extends AbstractTableModel {

	/** The Constant logger. */
	private static final transient Logger logger;
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7615795787533393857L;

	static {
		logger = Logger.getLogger(LoggerNames.RIPPER_HISTORY_TABLE.name());
		logger.setParent(Logger.getLogger(LoggerNames.GUI.name()));
	}

	/** The col names. */
	private final String[] colNames;
	
	/** The col types. */
	private final Class<?>[] colTypes;
	
	/** The rip list. */
	private final ArrayList<Download> ripList;

	/**
	 * Instantiates a new ripper queue table.
	 *
	 * @param colNames the col names
	 * @param colTypes the col types
	 * @param i the i
	 */
	public RipperQueueTable(String[] colNames, Class<?>[] colTypes, int i) {
		logger.info("Buiding Ripper Table");
		this.colNames = colNames;
		this.colTypes = colTypes;
		ripList = new ArrayList<>();
	}

	/**
	 * Adds the row.
	 *
	 * @param newEntry the new entry
	 */
	public void addRow(Download newEntry) {
		int oldSize = ripList.size();
		int pos = ripList.indexOf(newEntry);
		if (pos > -1) {
			logger.finer("Updating " + newEntry);
			ripList.remove(pos);
		} else {
			logger.finer("Adding " + newEntry);
			pos = ripList.size();
		}
		addRow(pos, newEntry);
		if (oldSize == ripList.size()) {
			fireTableRowsUpdated(pos, pos);
		} else {
			fireTableRowsInserted(pos, pos);
		}
	}

	/**
	 * Adds the row.
	 *
	 * @param pos the pos
	 * @param newEntry the new entry
	 */
	public void addRow(int pos, Download newEntry) {
		int oldSize = ripList.size();
		if (ripList.contains(newEntry)) {
			logger.finer("Removing " + newEntry);
			ripList.remove(newEntry);
		}
		logger.finer("Adding " + newEntry);
		ripList.add(pos, newEntry);
		if (oldSize == ripList.size()) {
			fireTableRowsUpdated(pos, pos);
		} else {
			fireTableRowsInserted(pos, pos);
		}
	}

	/**
	 * Gets the column class.
	 *
	 * @param columnIndex the column index
	 * @return the column class
	 */
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return colTypes[columnIndex];
	}

	/**
	 * Gets the column count.
	 *
	 * @return the column count
	 */
	@Override
	public int getColumnCount() {
		return colNames.length;
	}

	/**
	 * Gets the column name.
	 *
	 * @param column the column
	 * @return the column name
	 */
	@Override
	public String getColumnName(int column) {
		return colNames[column];
	}

	/**
	 * Gets the row count.
	 *
	 * @return the row count
	 */
	@Override
	public int getRowCount() {
		return ripList.size();
	}

	/**
	 * Gets the table entries.
	 *
	 * @return the table entries
	 */
	public ArrayList<Download> getTableEntries() {
		return ripList;
	}

	/**
	 * Gets the value at.
	 *
	 * @param rowIndex the row index
	 * @param columnIndex the column index
	 * @return the value at
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Download download = ripList.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return download.getURL();
		case 1:
			return download.getUsername();
		case 2:
			return download.getPassword();
		case 3:
			return download.getShouldDownload();
		}
		return "";
	}

	/**
	 * Checks if is cell editable.
	 *
	 * @param row the row
	 * @param col the col
	 * @return true, if is cell editable
	 */
	@Override
	public boolean isCellEditable(int row, int col) {
		return getColumnClass(col).equals(Boolean.class);
	}

	/**
	 * Removes the row.
	 *
	 * @param row the row
	 * @return the download
	 */
	public Download removeRow(int row) {
		return ripList.remove(row);
	}

	/**
	 * Removes the row by URL.
	 *
	 * @param url the url
	 * @return true, if successful
	 */
	public boolean removeRowByURL(URL url) {
		Download download = null;
		for (Download d : ripList) {
			if (d.getURL().equals(url)) {
				download = d;
			}
		}
		if (download == null) {
			return false;
		}
		return ripList.remove(download);
	}

	/**
	 * Sets the value at.
	 *
	 * @param newData the new data
	 * @param rowIndex the row index
	 * @param columnIndex the column index
	 */
	@Override
	public void setValueAt(Object newData, int rowIndex, int columnIndex) {
		Download download = ripList.get(rowIndex);
		switch (columnIndex) {
		case 0:
			download.changeURL((URL) newData);
		case 1:
			download.changeUsername((String) newData);
		case 2:
			download.changePassword((char[]) newData);
		case 3:
			download.changeShouldRip((Boolean) newData);
		}
	}

}
