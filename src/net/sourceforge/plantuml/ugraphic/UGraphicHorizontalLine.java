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
 * Revision $Revision: 8033 $
 *
 */
package net.sourceforge.plantuml.ugraphic;

public class UGraphicHorizontalLine extends AbstractUGraphicHorizontalLine {

	private final double startingX;
	private final double endingX;
	private final UStroke defaultStroke;

	public UGraphicHorizontalLine(UGraphic ug, double startingX, double endingX, UStroke defaultStroke) {
		super(ug);
		this.startingX = startingX;
		this.endingX = endingX;
		this.defaultStroke = defaultStroke;
	}

	@Override
	protected AbstractUGraphicHorizontalLine copy(UGraphic ug) {
		return new UGraphicHorizontalLine(ug, startingX, endingX, defaultStroke);
	}

	@Override
	protected void drawHline(UGraphic ug, UHorizontalLine line, UTranslate translate) {
		line.drawLineInternal(ug.apply(translate), startingX, endingX, 0, defaultStroke);
	}
}
