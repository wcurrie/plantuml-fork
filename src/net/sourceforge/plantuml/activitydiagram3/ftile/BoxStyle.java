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
 * Revision $Revision: 8475 $
 *
 */
package net.sourceforge.plantuml.activitydiagram3.ftile;

import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.ugraphic.Shadowable;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UPath;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.USegmentType;
import net.sourceforge.plantuml.ugraphic.UTranslate;

// Created from Luc Trudeau original work
public enum BoxStyle {
	PLAIN, SDL_INPUT('<'), SDL_OUTPUT('>'), SDL_PROCEDURE('|'), SDL_SAVE('/'), SDL_CONTINUOUS('}'), SDL_TASK(']');

	private static final int CORNER = 25;
	private final char style;
	private static int DELTA_INPUT_OUTPUT = 10;
	private static double DELTA_CONTINUOUS = 5.0;
	private static int PADDING = 5;

	private BoxStyle() {
		this('\0');
	}

	private BoxStyle(char style) {
		this.style = style;
	}

	public static BoxStyle fromChar(char style) {
		for (BoxStyle bs : BoxStyle.values()) {
			if (bs.style == style) {
				return bs;
			}
		}
		return PLAIN;
	}

	public UDrawable getUDrawable(final double width, final double height, final boolean shadowing) {
		return new UDrawable() {
			public void drawU(UGraphic ug) {
				if (BoxStyle.this == SDL_PROCEDURE) {
					drawSdlProcedure(ug, width, height, shadowing);
					return;
				}
				final Shadowable s = getShape(width, height, shadowing);
				ug.draw(s);
			}
		};
	}

	private void drawSdlProcedure(UGraphic ug, double width, double height, boolean shadowing) {
		final URectangle rect = new URectangle(width, height);
		if (shadowing) {
			rect.setDeltaShadow(3);
		}
		ug.draw(rect);
		final ULine vline = new ULine(0, height);
		ug.apply(new UTranslate(PADDING, 0)).draw(vline);
		ug.apply(new UTranslate(width - PADDING, 0)).draw(vline);
	}

	private Shadowable getShape(double width, double height, boolean shadowing) {
		final Shadowable rect = getShapeInternal(width, height);
		if (shadowing) {
			rect.setDeltaShadow(3);
		}
		return rect;
	}

	private Shadowable getShapeInternal(double width, double height) {
		if (this == SDL_INPUT) {
			return getShapeSdlInput(width, height);
		} else if (this == SDL_OUTPUT) {
			return getShapeSdlOutput(width, height);
		} else if (this == SDL_SAVE) {
			return getShapeSdlSave(width, height);
		} else if (this == SDL_CONTINUOUS) {
			return getShapeSdlContinuous(width, height);
		} else if (this == SDL_TASK) {
			return getShapeTask(width, height);
		}
		return getShapePlain(width, height);
	}

	private Shadowable getShapePlain(double width, double height) {
		return new URectangle(width, height, CORNER, CORNER);
	}

	private Shadowable getShapeTask(double width, double height) {
		return new URectangle(width, height);
	}

	private Shadowable getShapeSdlContinuous(double width, double height) {
		final UPath result = new UPath();
		final double c1[] = { DELTA_CONTINUOUS, 0 };
		final double c2[] = { 0, height / 2 };
		final double c3[] = { DELTA_CONTINUOUS, height };

		result.add(c1, USegmentType.SEG_MOVETO);
		result.add(c2, USegmentType.SEG_LINETO);
		result.add(c3, USegmentType.SEG_LINETO);

		final double c4[] = { width - DELTA_CONTINUOUS, 0 };
		final double c5[] = { width, height / 2 };
		final double c6[] = { width - DELTA_CONTINUOUS, height };

		result.add(c4, USegmentType.SEG_MOVETO);
		result.add(c5, USegmentType.SEG_LINETO);
		result.add(c6, USegmentType.SEG_LINETO);
		return result;
	}

	private Shadowable getShapeSdlInput(double width, double height) {
		final UPolygon result = new UPolygon();
		result.addPoint(0, 0);
		result.addPoint(width + DELTA_INPUT_OUTPUT, 0);
		result.addPoint(width, height / 2);
		result.addPoint(width + DELTA_INPUT_OUTPUT, height);
		result.addPoint(0, height);
		return result;
	}

	private Shadowable getShapeSdlSave(double width, double height) {
		final UPolygon result = new UPolygon();
		result.addPoint(0.0, 0.0);
		result.addPoint(width - DELTA_INPUT_OUTPUT, 0.0);
		result.addPoint(width, height);
		result.addPoint(DELTA_INPUT_OUTPUT, height);
		return result;
	}

	private Shadowable getShapeSdlOutput(double width, double height) {
		final UPolygon result = new UPolygon();
		result.addPoint(0.0, 0.0);
		result.addPoint(width, 0.0);
		result.addPoint(width + DELTA_INPUT_OUTPUT, height / 2);
		result.addPoint(width, height);
		result.addPoint(0.0, height);
		return result;
	}

}
