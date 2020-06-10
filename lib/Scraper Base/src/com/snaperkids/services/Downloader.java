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
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.logging.FileHandler;
import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class Downloader.
 *
 * @author Snaperkids
 */
public abstract class Downloader implements Runnable {

	private static boolean fileHandlerAdded = false;
	
	protected static FileHandler fileHandler = null;

	/** The download link. */
	protected final URL downloadLink;

	/** The file path. */
	protected final Path filePath;

	/** The metadata. */
	protected final Map<String, String> metadata;
	
	protected final Logger logger;

	protected ExecutorService executor;
	
	protected String title;
	
	protected String fileExtension;
	
	private boolean hasFailedBefore;
	
	{
		logger = Logger.getLogger(this.getClass().getSimpleName());
		logger.setParent(Logger.getGlobal());
		logger.setLevel(Level.ALL);
	}

	/**
	 * Instantiates a new downloader.
	 *
	 * @param url        the url
	 * @param title      the title
	 * @param fileNum    the file num
	 * @param numPadding the num padding 
	 * @param metadata   the metadata
	 * @param executor   the executor
	 * @throws IOException
	 */
	protected Downloader(URL url, String title, int fileNum, int numPadding, Map<String, String> metadata, ExecutorService executor)
			throws IOException {
		fileHandler = Downloader.createFileHandler(this.getClass().getSimpleName());
		if(!fileHandlerAdded) {
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
	 * Download file.
	 */
	protected void downloadFile() {
		try (ReadableByteChannel downloadChannel = Channels.newChannel(openDownloadStream());
				FileOutputStream stream = new FileOutputStream(filePath.toFile());) {
			stream.getChannel().transferFrom(downloadChannel, 0, Long.MAX_VALUE);
		} catch (final FileNotFoundException e) {
			logger.log(Level.WARNING, "Could not find: " + filePath.getFileName(), e);
		} catch (SocketException e) {
			if(!hasFailedBefore) {
				hasFailedBefore = true;
				executor.submit(this);
			}else {
				logger.log(Level.WARNING, "SocketException while downloading: " + filePath.getFileName(), e);
			}
		} catch (final IOException e) {
			logger.log(Level.WARNING, "Exception downloading: " + filePath.getFileName(), e);
		}
	}

	/**
	 * Download file.
	 *
	 * @param username the username
	 * @param password the password
	 */
	protected void downloadFile(String username, String password) {
		try (ReadableByteChannel downloadChannel = Channels.newChannel(openDownloadStream(username, password));
				FileOutputStream stream = new FileOutputStream(filePath.toFile());) {
			stream.getChannel().transferFrom(downloadChannel, 0, Long.MAX_VALUE);
		} catch (final FileNotFoundException e) {
			logger.log(Level.WARNING, "Could not find: " + filePath.getFileName(), e);
		} catch (final IOException e) {
			logger.log(Level.WARNING, "Exception downloading: " + filePath.getFileName(), e);
		}
		
	}

	/**
	 * Gets the file path.
	 *
	 * @param title      the title
	 * @param fileNum    the file num
	 * @param numPadding the num padding
	 * @return the file path
	 * @throws IOException
	 */
	protected abstract Path getFilePath(String title, int fileNum, int numPadding) throws IOException;

	/**
	 * Open download stream.
	 *
	 * @return the input stream
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected InputStream openDownloadStream() throws IOException{
		final URLConnection con = downloadLink.openConnection();
		return con.getInputStream();
	}

	/**
	 * Open download stream.
	 *
	 * @param username the username
	 * @param password the password
	 * @return the input stream
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected InputStream openDownloadStream(String username, String password) throws IOException {
		return openDownloadStream();
	}

	/**
	 * Run.
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
	 * Write metadata.
	 */
	protected abstract void writeMetadata();

}
