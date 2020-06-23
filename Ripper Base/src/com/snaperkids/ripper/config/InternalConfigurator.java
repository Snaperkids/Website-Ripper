/*
 *
 */
package com.snaperkids.ripper.config;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.snaperkids.ripper.utils.ExitCodes;
import com.snaperkids.ripper.utils.LoggerNames;

// TODO: Write Javadocs
/**
 * The Class InternalConfigurator.
 */
public final class InternalConfigurator {

	/** The Constant logger. */
	private static final Logger logger;

	/** The Constant RIPPER_SETTINGS. */
	private static final Properties RIPPER_SETTINGS;

	/** The Constant RIPPER_SETTINGS_FILE. */
	private static final Path RIPPER_SETTINGS_FILE = Paths.get("settings.cfg");

	static {
		Properties defaults = new Properties();
		try (InputStream defaultFile = Setting.class.getResourceAsStream("defaults.cfg")) {
			defaults.loadFromXML(defaultFile);
		} catch (InvalidPropertiesFormatException e) {
			e.printStackTrace();
			System.exit(ExitCodes.INVALID_SETTINGS_FILE_FORMAT.ordinal());
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(ExitCodes.UNABLE_TO_LOAD_SETTINGS_FILE.ordinal());
		}
		Properties loadedSettings = new Properties();
		try (InputStream settingsFile = new FileInputStream(RIPPER_SETTINGS_FILE.toFile())) {
			loadedSettings.loadFromXML(settingsFile);
		} catch (IOException e) {
			loadedSettings = defaults;
		} finally {
			RIPPER_SETTINGS = loadedSettings;
		}
		logger = Logger.getLogger(LoggerNames.CONFIGURATION.name());
		logger.setLevel(Level.parse(RIPPER_SETTINGS.getProperty(Setting.DEBUG_LEVEL.name())));
		logger.setParent(Logger.getGlobal());
		logger.info("Ripper settings loaded.");
	}

	/**
	 * Change setting.
	 *
	 * @param setting  the setting
	 * @param newValue the new value
	 * @return the object
	 */
	public static synchronized Object changeSetting(Setting setting, String newValue) {
		// if(getCallerClass(Thread.currentThread().getStackTrace()) instanceof Scraper)
		// {
		// return null;
		// }
		return RIPPER_SETTINGS.setProperty(setting.name(), newValue);
	}

	// private static Object getCallerClass(StackTraceElement[] stackTrace) {
	// for (int i=1; i<stackTrace.length; i++) {
	// StackTraceElement ste = stackTrace[i];
	// if (!ste.getClassName().equals(InternalConfigurator.class.getName()) &&
	// ste.getClassName().indexOf("java.lang.Thread")!=0) {
	// return ste.getClassName();
	// }
	// }
	// return null;
	// }

	/**
	 * Confirm file structure.
	 */
	public static void confirmFileStructure() {
		logger.info("Confirming file structure.");
		Path saveDirectory = Path.of(InternalConfigurator.getSetting(Setting.SAVE_DIRECTORY));
		if (!saveDirectory.toFile().exists()) {
			saveDirectory.toFile().mkdirs();
		}
		Path logsDirectory = Path.of(InternalConfigurator.getSetting(Setting.LOGS_DIRECTORY));
		if (!logsDirectory.toFile().exists()) {
			logsDirectory.toFile().mkdirs();
		}
		Path binDirectory = Path.of(InternalConfigurator.getSetting(Setting.SCRAPER_DIRECTORY));
		if (!binDirectory.toFile().exists()) {
			binDirectory.toFile().mkdirs();
		}
	}

	/**
	 * Gets the setting.
	 *
	 * @param setting the setting
	 * @return the setting
	 */
	public static String getSetting(Setting setting) {
		return RIPPER_SETTINGS.getProperty(setting.name());
	}

	/**
	 * Save ripper settings.
	 */
	public static void saveRipperSettings() {
		logger.info("Saving ripper settings.");
		try (OutputStream fileStream = new FileOutputStream(RIPPER_SETTINGS_FILE.toFile())) {
			RIPPER_SETTINGS.storeToXML(fileStream, "These are the settings for the ripper.");
		} catch (IOException e) {
			logger.log(Level.WARNING, "Could not save ripper settings.", e);
		}
	}

}
