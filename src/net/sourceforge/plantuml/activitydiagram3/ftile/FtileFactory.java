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
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.sequencediagram.NotePosition;

public interface FtileFactory {

	public StringBounder getStringBounder();

	public boolean shadowing();
	
	public Ftile start(Swimlane swimlane);

	public Ftile stop(Swimlane swimlane);

	public Ftile activity(Display label, HtmlColor color, Swimlane swimlane, BoxStyle style);

	public Ftile addNote(Ftile ftile, Display note, NotePosition notePosition);

	public Ftile decorateIn(Ftile ftile, LinkRendering linkRendering);

	public Ftile decorateOut(Ftile ftile, LinkRendering linkRendering);

	public Ftile assembly(Ftile tile1, Ftile tile2);

	public Ftile repeat(Swimlane swimlane, Ftile repeat, Display test);

	public Ftile createWhile(Ftile whileBlock, Display test, Display yes, Display out, LinkRendering afterEndwhile);

	public Ftile createIf(Swimlane swimlane, Ftile tile1, Ftile tile2, Display labelTest, Display label1, Display label2);

	public Ftile createFork(List<Ftile> all);

	public Ftile createSplit(List<Ftile> all);

	public Ftile createGroup(Ftile list, Display name);

}
