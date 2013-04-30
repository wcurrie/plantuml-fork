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

import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;

public class Snake {

	private final List<Point2D.Double> points = new ArrayList<Point2D.Double>();

	public void addPoint(double x, double y) {
		this.points.add(new Point2D.Double(x, y));
	}

	public void addPoint(Point2D p) {
		addPoint(p.getX(), p.getY());
	}

	public void drawU(UGraphic ug) {
		for (int i = 0; i < points.size() - 1; i++) {
			drawLine(ug, points.get(i), points.get(i + 1));
		}
	}

	private void drawLine(UGraphic ug, Point2D.Double pt1, Point2D.Double pt2) {
		drawLine(ug, pt1.getX(), pt1.getY(), pt2.getX(), pt2.getY());
	}

	private void drawLine(UGraphic ug, double x1, double y1, double x2, double y2) {
		final double xmin = Math.min(x1, x2);
		final double xmax = Math.max(x1, x2);
		final double ymin = Math.min(y1, y2);
		final double ymax = Math.max(y1, y2);
		ug.drawNewWay(xmin, ymin, new ULine(xmax - xmin, ymax - ymin));
	}

}
