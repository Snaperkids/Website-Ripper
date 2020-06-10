module com.snaperkids.scraper.base {
	exports com.snaperkids.services;
	exports com.snaperkids.io;
	exports com.snaperkids.services.utils;
	
	requires transitive java.logging;
	requires transitive com.snaperkids.ripper.base;
}