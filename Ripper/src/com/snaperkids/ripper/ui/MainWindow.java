/*
 *
 */
package com.snaperkids.ripper.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import com.snaperkids.ripper.config.InternalConfigurator;
import com.snaperkids.ripper.config.Setting;
import com.snaperkids.ripper.ui.components.LoggerPane;
import com.snaperkids.ripper.ui.components.OptionsPane;
import com.snaperkids.ripper.ui.components.RipperQueue;
import com.snaperkids.ripper.ui.components.UserEntryPane;
import com.snaperkids.ripper.ui.logging.GUIOutputHandler;
import com.snaperkids.ripper.utils.ExitCodes;
import com.snaperkids.ripper.utils.LoggerNames;

// TODO: Write Javadocs
/**
 * The Class MainWindow.
 */
public class MainWindow {

	/** The Constant globalLogger. */
	private static final Logger globalLogger;

	/** The Constant logger. */
	private static final Logger logger;
	static {
		InternalConfigurator.confirmFileStructure();
		Level level = Level.parse(InternalConfigurator.getSetting(Setting.DEBUG_LEVEL));
		globalLogger = Logger.getGlobal();
		globalLogger.setLevel(level);
		globalLogger.addHandler(new GUIOutputHandler());
		logger = Logger.getLogger(LoggerNames.GUI.name());
		logger.setParent(globalLogger);
		logger.setLevel(level);
		FileHandler handler = null, globalLog = null;
		try {
			globalLog = new FileHandler(
					InternalConfigurator.getSetting(Setting.LOGS_DIRECTORY) + File.separatorChar + "ripper.log");
			handler = new FileHandler(
					InternalConfigurator.getSetting(Setting.LOGS_DIRECTORY) + File.separatorChar + "gui.log");
		} catch (SecurityException | IOException e1) {
			logger.log(Level.SEVERE, "Failed to create log files.", e1);
			System.exit(ExitCodes.COULD_NOT_MAKE_LOGS.ordinal());
		}
		globalLogger.addHandler(globalLog);
		logger.addHandler(handler);
		logger.log(Level.INFO, "Main Loggers Built");
	}

	/**
	 * Launch the application.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					logger.log(Level.SEVERE, "Failed to create gui.", e);
					System.exit(ExitCodes.FAILED_TO_MAKE_GUI.ordinal());
				}
			}
		});
	}

	/** The frame. */
	private JFrame frame;

	/** The user entry pane. */
	private final UserEntryPane userEntryPane = new UserEntryPane();

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		logger.log(Level.INFO, "Building Main Window.");
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.TOP);
		frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);

		JPanel loggingPage = new JPanel();
		tabbedPane.addTab("Logging", null, loggingPage, null);
		loggingPage.setLayout(new BorderLayout(0, 0));

		LoggerPane loggerPane = new LoggerPane();
		loggingPage.add(loggerPane);

		JPanel ripperControlsPage = new JPanel();
		tabbedPane.addTab("Ripper Controls", null, ripperControlsPage, null);
		ripperControlsPage.setLayout(new BorderLayout(0, 0));
		ripperControlsPage.add(userEntryPane, BorderLayout.SOUTH);

		RipperQueue ripperQueue = new RipperQueue();
		ripperControlsPage.add(ripperQueue, BorderLayout.CENTER);

		OptionsPane optionsPage = new OptionsPane(ripperQueue);
		tabbedPane.addTab("Options", null, optionsPage, null);
		frame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				logger.info("Window closing.");
				ripperQueue.saveModel();
				InternalConfigurator.saveRipperSettings();
			}

		});
	}

}
