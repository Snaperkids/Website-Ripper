/*
 *
 */
package com.snaperkids.services;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Collection;
import java.util.Map;

public interface Scraper {
	
	/**
	 * Gets the author details
	 */
	public Map<ModuleDescriptionFields, String> getModuleDetails();
	
	boolean canRipWebsite(URL link);

	/**
	 * Download files.
	 *
	 * @param maxNumThreads the max num threads
	 * @throws InterruptedException the interrupted exception
	 */
	public void downloadFiles(int maxNumThreads) throws InterruptedException;

	public void downloadFiles(URL url, String username, char[] password, String saveFolder, int maxNumThreads)
			throws IOException, SocketTimeoutException, InterruptedException;

	/**
	 * Gets the download links.
	 *
	 * @throws IOException
	 */
	public void getDownloadLinks() throws IOException;

	/**
	 * Gets the links.
	 *
	 */
	public void getLinks();

	/**
	 * Gets the titles and metadata.
	 *
	 */
	public void getTitlesAndMetadata();

	public Collection<Protocol> getSupportedProtocols();

}