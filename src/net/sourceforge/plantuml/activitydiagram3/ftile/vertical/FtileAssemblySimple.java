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
package net.sourceforge.plantuml.activitydiagram3.ftile.vertical;

import java.awt.geom.Dimension2D;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.activitydiagram3.LinkRendering;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UGraphic;

class FtileAssemblySimple implements Ftile {

	private final Ftile tile1;
	private final Ftile tile2;

	FtileAssemblySimple(Ftile tile1, Ftile tile2) {
		this.tile1 = tile1;
		this.tile2 = tile2;
	}

	public void drawU(UGraphic ug, double x, double y) {
		final StringBounder stringBounder = ug.getStringBounder();
		final Dimension2D dimTotal = calculateDimension(stringBounder);
		final Dimension2D dim1 = tile1.calculateDimension(stringBounder);
		final Dimension2D dim2 = tile2.calculateDimension(stringBounder);
		final double dx1 = dimTotal.getWidth() - dim1.getWidth();
		final double dx2 = dimTotal.getWidth() - dim2.getWidth();
		tile1.drawU(ug, x + dx1 / 2, y);
		tile2.drawU(ug, x + dx2 / 2, y + dim1.getHeight());

	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return Dimension2DDouble.mergeTB(tile1.calculateDimension(stringBounder),
				tile2.calculateDimension(stringBounder));
	}

	public List<Url> getUrls() {
		throw new UnsupportedOperationException();
	}

	public boolean isKilled() {
		return tile2.isKilled();
	}

	public LinkRendering getInLinkRendering() {
		return tile1.getInLinkRendering();
	}

}
