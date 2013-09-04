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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.SpriteContainerEmpty;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.activitydiagram3.LinkRendering;
import net.sourceforge.plantuml.activitydiagram3.ftile.AbstractConnection;
import net.sourceforge.plantuml.activitydiagram3.ftile.AbstractFtile;
import net.sourceforge.plantuml.activitydiagram3.ftile.Arrows;
import net.sourceforge.plantuml.activitydiagram3.ftile.Connection;
import net.sourceforge.plantuml.activitydiagram3.ftile.Diamond;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileMinWidth;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileUtils;
import net.sourceforge.plantuml.activitydiagram3.ftile.Snake;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

class FtileIf extends AbstractFtile {

	private final Ftile tile1;
	private final Ftile tile2;
	private final HtmlColor borderColor;
	private final HtmlColor backColor;

	private final HtmlColor arrowColor;

	private final TextBlock label;
	private final TextBlock label1;
	private final TextBlock label2;

	private FtileIf(Ftile tile1, Ftile tile2, HtmlColor borderColor, HtmlColor backColor, Display labelTest,
			Display label1, Display label2, UFont font, HtmlColor arrowColor) {
		super(tile1.shadowing() || tile2.shadowing());
		this.borderColor = borderColor;
		this.backColor = backColor;
		this.tile1 = tile1;
		this.tile2 = tile2;

		this.arrowColor = arrowColor;

		final FontConfiguration fc = new FontConfiguration(font, HtmlColorUtils.BLACK);

		this.label = TextBlockUtils.create(labelTest, fc, HorizontalAlignment.LEFT, new SpriteContainerEmpty());
		this.label1 = TextBlockUtils.create(label1, fc, HorizontalAlignment.LEFT, new SpriteContainerEmpty());
		this.label2 = TextBlockUtils.create(label2, fc, HorizontalAlignment.LEFT, new SpriteContainerEmpty());

	}

	public Set<Swimlane> getSwimlanes() {
		final Set<Swimlane> result = new HashSet<Swimlane>();
		result.addAll(tile1.getSwimlanes());
		result.addAll(tile2.getSwimlanes());
		return Collections.unmodifiableSet(result);
	}

	public Swimlane getSwimlaneIn() {
		return tile1.getSwimlaneIn();
	}

	public Swimlane getSwimlaneOut() {
		return getSwimlaneIn();
	}

	static Ftile create(Ftile tile1, Ftile tile2, HtmlColor borderColor, HtmlColor backColor, Display labelTest,
			Display label1, Display label2, UFont font, HtmlColor arrowColor, HtmlColor endThenInlinkColor,
			HtmlColor endElseInlinkColor) {

		tile1 = new FtileMinWidth(tile1, 30);
		tile2 = new FtileMinWidth(tile2, 30);
		final FtileIf result = new FtileIf(tile1, tile2, borderColor, backColor, labelTest, label1, label2, font,
				arrowColor);
		final List<Connection> conns = new ArrayList<Connection>();
		conns.add(result.new ConnectionHorizontalThenVertical(tile1));
		conns.add(result.new ConnectionHorizontalThenVertical(tile2));
		if (tile1.isKilled() == false && tile2.isKilled() == false) {
			conns.add(result.new ConnectionVerticalThenHorizontal(tile1, endThenInlinkColor));
			conns.add(result.new ConnectionVerticalThenHorizontal(tile2, endElseInlinkColor));
		} else if (tile1.isKilled() == false && tile2.isKilled()) {
			conns.add(result.new ConnectionVerticalThenHorizontalDirect(tile1, endThenInlinkColor));
		} else if (tile1.isKilled() && tile2.isKilled() == false) {
			conns.add(result.new ConnectionVerticalThenHorizontalDirect(tile2, endElseInlinkColor));
		}
		return FtileUtils.addConnection(result, conns);
	}

	class ConnectionHorizontalThenVertical extends AbstractConnection {
		public ConnectionHorizontalThenVertical(Ftile tile) {
			super(null, tile);
		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();
			final Dimension2D dimTotal = calculateDimensionInternal(stringBounder);
			final Point2D p1 = new Point2D.Double(dimTotal.getWidth() / 2 + getDX(), Diamond.diamondHalfSize);
			final Point2D p2 = translate(stringBounder).getTranslated(getFtile2().getPointIn(stringBounder));
			final double x1 = p1.getX();
			final double y1 = p1.getY();
			final double x2 = p2.getX();
			final double y2 = p2.getY();

			final LinkRendering linkIn = getFtile2().getInLinkRendering();

			final Snake snake = new Snake(linkIn == null ? arrowColor : linkIn.getColor(), Arrows.asToDown());
			snake.addPoint(x1, y1);
			snake.addPoint(x2, y1);
			snake.addPoint(x2, y2);

			snake.drawU(ug);
		}

		private double getDX() {
			if (getFtile2() == tile1) {
				return -Diamond.diamondHalfSize;
			}
			if (getFtile2() == tile2) {
				return Diamond.diamondHalfSize;
			}
			throw new IllegalStateException();
		}

		private UTranslate translate(StringBounder stringBounder) {
			if (getFtile2() == tile1) {
				return getTranslate1(stringBounder);
			}
			if (getFtile2() == tile2) {
				return getTranslate2(stringBounder);
			}
			throw new IllegalStateException();
		}
	}

	class ConnectionVerticalThenHorizontal extends AbstractConnection {
		private final HtmlColor myArrowColor;

