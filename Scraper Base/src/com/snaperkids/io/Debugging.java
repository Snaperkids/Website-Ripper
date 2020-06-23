/*
 *
 */
package com.snaperkids.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

/**
 * <code>Debugging</code> is a utility class that contains numerous output and
 * data verification methods to write out data and check data values. This is
 * provided as an assistance to developers. These methods are not intended to be
 * used for general input/output functions, but are to be used for error
 * streams.
 *
 * @author Snaperkids
 * @version 1.2, 23 June 2020
 * @since 0.2
 */
public final class Debugging {

	/**
	 * Writes the provided data to an output file in the debug directory. This is
	 * not to be used as a general output method. This method will overwrite any
	 * file with the same filename.
	 *
	 * @param outputLines the lines to be output to the file
	 * @param filename    the name of the file to be written to
	 */
	public static void writeToFile(List<?> outputLines, String filename) {
		writeToFile(outputLines, filename, false);
	}

	/**
	 * Writes the provided data to an output file in the debug directory. This is
	 * not to be used as a general output method. This method may overwrite the
	 * contents of any file with the same filename depending on the value of the
	 * append field.
	 *
	 * @param outputLines the lines to be output to the file
	 * @param filename    the name of the file to be written to
	 * @param append      if {@code true}, then the lines will be added to the end
	 *                    of the file with the same file name if it exists
	 */
	public static void writeToFile(List<?> outputLines, String filename, boolean append) {
		File debugDirectory = Path.of("debug").toFile();
		if (!debugDirectory.exists()) {
			debugDirectory.mkdir();
		}

		// If not appending to an existing file, delete any existing file and make a new
		// one.
		File outputFile = Path.of("debug", filename).toFile();
		if (!append && outputFile.exists()) {
			outputFile.delete();
			try {
				outputFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try (PrintWriter writer = new PrintWriter(new FileWriter(outputFile, append));) {
			for (final Object lines : outputLines) {
				if (lines.getClass().isArray()) {
					writer.println(Arrays.toString((Object[]) lines));
				} else {
					writer.println(lines.toString());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
