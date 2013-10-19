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

import java.awt.geom.Dimension2D;
import java.util.StringTokenizer;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UText;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class AtomText implements Atom {

	private final FontConfiguration fontConfiguration;
	private final String text;

	public static AtomText create(String text, FontConfiguration fontConfiguration) {
		return new AtomText(text, fontConfiguration);
	}

	public static AtomText createHeading(String text, FontConfiguration fontConfiguration, int order) {
		if (order == 0) {
			fontConfiguration = fontConfiguration.bigger(4).bold();
		} else if (order == 1) {
			fontConfiguration = fontConfiguration.bigger(2).bold();
		} else if (order == 2) {
			fontConfiguration = fontConfiguration.bigger(1).bold();
		} else {
			fontConfiguration = fontConfiguration.italic();
		}
		return new AtomText(text, fontConfiguration);
	}

	@Override
	public String toString() {
		return text + " " + fontConfiguration;
	}

	// public Text bold() {
	// return new Text(text, FontStyle.BOLD);
	// }

	private AtomText(String text, FontConfiguration style) {
		this.text = text;
		this.fontConfiguration = style;
	}

	public final String getText() {
		return text;
	}

	public FontConfiguration getFontConfiguration() {
		return fontConfiguration;
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		final Dimension2D rect = stringBounder.calculateDimension(fontConfiguration.getFont(), text);
		final int spaceBottom = Math.abs(fontConfiguration.getSpace());
		Log.debug("g2d=" + rect);
		Log.debug("Size for " + text + " is " + rect);
		double h = rect.getHeight();
		if (h < 10) {
			h = 10;
		}
		final double width = text.indexOf('\t') == -1 ? rect.getWidth() : getWidth(stringBounder);
		return new Dimension2DDouble(width, h + spaceBottom);
	}

	public double getFontSize2D() {
		return fontConfiguration.getFont().getSize2D();
	}

	public double getH1(StringBounder stringBounder) {
		return getFontSize2D();
	}

	double getTabSize(StringBounder stringBounder) {
		return stringBounder.calculateDimension(fontConfiguration.getFont(), "        ").getWidth();
	}

	public void drawU(UGraphic ug) {
		// if (url != null) {
		// ug.startUrl(url);
		// }
		ug = ug.apply(new UChangeColor(fontConfiguration.getColor()));

		final StringTokenizer tokenizer = new StringTokenizer(text, "\t", true);

		double x = 0;
		// final int ypos = fontConfiguration.getSpace();
		final Dimension2D rect = ug.getStringBounder().calculateDimension(fontConfiguration.getFont(), text);
		final double ypos = rect.getHeight();
		if (tokenizer.hasMoreTokens()) {
			final double tabSize = getTabSize(ug.getStringBounder());
			while (tokenizer.hasMoreTokens()) {
				final String s = tokenizer.nextToken();
				if (s.equals("\t")) {
					final double remainder = x % tabSize;
					x += tabSize - remainder;
				} else {
					final UText utext = new UText(s, fontConfiguration);
					final Dimension2D dim = ug.getStringBounder().calculateDimension(fontConfiguration.getFont(), s);
					ug.apply(new UTranslate(x, ypos)).draw(utext);
					x += dim.getWidth();
				}
			}
		}
		// if (url != null) {
		// ug.closeAction();
		// }
	}

	double getWidth(StringBounder stringBounder) {
		final StringTokenizer tokenizer = new StringTokenizer(text, "\t", true);
		final double tabSize = getTabSize(stringBounder);
		double x = 0;
		while (tokenizer.hasMoreTokens()) {
			final String s = tokenizer.nextToken();
			if (s.equals("\t")) {
				final double remainder = x % tabSize;
				x += tabSize - remainder;
			} else {
				final Dimension2D dim = stringBounder.calculateDimension(fontConfiguration.getFont(), s);
				x += dim.getWidth();
			}
		}
		return x;
	}

}
