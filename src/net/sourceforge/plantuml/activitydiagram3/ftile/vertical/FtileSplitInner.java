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
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileMarged;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

class FtileSplitInner extends AbstractFtile {

	private final double margin = 7;

	private final double smallArrow = 20;

	private final List<Ftile> splits = new ArrayList<Ftile>();
	private final VerticalFactory factory;

	public FtileSplitInner(List<Ftile> forks, VerticalFactory factory, HtmlColor color) {
		this.factory = factory;
		for (Ftile ftile : forks) {
			final Ftile arrowStart;
			if (ftile instanceof FtileGroup) {
				arrowStart = new FtileVerticalLine(smallArrow, color);
			} else {
				arrowStart = new FtileVerticalArrow(smallArrow, color);
			}
			final Ftile tmp = new FtileAssemblySimple(arrowStart, ftile);
			this.splits.add(new FtileMarged(tmp, margin * 2));
		}
	}

	public TextBlock asTextBlock() {
		return new TextBlock() {

			public void drawUNewWayINLINED(UGraphic ug) {
				final StringBounder stringBounder = ug.getStringBounder();
				final Dimension2D dimTotal = calculateDimension(stringBounder);

				double xpos = 0;
				for (Ftile ftile : splits) {
					ftile.asTextBlock().drawUNewWayINLINED(ug.apply(new UTranslate(xpos, 0)));
					final Dimension2D dim = ftile.asTextBlock().calculateDimension(stringBounder);
					if (ftile.isKilled() == false) {
						final Ftile arrow = factory.createVerticalArrow(dimTotal.getHeight() - dim.getHeight());
						final double diffx = dim.getWidth()
								- arrow.asTextBlock().calculateDimension(stringBounder).getWidth();
						arrow.asTextBlock().drawUNewWayINLINED(
								ug.apply(new UTranslate(xpos + diffx / 2, dim.getHeight())));
					}
					xpos += dim.getWidth();
				}
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				double height = 0;
				double width = 0;
				for (Ftile ftile : splits) {
					final Dimension2D dim = ftile.asTextBlock().calculateDimension(stringBounder);
					width += dim.getWidth();
					if (dim.getHeight() > height) {
						height = dim.getHeight();
					}
				}
				return new Dimension2DDouble(width, height + smallArrow);
			}

			public List<Url> getUrls() {
				throw new UnsupportedOperationException();
			}
		};
	}

	double getX1(StringBounder stringBounder) {
		final Ftile first = splits.get(0);
		final Dimension2D dim = first.asTextBlock().calculateDimension(stringBounder);
		return dim.getWidth() / 2;
	}

	double getX2(StringBounder stringBounder) {
		final Ftile last = splits.get(splits.size() - 1);
		final Dimension2D dimTotal = asTextBlock().calculateDimension(stringBounder);
		final Dimension2D dim = last.asTextBlock().calculateDimension(stringBounder);
		return dimTotal.getWidth() - dim.getWidth() / 2;
	}

	public boolean isKilled() {
		return false;
	}

}
