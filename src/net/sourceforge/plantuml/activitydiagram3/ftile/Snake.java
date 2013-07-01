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

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class Snake {

	private final List<Point2D.Double> points = new ArrayList<Point2D.Double>();
	private final UPolygon endDecoration;
	private final HtmlColor color;
	private final boolean mergeable;

	private Snake(HtmlColor color, UPolygon endDecoration, boolean mergeable) {
		this.endDecoration = endDecoration;
		this.color = color;
		this.mergeable = mergeable;
	}

	public Snake(HtmlColor color, UPolygon endDecoration) {
		this(color, endDecoration, false);
	}

	public Snake(HtmlColor color) {
		this(color, null, false);
	}

	public Snake(HtmlColor color, boolean mergeable) {
		this(color, null, mergeable);
	}

	public void addPoint(double x, double y) {
		this.points.add(new Point2D.Double(x, y));
	}

	public void addPoint(Point2D p) {
		addPoint(p.getX(), p.getY());
	}

	public void drawU(UGraphic ug) {
		ug = ug.apply(new UChangeColor(color));
		for (int i = 0; i < points.size() - 1; i++) {
			drawLine(ug.apply(new UStroke(1.5)), points.get(i), points.get(i + 1));
		}
		if (endDecoration != null) {
			ug = ug.apply(new UChangeBackColor(color));
			final Point2D end = points.get(points.size() - 1);
			ug.apply(new UTranslate(end)).apply(new UStroke()).draw(endDecoration);
		}
	}

	private void drawLine(UGraphic ug, Point2D.Double pt1, Point2D.Double pt2) {
		drawLine(ug, pt1.getX(), pt1.getY(), pt2.getX(), pt2.getY());
	}

	private void drawLine(UGraphic ug, double x1, double y1, double x2, double y2) {
		ug = ug.apply(new UTranslate(x1, y1));

		if (mergeable) {
			ug.draw(new ULineMergeable(x2 - x1, y2 - y1));
		} else {
			ug.draw(new ULine(x2 - x1, y2 - y1));
		}
	}

}
