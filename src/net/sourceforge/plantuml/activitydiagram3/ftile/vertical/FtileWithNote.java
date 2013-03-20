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
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class FtileWithNote implements Ftile {

	private final Ftile tile;
	private final TextBlock note;
	private final HtmlColor arrowColor;

	public FtileWithNote(Ftile tile, TextBlock note, HtmlColor arrowColor) {
		this.tile = tile;
		this.note = note;
		this.arrowColor = arrowColor;
	}

	public void drawUNewWayINLINED(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final Dimension2D dimTotal = calculateDimension(stringBounder);
		final Dimension2D dimNote = note.calculateDimension(stringBounder);
		final Dimension2D dimTile = tile.calculateDimension(stringBounder);
		final double yForNote = (dimTotal.getHeight() - dimNote.getHeight()) / 2;
		final double yForFtile = (dimTotal.getHeight() - dimTile.getHeight()) / 2;

		if (yForFtile > 0) {
			drawMissingLink(ug, dimTotal, yForFtile);
		}
		note.drawUNewWayINLINED(ug.apply(new UTranslate(0, yForNote)));
		final double marge = dimNote.getWidth();
		tile.drawUNewWayINLINED(ug.apply(new UTranslate(marge, yForFtile)));
	}

	private void drawMissingLink(UGraphic ug, Dimension2D dimTotal, double yForFtile) {
		final FtileVerticalLine line = new FtileVerticalLine(yForFtile, arrowColor);
		line.drawUNewWayINLINED(ug.apply(new UTranslate(dimTotal.getWidth() / 2 - 1, 0)));
		line.drawUNewWayINLINED(ug.apply(new UTranslate(dimTotal.getWidth() / 2 - 1, dimTotal.getHeight() - yForFtile)));

	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		final Dimension2D dimNote = note.calculateDimension(stringBounder);
		final Dimension2D dimTile = tile.calculateDimension(stringBounder);
		final double height = Math.max(dimNote.getHeight(), dimTile.getHeight());
		return new Dimension2DDouble(dimTile.getWidth() + 2 * dimNote.getWidth(), height);
	}

	public List<Url> getUrls() {
		throw new UnsupportedOperationException();
	}

	public boolean isKilled() {
		return tile.isKilled();
	}

	public LinkRendering getInLinkRendering() {
		return tile.getInLinkRendering();
	}

}
