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
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.ugraphic.Shadowable;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UStroke;

class FtileBox implements Ftile {

	private static final int CORNER = 25;
	private static final int MARGIN = 10;

	private final TextBlock tb;

	private final HtmlColor color;
	private final HtmlColor backColor;

	public FtileBox(Display label, HtmlColor color, HtmlColor backColor, UFont font) {
		this.color = color;
		this.backColor = backColor;
		// final UFont font = new UFont("Serif", Font.PLAIN, 14);
		final FontConfiguration fc = new FontConfiguration(font, HtmlColorUtils.BLACK);
		tb = TextBlockUtils.create(label, fc, HorizontalAlignement.LEFT, new SpriteContainerEmpty());
	}

	public void drawU(UGraphic ug, double x, double y) {
		final Dimension2D dimTotal = calculateDimension(ug.getStringBounder());
		// final Dimension2D dimDesc =
		// tb.calculateDimension(ug.getStringBounder());

		final double widthTotal = dimTotal.getWidth();
		final double heightTotal = dimTotal.getHeight();
		final Shadowable rect = new URectangle(widthTotal, heightTotal, CORNER, CORNER);
		if (SHADOWING) {
			rect.setDeltaShadow(3);
		}
		ug.getParam().setColor(color);
		ug.getParam().setBackcolor(backColor);
		ug.getParam().setStroke(new UStroke(1.5));
		ug.draw(x, y, rect);
		ug.getParam().setStroke(new UStroke(1));

		tb.drawU(ug, x + MARGIN, y + MARGIN);

	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		final Dimension2D dim = tb.calculateDimension(stringBounder);
		return Dimension2DDouble.delta(dim, 2 * MARGIN, 2 * MARGIN);
	}

	public List<Url> getUrls() {
		return tb.getUrls();
	}

	public boolean isKilled() {
		return false;
	}

}
