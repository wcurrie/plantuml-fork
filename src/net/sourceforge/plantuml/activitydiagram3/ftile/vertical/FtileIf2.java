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

import java.awt.Font;
import java.awt.geom.Dimension2D;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.SpriteContainerEmpty;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.activitydiagram3.ftile.Diamond;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileMinWidth;
import net.sourceforge.plantuml.activitydiagram3.ftile.Snake;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UStroke;

class FtileIf2 implements Ftile {

	private final Ftile tile1;
	private final Ftile tile2;
	private final TextBlock label;
	private final TextBlock label1;
	private final TextBlock label2;

	public FtileIf2(Ftile tile1, Ftile tile2, Display labelTest, Display label1, Display label2) {
		this.tile1 = new FtileMinWidth(tile1, 30);
		this.tile2 = new FtileMinWidth(tile2, 30);
		final UFont font = new UFont("Serif", Font.PLAIN, 14);
		final FontConfiguration fc = new FontConfiguration(font, HtmlColorUtils.BLACK);
		if (labelTest == null) {
			label = TextBlockUtils.empty(0, 0);
		} else {
			this.label = TextBlockUtils.create(labelTest, fc, HorizontalAlignement.LEFT, new SpriteContainerEmpty());
		}
		if (label1 == null) {
			this.label1 = TextBlockUtils.empty(0, 0);
		} else {
			this.label1 = TextBlockUtils.create(label1, fc, HorizontalAlignement.LEFT, new SpriteContainerEmpty());
		}
		if (label2 == null) {
			this.label2 = TextBlockUtils.empty(0, 0);
		} else {
			this.label2 = TextBlockUtils.create(label2, fc, HorizontalAlignement.LEFT, new SpriteContainerEmpty());
		}
	}

	public void drawU(UGraphic ug, final double x, final double y) {
		final StringBounder stringBounder = ug.getStringBounder();
		final Dimension2D dimTotal = calculateDimension(stringBounder);
		final Dimension2D dim1 = tile1.calculateDimension(stringBounder);
		final Dimension2D dim2 = tile2.calculateDimension(stringBounder);
		final Dimension2D dimLabel = label.calculateDimension(stringBounder);

		final double delta1 = getDelta1(stringBounder);
		final double y1 = y + delta1 + (dimTotal.getHeight() - delta1 - dim1.getHeight()) / 2;
		final double x1 = x;
		tile1.drawU(ug, x1, y1);
		final double x2 = x + dimTotal.getWidth() - dim2.getWidth();
		final double y2 = y + delta1 + (dimTotal.getHeight() - delta1 - dim2.getHeight()) / 2;
		tile2.drawU(ug, x2, y2);

		final double xDiamond = x + (dimTotal.getWidth() - 2 * Diamond.diamondHalfSize) / 2;
		final double yDiamond = y + dimLabel.getHeight() - Diamond.diamondHalfSize / 2 + 10;
		drawDiamond(ug, xDiamond, yDiamond);

		ug.getParam().setStroke(new UStroke(1.5));
		final Snake s1 = new Snake();
		s1.addPoint(xDiamond, y + Diamond.diamondHalfSize);
		s1.addPoint(x1 + dim1.getWidth() / 2, y + Diamond.diamondHalfSize);
		s1.addPoint(x1 + dim1.getWidth() / 2, y1);
		// s1.drawU(ug);

		final Snake s2 = new Snake();
		s2.addPoint(xDiamond + 2 * Diamond.diamondHalfSize, y + Diamond.diamondHalfSize);
		s2.addPoint(x2 + dim2.getWidth() / 2, y + Diamond.diamondHalfSize);
		s2.addPoint(x2 + dim2.getWidth() / 2, y2);
		// s2.drawU(ug);

		if (tile1.isKilled() == false) {
			final Snake s3 = new Snake();
			s3.addPoint(x1 + dim1.getWidth() / 2, y1 + dim1.getHeight());
			s3.addPoint(x1 + dim1.getWidth() / 2, y + dimTotal.getHeight() - Diamond.diamondHalfSize);
			s3.addPoint(xDiamond, y + dimTotal.getHeight() - Diamond.diamondHalfSize);
			// s3.drawU(ug);
		}

		if (tile2.isKilled() == false) {
			final Snake s4 = new Snake();
			s4.addPoint(x2 + dim2.getWidth() / 2, y2 + dim2.getHeight());
			s4.addPoint(x2 + dim2.getWidth() / 2, y + dimTotal.getHeight() - Diamond.diamondHalfSize);
			s4.addPoint(xDiamond + 2 * Diamond.diamondHalfSize, y + dimTotal.getHeight() - Diamond.diamondHalfSize);
			// s4.drawU(ug);
		}
		ug.getParam().setStroke(new UStroke());
		drawDiamond(ug, xDiamond, y + dimTotal.getHeight() - 2 * Diamond.diamondHalfSize);

		ug.getParam().setColor(HtmlColorUtils.getColorIfValid("#A80036"));
		ug.getParam().setBackcolor(HtmlColorUtils.getColorIfValid("#A80036"));
		// ug.draw(x1 + dim1.getWidth() / 2, y1, Arrows.asToDown());
		// ug.draw(x2 + dim2.getWidth() / 2, y2, Arrows.asToDown());

		if (tile1.isKilled() == false) {
			// ug.draw(xDiamond, y + dimTotal.getHeight() - Diamond.diamondHalfSize, Arrows.asToRight());
		}

		if (tile2.isKilled() == false) {
			// ug.draw(xDiamond + 2 * Diamond.diamondHalfSize, y + dimTotal.getHeight() - Diamond.diamondHalfSize,
			// Arrows.asToLeft());
		}
		label.drawU(ug, x + dimTotal.getWidth() / 2 + 9, y);

		final Dimension2D dimLabel1 = label1.calculateDimension(stringBounder);
		label1.drawU(ug, xDiamond - dimLabel1.getWidth(), yDiamond);

		final Dimension2D dimLabel2 = label2.calculateDimension(stringBounder);
		label2.drawU(ug, xDiamond + 2 * Diamond.diamondHalfSize, yDiamond);

	}

	private void drawDiamond(UGraphic ug, double xTheoricalPosition, double yTheoricalPosition) {
		ug.getParam().setStroke(new UStroke(1.5));
		ug.getParam().setColor(HtmlColorUtils.getColorIfValid("#A80036"));
		ug.getParam().setBackcolor(HtmlColorUtils.getColorIfValid("#FEFECE"));
		ug.draw(xTheoricalPosition, yTheoricalPosition, Diamond.asPolygon());
		ug.getParam().setStroke(new UStroke());
	}

	private double getDelta1(StringBounder stringBounder) {
		final Dimension2D dimLabel = label.calculateDimension(stringBounder);
		return dimLabel.getHeight();
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		Dimension2D dim = Dimension2DDouble.mergeLR(tile1.calculateDimension(stringBounder),
				tile2.calculateDimension(stringBounder));
		dim = Dimension2DDouble.delta(dim, 25, 30 + 2 * Diamond.diamondHalfSize + getDelta1(stringBounder));
		return dim;
	}

	public List<Url> getUrls() {
		throw new UnsupportedOperationException();
	}

	public boolean isKilled() {
		return false;
	}

}