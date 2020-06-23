/*
 *
 */
package com.snaperkids.services;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Collection;
import java.util.Map;

// TODO: Write Javadocs
/**
 * 
 * 
 * @author Snaperkids
 * @version 1.2, 23 June 2020
 * @since 0.2
 */
public interface Scraper {

	/**
	 * Gets the author details
	 */
	public Map<ModuleDescriptionFields, String> getModuleDetails();

	/**
	 * 
	 * @param link
	 * @return
	 */
	public boolean canRipWebsite(URL link);

	/**
	 * Download files.
	 *
	 * @param maxNumThreads the max num threads
	 * @throws InterruptedException the interrupted exception
	 */
	public void downloadFiles(int maxNumThreads) throws InterruptedException;

	/**
	 * 
	 * @param url
	 * @param username
	 * @param password
	 * @param saveFolder
	 * @param maxNumThreads
	 * @throws IOException
	 * @throws SocketTimeoutException
	 * @throws InterruptedException
	 */
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

	/**
	 * 
	 * @return
	 */
	public Collection<Protocol> getSupportedProtocols();

}