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
package net.sourceforge.plantuml.acearth;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.command.PSystemBasicFactory1317;
import net.sourceforge.plantuml.core.DiagramType;

import com.ctreber.acearth.plugins.markers.Marker;

public class PSystemXearthFactory1317 extends PSystemBasicFactory1317<PSystemXearth> {

	private final Map<String, String> config = new LinkedHashMap<String, String>();
	private final List<Marker> markers = new ArrayList<Marker>();
	private int width;
	private int height;

	public PSystemXearth init(String startLine) {
		this.width = 512;
		this.height = 512;
		this.config.clear();
		this.markers.clear();
		return null;
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

	@Override
	public PSystemXearth executeLine(PSystemXearth system, String line) {
		if (system == null && line.startsWith("xearth")) {
			extractDimension(line);
			system = new PSystemXearth(width, height, config, markers);
			return system;
		}
		if (system == null) {
			return null;
		}
		if (line.startsWith("#") || line.startsWith("'")) {
			return system;
		}
		final Pattern p = Pattern.compile("(\\w+)\\s*=\\s*(.*)");
		final Matcher m = p.matcher(line);
		if (m.find()) {
			config.put(m.group(1), m.group(2));
			return system;
		}
		try {
			final Marker marker = Marker.loadMarkerFile(line);
			if (marker != null) {
				markers.add(marker);
				return system;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

}
