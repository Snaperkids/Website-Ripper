/*
 *
 */
package com.snaperkids.ripper.ui.components.logic;

import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.swing.table.AbstractTableModel;

import com.snaperkids.ripper.utils.LoggerNames;

public class RipperQueueTable extends AbstractTableModel {

	private static final transient Logger logger;
	/**
	 *
	 */
	private static final long serialVersionUID = -7615795787533393857L;

	static {
		logger = Logger.getLogger(LoggerNames.RIPPER_HISTORY_TABLE.name());
		logger.setParent(Logger.getLogger(LoggerNames.GUI.name()));
	}

	private final String[] colNames;
	private final Class<?>[] colTypes;
	private final ArrayList<Download> ripList;

	public RipperQueueTable(String[] colNames, Class<?>[] colTypes, int i) {
		logger.info("Buiding Ripper Table");
		this.colNames = colNames;
		this.colTypes = colTypes;
		ripList = new ArrayList<>();
	}

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

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return colTypes[columnIndex];
	}

	@Override
	public int getColumnCount() {
		return colNames.length;
	}

	@Override
	public String getColumnName(int column) {
		return colNames[column];
	}

	@Override
	public int getRowCount() {
		return ripList.size();
	}

	public ArrayList<Download> getTableEntries() {
		return ripList;
	}

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

	@Override
	public boolean isCellEditable(int row, int col) {
		return getColumnClass(col).equals(Boolean.class);
	}

	public Download removeRow(int row) {
		return ripList.remove(row);
	}

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
