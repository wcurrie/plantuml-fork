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
 * Revision $Revision: 8126 $
 *
 */
package net.sourceforge.plantuml.ugraphic;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.graphic.TextBlock;

public class UHorizontalLine implements UShape {

	private final double skipAtStart;
	private final double skipAtEnd;
	private final TextBlock title;
	private final boolean blankTitle;
	private final char style;

	private UHorizontalLine(double skipAtStart, double skipAtEnd, TextBlock title, boolean blankTitle, char style) {
		this.skipAtEnd = skipAtEnd;
		this.skipAtStart = skipAtStart;
		this.title = title;
		this.blankTitle = blankTitle;
		this.style = style;
	}

	public static UHorizontalLine infinite(double skipAtStart, double skipAtEnd, char style) {
		return new UHorizontalLine(skipAtStart, skipAtEnd, null, false, style);
	}

	public static UHorizontalLine infinite(double skipAtStart, double skipAtEnd, TextBlock title, char style) {
		return new UHorizontalLine(skipAtStart, skipAtEnd, title, false, style);
	}

	// static public UHorizontalLine infinite(UStroke stroke) {
	// return new UHorizontalLine(0, 0, null, false, stroke);
	// }

	public void drawLineInternal(final UGraphic ug, double startingX, double endingX, double y, UStroke defaultStroke) {
		startingX = startingX + skipAtStart;
		endingX = endingX - skipAtEnd;
		final double widthToUse = endingX - startingX;
		final UStroke strokeToUse = style == '\0' ? defaultStroke : getStroke();
		final UGraphic ug2 = ug.apply(strokeToUse).apply(new UTranslate(startingX, y));
		if (title == null) {
			drawHline(ug2, 0, widthToUse);
			return;
		}
		final Dimension2D dimTitle = title.calculateDimension(ug.getStringBounder());
		final double space = (widthToUse - dimTitle.getWidth()) / 2;
		drawHline(ug2, 0, space);
		drawTitleInternal(ug, startingX, endingX, y, false);
		drawHline(ug2, widthToUse - space, space);
	}

	private static void drawHline(UGraphic ug, double startX, double len) {
		ug.apply(new UTranslate(startX, 0)).draw(new ULine(len, 0));
	}

	public void drawTitleInternal(UGraphic ug, double startingX, double endingX, double y, boolean clearArea) {
		if (title == null || blankTitle) {
			return;
		}
		final double widthToUse = endingX - startingX;
		final Dimension2D dimTitle = title.calculateDimension(ug.getStringBounder());
		final double space = (widthToUse - dimTitle.getWidth()) / 2;
		final double x1 = startingX + space;
		final double y1 = y - dimTitle.getHeight() / 2 - 0.5;
		ug = ug.apply(new UTranslate(x1, y1));
		if (clearArea) {
			ug.apply(getStroke()).draw(new URectangle(dimTitle));
		}
		title.drawU(ug);
	}

	private UHorizontalLine blankTitle() {
		return new UHorizontalLine(skipAtStart, skipAtEnd, title, true, style);
	}

	public void drawMe(UGraphic ug) {
		if (style == '=') {
			ug.draw(this);
			ug.apply(new UTranslate(0, 2)).draw(blankTitle());
		} else {
			ug.draw(this);
		}
	}

	public UStroke getStroke() {
		if (style == '\0') {
			throw new IllegalStateException();
			// return null;
		} else if (style == '=') {
			return new UStroke();
		} else if (style == '.') {
			return new UStroke(1, 2, 1);
		} else if (style == '-') {
			return new UStroke();
		} else {
			return new UStroke(1.5);
		}
	}

}
