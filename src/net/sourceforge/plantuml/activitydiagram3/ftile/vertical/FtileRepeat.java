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
import net.sourceforge.plantuml.activitydiagram3.ftile.AbstractFtileOld;
import net.sourceforge.plantuml.activitydiagram3.ftile.Arrows;
import net.sourceforge.plantuml.activitydiagram3.ftile.Diamond;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileAssemblySimple;
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
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

class FtileRepeat extends AbstractFtileOld {

	private final double smallArrow = 20;
	final private double heighttop = 0;
	final private double heightbottom = 0;

	private final Ftile repeat;
	private final TextBlock test;

	private final HtmlColor arrowColor;

	public FtileRepeat(Ftile repeat, Display test, VerticalFactory factory, HtmlColor borderColor, HtmlColor backColor,
			HtmlColor arrowColor, UFont font, HtmlColor endRepeatLinkColor) {
		this.arrowColor = arrowColor;

		final HtmlColor firstArrowColor = LinkRendering.getColor(repeat.getInLinkRendering(), arrowColor);

		Ftile tmp = new FtileAssemblySimple(new FtileDiamond(borderColor, backColor), new FtileAssemblySimple(
				new FtileVerticalArrow(smallArrow, firstArrowColor), repeat));

		endRepeatLinkColor = LinkRendering.getColor(endRepeatLinkColor, arrowColor);

		tmp = new FtileAssemblySimple(tmp, new FtileAssemblySimple(new FtileVerticalArrow(smallArrow,
				endRepeatLinkColor), new FtileDiamond(borderColor, backColor)));
		this.repeat = new FtileMarged(tmp, 10);
		// final UFont font = new UFont("Serif", Font.PLAIN, 14);
		final FontConfiguration fc = new FontConfiguration(font, HtmlColorUtils.BLACK);
		if (test == null) {
			this.test = TextBlockUtils.empty(0, 0);
		} else {
			this.test = TextBlockUtils.create(test, fc, HorizontalAlignement.LEFT, new SpriteContainerEmpty());
		}
	}

	public TextBlock asTextBlock() {
		return new TextBlock() {

			public void drawUNewWayINLINED(UGraphic ug) {
				final StringBounder stringBounder = ug.getStringBounder();
				final Dimension2D dimTotal = calculateDimension(stringBounder);
				final Dimension2D dimRepeat = repeat.asTextBlock().calculateDimension(stringBounder);
				final double diffx = dimTotal.getWidth() - dimRepeat.getWidth();

				repeat.asTextBlock().drawUNewWayINLINED(ug.apply(new UTranslate(diffx / 2, heighttop)));

				final Snake s1 = new Snake();
				s1.addPoint(dimTotal.getWidth() / 2 + Diamond.diamondHalfSize, Diamond.diamondHalfSize);
				s1.addPoint(dimTotal.getWidth() / 2 + Diamond.diamondHalfSize + dimRepeat.getWidth() / 2,
						Diamond.diamondHalfSize);
				s1.addPoint(dimTotal.getWidth() / 2 + Diamond.diamondHalfSize + dimRepeat.getWidth() / 2,
						dimTotal.getHeight() - Diamond.diamondHalfSize);
				s1.addPoint(dimTotal.getWidth() / 2 + Diamond.diamondHalfSize, dimTotal.getHeight()
						- Diamond.diamondHalfSize);
				s1.drawU(ug.apply(new UStroke(1.5)).apply(new UChangeColor(arrowColor)));

				ug = ug.apply(new UChangeBackColor(arrowColor)).apply(new UChangeColor(arrowColor));
				ug.drawNewWay(dimTotal.getWidth() / 2 + Diamond.diamondHalfSize, Diamond.diamondHalfSize,
						Arrows.asToLeft());
				ug.drawNewWay(dimTotal.getWidth() / 2 + Diamond.diamondHalfSize + dimRepeat.getWidth() / 2,
						dimTotal.getHeight() / 2, Arrows.asToUp());

				final Dimension2D dimTest = test.calculateDimension(stringBounder);
				test.drawUNewWayINLINED(ug.apply(new UTranslate(dimTotal.getWidth() / 2 + Diamond.diamondHalfSize,
						dimTotal.getHeight() - Diamond.diamondHalfSize - dimTest.getHeight())));
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				Dimension2D dim = repeat.asTextBlock().calculateDimension(stringBounder);
				dim = Dimension2DDouble.delta(dim, 20, heighttop + heightbottom);
				return dim;
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
