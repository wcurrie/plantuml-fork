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
 */
package net.sourceforge.plantuml.version;

import java.io.IOException;

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.ErrorUml;
import net.sourceforge.plantuml.ErrorUmlType;
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.PSystemError;
import net.sourceforge.plantuml.StartUtils;
import net.sourceforge.plantuml.api.PSystemFactory1317;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.DiagramType;
import net.sourceforge.plantuml.core.UmlSource;

public class PSystemLicenseFactory1317 implements PSystemFactory1317 {

	private AbstractPSystem executeLine(String line) {
		try {
			if (line.matches("(?i)^li[sc][ea]n[sc]e\\s*$")) {
				return PSystemLicense.create();
			}
		} catch (IOException e) {
			Log.error("Error " + e);
		}
		return null;
	}

	public DiagramType getDiagramType() {
		return DiagramType.UML;
	}

	public Diagram createSystem(UmlSource source) {

		if (source.isEmpty()) {
			return buildEmptyError(source);
		}

		final IteratorCounter it = source.iterator();
		final String startLine = it.next();
		if (StartUtils.isArobaseStartDiagram(startLine) == false) {
			throw new UnsupportedOperationException();
		}

		if (it.hasNext() == false) {
			return buildEmptyError(source);
		}
		final String s = it.next();
		if (StartUtils.isArobaseEndDiagram(s)) {
			return buildEmptyError(source);
		}
		final AbstractPSystem sys = executeLine(s);
		if (sys == null) {
			return new PSystemError(source, new ErrorUml(ErrorUmlType.SYNTAX_ERROR, "Syntax Error?",
					it.currentNum() - 1));
		}
		sys.setSource(source);
		return sys;

	}

	private AbstractPSystem buildEmptyError(UmlSource source) {
		final PSystemError result = new PSystemError(source, new ErrorUml(ErrorUmlType.SYNTAX_ERROR,
				"Empty description", 1));
		result.setSource(source);
		return result;
	}

}
