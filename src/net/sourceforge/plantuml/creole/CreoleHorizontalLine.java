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

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UHorizontalLine;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class CreoleHorizontalLine implements Atom {

	private final FontConfiguration fontConfiguration;
	private final String line;
	private final char style;

	public static CreoleHorizontalLine create(FontConfiguration fontConfiguration, String line, char style) {
		return new CreoleHorizontalLine(fontConfiguration, line, style);
	}

	private UHorizontalLine getHorizontalLine() {
		if (line.length() == 0) {
			return UHorizontalLine.infinite(0, 0, style);
		}
		final CreoleParser parser = new CreoleParser(fontConfiguration);
		final Sheet sheet = parser.createSheet(Display.getWithNewlines(line));
		final TextBlock tb = new SheetBlock(sheet, null);
		return UHorizontalLine.infinite(0, 0, tb, style);
	}

	private CreoleHorizontalLine(FontConfiguration fontConfiguration, String line, char style) {
		this.fontConfiguration = fontConfiguration;
		this.line = line;
		this.style = style;
	}

	public void drawU(UGraphic ug) {
		// ug = ug.apply(new UChangeColor(fontConfiguration.getColor()));
		ug = ug.apply(new UTranslate(0, 5));
		ug.draw(getHorizontalLine());
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return new Dimension2DDouble(10, 10);
	}

	public double getH1(StringBounder stringBounder) {
		return 10;
	}

}
