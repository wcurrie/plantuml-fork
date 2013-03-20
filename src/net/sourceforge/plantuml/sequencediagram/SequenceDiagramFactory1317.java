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
package net.sourceforge.plantuml.sequencediagram;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.ErrorUml;
import net.sourceforge.plantuml.ErrorUmlType;
import net.sourceforge.plantuml.PSystemError;
import net.sourceforge.plantuml.StartUtils;
import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommandControl;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.PSystemCommandFactory1317;
import net.sourceforge.plantuml.command.ProtectedCommand;
import net.sourceforge.plantuml.command.note.sequence.FactorySequenceNoteCommand;
import net.sourceforge.plantuml.command.note.sequence.FactorySequenceNoteOnArrowCommand;
import net.sourceforge.plantuml.command.note.sequence.FactorySequenceNoteOverSeveralCommand;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.sequencediagram.command.CommandActivate;
import net.sourceforge.plantuml.sequencediagram.command.CommandActivate2;
import net.sourceforge.plantuml.sequencediagram.command.CommandArrow;
import net.sourceforge.plantuml.sequencediagram.command.CommandAutoNewpage;
import net.sourceforge.plantuml.sequencediagram.command.CommandAutoactivate;
import net.sourceforge.plantuml.sequencediagram.command.CommandAutonumber;
import net.sourceforge.plantuml.sequencediagram.command.CommandBoxEnd;
import net.sourceforge.plantuml.sequencediagram.command.CommandBoxStart;
import net.sourceforge.plantuml.sequencediagram.command.CommandDelay;
import net.sourceforge.plantuml.sequencediagram.command.CommandDivider;
import net.sourceforge.plantuml.sequencediagram.command.CommandExoArrowLeft;
import net.sourceforge.plantuml.sequencediagram.command.CommandExoArrowRight;
import net.sourceforge.plantuml.sequencediagram.command.CommandFootbox;
import net.sourceforge.plantuml.sequencediagram.command.CommandFootboxOld;
import net.sourceforge.plantuml.sequencediagram.command.CommandGrouping;
import net.sourceforge.plantuml.sequencediagram.command.CommandHSpace;
import net.sourceforge.plantuml.sequencediagram.command.CommandIgnoreNewpage;
import net.sourceforge.plantuml.sequencediagram.command.CommandNewpage;
import net.sourceforge.plantuml.sequencediagram.command.CommandParticipantA;
import net.sourceforge.plantuml.sequencediagram.command.CommandParticipantA2;
import net.sourceforge.plantuml.sequencediagram.command.CommandParticipantA3;
import net.sourceforge.plantuml.sequencediagram.command.CommandParticipantA4;
import net.sourceforge.plantuml.sequencediagram.command.CommandReferenceMultilinesOverSeveral;
import net.sourceforge.plantuml.sequencediagram.command.CommandReferenceOverSeveral;
import net.sourceforge.plantuml.sequencediagram.command.CommandReturn;
import net.sourceforge.plantuml.sequencediagram.command.CommandSkin;
import net.sourceforge.plantuml.sequencediagram.command.CommandUrl;
import net.sourceforge.plantuml.version.IteratorCounter;

public class SequenceDiagramFactory1317 extends PSystemCommandFactory1317<SequenceDiagram> {

	@Override
	protected SequenceDiagram initCommands(List<Command> cmds) {
		final SequenceDiagram system = new SequenceDiagram();

		addCommonCommands(system, cmds);

		addCommand(new CommandActivate(system), cmds);

		addCommand(new CommandParticipantA(system), cmds);
		addCommand(new CommandParticipantA2(system), cmds);
		addCommand(new CommandParticipantA3(system), cmds);
		addCommand(new CommandParticipantA4(system), cmds);
		addCommand(new CommandArrow(system), cmds);
		// addCommand(new CommandArrowCrossX(system));
		addCommand(new CommandExoArrowLeft(system), cmds);
		addCommand(new CommandExoArrowRight(system), cmds);

		final FactorySequenceNoteCommand factorySequenceNoteCommand = new FactorySequenceNoteCommand();
		addCommand(factorySequenceNoteCommand.createSingleLine(system), cmds);

		final FactorySequenceNoteOverSeveralCommand factorySequenceNoteOverSeveralCommand = new FactorySequenceNoteOverSeveralCommand();
		addCommand(factorySequenceNoteOverSeveralCommand.createSingleLine(system), cmds);

		addCommand(new CommandBoxStart(system), cmds);
		addCommand(new CommandBoxEnd(system), cmds);
		addCommand(new CommandGrouping(system), cmds);

		addCommand(new CommandActivate2(system), cmds);
		addCommand(new CommandReturn(system), cmds);

		final FactorySequenceNoteOnArrowCommand factorySequenceNoteOnArrowCommand = new FactorySequenceNoteOnArrowCommand();
		addCommand(factorySequenceNoteOnArrowCommand.createSingleLine(system), cmds);

		addCommand(factorySequenceNoteCommand.createMultiLine(system), cmds);
		addCommand(factorySequenceNoteOverSeveralCommand.createMultiLine(system), cmds);
		addCommand(factorySequenceNoteOnArrowCommand.createMultiLine(system), cmds);

		addCommand(new CommandNewpage(system), cmds);
		addCommand(new CommandIgnoreNewpage(system), cmds);
		addCommand(new CommandAutoNewpage(system), cmds);
		addCommand(new CommandDivider(system), cmds);
		addCommand(new CommandHSpace(system), cmds);
		addCommand(new CommandReferenceOverSeveral(system), cmds);
		addCommand(new CommandReferenceMultilinesOverSeveral(system), cmds);
		addCommand(new CommandSkin(system), cmds);
		addCommand(new CommandAutonumber(system), cmds);
		addCommand(new CommandAutoactivate(system), cmds);
		addCommand(new CommandFootbox(system), cmds);
		addCommand(new CommandDelay(system), cmds);
		addCommand(new CommandFootboxOld(system), cmds);
		addCommand(new CommandUrl(system), cmds);
		
		return system;
	}

