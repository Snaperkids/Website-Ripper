/*
 *
 */
package com.snaperkids.ripper.ui.components;

import java.awt.BorderLayout;
import java.util.logging.Handler;
import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import com.snaperkids.ripper.ui.logging.GUIOutputHandler;
import com.snaperkids.ripper.utils.LoggerNames;

public class LoggerPane extends JPanel {

	private static final Logger logger;
	/**
	 *
	 */
	private static final long serialVersionUID = 6309497757932248865L;

	static {
		logger = Logger.getLogger(LoggerNames.LOGGER_PANE.name());
		logger.setParent(Logger.getLogger(LoggerNames.GUI.name()));
	}

	/**
	 * Create the panel.
	 */
	public LoggerPane() {
		GUIOutputHandler handler = null;
		for (Handler h : Logger.getGlobal().getHandlers()) {
			if (h instanceof GUIOutputHandler) {
				handler = (GUIOutputHandler) h;
			}
		}
		if (handler == null) {
			throw new IllegalStateException("Output Handler does not exist.");
		}

		logger.info("Building Logger Pane");
		setLayout(new BorderLayout(0, 0));
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setAutoscrolls(true);
		add(scrollPane);
		logger.info("Building Logger Text Area.");
		JTextPane textPane = handler.getTextPane();
		logger.info("Adding Logger text area.");
		scrollPane.setViewportView(textPane);
		textPane.setEditable(false);
		textPane.addCaretListener(new CaretListener() {

			@Override
			public void caretUpdate(CaretEvent e) {
				JScrollBar vertical = scrollPane.getVerticalScrollBar();
				vertical.setValue(vertical.getMaximum());
			}
		});

	}

}
