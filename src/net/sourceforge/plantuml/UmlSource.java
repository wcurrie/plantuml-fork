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
 * Revision $Revision: 4768 $
 *
 */
package net.sourceforge.plantuml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.cucadiagram.Display;

final public class UmlSource {

	final private List<String> source;

	public UmlSource(List<? extends CharSequence> source) {
		final List<String> tmp = new ArrayList<String>();
		final DiagramType type = DiagramType.getTypeFromArobaseStart(source.get(0).toString());
		if (type == DiagramType.UML) {
			final StringBuilder pending = new StringBuilder();
			for (CharSequence cs : source) {
				final String s = cs.toString();
				if (s.endsWith("\\")) {
					pending.append(s.substring(0, s.length() - 1));
				} else {
					pending.append(s);
					tmp.add(pending.toString());
					pending.setLength(0);
				}
			}
		} else {
			for (CharSequence s : source) {
				tmp.add(s.toString());
			}
		}
		this.source = Collections.unmodifiableList(tmp);
	}

	public DiagramType getDiagramType() {
		return DiagramType.getTypeFromArobaseStart(source.get(0));
	}

	public Iterator<String> iterator() {
		return source.iterator();
	}

	public String getPlainString() {
		final StringBuilder sb = new StringBuilder();
		for (String s : source) {
			sb.append(s);
			sb.append('\n');
		}
		return sb.toString();
	}

	public String getLine(int n) {
		return source.get(n);
	}

	public int getSize() {
		return source.size();
	}

	public boolean isEmpty() {
		for (String s : source) {
			if (StartUtils.isArobaseStartDiagram(s)) {
				continue;
			}
			if (StartUtils.isArobaseEndDiagram(s)) {
				continue;
			}
			if (s.matches("\\s*'.*")) {
				continue;
			}
			if (s.trim().length() != 0) {
				return false;
			}
		}
		return true;
	}

	public Display getTitle() {
		final Pattern p = Pattern.compile("(?i)^\\s*title\\s+(.+)$");
		for (String s : source) {
			final Matcher m = p.matcher(s);
			final boolean ok = m.matches();
			if (ok) {
				return Display.asList(m.group(1));
			}
		}
		return Display.emptyList();
	}

}
