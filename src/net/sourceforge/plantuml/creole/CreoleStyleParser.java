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
 * Revision $Revision: 11025 $
 *
 */
package net.sourceforge.plantuml.creole;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.graphic.FontStyle;

public class CreoleStyleParser {

	private final List<Command> commands = new ArrayList<Command>();

	public CreoleStyleParser() {
		this.commands.add(CommandCreoleStyle.createCreole(FontStyle.BOLD));
		this.commands.add(CommandCreoleStyle.createLegacy(FontStyle.BOLD));
		this.commands.add(CommandCreoleStyle.createLegacyEol(FontStyle.BOLD));
		this.commands.add(CommandCreoleStyle.createCreole(FontStyle.ITALIC));
		this.commands.add(CommandCreoleStyle.createLegacy(FontStyle.ITALIC));
		this.commands.add(CommandCreoleStyle.createLegacyEol(FontStyle.ITALIC));
	}

	public void modifyStripe(String line, Stripe stripe) {
		final StringBuilder pending = new StringBuilder();

		while (line.length() > 0) {
			final Command cmd = searchCommand(line);
			if (cmd == null) {
				pending.append(line.charAt(0));
				line = line.substring(1);
			} else {
				addPending(pending, stripe);
				line = cmd.executeAndGetRemaining(line, stripe);
			}
		}
		addPending(pending, stripe);
	}

	private void addPending(StringBuilder pending, Stripe stripe) {
		if (pending.length() == 0) {
			return;
		}
		stripe.addRawText(pending.toString());
		pending.setLength(0);
	}

	private Command searchCommand(String line) {
		for (Command cmd : commands) {
			final int i = cmd.matchingSize(line);
			if (i != 0) {
				return cmd;
			}
		}
		return null;
	}
}
