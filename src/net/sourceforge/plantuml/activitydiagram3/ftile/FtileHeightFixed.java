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

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.Set;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class FtileHeightFixed extends AbstractFtile {

	private final Ftile tile;
	private final double fixedHeight;

	public FtileHeightFixed(Ftile tile, double fixedHeight) {
		this.tile = tile;
		this.fixedHeight = fixedHeight;
	}
	
	public Set<Swimlane> getSwimlanes() {
		return tile.getSwimlanes();
	}
	
	public Swimlane getSwimlaneIn() {
		return tile.getSwimlaneIn();
	}

	public Swimlane getSwimlaneOut() {
		return tile.getSwimlaneOut();
	}
	


	public Point2D getPointIn(StringBounder stringBounder) {
		final Point2D p = tile.getPointIn(stringBounder);
		return getTranslate(stringBounder).getTranslated(p);
	}

	public Point2D getPointOut(StringBounder stringBounder) {
		final Point2D p = tile.getPointOut(stringBounder);
		return getTranslate(stringBounder).getTranslated(p);
	}

	private UTranslate getTranslate(StringBounder stringBounder) {
		final Dimension2D dim = tile.asTextBlock().calculateDimension(stringBounder);
		if (dim.getHeight() > fixedHeight) {
			throw new IllegalStateException();
		}
		return new UTranslate(0, (fixedHeight - dim.getHeight()) / 2);
	}

	public TextBlock asTextBlock() {
		return new TextBlock() {

			public void drawU(UGraphic ug) {
				ug.apply(getTranslate(ug.getStringBounder())).draw(tile);
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				final Dimension2D dim = tile.asTextBlock().calculateDimension(stringBounder);
				return new Dimension2DDouble(dim.getWidth(), fixedHeight);
			}

			public List<Url> getUrls() {
				throw new UnsupportedOperationException();
			}
		};
	}

	public boolean isKilled() {
		return tile.isKilled();
	}

}
