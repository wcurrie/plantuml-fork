/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
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
 * Revision $Revision: 4636 $
 *
 */
package net.sourceforge.plantuml.sequencediagram.teoz;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.sequencediagram.ParticipantEnglober;
import net.sourceforge.plantuml.sequencediagram.ParticipantType;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.SimpleContext2D;
import net.sourceforge.plantuml.skin.Skin;
import net.sourceforge.plantuml.ugraphic.UGraphic;

public class LivingSpace {

	private final Participant p;
	private final Skin skin;
	private final ISkinParam skinParam;
	private final ComponentType headType;
	private final ComponentType tailType;

	private final LivingSpace previous;
	private LivingSpace next;

	private final Real posB;
	private Real posC;
	private Real posD;

	@Override
	public String toString() {
		return p.getCode() + " B=" + posB.getCurrentValue() + "/C=" + currentValue(posC) + "/D=" + currentValue(posD);
	}

	private static String currentValue(Real pos) {
		if (pos == null) {
			return null;
		}
		return "" + pos.getCurrentValue();
	}

	public LivingSpace(Participant p, ParticipantEnglober englober, Skin skin, ISkinParam skinParam, Real position,
			LivingSpace previous) {
		this.p = p;
		this.skin = skin;
		this.skinParam = skinParam;
		this.posB = position;
		this.previous = previous;
		if (p.getType() == ParticipantType.PARTICIPANT) {
			headType = ComponentType.PARTICIPANT_HEAD;
			tailType = ComponentType.PARTICIPANT_TAIL;
		} else if (p.getType() == ParticipantType.ACTOR) {
			headType = ComponentType.ACTOR_HEAD;
			tailType = ComponentType.ACTOR_TAIL;
		} else if (p.getType() == ParticipantType.BOUNDARY) {
			headType = ComponentType.BOUNDARY_HEAD;
			tailType = ComponentType.BOUNDARY_TAIL;
		} else if (p.getType() == ParticipantType.CONTROL) {
			headType = ComponentType.CONTROL_HEAD;
			tailType = ComponentType.CONTROL_TAIL;
		} else if (p.getType() == ParticipantType.ENTITY) {
			headType = ComponentType.ENTITY_HEAD;
			tailType = ComponentType.ENTITY_TAIL;
		} else if (p.getType() == ParticipantType.DATABASE) {
			headType = ComponentType.DATABASE_HEAD;
			tailType = ComponentType.DATABASE_TAIL;
		} else {
			throw new IllegalArgumentException();
		}

	}

	public void drawHead(UGraphic ug) {
		final Component comp = skin.createComponent(headType, null, skinParam, p.getDisplay(false));
		final Dimension2D dim = comp.getPreferredDimension(ug.getStringBounder());
		final Area area = new Area(dim);
		comp.drawU(ug, area, new SimpleContext2D(false));
	}

	public Dimension2D getHeadPreferredDimension(StringBounder stringBounder) {
		final Component comp = skin.createComponent(headType, null, skinParam, p.getDisplay(false));
		final Dimension2D dim = comp.getPreferredDimension(stringBounder);
		return dim;
	}

	private double getPreferredWidth(StringBounder stringBounder) {
		return getHeadPreferredDimension(stringBounder).getWidth();
	}

	public Real getPosC(StringBounder stringBounder) {
		if (posC == null) {
			this.posC = posB.addFixed(this.getPreferredWidth(stringBounder) / 2);
		}
		return posC;
	}

	public Real getPosD(StringBounder stringBounder) {
		if (posD == null) {
			this.posD = posB.addFixed(this.getPreferredWidth(stringBounder));
		}
		return posD;
	}

	public Real getPosB() {
		return posB;
	}

	public final LivingSpace getNext() {
		return next;
	}

	final void setNext(LivingSpace next) {
		this.next = next;
	}

}
