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
import net.sourceforge.plantuml.ugraphic.UEmpty;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

class FtileWhile extends AbstractFtile {

	private final Ftile whileBlock;

	private final TextBlock test;
	private final TextBlock out;

	private final HtmlColor borderColor;
	private final HtmlColor backColor;

	private final HtmlColor arrowColor;
	private final HtmlColor endInlinkColor;

	public Set<Swimlane> getSwimlanes() {
		return whileBlock.getSwimlanes();
	}

	public Swimlane getSwimlaneIn() {
		return whileBlock.getSwimlaneIn();
	}

	public Swimlane getSwimlaneOut() {
		return getSwimlaneIn();
	}

	private FtileWhile(Ftile whileBlock, Display test, HtmlColor borderColor, HtmlColor backColor,
			HtmlColor arrowColor, Display yes, Display out, UFont font, HtmlColor endInlinkColor) {
		super(whileBlock.shadowing());
		this.arrowColor = arrowColor;
		this.whileBlock = whileBlock;
		this.borderColor = borderColor;
		this.backColor = backColor;
		this.endInlinkColor = endInlinkColor;

		final FontConfiguration fc = new FontConfiguration(font, HtmlColorUtils.BLACK);
		final TextBlock tmpb = TextBlockUtils.create(yes, fc, HorizontalAlignment.LEFT, new SpriteContainerEmpty());
		if (test == null) {
			this.test = tmpb;
		} else {
			this.test = TextBlockUtils.mergeTB(
					TextBlockUtils.create(test, fc, HorizontalAlignment.LEFT, new SpriteContainerEmpty()), tmpb,
					HorizontalAlignment.CENTER);
		}
		this.out = TextBlockUtils.create(out, fc, HorizontalAlignment.LEFT, new SpriteContainerEmpty());

	}

	public static Ftile create(Ftile whileBlock, Display test, HtmlColor borderColor, HtmlColor backColor,
			HtmlColor arrowColor, Display yes, Display out, UFont font, HtmlColor endInlinkColor,
			LinkRendering afterEndwhile) {
		final FtileWhile result = new FtileWhile(whileBlock, test, borderColor, backColor, arrowColor, yes, out, font,
				endInlinkColor);
		final List<Connection> conns = new ArrayList<Connection>();
		conns.add(result.new ConnectionIn());
		conns.add(result.new ConnectionBack());
		HtmlColor afterEndwhileColor = arrowColor;
		if (afterEndwhile != null && afterEndwhile.getColor() != null) {
			afterEndwhileColor = afterEndwhile.getColor();
		}

		conns.add(result.new ConnectionOut(afterEndwhileColor));
		return FtileUtils.addConnection(result, conns);
	}

	class ConnectionIn extends AbstractConnection {
		public ConnectionIn() {
			super(null, null);
		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();
			final UTranslate translate = getTranslate(stringBounder);

			final HtmlColor firstArrowColor = LinkRendering.getColor(whileBlock.getInLinkRendering(), arrowColor);
			final Point2D p2 = translate.getTranslated(whileBlock.getPointIn(stringBounder));
			final Snake snake = new Snake(firstArrowColor, Arrows.asToDown());
			final Dimension2D dimTotal = calculateDimensionInternal(stringBounder);
			snake.addPoint(dimTotal.getWidth() / 2, 2 * Diamond.diamondHalfSize);
			snake.addPoint(p2.getX(), p2.getY());

			snake.drawU(ug);
		}
	}

	class ConnectionBack extends AbstractConnection {
		public ConnectionBack() {
			super(null, null);
		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();
			final UTranslate translate = getTranslate(stringBounder);

			final Point2D p2 = translate.getTranslated(whileBlock.getPointOut(stringBounder));
			final Snake snake = new Snake(endInlinkColor, Arrows.asToLeft());
			final Dimension2D dimTotal = calculateDimensionInternal(stringBounder);
			final Dimension2D dimWhileBlock = whileBlock.asTextBlock().calculateDimension(stringBounder);

			snake.addPoint(p2.getX(), p2.getY());
			snake.addPoint(p2.getX(), p2.getY() + Diamond.diamondHalfSize);
			final double posX = dimTotal.getWidth() / 2 + dimWhileBlock.getWidth() / 2 + Diamond.diamondHalfSize;
			snake.addPoint(posX, p2.getY() + Diamond.diamondHalfSize);
			snake.addPoint(posX, Diamond.diamondHalfSize);
			snake.addPoint(dimTotal.getWidth() / 2 + Diamond.diamondHalfSize, Diamond.diamondHalfSize);

			ug = ug.apply(new UChangeColor(endInlinkColor)).apply(new UChangeBackColor(endInlinkColor));
			ug.apply(new UTranslate(posX, dimTotal.getHeight() / 2)).draw(Arrows.asToUp());
			snake.drawU(ug);

			ug.apply(new UTranslate(dimTotal.getWidth() / 2, p2.getY() + Diamond.diamondHalfSize)).draw(
					new UEmpty(5, 15));

		}
	}

	class ConnectionOut extends AbstractConnection {
		private final HtmlColor afterEndwhileColor;

