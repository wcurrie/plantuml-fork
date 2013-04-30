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
 * Revision $Revision: 3837 $
 *
 */
package net.sourceforge.plantuml.jcckit;

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.command.PSystemBasicFactory1317;
import net.sourceforge.plantuml.core.DiagramType;

public class PSystemJcckitFactory1317 extends PSystemBasicFactory1317<PSystemJcckit> {

	private StringBuilder data;
	private int width;
	private int height;

	public PSystemJcckitFactory1317(DiagramType diagramType) {
		super(diagramType);
	}

	public PSystemJcckit init(String startLine) {
		this.data = null;
		this.width = 640;
		this.height = 400;
		if (getDiagramType() == DiagramType.UML) {
			return null;
		} else if (getDiagramType() == DiagramType.JCCKIT) {
			extractDimension(startLine);
			data = new StringBuilder();
			return createSystem();
		} else {
			throw new IllegalStateException(getDiagramType().name());
		}

	}

	private void extractDimension(String startLine) {
		final Pattern p = Pattern.compile("\\((\\d+),(\\d+)\\)");
		final Matcher m = p.matcher(startLine);
		final boolean ok = m.find();
		if (ok) {
			width = Integer.parseInt(m.group(1));
			height = Integer.parseInt(m.group(2));
		}
	}

	String getDimension() {
		return "" + width + "-" + height;
	}

	private PSystemJcckit createSystem() {
		final Properties p = new Properties();
		try {
			p.load(new StringReader(data.toString()));
			// For Java 1.5
			// p.load(new ByteArrayInputStream(data.toString().getBytes("ISO-8859-1")));
		} catch (IOException e) {
			Log.error("Error " + e);
			e.printStackTrace();
			return null;
		}
		return new PSystemJcckit(p, width, height);
	}

	@Override
	public PSystemJcckit executeLine(PSystemJcckit system, String line) {
		if (system == null && line.startsWith("jcckit")) {
			data = new StringBuilder();
			extractDimension(line);
			return createSystem();
		}
		if (data == null) {
			return null;
		}
		data.append(line.trim());
		data.append("\n");
		return createSystem();
	}

}
