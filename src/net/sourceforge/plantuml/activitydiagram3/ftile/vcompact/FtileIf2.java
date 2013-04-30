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
package net.sourceforge.plantuml.activitydiagram3.ftile.vcompact;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.activitydiagram3.ftile.AbstractFtile2;
import net.sourceforge.plantuml.activitydiagram3.ftile.Diamond;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

class FtileIf2 extends AbstractFtile2 {

	private final Ftile tile1;
	private final Ftile tile2;
	private final HtmlColor borderColor;
	private final HtmlColor backColor;

	public FtileIf2(Ftile tile1, Ftile tile2, HtmlColor borderColor, HtmlColor backColor) {
		this.borderColor = borderColor;
		this.backColor = backColor;
		this.tile1 = tile1;
		this.tile2 = tile2;
	}

	private UTranslate getTranslate1(StringBounder stringBounder) {
		final Dimension2D dimTotal = calculateDimensionInternal(stringBounder);
		final Dimension2D dim1 = tile1.asTextBlock().calculateDimension(stringBounder);

		final double y1 = (dimTotal.getHeight() - dim1.getHeight()) / 2;
		final double x1 = 0;
		return new UTranslate(x1, y1);
	}

	private UTranslate getTranslate2(StringBounder stringBounder) {
		final Dimension2D dimTotal = calculateDimensionInternal(stringBounder);
		final Dimension2D dim2 = tile2.asTextBlock().calculateDimension(stringBounder);

		final double x2 = dimTotal.getWidth() - dim2.getWidth();
		final double y2 = (dimTotal.getHeight() - dim2.getHeight()) / 2;
		return new UTranslate(x2, y2);

	}

	@Override
	public UTranslate getTranslateFor(Ftile child, StringBounder stringBounder) {
		if (child == tile1) {
			return getTranslate1(stringBounder);
		}
		if (child == tile2) {
			return getTranslate2(stringBounder);
		}
		throw new UnsupportedOperationException();
	}

	public TextBlock asTextBlock() {
		return new TextBlock() {

			public void drawUNewWayINLINED(UGraphic ug) {
				final StringBounder stringBounder = ug.getStringBounder();
				final Dimension2D dimTotal = calculateDimension(stringBounder);
				tile1.asTextBlock().drawUNewWayINLINED(ug.apply(getTranslate1(stringBounder)));
				tile2.asTextBlock().drawUNewWayINLINED(ug.apply(getTranslate2(stringBounder)));

				final double xDiamond = (dimTotal.getWidth() - 2 * Diamond.diamondHalfSize) / 2;
				drawDiamond(ug, xDiamond, 0);

				if (tile1.isKilled() == false && tile2.isKilled() == false) {
					drawDiamond(ug, xDiamond, dimTotal.getHeight() - 2 * Diamond.diamondHalfSize);

				}
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				return calculateDimensionInternal(stringBounder);
			}

			public List<Url> getUrls() {
				throw new UnsupportedOperationException();
			}
		};
	}

	public boolean isKilled() {
		return false;
	}

	private void drawDiamond(UGraphic ug, double xTheoricalPosition, double yTheoricalPosition) {
		ug.apply(new UChangeColor(borderColor)).apply(new UStroke(1.5)).apply(new UChangeBackColor(backColor))
				.drawNewWay(xTheoricalPosition, yTheoricalPosition, Diamond.asPolygon());
	}

	private Dimension2D calculateDimensionInternal(StringBounder stringBounder) {
		Dimension2D dim = Dimension2DDouble.mergeLR(tile1.asTextBlock().calculateDimension(stringBounder), tile2
				.asTextBlock().calculateDimension(stringBounder));
		dim = Dimension2DDouble.delta(dim, 0, 2 * 2 * Diamond.diamondHalfSize);
		return dim;
	}

	public Point2D getPointIn(StringBounder stringBounder) {
		final Dimension2D dimTotal = calculateDimensionInternal(stringBounder);
		return new Point2D.Double(dimTotal.getWidth() / 2, 0);
	}

	public Point2D getPointOut(StringBounder stringBounder) {
		final Dimension2D dimTotal = calculateDimensionInternal(stringBounder);
		return new Point2D.Double(dimTotal.getWidth() / 2, dimTotal.getHeight());
	}

}