		public ConnectionOut(HtmlColor afterEndwhileColor) {
			super(null, null);
			this.afterEndwhileColor = afterEndwhileColor;
		}

		public void drawU(UGraphic ug) {
			final StringBounder stringBounder = ug.getStringBounder();
			final Snake snake = new Snake(afterEndwhileColor);
			final Dimension2D dimTotal = calculateDimensionInternal(stringBounder);
			snake.addPoint(dimTotal.getWidth() / 2 - Diamond.diamondHalfSize, Diamond.diamondHalfSize);
			final Dimension2D dimWhileBlock = whileBlock.asTextBlock().calculateDimension(stringBounder);
			final double posX = dimTotal.getWidth() / 2 - dimWhileBlock.getWidth() / 2 - Diamond.diamondHalfSize;
			snake.addPoint(posX, Diamond.diamondHalfSize);
			snake.addPoint(posX, dimTotal.getHeight());
			snake.addPoint(dimTotal.getWidth() / 2, dimTotal.getHeight());

			ug = ug.apply(new UChangeColor(afterEndwhileColor)).apply(new UChangeBackColor(afterEndwhileColor));
			ug.apply(new UTranslate(posX, dimTotal.getHeight() / 2)).draw(Arrows.asToDown());
			snake.drawU(ug);
		}
	}

	public TextBlock asTextBlock() {
		return new TextBlock() {

			public void drawU(UGraphic ug) {
				final StringBounder stringBounder = ug.getStringBounder();
				final Dimension2D dimTotal = calculateDimension(stringBounder);

				ug.apply(getTranslate(stringBounder)).draw(whileBlock);

				final double xDiamond = (dimTotal.getWidth() - 2 * Diamond.diamondHalfSize) / 2;
				drawDiamond(ug, xDiamond, 0);

				// final Dimension2D dimOut = out.calculateDimension(stringBounder);
				//
				// whileBlock.asTextBlock().drawUNewWayINLINED(ug.apply(new UTranslate(dimOut.getWidth(), 0)));
				//
				// final Dimension2D dimWhile = whileBlock.asTextBlock().calculateDimension(stringBounder);
				// ug = ug.apply(new UChangeColor(LinkRendering.getColor(afterEndwhileColor, arrowColor)));
				// ug = ug.apply(new UChangeColor(LinkRendering.getColor(endInlinkColor, arrowColor)));
				//
				// ug = ug.apply(new UChangeColor(LinkRendering.getColor(endInlinkColor, arrowColor)));
				// ug = ug.apply(new UChangeBackColor(LinkRendering.getColor(endInlinkColor, arrowColor)));
				//
				// ug = ug.apply(new UChangeColor(LinkRendering.getColor(afterEndwhileColor, arrowColor)));
				// ug = ug.apply(new UChangeBackColor(LinkRendering.getColor(afterEndwhileColor, arrowColor)));
				//
				final Dimension2D dimLabel = test.calculateDimension(stringBounder);
				test.drawU(ug.apply(new UTranslate((dimTotal.getWidth() - dimLabel.getWidth()) / 2,
						2 * Diamond.diamondHalfSize - 5)));
				out.drawU(ug);
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				return calculateDimensionInternal(stringBounder);
			}

			public List<Url> getUrls() {
				throw new UnsupportedOperationException();
			}
		};
	}

	private void drawDiamond(UGraphic ug, double xTheoricalPosition, double yTheoricalPosition) {
		ug.apply(new UChangeColor(borderColor)).apply(new UStroke(1.5)).apply(new UChangeBackColor(backColor))
				.apply(new UTranslate(xTheoricalPosition, yTheoricalPosition)).draw(Diamond.asPolygon(shadowing()));
	}

	public boolean isKilled() {
		return false;
	}

	private Dimension2D calculateDimensionInternal(StringBounder stringBounder) {
		Dimension2D dim = whileBlock.asTextBlock().calculateDimension(stringBounder);
		dim = Dimension2DDouble.delta(dim, 2 * getDeltaX(stringBounder), getDeltaY(stringBounder) + 4
				* Diamond.diamondHalfSize);
		return dim;
	}

	private UTranslate getTranslate(StringBounder stringBounder) {
		return new UTranslate(getDeltaX(stringBounder), getDeltaY(stringBounder));
	}

	private double getDeltaY(StringBounder stringBounder) {
		final double h = test.calculateDimension(stringBounder).getHeight();
		// if (h < 4 * Diamond.diamondHalfSize) {
		// return 4 * Diamond.diamondHalfSize;
		// }
		return h + 4 * Diamond.diamondHalfSize;
	}

	private double getDeltaX(StringBounder stringBounder) {
		final double widthWhile = whileBlock.asTextBlock().calculateDimension(stringBounder).getWidth();
		final double widthOut = out.calculateDimension(stringBounder).getWidth();
		final double diffX = widthOut - widthWhile / 2;
		if (diffX < Diamond.diamondHalfSize) {
			return 2 * Diamond.diamondHalfSize;
		}
		return diffX + Diamond.diamondHalfSize;
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
