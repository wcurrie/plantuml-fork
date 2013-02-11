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
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactory;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileMarged;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.SymbolContext;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.graphic.USymbol;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UStroke;

class FtileGroup implements Ftile {

	private final Ftile inner;
	private final TextBlock name;
	private final HtmlColor color;

	public FtileGroup(Ftile inner, Display test, HtmlColor color) {
		this.inner = new FtileMarged(inner, 10);
		this.color = color;
		final UFont font = new UFont("Serif", Font.PLAIN, 14);
		final FontConfiguration fc = new FontConfiguration(font, HtmlColorUtils.BLACK);
		if (test == null) {
			this.name = TextBlockUtils.empty(0, 0);
		} else {
			this.name = TextBlockUtils.create(test, fc, HorizontalAlignement.LEFT, new SpriteContainerEmpty());
		}
	}

	public void drawU(UGraphic ug, final double x, final double y) {
		final StringBounder stringBounder = ug.getStringBounder();
		final Dimension2D dimTotal = calculateDimension(stringBounder);
		final Dimension2D dimInner = inner.calculateDimension(stringBounder);

		final SymbolContext symbolContext = new SymbolContext(HtmlColorUtils.WHITE, HtmlColorUtils.BLACK).withShadow(
				SHADOWING).withStroke(new UStroke(2));
		USymbol.FRAME.asBig(name, TextBlockUtils.empty(0, 0), dimTotal.getWidth(), dimTotal.getHeight(), symbolContext)
				.drawU(ug, x, y);
		ug.getParam().setStroke(new UStroke());

		final double diffY = dimTotal.getHeight() - dimInner.getHeight();
		inner.drawU(ug, x, y + diffY / 2);

		Ftile line1 = new FtileVerticalArrow(diffY / 2, color);
		line1.drawU(ug, x + dimTotal.getWidth() / 2 - line1.calculateDimension(stringBounder).getWidth() / 2, y);

		Ftile line2 = new FtileVerticalLine(diffY / 2, color);
		line2.drawU(ug, x + dimTotal.getWidth() / 2 - line2.calculateDimension(stringBounder).getWidth() / 2, y
				+ dimInner.getHeight() + diffY / 2);

		// ug.getParam().setColor(HtmlColorUtils.BLACK);
		// ug.getParam().setBackcolor(null);
		// ug.draw(x, y, new URectangle(dimTotal.getWidth(),
		// dimTotal.getHeight());

	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		Dimension2D dim = inner.calculateDimension(stringBounder);
		dim = Dimension2DDouble.delta(dim, 0, 50);
		return dim;
	}

	public List<Url> getUrls() {
		throw new UnsupportedOperationException();
	}

	public boolean isKilled() {
		return false;
	}

}
