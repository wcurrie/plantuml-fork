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
 * Revision $Revision: 5183 $
 *
 */
package net.sourceforge.plantuml.activitydiagram3.ftile.vertical;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.activitydiagram3.ftile.AbstractFtile;
import net.sourceforge.plantuml.activitydiagram3.ftile.Diamond;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class FtileDiamond extends AbstractFtile {

	private final HtmlColor backColor;
	private final HtmlColor borderColor;
	private final Swimlane swimlane;
	private final TextBlock north;
	private final TextBlock west;
	private final TextBlock east;

	public FtileDiamond(boolean shadowing, HtmlColor backColor, HtmlColor borderColor, Swimlane swimlane) {
		this(shadowing, backColor, borderColor, swimlane, TextBlockUtils.empty(0, 0), TextBlockUtils.empty(0, 0),
				TextBlockUtils.empty(0, 0));
	}

	public FtileDiamond withNorth(TextBlock north) {
		return new FtileDiamond(shadowing(), backColor, borderColor, swimlane, north, west, east);
	}

	public FtileDiamond withWest(TextBlock west) {
		return new FtileDiamond(shadowing(), backColor, borderColor, swimlane, north, west, east);
	}

	public FtileDiamond withEast(TextBlock east) {
		return new FtileDiamond(shadowing(), backColor, borderColor, swimlane, north, west, east);
	}

	private FtileDiamond(boolean shadowing, HtmlColor backColor, HtmlColor borderColor, Swimlane swimlane,
			TextBlock north, TextBlock west, TextBlock east) {
		super(shadowing);
		this.backColor = backColor;
		this.swimlane = swimlane;
		this.borderColor = borderColor;
		this.north = north;
		this.west = west;
		this.east = east;
	}

	public Set<Swimlane> getSwimlanes() {
		if (swimlane == null) {
			return Collections.emptySet();
		}
		return Collections.singleton(swimlane);
	}

	public Swimlane getSwimlaneIn() {
		return swimlane;
	}

	public Swimlane getSwimlaneOut() {
		return swimlane;
	}

	public TextBlock asTextBlock() {
		return new TextBlock() {

			public void drawU(UGraphic ug) {
				final double deltaX = getDeltaX(ug.getStringBounder());
				ug.apply(new UTranslate(deltaX / 2, 0)).apply(new UChangeColor(borderColor)).apply(new UStroke(1.5))
						.apply(new UChangeBackColor(backColor)).draw(Diamond.asPolygon(shadowing()));
				north.drawU(ug.apply(new UTranslate(0, Diamond.diamondHalfSize * 2)));

				final Dimension2D dimWeat = west.calculateDimension(ug.getStringBounder());
				west.drawU(ug.apply(new UTranslate(deltaX / 2 - dimWeat.getWidth(), -dimWeat.getHeight()
						+ Diamond.diamondHalfSize)));

				final Dimension2D dimEast = west.calculateDimension(ug.getStringBounder());
				east.drawU(ug.apply(new UTranslate(deltaX / 2 + Diamond.diamondHalfSize * 2, -dimEast.getHeight()
						+ Diamond.diamondHalfSize)));

			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				return calculateDimensionInternal(stringBounder);
			}

			public List<Url> getUrls(StringBounder stringBounder) {
				return Collections.emptyList();
			}
		};
	}

	private Dimension2D calculateDimensionInternal(StringBounder stringBounder) {
		final Dimension2D dimNorth = north.calculateDimension(stringBounder);
		final Dimension2D dimDiamond = new Dimension2DDouble(Diamond.diamondHalfSize * 2, Diamond.diamondHalfSize * 2);
		return Dimension2DDouble.mergeTB(dimDiamond, dimNorth);
	}

	public boolean isKilled() {
		return false;
	}

	public Point2D getPointIn(StringBounder stringBounder) {
		return new Point2D.Double(calculateDimensionInternal(stringBounder).getWidth() / 2, 0);
	}

	public Point2D getPointOut(StringBounder stringBounder) {
		return new Point2D.Double(calculateDimensionInternal(stringBounder).getWidth() / 2, Diamond.diamondHalfSize * 2);
	}

	private double getDeltaX(StringBounder stringBounder) {
		return calculateDimensionInternal(stringBounder).getWidth() - Diamond.diamondHalfSize * 2;
	}

}
