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
	private final UStroke stroke;

	private UHorizontalLine(double skipAtStart, double skipAtEnd, TextBlock title, boolean blankTitle, UStroke stroke) {
		this.skipAtEnd = skipAtEnd;
		this.skipAtStart = skipAtStart;
		this.title = title;
		this.blankTitle = blankTitle;
		this.stroke = stroke;
	}

	public static UHorizontalLine infinite(double skipAtStart, double skipAtEnd, UStroke stroke) {
		return new UHorizontalLine(skipAtStart, skipAtEnd, null, false, stroke);
	}

	public static UHorizontalLine infinite(double skipAtStart, double skipAtEnd, TextBlock title, UStroke stroke) {
		return new UHorizontalLine(skipAtStart, skipAtEnd, title, false, stroke);
	}

	static public UHorizontalLine infinite(UStroke stroke) {
		return new UHorizontalLine(0, 0, null, false, stroke);
	}

	public void drawLine(UGraphic ug, double startingX, double endingX, double y, UStroke defaultStroke) {
		final double widthToUse = endingX - startingX;
		final UStroke strokeToUse = stroke == null ? defaultStroke : stroke;
		if (title == null) {
			ug.apply(strokeToUse).apply(new UTranslate(startingX + skipAtStart, y)).draw(new ULine(widthToUse - skipAtStart - skipAtEnd, 0));
			return;
		}
		final Dimension2D dimTitle = title.calculateDimension(ug.getStringBounder());
		final double space = (widthToUse - dimTitle.getWidth()) / 2;
		ug.apply(strokeToUse).apply(new UTranslate(startingX + skipAtStart - 1, y)).draw(new ULine(space - skipAtEnd - skipAtEnd, 0));
		drawTitle(ug, startingX, endingX, y, false);
		ug.apply(strokeToUse).apply(new UTranslate(startingX + skipAtStart + widthToUse - space, y)).draw(new ULine(space - skipAtStart - skipAtEnd, 0));
	}

	public void drawTitle(UGraphic ug, double startingX, double endingX, double y, boolean clearArea) {
		if (title == null || blankTitle) {
			return;
		}
		final double widthToUse = endingX - startingX;
		final Dimension2D dimTitle = title.calculateDimension(ug.getStringBounder());
		final double space = (widthToUse - dimTitle.getWidth()) / 2;
		final double x1 = startingX + space;
		final double y1 = y - dimTitle.getHeight() / 2 - 0.5;
		if (clearArea) {
			ug.apply(stroke).apply(new UTranslate(x1, y1)).draw(new URectangle(dimTitle.getWidth(), dimTitle.getHeight()));
		}
		title.drawU(ug.apply(new UTranslate(x1, y1)));
	}

	public UHorizontalLine blankTitle() {
		return new UHorizontalLine(skipAtStart, skipAtEnd, title, true, stroke);
	}

	public UStroke getStroke() {
		return stroke;
	}

}
