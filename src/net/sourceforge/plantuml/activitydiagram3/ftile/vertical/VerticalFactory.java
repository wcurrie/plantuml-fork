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

import java.util.List;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactory;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.ugraphic.UFont;

public class VerticalFactory implements FtileFactory {

	private final ISkinParam skinParam;
	private final Rose rose = new Rose();

	public VerticalFactory(ISkinParam skinParam) {
		this.skinParam = skinParam;
	}

	public Ftile start() {
		final HtmlColor color = rose.getHtmlColor(skinParam, ColorParam.activityStart);
		return new FtileCircleStart(color);
	}

	public Ftile stop() {
		final HtmlColor color = rose.getHtmlColor(skinParam, ColorParam.activityEnd);
		return new FtileCircleStop(color);
	}

	public Ftile activity(Display label, HtmlColor color) {
		final HtmlColor borderColor = rose.getHtmlColor(skinParam, ColorParam.activityBorder);
		final HtmlColor backColor = color == null ? rose.getHtmlColor(skinParam, ColorParam.activityBackground) : color;
		final UFont font = skinParam.getFont(FontParam.ACTIVITY2, null);
		return new FtileBox(label, borderColor, backColor, font);
	}

	public Ftile assembly(Ftile tile1, Ftile tile2) {
		final Ftile arrow;
		final HtmlColor color = rose.getHtmlColor(skinParam, ColorParam.activityArrow);
		if (tile2 instanceof FtileGroup) {
			arrow = new FtileVerticalLine(15, color);
		} else {
			arrow = new FtileVerticalArrow(35, color);
		}
		Ftile result = new FtileAssemblySimple(tile1, arrow);
		result = new FtileAssemblySimple(result, tile2);
		return result;
	}

	public Ftile repeat(Ftile repeat, Display test) {
		final HtmlColor borderColor = rose.getHtmlColor(skinParam, ColorParam.activityBorder);
		final HtmlColor backColor = rose.getHtmlColor(skinParam, ColorParam.activityBackground);
		final HtmlColor arrowColor = rose.getHtmlColor(skinParam, ColorParam.activityArrow);
		final UFont font = skinParam.getFont(FontParam.ACTIVITY_ARROW2, null);
		return new FtileRepeat(repeat, test, this, borderColor, backColor, arrowColor, font);
	}

	public Ftile createWhile(Ftile whileBlock, Display test) {
		final HtmlColor borderColor = rose.getHtmlColor(skinParam, ColorParam.activityBorder);
		final HtmlColor backColor = rose.getHtmlColor(skinParam, ColorParam.activityBackground);
		final HtmlColor arrowColor = rose.getHtmlColor(skinParam, ColorParam.activityArrow);
		final UFont font = skinParam.getFont(FontParam.ACTIVITY_ARROW2, null);
		return new FtileWhile(whileBlock, test, this, borderColor, backColor, arrowColor, font);
	}

	public Ftile createIf(Ftile tile1, Ftile tile2, Display labelTest, Display label1, Display label2) {
		final HtmlColor borderColor = rose.getHtmlColor(skinParam, ColorParam.activityBorder);
		final HtmlColor backColor = rose.getHtmlColor(skinParam, ColorParam.activityBackground);
		final HtmlColor arrowColor = rose.getHtmlColor(skinParam, ColorParam.activityArrow);
		final UFont font = skinParam.getFont(FontParam.ACTIVITY_ARROW2, null);
		return new FtileIf2(tile1, tile2, labelTest, label1, label2, borderColor, backColor, arrowColor, font);
	}

	public Ftile createFork(List<Ftile> all) {
		final HtmlColor colorBar = rose.getHtmlColor(skinParam, ColorParam.activityBar);
		final HtmlColor arrowColor = rose.getHtmlColor(skinParam, ColorParam.activityArrow);
		return new FtileFork(new FtileForkInner(all, this, arrowColor), colorBar);
	}

	public Ftile createVerticalArrow(double size) {
		final HtmlColor arrowColor = rose.getHtmlColor(skinParam, ColorParam.activityArrow);
		return new FtileVerticalArrow(size, arrowColor);
	}

	public Ftile createGroup(Ftile list, Display name) {
		final HtmlColor arrowColor = rose.getHtmlColor(skinParam, ColorParam.activityArrow);
		return new FtileGroup(list, name, arrowColor);
	}
}
