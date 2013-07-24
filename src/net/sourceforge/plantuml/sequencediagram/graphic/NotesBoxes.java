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
 * Revision $Revision: 10073 $
 *
 */
package net.sourceforge.plantuml.sequencediagram.graphic;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.sequencediagram.InGroupable;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.ugraphic.UGraphic;

final class NotesBoxes extends GraphicalElement implements InGroupable {

	private final List<NoteBox> notes = new ArrayList<NoteBox>();
	private final List<ParticipantBox> participants = new ArrayList<ParticipantBox>();

	NotesBoxes(double startingY) {
		super(startingY);
	}

	public void add(NoteBox noteBox, ParticipantBox participantBox) {
		notes.add(noteBox);
		participants.add(participantBox);
	}

	public void ensureConstraints(StringBounder stringBounder, ConstraintSet constraintSet) {
		for (int i = 0; i < notes.size(); i++) {
			final NoteBox noteBox = notes.get(i);
			final ParticipantBox participantBox = participants.get(i);
			final double width = noteBox.getPreferredWidth(stringBounder);
			constraintSet.getConstraintAfter(participantBox).ensureValue(width / 2);
			constraintSet.getConstraintBefore(participantBox).ensureValue(width / 2);
			for (int j = i + 1; j < notes.size(); j++) {
				final NoteBox noteBox2 = notes.get(j);
				final ParticipantBox participantBox2 = participants.get(j);
				final double width2 = noteBox2.getPreferredWidth(stringBounder);
				constraintSet.getConstraint(participantBox, participantBox2).ensureValue((width + width2) / 2);
			}
		}
	}

	public double getMinX(StringBounder stringBounder) {
		double result = Double.MAX_VALUE;
		for (NoteBox n : notes) {
			final double m = n.getMinX(stringBounder);
			if (m < result) {
				result = m;
			}
		}
		return result;
	}

	public double getMaxX(StringBounder stringBounder) {
		double result = -Double.MAX_VALUE;
		for (NoteBox n : notes) {
			final double m = n.getMaxX(stringBounder);
			if (m > result) {
				result = m;
			}
		}
		return result;
	}

	public String toString(StringBounder stringBounder) {
		return toString();
	}

	@Override
	protected void drawInternalU(UGraphic ug, double maxX, Context2D context) {
		for (NoteBox n : notes) {
			n.drawInternalU(ug, maxX, context);
		}
	}

	@Override
	public double getStartingX(StringBounder stringBounder) {
		double result = Double.MAX_VALUE;
		for (NoteBox n : notes) {
			final double m = n.getStartingX(stringBounder);
			if (m < result) {
				result = m;
			}
		}
		return result;
	}

	@Override
	public double getPreferredWidth(StringBounder stringBounder) {
		final double result = getMaxX(stringBounder) - getMinX(stringBounder);
		return result;
	}

	@Override
	public double getPreferredHeight(StringBounder stringBounder) {
		double result = 0;
		for (NoteBox n : notes) {
			final double m = n.getPreferredHeight(stringBounder);
			if (m > result) {
				result = m;
			}
		}
		return result;
	}

}