		public ConnectionVerticalThenHorizontal(Ftile tile, HtmlColor myArrowColor) {
			super(tile, null);
			this.myArrowColor = myArrowColor == null ? arrowColor : myArrowColor;
		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();
			final Dimension2D dimTotal = calculateDimensionInternal(stringBounder);

			final Point2D pointOut = getFtile1().getPointOut(stringBounder);
			if (pointOut == null) {
				return;
			}
			final Point2D p1 = translate(stringBounder).getTranslated(pointOut);
			final Point2D p2 = new Point2D.Double(dimTotal.getWidth() / 2 + getDX(), dimTotal.getHeight()
					- Diamond.diamondHalfSize);

			final double x1 = p1.getX();
			final double y1 = p1.getY();
			final double x2 = p2.getX();
			final double y2 = p2.getY();

			final UPolygon arrow = x2 > x1 ? Arrows.asToRight() : Arrows.asToLeft();
			final Snake snake = new Snake(myArrowColor, arrow);
			snake.addPoint(x1, y1);
			snake.addPoint(x1, y2);
			snake.addPoint(x2, y2);

			snake.drawU(ug);
		}

		private double getDX() {
			if (getFtile1() == tile1) {
				return -Diamond.diamondHalfSize;
			}
			if (getFtile1() == tile2) {
				return Diamond.diamondHalfSize;
			}
			throw new IllegalStateException();
		}

		private UTranslate translate(StringBounder stringBounder) {
			if (getFtile1() == tile1) {
				return getTranslate1(stringBounder);
			}
			if (getFtile1() == tile2) {
				return getTranslate2(stringBounder);
			}
			throw new IllegalStateException();
		}
	}

	class ConnectionVerticalThenHorizontalDirect extends AbstractConnection {
		private final HtmlColor myArrowColor;

		public ConnectionVerticalThenHorizontalDirect(Ftile tile, HtmlColor myArrowColor) {
			super(tile, null);
			this.myArrowColor = myArrowColor == null ? arrowColor : myArrowColor;
		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();
			final Dimension2D dimTotal = calculateDimensionInternal(stringBounder);

			final Point2D pointOut = getFtile1().getPointOut(stringBounder);
			if (pointOut == null) {
				return;
			}
			final Point2D p1 = translate(stringBounder).getTranslated(pointOut);
			final Point2D p2 = new Point2D.Double(dimTotal.getWidth() / 2, dimTotal.getHeight()
					- Diamond.diamondHalfSize);

			final double x1 = p1.getX();
			final double y1 = p1.getY();
			final double x2 = p2.getX();
			final double y2 = p2.getY();

			final Snake snake = new Snake(myArrowColor, true);
			snake.addPoint(x1, y1);
			snake.addPoint(x1, y2);
			snake.addPoint(x2, y2);
			snake.addPoint(x2, dimTotal.getHeight());

			snake.drawU(ug);
		}

		private UTranslate translate(StringBounder stringBounder) {
			if (getFtile1() == tile1) {
				return getTranslate1(stringBounder);
			}
			if (getFtile1() == tile2) {
				return getTranslate2(stringBounder);
			}
			throw new IllegalStateException();
		}
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

			public void drawU(UGraphic ug) {
				final StringBounder stringBounder = ug.getStringBounder();
				final Dimension2D dimTotal = calculateDimension(stringBounder);
				ug.apply(getTranslate1(stringBounder)).draw(tile1);
				ug.apply(getTranslate2(stringBounder)).draw(tile2);

				final double xDiamond = (dimTotal.getWidth() - 2 * Diamond.diamondHalfSize) / 2;
				drawDiamond(ug, xDiamond, 0);

				if (tile1.isKilled() == false && tile2.isKilled() == false) {
					drawDiamond(ug, xDiamond, dimTotal.getHeight() - 2 * Diamond.diamondHalfSize);
				}

				final Dimension2D dimLabel = label.calculateDimension(stringBounder);
				label.drawU(ug.apply(new UTranslate(dimTotal.getWidth() / 2 + 5, -dimLabel.getHeight() - 7)));

				final Dimension2D dimLabel1 = label1.calculateDimension(stringBounder);
				label1.drawU(ug.apply(new UTranslate(xDiamond - dimLabel1.getWidth(), -dimLabel1.getHeight()
						+ Diamond.diamondHalfSize)));

				final Dimension2D dimLabel2 = label2.calculateDimension(stringBounder);
				label2.drawU(ug.apply(new UTranslate(xDiamond + 2 * Diamond.diamondHalfSize, -dimLabel2.getHeight()
						+ Diamond.diamondHalfSize)));
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				return calculateDimensionInternal(stringBounder);
			}

			public List<Url> getUrls(StringBounder stringBounder) {
				throw new UnsupportedOperationException();
			}
		};
	}

	public boolean isKilled() {
		return tile1.isKilled() && tile2.isKilled();
	}

	private void drawDiamond(UGraphic ug, double xTheoricalPosition, double yTheoricalPosition) {
		ug.apply(new UChangeColor(borderColor)).apply(new UStroke(1.5)).apply(new UChangeBackColor(backColor))
				.apply(new UTranslate(xTheoricalPosition, yTheoricalPosition)).draw(Diamond.asPolygon(shadowing()));
	}

	private Dimension2D calculateDimensionInternal(StringBounder stringBounder) {
		Dimension2D dim = Dimension2DDouble.mergeLR(tile1.asTextBlock().calculateDimension(stringBounder), tile2
				.asTextBlock().calculateDimension(stringBounder));
		dim = Dimension2DDouble.delta(dim, 25, 100);
		// dim = Dimension2DDouble.delta(dim, 0, 2 * 2 * Diamond.diamondHalfSize);
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
