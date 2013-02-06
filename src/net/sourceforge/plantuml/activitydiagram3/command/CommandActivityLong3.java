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
 * Revision $Revision: 5031 $
 *
 */
package net.sourceforge.plantuml.activitydiagram3.command;

import java.util.List;

import net.sourceforge.plantuml.activitydiagram3.ActivityDiagram3;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.CommandMultilines2;
import net.sourceforge.plantuml.command.MultilinesStrategy;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.cucadiagram.Display;

public class CommandActivityLong3 extends CommandMultilines2<ActivityDiagram3> {

	public CommandActivityLong3(final ActivityDiagram3 diagram) {
		super(diagram, getRegexConcat(), MultilinesStrategy.REMOVE_STARTING_QUOTE);
	}

	@Override
	public String getPatternEnd() {
		return "^(.*);$";
	}

	static RegexConcat getRegexConcat() {
		return new RegexConcat(new RegexLeaf("^"), //
				new RegexLeaf(":"), //
				new RegexLeaf("DATA", "(.*)"), //
				new RegexLeaf("$"));
	}

	public CommandExecutionResult executeNow(List<String> lines) {
		removeStarting(lines);
		removeEnding(lines);
		getSystem().addActivity(new Display(lines));
		return CommandExecutionResult.ok();
	}

	private void removeStarting(List<String> lines) {
		if (lines.size() == 0) {
			return;
		}
		final String s = lines.get(0).trim();
		lines.set(0, s.substring(1));
	}

	private void removeEnding(List<String> lines) {
		if (lines.size() == 0) {
			return;
		}
		final int n = lines.size() - 1;
		final String s = lines.get(n);
		lines.set(n, s.substring(0, s.length() - 1));
	}

}
