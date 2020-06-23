/**
 *
 */
package com.snaperkids.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.logging.FileHandler;
import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import com.snaperkids.services.utils.AuthenticationMethod;

/**
 * The <code>Downloader</code> class is the abstract base class for all file
 * downloaders which download files. The <code>Downloader</code> object
 * encapsulates the state information for the given download. This class
 * provides methods that provide file downloading and event logging
 * capabilities.
 * 
 * @author Snaperkids
 * @version 1.3, 23 June 2020
 * @since 0.2
 */
public abstract class Downloader implements Runnable {

	private static boolean fileHandlerAdded = false;

	/**
	 * The file handler for event logging. A single file handler is used to log all
	 * events from all instances of the same implementation of this class.
	 */
	protected static FileHandler fileHandler = null;

	/** The link to the file this <code>Downloader</code> object will download. */
	protected final URL downloadLink;

	/** The path of the file this object will save the file to. */
	protected final Path filePath;

	/** The metadata of the file this object will download. */
	protected final Map<String, String> metadata;

	/**
	 * The <code>{@link Logger}</code> object used for event logging the events of
	 * this object.
	 */
	protected final Logger logger;

	/**
	 * The <code>{@link ExecutorService}</code> object that this object has been
	 * submitted to for execution.
	 */
	protected ExecutorService executor;

	/** The title of the file this object will download. */
	protected String title;

	/** The file extension of the file this object will download. */
	protected String fileExtension;

	private boolean hasFailedBefore;

	{
		logger = Logger.getLogger(this.getClass().getSimpleName());
		logger.setParent(Logger.getGlobal());
		logger.setLevel(Level.ALL);
	}

	/**
	 * Constructs a new <code>Downloader</code> object to download the specified
	 * file from the given link.
	 *
	 * @param url      - the url of the file this object will download
	 * @param title    - the title of the file this object will download
	 * @param metadata - the metadata associated with the file to be downloaded
	 * @param executor - the executor this object was submitted to for execution
	 * @throws IOException - if an error occurs while creating the file handler for
	 *                     event logging
	 */
	protected Downloader(URL url, String title, Map<String, String> metadata, ExecutorService executor)
			throws IOException {
		this(url, title, 0, 0, metadata, executor);
	}

	/**
	 * Constructs a new <code>Downloader</code> object to download the specified
	 * file from the given link. A file number and number of padding characters can
	 * be specified to insure that sequential file sets are kept in order.
	 *
	 * @param url        - the url of the file this object will download
	 * @param title      - the title of the file this object will download
	 * @param fileNum    - the number corresponding to this files position in a set
	 *                   of files
	 * @param numPadding - the number characters needed to pad the file to insure
	 *                   proper file indexing by the operating system's file
	 *                   explorer
	 * @param metadata   - the metadata associated with the file to be downloaded
	 * @param executor   - the executor this object was submitted to for execution
	 * @throws IOException - if an error occurs while creating the file handler for
	 *                     event logging
	 */
	protected Downloader(URL url, String title, int fileNum, int numPadding, Map<String, String> metadata,
			ExecutorService executor) throws IOException {
		fileHandler = Downloader.createFileHandler(this.getClass().getSimpleName());
		if (!fileHandlerAdded) {
			logger.addHandler(fileHandler);
			fileHandlerAdded = true;
		}

		logger.finer("Initializing downloader.");
		hasFailedBefore = false;
		downloadLink = url;
		this.metadata = metadata;
		this.title = title;
		filePath = getFilePath(title, fileNum, numPadding);
		this.executor = executor;
	}

	private static synchronized FileHandler createFileHandler(String className) throws SecurityException, IOException {
		if (fileHandler != null) {
			return fileHandler;
		}

		File logFile = new File(className + ".log");
		if (logFile.exists()) {
			logFile.delete();
		}
		FileHandler newFileHandler = new FileHandler(logFile.getPath(), true);
		newFileHandler.setFilter(new Filter() {

			@Override
			public boolean isLoggable(LogRecord record) {
				return record.getThreadID() == Thread.currentThread().getId();
			}

		});
		return newFileHandler;
	}

	/**
	 * Downloads the file from the provided url to the specified file.
	 */
	protected void downloadFile() {
		try (ReadableByteChannel downloadChannel = Channels.newChannel(openDownloadStream());
				FileOutputStream stream = new FileOutputStream(filePath.toFile());) {
			stream.getChannel().transferFrom(downloadChannel, 0, Long.MAX_VALUE);
		} catch (final FileNotFoundException e) {
			logger.log(Level.WARNING, "Could not find: " + filePath.getFileName(), e);
		} catch (SocketException e) {
			if (!hasFailedBefore) {
				hasFailedBefore = true;
				executor.submit(this);
			} else {
				logger.log(Level.WARNING, "SocketException while downloading: " + filePath.getFileName(), e);
			}
		} catch (final IOException e) {
			logger.log(Level.WARNING, "Exception downloading: " + filePath.getFileName(), e);
		}
	}

