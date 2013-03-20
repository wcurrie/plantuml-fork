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
 * Revision $Revision: 10006 $
 *
 */
package net.sourceforge.plantuml.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.api.PSystemFactory1317;
import net.sourceforge.plantuml.core.DiagramType;

public abstract class PSystemCommandFactory1317<U extends UmlDiagram> implements PSystemFactory1317 {

	private final DiagramType type;
	// private List<Command> cmds;

	protected PSystemCommandFactory1317() {
		this(DiagramType.UML);
	}

	protected PSystemCommandFactory1317(DiagramType type) {
		this.type = type;
	}

	public String checkFinalError(U system) {
		return null;
	}

	final public CommandControl isValid(List<String> lines, List<Command> cmds) {
		for (Command cmd : cmds) {
			final CommandControl result = cmd.isValid(lines);
			if (result == CommandControl.OK || result == CommandControl.OK_PARTIAL) {
				return result;
			}
		}
		return CommandControl.NOT_OK;

	}

	final public Command createCommand(List<String> lines, List<Command> cmds) {
		for (Command cmd : cmds) {
			final CommandControl result = cmd.isValid(lines);
			if (result == CommandControl.OK) {
				return cmd;
			} else if (result == CommandControl.OK_PARTIAL) {
				throw new IllegalArgumentException();
			}
		}
		throw new IllegalArgumentException();
	}

//	final public void init(String startLine) {
//		cmds = new ArrayList<Command>();
//		initCommands();
//	}

	protected abstract U initCommands(List<Command> cmds);

	final protected void addCommonCommands(U system, List<Command> cmds) {
		addCommand(new CommandNope(system), cmds);
		addCommand(new CommandComment(system), cmds);
		addCommand(new CommandMultilinesComment(system), cmds);
		addCommand(new CommandPragma(system), cmds);
		addCommand(new CommandTitle(system), cmds);
		addCommand(new CommandMultilinesTitle(system), cmds);

		addCommand(new CommandFooter(system), cmds);
		addCommand(new CommandMultilinesFooter(system), cmds);

		addCommand(new CommandHeader(system), cmds);
		addCommand(new CommandMultilinesHeader(system), cmds);

		addCommand(new CommandSkinParam(system), cmds);
		addCommand(new CommandSkinParamMultilines(system), cmds);
		addCommand(new CommandMinwidth(system), cmds);
		addCommand(new CommandRotate(system), cmds);
		addCommand(new CommandScale(system), cmds);
		addCommand(new CommandScaleWidthAndHeight(system), cmds);
		addCommand(new CommandScaleWidthOrHeight(system), cmds);
		addCommand(new CommandHideUnlinked(system), cmds);
		final FactorySpriteCommand factorySpriteCommand = new FactorySpriteCommand();
		addCommand(factorySpriteCommand.createMultiLine(system), cmds);
		addCommand(factorySpriteCommand.createSingleLine(system), cmds);
		addCommand(new CommandSpriteFile(system), cmds);

	}

	protected final void addCommand(Command cmd, List<Command> cmds) {
		cmds.add(cmd);
	}

	final public List<String> getDescription(List<Command> cmds) {
		final List<String> result = new ArrayList<String>();
		for (Command cmd : cmds) {
			result.addAll(Arrays.asList(cmd.getDescription()));
		}
		return Collections.unmodifiableList(result);

	}

	final public DiagramType getDiagramType() {
		return type;
	}

}
