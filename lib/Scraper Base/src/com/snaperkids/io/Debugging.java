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

// TODO: Auto-generated Javadoc
/**
 * The Class Debugging.
 *
 * @author Snaperkids
 */
public final class Debugging {

	/**
	 * Write to file.
	 *
	 * @param outputLines the output lines
	 * @param filename    the filename
	 */
	public static void writeToFile(List<?> outputLines, String filename) {
		File debugDirectory = Path.of("debug").toFile();
		if (!debugDirectory.exists()) {
			debugDirectory.mkdir();
		}
		
		try (PrintWriter writer = new PrintWriter(new FileWriter(Path.of("debug", filename).toFile()));) {
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

	/**
	 * Write to file.
	 *
	 * @param writer      the writer
	 * @param outputLines the output lines
	 */
	private static void writeToFile(PrintWriter writer, String... outputLines) {
		for (String line : outputLines) {
			writer.println(line);
		}
	}

	/**
	 * Write to file.
	 *
	 * @param filename    the filename
	 * @param outputLines the output lines
	 */
	public static void writeToFile(String filename, List<String> outputLines) {
		Debugging.writeToFile(filename, outputLines.toArray(new String[0]));
	}

	/**
	 * Write to file.
	 *
	 * @param filename    the filename
	 * @param outputLines the output lines
	 */
	public static void writeToFile(String filename, String... outputLines) {
		try (PrintWriter writer = new PrintWriter(new FileWriter(filename));) {
			Debugging.writeToFile(writer, outputLines);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
