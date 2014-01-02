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

import net.sourceforge.plantuml.ugraphic.UTranslate;

public class FtileGeometry {

	private final double left;
	private final double inY;
	private final double outY;

	public FtileGeometry(double left, double inY) {
		this.left = left;
		this.inY = inY;
		this.outY = Double.MIN_NORMAL;
	}

	public FtileGeometry(double left, double inY, double outY) {
		this.left = left;
		this.inY = inY;
		this.outY = outY;
	}

	public boolean hasPointOut() {
		return outY != Double.MIN_NORMAL;
	}

	public Point2D getPointIn() {
		return new Point2D.Double(left, inY);
	}

	public Point2D getPointOut() {
		if (outY == Double.MIN_NORMAL) {
			throw new UnsupportedOperationException();
		}
		return new Point2D.Double(left, outY);
	}
	
	public FtileGeometry withoutPointOut() {
		return new FtileGeometry(left, inY);
	}

	public FtileGeometry translate(UTranslate translate) {
		final double dx = translate.getDx();
		final double dy = translate.getDy();
		if (this.outY == Double.MIN_NORMAL) {
			return new FtileGeometry(left + dx, inY + dy);
		}
		return new FtileGeometry(left + dx, inY + dy, outY + dy);
	}

	public final double getInY() {
		return inY;
	}

	public final double getLeft() {
		return left;
	}

	public double getOutY() {
		return outY;
	}

}
