/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2012, Arnaud Roques
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
 * Revision $Revision: 7847 $
 *
 */
package net.sourceforge.plantuml.sequencediagram.graphic;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.sequencediagram.DiagramPositionsCollector;
import net.sourceforge.plantuml.sequencediagram.Message;
import net.sourceforge.plantuml.sequencediagram.NotePosition;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.skin.*;
import net.sourceforge.plantuml.ugraphic.UGraphic;

class MessageArrow extends Arrow {

	private final LivingParticipantBox p1;
	private final LivingParticipantBox p2;
    private final Message message;

    public MessageArrow(double startingY, Skin skin, Component arrow, LivingParticipantBox p1, LivingParticipantBox p2, Message message) {
		super(startingY, skin, arrow, message.getUrl());
        this.message = message;

        if (p1 == p2) {
			throw new IllegalArgumentException();
		}
		if (p1 == null || p2 == null) {
			throw new IllegalArgumentException();
		}
		this.p1 = p1;
		this.p2 = p2;
	}

	@Override
	public double getActualWidth(StringBounder stringBounder) {
		final double r = getRightEndInternal(stringBounder) - getLeftStartInternal(stringBounder);
		assert r > 0;
		return r;
	}

	private double getLeftStartInternal(StringBounder stringBounder) {
		return getParticipantAt(stringBounder, NotePosition.LEFT).getLiveThicknessAt(stringBounder,
				getArrowYStartLevel(stringBounder)).getSegment().getPos2();
	}

	private double getRightEndInternal(StringBounder stringBounder) {
		return getParticipantAt(stringBounder, NotePosition.RIGHT).getLiveThicknessAt(stringBounder,
				getArrowYStartLevel(stringBounder)).getSegment().getPos1();
	}

	@Override
	public double getPreferredHeight(StringBounder stringBounder) {
		return getArrowComponent().getPreferredHeight(stringBounder);
	}

	@Override
	public double getStartingX(StringBounder stringBounder) {
		return getLeftStartInternal(stringBounder);
	}

	@Override
	public int getDirection(StringBounder stringBounder) {
		final double x1 = p1.getParticipantBox().getCenterX(stringBounder);
		final double x2 = p2.getParticipantBox().getCenterX(stringBounder);
		if (x1 < x2) {
			return 1;
		}
		return -1;
	}

	public LivingParticipantBox getParticipantAt(StringBounder stringBounder, NotePosition position) {
		final int direction = getDirection(stringBounder);
		if (direction == 1 && position == NotePosition.RIGHT) {
			return p2;
		}
		if (direction == 1 && position == NotePosition.LEFT) {
			return p1;
		}
		if (direction == -1 && position == NotePosition.RIGHT) {
			return p1;
		}
		if (direction == -1 && position == NotePosition.LEFT) {
			return p2;
		}
		throw new IllegalArgumentException();
	}

	@Override
	public double getPreferredWidth(StringBounder stringBounder) {
		return getArrowComponent().getPreferredWidth(stringBounder);
	}

	@Override
	protected void drawInternalU(UGraphic ug, double maxX, Context2D context) {
		final StringBounder stringBounder = ug.getStringBounder();
        ug.translate(getStartingX(stringBounder), getStartingY());
        double arrowYStartLevel = getArrowYStartLevel(stringBounder);
        if (message.getArrowConfiguration().isLeftToRightNormal()) {
            DiagramPositionsCollector.getInstance().recordMessage(message.getParticipant1().getCode(), message.getParticipant2().getCode(), arrowYStartLevel, message.getLabel());
        } else {
            DiagramPositionsCollector.getInstance().recordMessage(message.getParticipant2().getCode(), message.getParticipant1().getCode(), arrowYStartLevel, message.getLabel());
        }
		startUrl(ug);
		getArrowComponent().drawU(ug, new Area(getActualDimension(stringBounder)), context);
		endUrl(ug);
	}

	private Dimension2D getActualDimension(StringBounder stringBounder) {
		return new Dimension2DDouble(getActualWidth(stringBounder) - getPaddingArrowHead(), getArrowComponent()
				.getPreferredHeight(stringBounder));
	}

	@Override
	public double getArrowYStartLevel(StringBounder stringBounder) {
		if (getArrowComponent() instanceof ArrowComponent) {
			final ArrowComponent arrowComponent = (ArrowComponent) getArrowComponent();
			final Dimension2D dim = new Dimension2DDouble(arrowComponent.getPreferredWidth(stringBounder),
					arrowComponent.getPreferredHeight(stringBounder));
			return getStartingY() + arrowComponent.getStartPoint(stringBounder, dim).getY();
		}
		return getStartingY();
	}

	@Override
	public double getArrowYEndLevel(StringBounder stringBounder) {
		if (getArrowComponent() instanceof ArrowComponent) {
			final ArrowComponent arrowComponent = (ArrowComponent) getArrowComponent();
			final Dimension2D dim = new Dimension2DDouble(arrowComponent.getPreferredWidth(stringBounder),
					arrowComponent.getPreferredHeight(stringBounder));
			return getStartingY() + arrowComponent.getEndPoint(stringBounder, dim).getY();
		}
		return getStartingY() + getArrowComponent().getPreferredHeight(stringBounder);
	}

	public double getMaxX(StringBounder stringBounder) {
		return getRightEndInternal(stringBounder);
	}

	public double getMinX(StringBounder stringBounder) {
		return getLeftStartInternal(stringBounder);
	}

	public String toString(StringBounder stringBounder) {
		return getMinX(stringBounder) + "-" + getMaxX(stringBounder);
	}

}
