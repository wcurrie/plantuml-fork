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
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.activitydiagram3.LinkRendering;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

class FtileFork implements Ftile {

	private final double barHeight = 6;
	private final Ftile inner;
	private final HtmlColor colorBar;

	public FtileFork(FtileForkInner inner, HtmlColor colorBar) {
		this.inner = inner;
		this.colorBar = colorBar;
	}

	public void drawUNewWayINLINED(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final Dimension2D dimTotal = calculateDimension(stringBounder);
		final Ftile black = new FtileBlackBlock(dimTotal.getWidth(), barHeight, colorBar);

		inner.drawUNewWayINLINED(ug.apply(new UTranslate(0, barHeight)));

		black.drawUNewWayINLINED(ug);
		black.drawUNewWayINLINED(ug.apply(new UTranslate(0, (dimTotal.getHeight() - barHeight))));
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		final Dimension2D dim = inner.calculateDimension(stringBounder);
		return Dimension2DDouble.delta(dim, 0, 2 * barHeight);
	}

	public List<Url> getUrls() {
		throw new UnsupportedOperationException();
	}

	public boolean isKilled() {
		return false;
	}

	public LinkRendering getInLinkRendering() {
		return null;
	}

}
