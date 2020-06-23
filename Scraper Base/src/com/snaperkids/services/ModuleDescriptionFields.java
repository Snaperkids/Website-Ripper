package com.snaperkids.services;

/**
 * <code>ModuleDiscriptionFields</code> is an enumeration that specifies the
 * names of the various fields used to identify basic information about a
 * scraper. The members of this enum are used by the user interface as keys in a
 * map provided by the scraper implementation to retrieve the scrapers details.
 * This includes, but is not limited to, the author, version, and scraper name.
 * 
 * @author Snaperkids
 * @version 1.1, 23 June 2020
 * @since 0.2
 */
public enum ModuleDescriptionFields {

	/** The author of this scraper implementation. */
	AUTHOR,

	/** The version of this scraper implementation. */
	VERSION,

	/**
	 * The website where this scraper implementation can be found. This specifies
	 * where this implementation can be downloaded so as to allow debugging by third
	 * parties.
	 */
	WEBSITE,

	/** The title of this scraper implementation. */
	TITLE;

}
