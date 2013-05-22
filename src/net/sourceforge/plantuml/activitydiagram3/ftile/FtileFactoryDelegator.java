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
package net.sourceforge.plantuml.activitydiagram3.ftile;

import java.util.List;
import java.util.Set;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.activitydiagram3.LinkRendering;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.skin.rose.Rose;

public class FtileFactoryDelegator implements FtileFactory {

	private final FtileFactory factory;
	private final ISkinParam skinParam;
	private final Rose rose = new Rose();

	protected HtmlColor getInLinkRenderingColor(Ftile tile) {
		final HtmlColor color;
		final LinkRendering linkRendering = tile.getInLinkRendering();
		if (linkRendering == null || linkRendering.getColor() == null) {
			color = rose.getHtmlColor(getSkinParam(), ColorParam.activityArrow);
		} else {
			color = linkRendering.getColor();
		}
		return color;
	}

	public FtileFactoryDelegator(FtileFactory factory, ISkinParam skinParam) {
		this.factory = factory;
		this.skinParam = skinParam;
	}

	public Ftile start(Swimlane swimlane) {
		return factory.start(swimlane);
	}

	public Ftile stop(Swimlane swimlane) {
		return factory.stop(swimlane);
	}

	public Ftile activity(Display label, HtmlColor color, Swimlane swimlane) {
		return factory.activity(label, color, swimlane);
	}

	public Ftile addNote(Ftile ftile, Display note, NotePosition notePosition) {
		return factory.addNote(ftile, note, notePosition);
	}

	public Ftile decorateIn(Ftile ftile, LinkRendering linkRendering) {
		return factory.decorateIn(ftile, linkRendering);
	}

	public Ftile decorateOut(Ftile ftile, LinkRendering linkRendering) {
		return factory.decorateOut(ftile, linkRendering);
	}

	public Ftile assembly(Ftile tile1, Ftile tile2) {
		return factory.assembly(tile1, tile2);
	}

	public Ftile repeat(Ftile repeat, Display test) {
		return factory.repeat(repeat, test);
	}

	public Ftile createWhile(Ftile whileBlock, Display test, Display yes, Display out, LinkRendering afterEndwhile) {
		return factory.createWhile(whileBlock, test, yes, out, afterEndwhile);
	}

	public Ftile createIf(Ftile tile1, Ftile tile2, Display labelTest, Display label1, Display label2) {
		return factory.createIf(tile1, tile2, labelTest, label1, label2);
	}

	public Ftile createFork(List<Ftile> all) {
		return factory.createFork(all);
	}

	public Ftile createSplit(List<Ftile> all) {
		return factory.createSplit(all);
	}

	public Ftile createGroup(Ftile list, Display name) {
		return factory.createGroup(list, name);
	}

	public StringBounder getStringBounder() {
		return factory.getStringBounder();
	}

	protected final ISkinParam getSkinParam() {
		return skinParam;
	}

	protected final Rose getRose() {
		return rose;
	}

}
