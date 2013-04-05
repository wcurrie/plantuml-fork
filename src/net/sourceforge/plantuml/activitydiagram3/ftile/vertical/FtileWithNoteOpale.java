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
import java.awt.geom.Point2D;
import java.util.List;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.svek.image.Opale;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class FtileWithNoteOpale extends AbstractFtile {

	private final Ftile tile;
	private final Opale opale;
	// private final TextBlock note;
	private final HtmlColor arrowColor;
	private final NotePosition notePosition;
	private final double halfSuppSpace = 20;

	public FtileWithNoteOpale(Ftile tile, Display note, HtmlColor arrowColor, NotePosition notePosition) {
		this.tile = tile;
		this.notePosition = notePosition;
		this.arrowColor = arrowColor;

		final ISkinParam skinParam = new SkinParam(UmlDiagramType.ACTIVITY);

		final Rose rose = new Rose();
		final HtmlColor fontColor = rose.getFontColor(skinParam, FontParam.NOTE);
		final UFont fontNote = skinParam.getFont(FontParam.NOTE, null);

		final HtmlColor noteBackgroundColor = rose.getHtmlColor(skinParam, ColorParam.noteBackground);
		final HtmlColor borderColor = rose.getHtmlColor(skinParam, ColorParam.noteBorder);

		final TextBlock text = TextBlockUtils.create(note, new FontConfiguration(fontNote, fontColor),
				HorizontalAlignement.LEFT, skinParam);
		opale = new Opale(borderColor, noteBackgroundColor, text, skinParam.shadowing());

	}

	public TextBlock asTextBlock() {
		return new TextBlock() {

			public void drawUNewWayINLINED(UGraphic ug) {
				final StringBounder stringBounder = ug.getStringBounder();
				final Dimension2D dimTotal = calculateDimension(stringBounder);
				final Dimension2D dimNote = opale.calculateDimension(stringBounder);
				final Dimension2D dimTile = tile.asTextBlock().calculateDimension(stringBounder);
				final double yForNote = (dimTotal.getHeight() - dimNote.getHeight()) / 2;
				final double yForFtile = (dimTotal.getHeight() - dimTile.getHeight()) / 2;

				if (yForFtile > 0) {
					drawMissingLink(ug, dimTotal, yForFtile);
				}

				final double marge = dimNote.getWidth() + halfSuppSpace;
				if (notePosition == NotePosition.LEFT) {
					final Direction strategy = Direction.RIGHT;
					final Point2D pp1 = new Point2D.Double(dimNote.getWidth(), dimNote.getHeight() / 2);
					final Point2D pp2 = new Point2D.Double(marge, dimNote.getHeight() / 2);
					opale.setOpale(strategy, pp1, pp2);
					opale.drawUNewWayINLINED(ug.apply(new UTranslate(0, yForNote)));
				} else {
					final double dx = dimTotal.getWidth() - dimNote.getWidth();
					final Direction strategy = Direction.LEFT;
					final Point2D pp1 = new Point2D.Double(0, dimNote.getHeight() / 2);
					final Point2D pp2 = new Point2D.Double(-halfSuppSpace, dimNote.getHeight() / 2);
					opale.setOpale(strategy, pp1, pp2);
					opale.drawUNewWayINLINED(ug.apply(new UTranslate(dx, yForNote)));

					// note.drawUNewWayINLINED(ug.apply(new UTranslate(dx, yForNote)));
				}
				tile.asTextBlock().drawUNewWayINLINED(ug.apply(new UTranslate(marge, yForFtile)));
			}

			private void drawMissingLink(UGraphic ug, Dimension2D dimTotal, double yForFtile) {
				final FtileVerticalLine line = new FtileVerticalLine(yForFtile, arrowColor);
				line.asTextBlock().drawUNewWayINLINED(ug.apply(new UTranslate(dimTotal.getWidth() / 2 - 1, 0)));
				line.asTextBlock().drawUNewWayINLINED(
						ug.apply(new UTranslate(dimTotal.getWidth() / 2 - 1, dimTotal.getHeight() - yForFtile)));

			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				final Dimension2D dimNote = opale.calculateDimension(stringBounder);
				final Dimension2D dimTile = tile.asTextBlock().calculateDimension(stringBounder);
				final double height = Math.max(dimNote.getHeight(), dimTile.getHeight());
				return new Dimension2DDouble(dimTile.getWidth() + 2 * dimNote.getWidth() + halfSuppSpace * 2, height);
			}

			public List<Url> getUrls() {
				throw new UnsupportedOperationException();
			}
		};
	}

	public boolean isKilled() {
		return tile.isKilled();
	}

}
