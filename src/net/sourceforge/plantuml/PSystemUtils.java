/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
 * 
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 *
 * Revision $Revision: 9997 $
 *
 */
package net.sourceforge.plantuml;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.plantuml.api.ImageData;
import net.sourceforge.plantuml.cucadiagram.CucaDiagram;
import net.sourceforge.plantuml.html.CucaDiagramHtmlMaker;
import net.sourceforge.plantuml.png.PngSplitter;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;

public class PSystemUtils {

	public static List<File> exportDiagrams(PSystem system, File suggestedFile, FileFormatOption fileFormatOption)
			throws IOException {
		if (system instanceof SequenceDiagram) {
			return exportDiagramsSequence((SequenceDiagram) system, suggestedFile, fileFormatOption);
		}
		if (system instanceof CucaDiagram) {
			return exportDiagramsCuca((CucaDiagram) system, suggestedFile, fileFormatOption);
		}
		return exportDiagramsDefault(system, suggestedFile, fileFormatOption);
	}

	static private List<File> exportDiagramsDefault(PSystem system, File suggestedFile, FileFormatOption fileFormat)
			throws IOException {
		if (suggestedFile.exists() && suggestedFile.isDirectory()) {
			throw new IllegalArgumentException("File is a directory " + suggestedFile);
		}
		OutputStream os = null;
		try {
			os = new BufferedOutputStream(new FileOutputStream(suggestedFile));
			// system.exportDiagram(os, null, 0, fileFormat);
			system.exportDiagram(os, 0, fileFormat);
		} finally {
			if (os != null) {
				os.close();
			}
		}
		return Arrays.asList(suggestedFile);
	}

	private static List<File> exportDiagramsSequence(SequenceDiagram system, File suggestedFile,
			FileFormatOption fileFormat) throws IOException {
		final List<File> result = new ArrayList<File>();
		final int nbImages = system.getNbImages();
		for (int i = 0; i < nbImages; i++) {

			final File f = fileFormat.getFileFormat().computeFilename(suggestedFile, i);
			Log.info("Creating file: " + f);
			final OutputStream fos = new BufferedOutputStream(new FileOutputStream(f));
			ImageData cmap = null;
			try {
				cmap = system.exportDiagram(fos, i, fileFormat);
			} finally {
				fos.close();
			}
			if (system.hasUrl() && cmap != null && cmap.containsData()) {
				system.exportCmap(suggestedFile, cmap);
			}
			Log.info("File size : " + f.length());
			result.add(f);
		}
		return result;
	}

	static public List<File> exportDiagramsCuca(CucaDiagram system, File suggestedFile, FileFormatOption fileFormat)
			throws IOException {
		if (suggestedFile.exists() && suggestedFile.isDirectory()) {
			throw new IllegalArgumentException("File is a directory " + suggestedFile);
		}

		if (fileFormat.getFileFormat() == FileFormat.HTML) {
			return createFilesHtml(system, suggestedFile);
		}

		ImageData cmap = null;
		OutputStream os = null;
		try {
			os = new BufferedOutputStream(new FileOutputStream(suggestedFile));
			cmap = system.exportDiagram(os, 0, fileFormat);
		} finally {
			if (os != null) {
				os.close();
			}
		}
		List<File> result = Arrays.asList(suggestedFile);

		if (system.hasUrl() && cmap != null && cmap.containsData()) {
			system.exportCmap(suggestedFile, cmap);
		}

		if (fileFormat.getFileFormat() == FileFormat.PNG) {
			result = new PngSplitter(suggestedFile, system.getHorizontalPages(), system.getVerticalPages(),
					system.getMetadata(), system.getDpi(fileFormat)).getFiles();
		}
		return result;

	}

	private static List<File> createFilesHtml(CucaDiagram system, File suggestedFile) throws IOException {
		final String name = suggestedFile.getName();
		final int idx = name.lastIndexOf('.');
		final File dir = new File(suggestedFile.getParentFile(), name.substring(0, idx));
		final CucaDiagramHtmlMaker maker = new CucaDiagramHtmlMaker(system, dir);
		return maker.create();
	}

}