	private void downloadFile(InputStream downloadStream) {
		try (ReadableByteChannel downloadChannel = Channels.newChannel(downloadStream);
				FileOutputStream stream = new FileOutputStream(filePath.toFile());) {
			stream.getChannel().transferFrom(downloadChannel, 0, Long.MAX_VALUE);
		} catch (final FileNotFoundException e) {
			logger.log(Level.WARNING, "Could not find: " + filePath.getFileName(), e);
		} catch (SocketException e) {
			if (!hasFailedBefore) {
				hasFailedBefore = true;
				executor.submit(this);
			} else {
				logger.log(Level.WARNING, "SocketException while downloading: " + filePath.getFileName(), e);
			}
		} catch (final IOException e) {
			logger.log(Level.WARNING, "Exception downloading: " + filePath.getFileName(), e);
		}
	}

	/**
	 * Downloads the file from the provided url to the specified file using the
	 * login credentials provided to access the website.
	 *
	 * NOTE: Currently there is no active support for OAuth2 authentication despite
	 * OAuth2 being specified in the {@link AuthenticationMethod} class. This is a
	 * work in progress. Any attempt to use OAuth2 authentication will result in an
	 * error due to the InputStream being a null pointer.
	 *
	 * @param username   - the username to use to login to the website
	 * @param password   - the password to use to login to the website
	 * @param authMethod - the method through which authentication is to be
	 *                   performed
	 */
	protected void downloadFile(String username, char[] password, AuthenticationMethod authMethod) {
		try {
			InputStream downloadStream;
			switch (authMethod) {
				case BASIC:
					downloadStream = openBasicAuthDownloadStream(username, password);
					break;
				case OAUTH2:
					downloadStream = openOAuth2DownloadStream(username, password);
					break;
				default:
					downloadStream = openDownloadStream();
			}
			downloadFile(downloadStream);
		} catch (IOException e) {
			logger.log(Level.WARNING, "Exception downloading: " + filePath.getFileName(), e);
		}
	}

	/**
	 * Opens an {@link InputStream} using OAuth2 authentication.
	 * 
	 * @param username - the username to use for authentication.
	 * @param password - the password to use for authentication.
	 * @return an <code>InputStream</code> object connected to the url of the file
	 *         to be downloaded
	 * @throws IOException - if an I/O error occurs while opening the input stream
	 */
	private InputStream openOAuth2DownloadStream(String username, char[] password) throws IOException {
		return null;
	}

	/**
	 * Opens an {@link InputStream} using Basic authentication.
	 * 
	 * @param username - the username to use for authentication.
	 * @param password - the password to use for authentication.
	 * @return an <code>InputStream</code> object connected to the url of the file
	 *         to be downloaded
	 * @throws IOException - if an I/O error occurs while opening the input stream
	 */
	private InputStream openBasicAuthDownloadStream(String username, char[] password) throws IOException {
		final String authString = username + ":" + new String(password);
		final byte[] encAuth = Base64.getEncoder().encode(authString.getBytes());
		final String encAuthString = new String(encAuth);
		String authHeader = "Basic " + encAuthString;

		final URLConnection con = downloadLink.openConnection();
		con.setRequestProperty("Authorization", authHeader);
		return con.getInputStream();
	}

	/**
	 * Gets the path of the file the downloaded file will be saved to.
	 *
	 * @param title      - the title of the file to be downloaded
	 * @param fileNum    - the number of the file corresponding to its position in a
	 *                   sequence of files
	 * @param numPadding - the number of padding elements needed to insure the file
	 *                   is properly positioned in sequence
	 * @return the path of the file that the downloaded file will be saved to
	 * @throws IOException - if an I/O error occurs while generating the file path
	 */
	protected abstract Path getFilePath(String title, int fileNum, int numPadding) throws IOException;

	/**
	 * Opens an {@link InputStream} using Basic authentication.
	 * 
	 * @return an <code>InputStream</code> object connected to the url of the file
	 *         to be downloaded
	 * @throws IOException - if an I/O error occurs while opening the input stream
	 */
	protected InputStream openDownloadStream() throws IOException {
		final URLConnection con = downloadLink.openConnection();
		return con.getInputStream();
	}

	/**
	 * Runs this <code>Downloader</code> object to download the file from the
	 * specified url to the given file location and write the file's metadata.
	 */
	@Override
	public void run() {
		logger.fine("Downloading " + filePath.getFileName());
		downloadFile();
		logger.fine("Finished downloading " + filePath.getFileName());

		logger.fine("Writing Metadata for " + filePath.getFileName());
		writeMetadata();
		logger.fine("Finished writing Metadata for " + filePath.getFileName());
	}

	/**
	 * Writes the file's metadata to the file.
	 */
	protected abstract void writeMetadata();

}
