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
 * Revision $Revision: 9786 $
 *
 */
package net.sourceforge.plantuml.activitydiagram3;

import java.util.Collections;
import java.util.Set;

import net.sourceforge.plantuml.activitydiagram3.ftile.BoxStyle;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactory;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.sequencediagram.NotePosition;

public class InstructionSimple implements Instruction {

	private final Display label;
	private final HtmlColor color;
	private final LinkRendering inlinkRendering;
	private Display note;
	private NotePosition notePosition;
	private final Swimlane swimlane;
	private final BoxStyle style;

	public InstructionSimple(Display label, HtmlColor color, LinkRendering inlinkRendering, Swimlane swimlane, BoxStyle style) {
		this.style = style;
		this.label = label;
		this.color = color;
		this.inlinkRendering = inlinkRendering;
		this.swimlane = swimlane;
	}

	public Ftile createFtile(FtileFactory factory) {
		final Ftile result = factory.activity(label, color, swimlane, style);
		if (note == null) {
			return result;
		}
		return factory.addNote(result, note, notePosition);
	}

	public void add(Instruction other) {
		throw new UnsupportedOperationException();
	}

	final public boolean kill() {
		return false;
	}

	public LinkRendering getInLinkRendering() {
		return inlinkRendering;
	}

	public void addNote(Display note, NotePosition position) {
		this.note = note;
		this.notePosition = position;
	}

	public Set<Swimlane> getSwimlanes() {
		return swimlane == null ? Collections.<Swimlane> emptySet() : Collections
				.<Swimlane> singleton(swimlane);
	}

}
