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

import net.sourceforge.plantuml.activitydiagram3.LinkRendering;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.HtmlColor;

public interface FtileFactory {

	public Ftile start();

	public Ftile stop();

	public Ftile activity(Display label, HtmlColor color, LinkRendering linkRendering, Display note);

	public Ftile assembly(Ftile tile1, Ftile tile2, LinkRendering linkRendering);

	public Ftile repeat(Ftile repeat, Display test, LinkRendering endRepeatLinkRendering);

	public Ftile createWhile(Ftile whileBlock, Display test, LinkRendering endInlinkRendering,
			LinkRendering afterEndwhile, Display yes, Display out);

	public Ftile createIf(Ftile tile1, Ftile tile2, Display labelTest, Display label1, Display label2,
			LinkRendering endThenInlinkRendering, LinkRendering endElseInlinkRendering);

	public Ftile createFork(List<Ftile> all);

	public Ftile createSplit(List<Ftile> all);

	public Ftile createGroup(Ftile list, Display name);

}
