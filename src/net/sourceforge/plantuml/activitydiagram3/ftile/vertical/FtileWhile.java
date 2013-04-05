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
import net.sourceforge.plantuml.SpriteContainerEmpty;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.activitydiagram3.LinkRendering;
import net.sourceforge.plantuml.activitydiagram3.ftile.Arrows;
import net.sourceforge.plantuml.activitydiagram3.ftile.Diamond;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileMarged;
import net.sourceforge.plantuml.activitydiagram3.ftile.Snake;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignement;
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

	private final double margin = 10;
	private final double smallArrow = 30;
	final private double heightbottom = 45;

	private final Ftile whileBlock;
	private final TextBlock test;
	private final TextBlock out;

	// private final HtmlColor borderColor;
	private final HtmlColor arrowColor;
	private final HtmlColor endInlinkColor;
	private HtmlColor afterEndwhileColor;

	public void changeAfterEndwhileColor(HtmlColor afterEndwhileColor) {
		this.afterEndwhileColor = afterEndwhileColor;
	}

	public FtileWhile(Ftile whileBlock, Display test, VerticalFactory factory, HtmlColor borderColor,
			HtmlColor backColor, HtmlColor arrowColor, UFont font, HtmlColor endInlinkColor, Display yes, Display out) {
		// this.borderColor = borderColor;
		this.arrowColor = arrowColor;
		this.endInlinkColor = endInlinkColor;

		final HtmlColor firstArrowColor = LinkRendering.getColor(whileBlock.getInLinkRendering(), arrowColor);

		final Ftile tmp = new FtileAssemblySimple(new FtileDiamond(borderColor, backColor), new FtileAssemblySimple(
				new FtileVerticalArrow(smallArrow, firstArrowColor), whileBlock));

		this.whileBlock = new FtileMarged(tmp, margin);

		final FontConfiguration fc = new FontConfiguration(font, HtmlColorUtils.BLACK);
		final TextBlock tmpb;
		if (yes == null) {
			tmpb = TextBlockUtils.empty(0, 0);
		} else {
			tmpb = TextBlockUtils.create(yes, fc, HorizontalAlignement.LEFT, new SpriteContainerEmpty());
		}
		if (test == null) {
			this.test = tmpb;
		} else {
			this.test = TextBlockUtils.mergeTB(
					TextBlockUtils.create(test, fc, HorizontalAlignement.LEFT, new SpriteContainerEmpty()), tmpb,
					HorizontalAlignement.CENTER);
		}
		if (out == null) {
			this.out = TextBlockUtils.empty(0, 0);
		} else {
			this.out = TextBlockUtils.create(out, fc, HorizontalAlignement.LEFT, new SpriteContainerEmpty());
		}
	}

	public TextBlock asTextBlock() {
		return new TextBlock() {

			public void drawUNewWayINLINED(UGraphic ug) {
				final StringBounder stringBounder = ug.getStringBounder();
				final Dimension2D dimTotal = calculateDimension(stringBounder);
				final Dimension2D dimOut = out.calculateDimension(stringBounder);

				whileBlock.asTextBlock().drawUNewWayINLINED(ug.apply(new UTranslate(dimOut.getWidth(), 0)));

				final Dimension2D dimWhile = whileBlock.asTextBlock().calculateDimension(stringBounder);
				final Snake s1 = new Snake();
				ug = ug.apply(new UChangeColor(LinkRendering.getColor(afterEndwhileColor, arrowColor)));
				s1.addPoint(dimTotal.getWidth() / 2 - Diamond.diamondHalfSize, Diamond.diamondHalfSize);
				s1.addPoint(1, Diamond.diamondHalfSize);
				s1.addPoint(1, dimTotal.getHeight());
				s1.addPoint(dimTotal.getWidth() / 2 - 1, dimTotal.getHeight());
				s1.drawU(ug.apply(new UStroke(1.5)));

				final Snake s2 = new Snake();
				ug = ug.apply(new UChangeColor(LinkRendering.getColor(endInlinkColor, arrowColor)));
				s2.addPoint(dimTotal.getWidth() / 2 + Diamond.diamondHalfSize, Diamond.diamondHalfSize);
				s2.addPoint(dimTotal.getWidth() - dimOut.getWidth(), Diamond.diamondHalfSize);
				s2.addPoint(dimTotal.getWidth() - dimOut.getWidth(), 12 + dimWhile.getHeight());
				s2.addPoint(dimTotal.getWidth() / 2, 12 + dimWhile.getHeight());
				s2.addPoint(dimTotal.getWidth() / 2, dimWhile.getHeight());
				s2.drawU(ug.apply(new UStroke(1.5)));

				ug.drawNewWay(dimTotal.getWidth() / 2, dimWhile.getHeight(), new UEmpty(20, 20));

				ug = ug.apply(new UChangeColor(LinkRendering.getColor(endInlinkColor, arrowColor)));
				ug = ug.apply(new UChangeBackColor(LinkRendering.getColor(endInlinkColor, arrowColor)));
				ug.drawNewWay(dimTotal.getWidth() / 2 + Diamond.diamondHalfSize, Diamond.diamondHalfSize,
						Arrows.asToLeft());
				ug.drawNewWay(dimTotal.getWidth() - dimOut.getWidth(), dimTotal.getHeight() / 2, Arrows.asToUp());

				ug = ug.apply(new UChangeColor(LinkRendering.getColor(afterEndwhileColor, arrowColor)));
				ug = ug.apply(new UChangeBackColor(LinkRendering.getColor(afterEndwhileColor, arrowColor)));
				ug.drawNewWay(1, dimTotal.getHeight() / 2, Arrows.asToDown());

				final Dimension2D dimLabel = test.calculateDimension(stringBounder);
				test.drawUNewWayINLINED(ug.apply(new UTranslate((dimTotal.getWidth() - dimLabel.getWidth()) / 2,
						2 * Diamond.diamondHalfSize - 5)));
				out.drawUNewWayINLINED(ug);
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				final Dimension2D dim = whileBlock.asTextBlock().calculateDimension(stringBounder);
				final Dimension2D dimOut = out.calculateDimension(stringBounder);
				return Dimension2DDouble.delta(dim, dimOut.getWidth() * 2, heightbottom);
			}

			public List<Url> getUrls() {
				throw new UnsupportedOperationException();
			}
		};
	}

	public boolean isKilled() {
		return false;
	}

}
