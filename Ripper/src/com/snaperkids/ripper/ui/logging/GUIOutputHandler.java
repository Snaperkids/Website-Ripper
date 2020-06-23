/*
 *
 */
package com.snaperkids.ripper.ui.logging;

import java.awt.Color;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.snaperkids.ripper.utils.LoggerNames;

// TODO: Write Javadocs
/**
 * The Class GUIOutputHandler.
 */
public class GUIOutputHandler extends Handler {

	/** The Constant logger. */
	private static final Logger logger;
	static {
		logger = Logger.getLogger(LoggerNames.GUI_OUTPUT_HANDLER.name());
		logger.setParent(Logger.getLogger(LoggerNames.GUI.name()));
	}

	/** The text pane. */
	JTextPane textPane;

	/**
	 * Instantiates a new GUI output handler.
	 */
	public GUIOutputHandler() {
		textPane = new JTextPane();

		textPane.setBackground(Color.LIGHT_GRAY);

		Style style = textPane.addStyle(Level.INFO.getLocalizedName(), null);
		StyleConstants.setForeground(style, Color.BLUE);

		style = textPane.addStyle(Level.WARNING.getLocalizedName(), null);
		StyleConstants.setForeground(style, Color.YELLOW);

		style = textPane.addStyle(Level.SEVERE.getLocalizedName(), null);
		StyleConstants.setForeground(style, Color.RED);

		style = textPane.addStyle(Level.CONFIG.getLocalizedName(), null);
		StyleConstants.setForeground(style, Color.BLACK);

		style = textPane.addStyle(Level.FINE.getLocalizedName(), null);
		StyleConstants.setForeground(style, Color.MAGENTA);
	}

	/**
	 * Close.
	 *
	 * @throws SecurityException the security exception
	 */
	@Override
	public void close() throws SecurityException {

	}

	/**
	 * Flush.
	 */
	@Override
	public void flush() {

	}

	/**
	 * Gets the text pane.
	 *
	 * @return the text pane
	 */
	public JTextPane getTextPane() {
		return textPane;
	}

	/**
	 * Publish.
	 *
	 * @param record the record
	 */
	@Override
	public void publish(LogRecord record) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				StyledDocument doc = textPane.getStyledDocument();
				StringWriter strOut = new StringWriter();
				PrintWriter printOut = new PrintWriter(strOut);
				printOut.printf("[%s] [Thread-%d]: %s.%s -> %s\n", record.getLevel(), record.getThreadID(),
						record.getSourceClassName(), record.getSourceMethodName(), record.getMessage());
				String text = strOut.toString();
				Style style = textPane.getStyle(record.getLevel().getLocalizedName());
				try {
					doc.insertString(doc.getLength(), text, style);
				} catch (BadLocationException e) {

				}
			}

		});
	}

}
