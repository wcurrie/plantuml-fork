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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.graphic.AddStyle;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.FontStyle;

public class CommandCreoleStyle implements Command {

	private final Pattern p;
	private final FontStyle style;

	public static CommandCreoleStyle createCreole(FontStyle style) {
		return new CommandCreoleStyle("^(" + style.getCreoleSyntax() + "(.+?)" + style.getCreoleSyntax() + ")", style);
	}

	public static Command createLegacy(FontStyle style) {
		return new CommandCreoleStyle("^(" + style.getActivationPattern() + "(.+?)" + style.getDeactivationPattern()
				+ ")", style);
	}

	public static Command createLegacyEol(FontStyle style) {
		return new CommandCreoleStyle("^(" + style.getActivationPattern() + "(.+))$", style);
	}

	private CommandCreoleStyle(String p, FontStyle style) {
		this.p = Pattern.compile(p);
		this.style = style;
	}

	public String executeAndGetRemaining(final String line, Stripe stripe) {
		final Matcher m = p.matcher(line);
		if (m.find() == false) {
			throw new IllegalStateException();
		}
		final FontConfiguration fc1 = stripe.getActualFontConfiguration();
		final FontConfiguration fc2 = new AddStyle(style, null).apply(fc1);
		stripe.setActualFontConfiguration(fc2);
		final int groupCount = m.groupCount();
		stripe.analyzeAndAdd(m.group(groupCount));
		stripe.setActualFontConfiguration(fc1);
		return line.substring(m.group(1).length());
	}

	public int matchingSize(String line) {
		final Matcher m = p.matcher(line);
		if (m.find() == false) {
			return 0;
		}
		return m.group(1).length();
	}

}
