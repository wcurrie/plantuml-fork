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
import java.awt.geom.Point2D;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.activitydiagram3.LinkRendering;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class FtileAssemblySimple implements Ftile {

	private final Ftile tile1;
	private final Ftile tile2;

	public FtileAssemblySimple(Ftile tile1, Ftile tile2) {
		this.tile1 = tile1;
		this.tile2 = tile2;
	}

	public TextBlock asTextBlock() {
		return new TextBlock() {

			public void drawUNewWayINLINED(UGraphic ug) {
				final StringBounder stringBounder = ug.getStringBounder();
				final Dimension2D dimTotal = calculateDimension(stringBounder);
				final TextBlock textBlock1 = tile1.asTextBlock();
				final TextBlock textBlock2 = tile2.asTextBlock();
				final Dimension2D dim1 = textBlock1.calculateDimension(stringBounder);
				final Dimension2D dim2 = textBlock2.calculateDimension(stringBounder);
				final double dx1 = dimTotal.getWidth() - dim1.getWidth();
				final double dx2 = dimTotal.getWidth() - dim2.getWidth();
				textBlock1.drawUNewWayINLINED(ug.apply(new UTranslate(dx1 / 2, 0)));
				textBlock2.drawUNewWayINLINED(ug.apply(new UTranslate(dx2 / 2, dim1.getHeight())));
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				final Dimension2D dim1 = tile1.asTextBlock().calculateDimension(stringBounder);
				final Dimension2D dim2 = tile2.asTextBlock().calculateDimension(stringBounder);
				return Dimension2DDouble.mergeTB(dim1, dim2);
			}

			public List<Url> getUrls() {
				throw new UnsupportedOperationException();
			}

		};
	}

	public boolean isKilled() {
		return tile2.isKilled();
	}

	public LinkRendering getInLinkRendering() {
		return tile1.getInLinkRendering();
	}

	public LinkRendering getOutLinkRendering() {
		return null;
	}

	public Point2D getPointIn(StringBounder stringBounder) {
		final Dimension2D dimTotal = asTextBlock().calculateDimension(stringBounder);
		final TextBlock textBlock1 = tile1.asTextBlock();
		final Dimension2D dim1 = textBlock1.calculateDimension(stringBounder);
		final double dx1 = dimTotal.getWidth() - dim1.getWidth();
		final Point2D pt = tile1.getPointIn(stringBounder);
		return new Point2D.Double(pt.getX() + dx1 / 2, pt.getY());
	}

	public Point2D getPointOut(StringBounder stringBounder) {
		final Dimension2D dimTotal = asTextBlock().calculateDimension(stringBounder);
		final TextBlock textBlock1 = tile1.asTextBlock();
		final TextBlock textBlock2 = tile2.asTextBlock();
		final Dimension2D dim1 = textBlock1.calculateDimension(stringBounder);
		final Dimension2D dim2 = textBlock2.calculateDimension(stringBounder);
		final double dx2 = dimTotal.getWidth() - dim2.getWidth();

		final Point2D pt = tile2.getPointOut(stringBounder);
		return new Point2D.Double(pt.getX() + dx2 / 2, pt.getY() + dim1.getHeight());
	}

}
