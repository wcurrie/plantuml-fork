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

import java.util.Arrays;
import java.util.List;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.activitydiagram3.LinkRendering;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileAssemblySimple;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactory;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileBox;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileCircleStart;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.FtileCircleStop;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.ugraphic.UFont;

public class VCompactFactory implements FtileFactory {

	private final ISkinParam skinParam;
	private final Rose rose = new Rose();
	private final StringBounder stringBounder;

	public StringBounder getStringBounder() {
		return stringBounder;
	}

	public VCompactFactory(ISkinParam skinParam, StringBounder stringBounder) {
		this.skinParam = skinParam;
		this.stringBounder = stringBounder;
	}

	public Ftile start() {
		final HtmlColor color = rose.getHtmlColor(skinParam, ColorParam.activityStart);
		return new FtileCircleStart(color);
	}

	public Ftile stop() {
		final HtmlColor color = rose.getHtmlColor(skinParam, ColorParam.activityEnd);
		return new FtileCircleStop(color);
	}

	public Ftile activity(Display label, final HtmlColor color) {
		final HtmlColor borderColor = rose.getHtmlColor(skinParam, ColorParam.activityBorder);
		final HtmlColor backColor = color == null ? rose.getHtmlColor(skinParam, ColorParam.activityBackground) : color;
		final UFont font = skinParam.getFont(FontParam.ACTIVITY2, null);
		return new FtileBox(label, borderColor, backColor, font);
	}

	public Ftile addNote(Ftile ftile, Display note, NotePosition notePosition) {
		return ftile;
	}

	public Ftile assembly(Ftile tile1, Ftile tile2) {
		return new FtileAssemblySimple(tile1, tile2);
	}

	public Ftile repeat(Ftile repeat, Display test) {
		final HtmlColor borderColor = rose.getHtmlColor(skinParam, ColorParam.activityBorder);
		final HtmlColor backColor = rose.getHtmlColor(skinParam, ColorParam.activityBackground);
		final HtmlColor arrowColor = rose.getHtmlColor(skinParam, ColorParam.activityArrow);
		final UFont font = skinParam.getFont(FontParam.ACTIVITY_ARROW2, null);
		final LinkRendering endRepeatLinkRendering = repeat.getOutLinkRendering();
		final HtmlColor endRepeatLinkColor = endRepeatLinkRendering == null ? null : endRepeatLinkRendering.getColor();
		return new FtileRepeat2(repeat, test, font);
	}

	public Ftile createWhile(Ftile whileBlock, Display test, Display yes, Display out) {
		final HtmlColor borderColor = rose.getHtmlColor(skinParam, ColorParam.activityBorder);
		final HtmlColor backColor = rose.getHtmlColor(skinParam, ColorParam.activityBackground);
		final HtmlColor arrowColor = rose.getHtmlColor(skinParam, ColorParam.activityArrow);
		final UFont font = skinParam.getFont(FontParam.ACTIVITY_ARROW2, null);
		final LinkRendering endInlinkRendering = whileBlock.getOutLinkRendering();
		final HtmlColor endInlinkColor = endInlinkRendering == null ? null : endInlinkRendering.getColor();
		return new FtileWhile2(whileBlock, test, borderColor, backColor, arrowColor, font, endInlinkColor, yes, out);
	}

	public Ftile createIf(Ftile tile1, Ftile tile2, Display labelTest, Display label1, Display label2) {
		final HtmlColor borderColor = rose.getHtmlColor(skinParam, ColorParam.activityBorder);
		final HtmlColor backColor = rose.getHtmlColor(skinParam, ColorParam.activityBackground);
		final HtmlColor arrowColor = rose.getHtmlColor(skinParam, ColorParam.activityArrow);
		final UFont font = skinParam.getFont(FontParam.ACTIVITY_ARROW2, null);
		final LinkRendering endThenInlinkRendering = tile1.getOutLinkRendering();
		final LinkRendering endElseInlinkRendering = tile2.getOutLinkRendering();
		final HtmlColor endThenInlinkColor = endThenInlinkRendering == null ? null : endThenInlinkRendering.getColor();
		final HtmlColor endElseInlinkColor = endElseInlinkRendering == null ? null : endElseInlinkRendering.getColor();

		// return new FtileIf2(tile1, tile2, borderColor, backColor);
		return new FtileForkInner2(Arrays.asList(tile1, tile2));
	}

	public Ftile createFork(List<Ftile> all) {
		return new FtileForkInner2(all);
	}

	public Ftile createSplit(List<Ftile> all) {
		return new FtileForkInner2(all);
	}

	public Ftile createGroup(Ftile list, Display name) {
		return list;
	}

	public Ftile decorateIn(final Ftile ftile, final LinkRendering linkRendering) {
		return ftile;
	}

	public Ftile decorateOut(final Ftile ftile, final LinkRendering linkRendering) {
		return ftile;
	}

}
