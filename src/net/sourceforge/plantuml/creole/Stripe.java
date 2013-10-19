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

import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.ImgValign;

public class Stripe {

	final private List<Atom> atoms = new ArrayList<Atom>();

	private FontConfiguration fontConfiguration;

	final private StripeStyle style;

	public Stripe(FontConfiguration fontConfiguration, StripeStyle style, CreoleContext context) {
		this.fontConfiguration = fontConfiguration;
		this.style = style;

		final Atom header = style.getHeader(fontConfiguration, context);

		if (header != null) {
			atoms.add(header);
		}
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
		if (line == null) {
			throw new IllegalArgumentException();
		}
		if (style.getType() == StripeStyleType.HEADING) {
			atoms.add(AtomText.createHeading(line, fontConfiguration, style.getOrder()));
		} else if (style.getType() == StripeStyleType.HORIZONTAL_LINE) {
			atoms.add(CreoleHorizontalLine.create(fontConfiguration, line, style.getStyle()));
		} else {
			new StyleParser().modifyStripe(line, this);
		}
	}

	public void addImage(String src) {
		atoms.add(AtomImg.create(src, ImgValign.TOP, 0));
	}

	public void addRawText(String s) {
		atoms.add(AtomText.create(s, fontConfiguration));
	}

	private final StripeStyle getStyle() {
		return style;
	}
}