	@Override
	public String checkFinalError(SequenceDiagram system) {
		if (system.isHideUnlinkedData()) {
			system.removeHiddenParticipants();
		}
		return super.checkFinalError(system);
	}

	private AbstractPSystem buildEmptyError(UmlSource source) {
		final PSystemError result = new PSystemError(source, new ErrorUml(ErrorUmlType.SYNTAX_ERROR,
				"Empty description", 1));
		result.setSource(source);
		return result;
	}

	private PSystemError buildEmptyError(UmlSource source, String err) {
		final PSystemError result = new PSystemError(source, new ErrorUml(ErrorUmlType.EXECUTION_ERROR, err, 1));
		result.setSource(source);
		return result;
	}

	public Diagram createSystem(UmlSource source) {
		final IteratorCounter it = source.iterator();
		final String startLine = it.next();
		if (StartUtils.isArobaseStartDiagram(startLine) == false) {
			throw new UnsupportedOperationException();
		}

		if (source.isEmpty()) {
			return buildEmptyError(source);
		}
		List<Command> cmds = new ArrayList<Command>();
		final SequenceDiagram sys = initCommands(cmds);
		// systemFactory.init(startLine);
		while (it.hasNext()) {
			final String s = it.next();
			if (StartUtils.isArobaseEndDiagram(s)) {
				final String err = checkFinalError(sys);
				if (err != null) {
					return buildEmptyError(source, err);
				}
				if (source.getTotalLineCount() == 2) {
					return buildEmptyError(source);
				}
				if (sys == null) {
					return null;
				}
				sys.makeDiagramReady();
				sys.setSource(source);
				return sys;
			}
			final CommandControl commandControl = isValid(Arrays.asList(s), cmds);
			if (commandControl == CommandControl.NOT_OK) {
				final ErrorUml err = new ErrorUml(ErrorUmlType.SYNTAX_ERROR, "Syntax Error?", it.currentNum() - 1);
				// if (OptionFlags.getInstance().isUseSuggestEngine()) {
				// final SuggestEngine engine = new SuggestEngine(source,
				// systemFactory);
				// final SuggestEngineResult result = engine.tryToSuggest();
				// if (result.getStatus() == SuggestEngineStatus.ONE_SUGGESTION)
				// {
				// err.setSuggest(result);
				// }
				// }
				return new PSystemError(source, err);
			} else if (commandControl == CommandControl.OK_PARTIAL) {
				final boolean ok = manageMultiline(s, it, cmds);
				if (ok == false) {
					return new PSystemError(source, new ErrorUml(ErrorUmlType.EXECUTION_ERROR, "Syntax Error?", it
							.currentNum() - 1));

				}
			} else if (commandControl == CommandControl.OK) {
				final Command cmd = new ProtectedCommand(createCommand(Arrays.asList(s), cmds));
				final CommandExecutionResult result = cmd.execute(Arrays.asList(s));
				if (result.isOk() == false) {
					return new PSystemError(source, new ErrorUml(ErrorUmlType.EXECUTION_ERROR, result.getError(), it
							.currentNum() - 1));
				}
			} else {
				assert false;
			}
		}
		sys.setSource(source);
		return sys;

	}

	private boolean manageMultiline(final String init, IteratorCounter it, List<Command> cmds) {
		final List<String> lines = new ArrayList<String>();
		lines.add(init);
		while (it.hasNext()) {
			final String s = it.next();
			if (StartUtils.isArobaseEndDiagram(s)) {
				return false;
			}
			lines.add(s);
			final CommandControl commandControl = isValid(lines, cmds);
			if (commandControl == CommandControl.NOT_OK) {
				// throw new IllegalStateException();
				return false;
			}
			if (commandControl == CommandControl.OK) {
				final Command cmd = createCommand(lines, cmds);
				return cmd.execute(lines).isOk();
			}
		}
		return false;

	}

}
