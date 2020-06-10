/*
 *
 */
package com.snaperkids.ripper;

import java.io.IOException;
import java.lang.module.ModuleFinder;
import java.lang.reflect.InvocationTargetException;
import java.net.SocketTimeoutException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.swing.SwingWorker;

import com.snaperkids.ripper.config.InternalConfigurator;
import com.snaperkids.ripper.config.Setting;
import com.snaperkids.ripper.ui.components.logic.Download;
import com.snaperkids.ripper.utils.LoggerNames;
import com.snaperkids.services.ModuleDescriptionFields;
import com.snaperkids.services.Protocol;
import com.snaperkids.services.Scraper;

import java.lang.module.Configuration;

public class Ripper extends SwingWorker<Void, Void> {

	private static final Logger					logger;
	private static final List<Scraper>			servicesList;
	public	static final Collection<Protocol>	SUPPORTED_PROTOCOLS = new HashSet<>();

	static {
		logger = Logger.getLogger(LoggerNames.RIPPER.name());
		logger.setParent(Logger.getGlobal());
		logger.info("Building list of available scrapers.");
		
		ModuleFinder finder = ModuleFinder.of(Path.of(InternalConfigurator.getSetting(Setting.SCRAPER_DIRECTORY)));
		ModuleLayer parent = Ripper.class.getModule().getLayer();
		
		//Line pulled from Stack Overflow user Krishna Telgave. Source::https://stackoverflow.com/questions/49644752/java-9-serviceloader-runtime-module-loading-and-replacement
		Set<String> moduleNames = finder.findAll().stream().map(moduleRef -> moduleRef.descriptor().name()).collect(Collectors.toSet());
		
		Configuration cf = parent.configuration().resolveAndBind(finder, ModuleFinder.of(), moduleNames);
		ClassLoader scl = ClassLoader.getSystemClassLoader();
		ModuleLayer layer = parent.defineModulesWithOneLoader(cf, scl);
		
		ServiceLoader<Scraper> services = ServiceLoader.load(layer, Scraper.class);
//		ServiceLoader<Scraper> services = ServiceLoader.load(Scraper.class);
		servicesList = new ArrayList<>();
		Iterator<Scraper> providerIter = services.iterator();
		while(providerIter.hasNext()) {
			Scraper s = providerIter.next();
			servicesList.add(s);
			logger.info(s.getClass().getCanonicalName());
		}
		
		for(Scraper s : servicesList) {
			SUPPORTED_PROTOCOLS.addAll(s.getSupportedProtocols());
			logger.info("Scraper Loaded: " + s.getClass().getCanonicalName());
		}
	}

	public static synchronized void ripWebsites(List<Download> urlsToScrape) {
		updateServices();
		for (Download url : urlsToScrape) {
			Scraper scraper = null;
			logger.info("Getting Scraper for: " + url.getURL().toString());
			for (Scraper s : servicesList) {
				if (s.canRipWebsite(url.getURL())) {
					logger.info("Found Compatable Scraper");
					try {
						scraper = s.getClass().getConstructor().newInstance();
					} catch (InstantiationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
			}
			if (scraper != null) {
				try {
					Map<ModuleDescriptionFields, String> moduleDescriptions = scraper.getModuleDetails();
					logger.info("Scraping using:\t" + moduleDescriptions.get(ModuleDescriptionFields.TITLE));
					logger.info("Author:\t" + moduleDescriptions.get(ModuleDescriptionFields.AUTHOR));
					logger.info("Version:\t" + moduleDescriptions.get(ModuleDescriptionFields.VERSION));
					logger.info("Website:\t" + moduleDescriptions.get(ModuleDescriptionFields.WEBSITE));
					scraper.downloadFiles(url.getURL(), url.getUsername(), url.getPassword(),
							InternalConfigurator.getSetting(Setting.SAVE_DIRECTORY),
							Integer.parseInt(InternalConfigurator.getSetting(Setting.MAX_THREADS)));
				} catch (SocketTimeoutException e) {
					logger.log(Level.WARNING, "Download took too long to download.", e);
				} catch (IOException e) {
					logger.log(Level.WARNING, "Could not downloa files.", e);
				} catch (InterruptedException e) {
					logger.log(Level.WARNING, "Thread interrupted.", e);
				}
			} else {
				logger.info("No Scraper found to Scrape:\t" + url.getURL().toString());
			}
		}

		Thread.currentThread().notify();
	}

	private static synchronized void updateServices() {
		for(Scraper s : servicesList)
			logger.info(s.getClass().getCanonicalName());
	}

	private final List<Download> urlsToScrape;

	public Ripper(ArrayList<Download> data) {
		logger.info("Configuring Scraper");
		urlsToScrape = new ArrayList<>();
		for (Download d : data) {
			if (d.getShouldDownload()) {
				logger.config(String.format("Will download %s.", d.getURL().getPath()));
				urlsToScrape.add(d);
			}
		}
		InternalConfigurator.confirmFileStructure();
	}

	@Override
	public Void doInBackground() {
		Ripper.ripWebsites(urlsToScrape);
		return null;
	}

}
