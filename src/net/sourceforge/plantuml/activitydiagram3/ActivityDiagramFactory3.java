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
 * Revision $Revision: 9786 $
 *
 */
package net.sourceforge.plantuml.activitydiagram3;

import net.sourceforge.plantuml.activitydiagram3.command.CommandActivity3;
import net.sourceforge.plantuml.activitydiagram3.command.CommandActivityLong3;
import net.sourceforge.plantuml.activitydiagram3.command.CommandElse3;
import net.sourceforge.plantuml.activitydiagram3.command.CommandEndif3;
import net.sourceforge.plantuml.activitydiagram3.command.CommandFork3;
import net.sourceforge.plantuml.activitydiagram3.command.CommandForkAgain3;
import net.sourceforge.plantuml.activitydiagram3.command.CommandForkEnd3;
import net.sourceforge.plantuml.activitydiagram3.command.CommandGroup3;
import net.sourceforge.plantuml.activitydiagram3.command.CommandGroupEnd3;
import net.sourceforge.plantuml.activitydiagram3.command.CommandIf2;
import net.sourceforge.plantuml.activitydiagram3.command.CommandIf4;
import net.sourceforge.plantuml.activitydiagram3.command.CommandKill3;
import net.sourceforge.plantuml.activitydiagram3.command.CommandLink3;
import net.sourceforge.plantuml.activitydiagram3.command.CommandNote3;
import net.sourceforge.plantuml.activitydiagram3.command.CommandNoteLong3;
import net.sourceforge.plantuml.activitydiagram3.command.CommandRepeat3;
import net.sourceforge.plantuml.activitydiagram3.command.CommandRepeatWhile3;
import net.sourceforge.plantuml.activitydiagram3.command.CommandSplit3;
import net.sourceforge.plantuml.activitydiagram3.command.CommandSplitAgain3;
import net.sourceforge.plantuml.activitydiagram3.command.CommandSplitEnd3;
import net.sourceforge.plantuml.activitydiagram3.command.CommandStart3;
import net.sourceforge.plantuml.activitydiagram3.command.CommandStop3;
import net.sourceforge.plantuml.activitydiagram3.command.CommandWhile3;
import net.sourceforge.plantuml.activitydiagram3.command.CommandWhileEnd3;
import net.sourceforge.plantuml.command.PSystemCommandFactory;

public class ActivityDiagramFactory3 extends PSystemCommandFactory {

	private ActivityDiagram3 system;

	public ActivityDiagram3 getSystem() {
		return system;
	}

	@Override
	protected void initCommands() {
		system = new ActivityDiagram3();

		addCommonCommands(system);
		addCommand(new CommandActivity3(system));
		addCommand(new CommandIf4(system));
		addCommand(new CommandIf2(system));
		addCommand(new CommandElse3(system));
		addCommand(new CommandEndif3(system));
		addCommand(new CommandRepeat3(system));
		addCommand(new CommandRepeatWhile3(system));
		addCommand(new CommandWhile3(system));
		addCommand(new CommandWhileEnd3(system));
		addCommand(new CommandFork3(system));
		addCommand(new CommandForkAgain3(system));
		addCommand(new CommandForkEnd3(system));
		addCommand(new CommandSplit3(system));
		addCommand(new CommandSplitAgain3(system));
		addCommand(new CommandSplitEnd3(system));
		addCommand(new CommandGroup3(system));
		addCommand(new CommandGroupEnd3(system));
		addCommand(new CommandStart3(system));
		addCommand(new CommandStop3(system));
		addCommand(new CommandKill3(system));
		addCommand(new CommandLink3(system));
		addCommand(new CommandNote3(system));
		addCommand(new CommandNoteLong3(system));

		addCommand(new CommandActivityLong3(system));

	}

}
