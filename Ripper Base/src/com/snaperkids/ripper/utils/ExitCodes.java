/*
 *
 */
package com.snaperkids.ripper.utils;

// TODO: Go back through error checking to make sure all exit codes are accounted for.
// TODO: Write Javadocs
/**
 * The Enum ExitCodes.
 */
public enum ExitCodes {

	/** Process exited not due to an error.. */
	NO_ERROR,
	
	/** Logs for error recording could not be made. */
	COULD_NOT_MAKE_LOGS,

	/** GUI was not generated. */
	FAILED_TO_MAKE_GUI,

	/** Process exited because the settings file format was invalid. */
	INVALID_SETTINGS_FILE_FORMAT,

	/** Process exited because the settings file couldn't be found. */
	SETTINGS_FILE_NOT_FOUND,

	/** Process exited because it was unable to load the settings file. */
	UNABLE_TO_LOAD_SETTINGS_FILE;

}
