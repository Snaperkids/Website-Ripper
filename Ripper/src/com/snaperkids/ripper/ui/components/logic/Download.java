/*
 *
 */
package com.snaperkids.ripper.ui.components.logic;

import java.io.Serializable;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.snaperkids.ripper.utils.LoggerNames;

public class Download implements Serializable {

	private static final Logger logger;
	/**
	 *
	 */
	private static final long serialVersionUID = 692628058598071345L;

	static {
		logger = Logger.getLogger(LoggerNames.DOWNLOAD.name());
		logger.setParent(Logger.getLogger(LoggerNames.GUI.name()));
	}

	private URL downloadURL;
	private char[] password;
	private Boolean shouldRip;

	private String username;

	public Download(URL downloadURL, String username, char[] password, Boolean shouldRip) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.finest(String.format(
					"Download Record made with:\n\tURL: %s\n\tUsername: %s\n\tPassword: %s\n\tRip Status: %s",
					downloadURL.getPath(), username, new String(password), shouldRip));
		} else {
			logger.finer("Creating Download Record.");
		}
		this.downloadURL = downloadURL;
		this.username = username;
		this.password = password;
		this.shouldRip = shouldRip;
	}

	void changePassword(char[] newPassword) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.finest(String.format("Changing Download Record password From %s to %s", new String(password),
					new String(newPassword)));
		} else {
			logger.finer("Changing Password for Download Record.");
		}
		password = newPassword;
	}

	void changeShouldRip(Boolean newShouldRip) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.finest(String.format("Changing Download Record URL From %s to %s", shouldRip, newShouldRip));
		} else {
			logger.finer("Changing Rip Status for Download Record.");
		}
		shouldRip = newShouldRip;
	}

	void changeURL(URL newURL) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.finest(String.format("Changing Download Record URL From %s to %s", downloadURL.getPath(),
					newURL.getPath()));
		} else {
			logger.finer("Changing URL for Download Record.");
		}
		downloadURL = newURL;
	}

	void changeUsername(String newUsername) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.finest(String.format("Changing Download Record Username From %s to %s", username, newUsername));
		} else {
			logger.finer("Changing Username for Download Record.");
		}
		username = newUsername;
	}

	@Override
	public boolean equals(Object obj) {
		logger.finer("Checking if Download Records are equivalent");
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Download)) {
			return false;
		}
		Download other = (Download) obj;
		return getURL().sameFile(other.getURL());
	}

	public char[] getPassword() {
		if (logger.isLoggable(Level.FINEST)) {
			logger.finest(String.format("Returning Download Record Password: %s", new String(password)));
		} else {
			logger.finer("Returning Password for Download Record.");
		}
		return password;
	}

	public Boolean getShouldDownload() {
		if (logger.isLoggable(Level.FINEST)) {
			logger.finest(String.format("Returning Download Record Rip Status: %s", shouldRip));
		} else {
			logger.finer("Returning DownloadStatus for Download Record.");
		}
		return shouldRip;
	}

	public URL getURL() {
		if (logger.isLoggable(Level.FINEST)) {
			logger.finest(String.format("Returning Download Record URL: %s", downloadURL.getPath()));
		} else {
			logger.finer("Returning URL for Download Record.");
		}
		return downloadURL;
	}

	public String getUsername() {
		if (logger.isLoggable(Level.FINEST)) {
			logger.finest(String.format("Returning Download Record Username: %s", username));
		} else {
			logger.finer("Returning Username for Download Record.");
		}
		return username;
	}

}
