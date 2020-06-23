package com.snaperkids.ripper.config;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import com.snaperkids.ripper.utils.LoggerNames;

// TODO: Write Javadocs
/**
 * The Class ScraperLoader.
 */
public final class ScraperLoader {

	/** The Constant logger. */
	private static final Logger logger;
	static {
		logger = Logger.getLogger(LoggerNames.RIPPER.name());
		logger.setParent(Logger.getGlobal());
		logger.info("Building list of available scrapers.");
	}

	/**
	 * Load scrapers.
	 *
	 * @return the URL class loader
	 */
	public static URLClassLoader loadScrapers() {
		URL[] scraperURLs = new URL[0];
		scraperURLs = getURLS(scraperURLs);
		return new URLClassLoader(scraperURLs, ScraperLoader.class.getClassLoader());
	}

	/**
	 * Gets the urls.
	 *
	 * @param scraperURLs the scraper UR ls
	 * @return the urls
	 */
	private static URL[] getURLS(URL[] scraperURLs) {
		ArrayList<Path> pathList = new ArrayList<>();
		Path currentDirectory = Path.of(InternalConfigurator.getSetting(Setting.SCRAPER_DIRECTORY));
		try (Stream<Path> paths = Files.walk(currentDirectory)) {
			paths.filter(Files::isRegularFile).forEach(pathList::add);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error occured while accessing bin folder.");
		}
		ArrayList<URL> urls = new ArrayList<>();
		for (Path p : pathList) {
			logger.info(p.toString());
			if (!(p.endsWith("RipperBase.jar") || p.endsWith("ScraperBase.jar"))) {
				try {
					urls.add(p.toUri().toURL());
					logger.finer(p.toUri().toURL().toString());
				} catch (MalformedURLException e) {
					logger.log(Level.WARNING, "Scraper could not be loaded:: " + p.toString());
				}
			}
		}

		return urls.toArray(scraperURLs);
	}

}
