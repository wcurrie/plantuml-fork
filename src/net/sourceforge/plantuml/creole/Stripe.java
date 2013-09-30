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

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.ugraphic.UFont;

public class Stripe {

	final private List<Atom> atoms = new ArrayList<Atom>();

	private FontConfiguration fontConfiguration;

	private StripeStyleType style = StripeStyleType.LIST_WITHOUT_NUMBER;

	public Stripe() {
		final UFont font = new UFont("Serif", Font.PLAIN, 14);
		fontConfiguration = new FontConfiguration(font, HtmlColorUtils.BLACK);
	}

	// public void add(Atom atom) {
	// this.atoms.add(atom);
	// }

	public List<Atom> getAtoms() {
		return atoms;
	}

	public FontConfiguration getActualFontConfiguration() {
		return fontConfiguration;
	}

	public void setActualFontConfiguration(FontConfiguration fontConfiguration) {
		this.fontConfiguration = fontConfiguration;
	}

	public void analyzeAndAdd(String line) {
		new CreoleStyleParser().modifyStripe(line, this);
	}

	public void addRawText(String s) {
		atoms.add(Text.create(s, fontConfiguration));
	}

	public final StripeStyleType getStyle() {
		return style;
	}

	public final void setStyle(StripeStyleType style) {
		this.style = style;
	}
}
