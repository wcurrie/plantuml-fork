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
 * Revision $Revision: 12495 $
 *
 */
package net.sourceforge.plantuml.sequencediagram.graphic;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.SkinParamBackcolored;
import net.sourceforge.plantuml.SkinParamBackcoloredReference;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.sequencediagram.Delay;
import net.sourceforge.plantuml.sequencediagram.Divider;
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.sequencediagram.GroupingLeaf;
import net.sourceforge.plantuml.sequencediagram.GroupingStart;
import net.sourceforge.plantuml.sequencediagram.GroupingType;
import net.sourceforge.plantuml.sequencediagram.HSpace;
import net.sourceforge.plantuml.sequencediagram.InGroupableList;
import net.sourceforge.plantuml.sequencediagram.LifeEvent;
import net.sourceforge.plantuml.sequencediagram.LifeEventType;
import net.sourceforge.plantuml.sequencediagram.Message;
import net.sourceforge.plantuml.sequencediagram.MessageExo;
import net.sourceforge.plantuml.sequencediagram.Newpage;
import net.sourceforge.plantuml.sequencediagram.Note;
import net.sourceforge.plantuml.sequencediagram.NoteStyle;
import net.sourceforge.plantuml.sequencediagram.Notes;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.sequencediagram.ParticipantEnglober;
import net.sourceforge.plantuml.sequencediagram.ParticipantEngloberContexted;
import net.sourceforge.plantuml.sequencediagram.ParticipantType;
import net.sourceforge.plantuml.sequencediagram.Reference;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.Skin;

class DrawableSetInitializer {

	private ComponentType defaultLineType;
	private final DrawableSet drawableSet;
	private final boolean showTail;

	private double freeX = 0;
	private Frontier freeY2 = null;
	private Frontier lastFreeY2 = null;

	private final double autonewpage;

	private ConstraintSet constraintSet;

	public DrawableSetInitializer(Skin skin, ISkinParam skinParam, boolean showTail, double autonewpage) {
		this.drawableSet = new DrawableSet(skin, skinParam);
		this.showTail = showTail;
		this.autonewpage = autonewpage;

	}

	private boolean useContinueLineBecauseOfDelay() {
		final String strategy = drawableSet.getSkinParam().getValue("lifelineStrategy");
		if ("nosolid".equalsIgnoreCase(strategy)) {
			return false;
		}
		for (Event ev : drawableSet.getAllEvents()) {
			if (ev instanceof Delay) {
				return true;
			}
		}
		return false;
	}

	private ParticipantRange getFullParticipantRange() {
		return new ParticipantRange(0, drawableSet.getAllParticipants().size());
	}

	private ParticipantRange getParticipantRange(Event ev) {
		return getFullParticipantRange();
	}

	private int getParticipantRangeIndex(Participant participant) {
		int r = 0;
		for (Participant p : drawableSet.getAllParticipants()) {
			r++;
			if (p == participant) {
				return r;
			}
		}
		throw new IllegalArgumentException();
	}

	public DrawableSet createDrawableSet(StringBounder stringBounder) {
		if (freeY2 != null) {
			throw new IllegalStateException();
		}

		this.defaultLineType = useContinueLineBecauseOfDelay() ? ComponentType.CONTINUE_LINE
				: ComponentType.PARTICIPANT_LINE;

		for (Participant p : drawableSet.getAllParticipants()) {
			prepareParticipant(stringBounder, p);
		}

		this.freeY2 = new FrontierStackImpl(drawableSet.getHeadHeight(stringBounder), drawableSet.getAllParticipants()
				.size());

		this.lastFreeY2 = this.freeY2;

		drawableSet.setTopStartingY(this.freeY2.getFreeY(getFullParticipantRange()));

		for (Participant p : drawableSet.getAllParticipants()) {
			final LivingParticipantBox living = drawableSet.getLivingParticipantBox(p);
			for (int i = 0; i < p.getInitialLife(); i++) {
				living.getLifeLine().addSegmentVariation(LifeSegmentVariation.LARGER,
						freeY2.getFreeY(getFullParticipantRange()), p.getLiveSpecificBackColor());
			}
		}

		final List<ParticipantBox> col = new ArrayList<ParticipantBox>();
		for (LivingParticipantBox livingParticipantBox : drawableSet.getAllLivingParticipantBox()) {
			col.add(livingParticipantBox.getParticipantBox());
		}

		constraintSet = new ConstraintSet(col, freeX);

		for (Event ev : new ArrayList<Event>(drawableSet.getAllEvents())) {
			final ParticipantRange range = getParticipantRange(ev);
			final double diffY = freeY2.getFreeY(range) - lastFreeY2.getFreeY(range);
			// final double diffY = freeY2.diff(lastFreeY2);
			if (autonewpage > 0 && diffY > 0 && diffY + getTotalHeight(0, stringBounder) > autonewpage) {
				prepareNewpageSpecial(stringBounder, new Newpage(null), ev, range);
			}
			if (ev instanceof MessageExo) {
				prepareMessageExo(stringBounder, (MessageExo) ev, range);
			} else if (ev instanceof Message) {
				prepareMessage(stringBounder, (Message) ev, range);
			} else if (ev instanceof Note) {
				prepareNote(stringBounder, (Note) ev, range);
			} else if (ev instanceof Notes) {
				prepareNotes(stringBounder, (Notes) ev, range);
			} else if (ev instanceof LifeEvent) {
				prepareLiveEvent(stringBounder, (LifeEvent) ev);
			} else if (ev instanceof GroupingLeaf) {
				prepareGroupingLeaf(stringBounder, (GroupingLeaf) ev, range);
			} else if (ev instanceof GroupingStart) {
				prepareGroupingStart(stringBounder, (GroupingStart) ev, range);
			} else if (ev instanceof Newpage) {
				prepareNewpage(stringBounder, (Newpage) ev, range);
			} else if (ev instanceof Divider) {
				prepareDivider(stringBounder, (Divider) ev, range);
			} else if (ev instanceof HSpace) {
				prepareHSpace(stringBounder, (HSpace) ev, range);
			} else if (ev instanceof Delay) {
				prepareDelay(stringBounder, (Delay) ev, col, range);
			} else if (ev instanceof Reference) {
				prepareReference(stringBounder, (Reference) ev, range);
			} else {
				throw new IllegalStateException();
			}
		}

		constraintSet.takeConstraintIntoAccount(stringBounder);
		takeParticipantEngloberTitleWidth3(stringBounder);

		prepareMissingSpace(stringBounder);

		drawableSet.setDimension(new Dimension2DDouble(freeX, getTotalHeight(
				freeY2.getFreeY(getFullParticipantRange()), stringBounder)));
		return drawableSet;
	}

	private void takeParticipantEngloberTitleWidth3(StringBounder stringBounder) {
		for (ParticipantEngloberContexted pe : drawableSet.getExistingParticipantEnglober()) {
			final double preferredWidth = drawableSet.getEngloberPreferedWidth(stringBounder,
					pe.getParticipantEnglober());
			final ParticipantBox first = drawableSet.getLivingParticipantBox(pe.getFirst2()).getParticipantBox();
			final ParticipantBox last = drawableSet.getLivingParticipantBox(pe.getLast2()).getParticipantBox();
			final double x1 = drawableSet.getX1(pe);
			final double x2 = drawableSet.getX2(stringBounder, pe);
			final double missing = preferredWidth - (x2 - x1);
			if (missing > 0) {
				constraintSet.pushToLeftParticipantBox(missing / 2, first, true);
				constraintSet.pushToLeftParticipantBox(missing / 2, last, false);
			}
		}
	}

	private double getTotalHeight(double y, StringBounder stringBounder) {
		final double signature = 0;
		return y + drawableSet.getTailHeight(stringBounder, showTail) + signature;
	}

	public double getYposition(StringBounder stringBounder, Newpage newpage) {
		if (newpage == null) {
			throw new IllegalArgumentException();
		}
		final GraphicalNewpage graphicalNewpage = (GraphicalNewpage) drawableSet.getEvent(newpage);
		return graphicalNewpage.getStartingY();
	}

	private void prepareMissingSpace(StringBounder stringBounder) {
		freeX = constraintSet.getMaxX();

		double missingSpace1 = 0;
		double missingSpace2 = 0;

		for (GraphicalElement ev : drawableSet.getAllGraphicalElements()) {
			if (ev instanceof GraphicalDelayText) {
				final double missing = ev.getPreferredWidth(stringBounder) - freeX;
				if (missing > 0) {
					missingSpace1 = Math.max(missingSpace1, missing / 2);
					missingSpace2 = Math.max(missingSpace2, missing / 2);
				}
				continue;
			}
			final double startX = ev.getStartingX(stringBounder);
			final double delta1 = -startX;
			if (delta1 > missingSpace1) {
				missingSpace1 = delta1;
			}
			if (ev instanceof Arrow) {
				final Arrow a = (Arrow) ev;
				a.setMaxX(freeX);
			}
			double width = ev.getPreferredWidth(stringBounder);
			if (ev instanceof Arrow) {
				final Arrow a = (Arrow) ev;
				if (width < a.getActualWidth(stringBounder)) {
					width = a.getActualWidth(stringBounder);
				}
			}
			if (ev instanceof GroupingGraphicalElementHeader) {
				final GroupingGraphicalElementHeader gh = (GroupingGraphicalElementHeader) ev;
				if (width < gh.getActualWidth(stringBounder)) {
					width = gh.getActualWidth(stringBounder);
				}
			}
			final double endX = startX + width;
			final double delta2 = endX - freeX;
			if (delta2 > missingSpace2) {
				missingSpace2 = delta2;
			}
		}

		if (missingSpace1 > 0) {
			constraintSet.pushToLeft(missingSpace1);
		}
		freeX = constraintSet.getMaxX() + missingSpace2;
	}

	private void prepareNewpage(StringBounder stringBounder, Newpage newpage, ParticipantRange range) {
		final GraphicalNewpage graphicalNewpage = new GraphicalNewpage(freeY2.getFreeY(range), drawableSet.getSkin()
				.createComponent(ComponentType.NEWPAGE, null, drawableSet.getSkinParam(), null));
		this.lastFreeY2 = freeY2;
		freeY2 = freeY2.add(graphicalNewpage.getPreferredHeight(stringBounder), range);
		drawableSet.addEvent(newpage, graphicalNewpage);
	}

	private void prepareNewpageSpecial(StringBounder stringBounder, Newpage newpage, Event justBefore,
			ParticipantRange range) {
		final GraphicalNewpage graphicalNewpage = new GraphicalNewpage(freeY2.getFreeY(range), drawableSet.getSkin()
				.createComponent(ComponentType.NEWPAGE, null, drawableSet.getSkinParam(), null));
		this.lastFreeY2 = freeY2;
		freeY2 = freeY2.add(graphicalNewpage.getPreferredHeight(stringBounder), range);
		drawableSet.addEvent(newpage, graphicalNewpage, justBefore);
	}

	private void prepareDivider(StringBounder stringBounder, Divider divider, ParticipantRange range) {
		final GraphicalDivider graphicalDivider = new GraphicalDivider(freeY2.getFreeY(range), drawableSet.getSkin()
				.createComponent(ComponentType.DIVIDER, null, drawableSet.getSkinParam(), divider.getText()));
		freeY2 = freeY2.add(graphicalDivider.getPreferredHeight(stringBounder), range);
		drawableSet.addEvent(divider, graphicalDivider);
	}

	private void prepareHSpace(StringBounder stringBounder, HSpace hspace, ParticipantRange range) {
		final GraphicalHSpace graphicalHSpace = new GraphicalHSpace(freeY2.getFreeY(range), hspace.getPixel());
		freeY2 = freeY2.add(graphicalHSpace.getPreferredHeight(stringBounder), range);
		drawableSet.addEvent(hspace, graphicalHSpace);
	}

	private void prepareDelay(StringBounder stringBounder, Delay delay, List<ParticipantBox> participants,
			ParticipantRange range) {
		final Component compText = drawableSet.getSkin().createComponent(ComponentType.DELAY_TEXT, null,
				drawableSet.getSkinParam(), delay.getText());
		final ParticipantBox first = participants.get(0);
		final ParticipantBox last = participants.get(participants.size() - 1);
		final GraphicalDelayText graphicalDivider = new GraphicalDelayText(freeY2.getFreeY(range), compText, first,
				last);
		for (ParticipantBox p : participants) {
			p.addDelay(graphicalDivider);
		}
		freeY2 = freeY2.add(graphicalDivider.getPreferredHeight(stringBounder), range);
		drawableSet.addEvent(delay, graphicalDivider);
	}

	final private InGroupablesStack inGroupableStack = new InGroupablesStack();

	private void prepareGroupingStart(StringBounder stringBounder, GroupingStart m, ParticipantRange range) {
		if (m.getType() != GroupingType.START) {
			throw new IllegalStateException();
		}
		final ISkinParam skinParam = new SkinParamBackcolored(drawableSet.getSkinParam(), m.getBackColorElement(),
				m.getBackColorGeneral());

		final Component comp = drawableSet.getSkin().createComponent(ComponentType.GROUPING_SPACE, null, skinParam,
				Display.create(m.getComment()));
		final double preferredHeight = comp.getPreferredHeight(stringBounder);
		freeY2 = freeY2.add(preferredHeight, range);

		final Display strings = m.getTitle().equals("group") ? Display.create(m.getComment()) : Display.create(
				m.getTitle(), m.getComment());
		final Component header = drawableSet.getSkin().createComponent(ComponentType.GROUPING_HEADER, null, skinParam,
				strings);
		final ParticipantBox veryfirst = drawableSet.getVeryfirst();
		final InGroupableList inGroupableList = new InGroupableList(veryfirst, m, freeY2.getFreeY(range));
		inGroupableStack.addList(inGroupableList);

		final GraphicalElement element = new GroupingGraphicalElementHeader(freeY2.getFreeY(range), header,
				inGroupableList, m.isParallel());
		inGroupableList.setMinWidth(element.getPreferredWidth(stringBounder));
		freeY2 = freeY2.add(element.getPreferredHeight(stringBounder), range);
		drawableSet.addEvent(m, element);
		if (m.isParallel()) {
			freeY2 = ((FrontierStack) freeY2).openBar();
		}

	}

	private void prepareGroupingLeaf(StringBounder stringBounder, final GroupingLeaf m, ParticipantRange range) {
		final GraphicalElement element;
		final ISkinParam skinParam = new SkinParamBackcolored(drawableSet.getSkinParam(), null, m.getBackColorGeneral());
		if (m.getType() == GroupingType.ELSE) {
			if (m.isParallel()) {
				freeY2 = ((FrontierStack) freeY2).restore();
			}
			final Component compElse = drawableSet.getSkin().createComponent(ComponentType.GROUPING_ELSE, null,
					skinParam, Display.create(m.getComment()));
			final Lazy lazy = new Lazy() {
				public double getNow() {
					final GraphicalElement after = drawableSet.getEvent(m.getJustAfter());
					if (after == null) {
						return 0;
					}
					return after.getStartingY();
				}
			};
			element = new GroupingGraphicalElementElse(freeY2.getFreeY(range), compElse,
					inGroupableStack.getTopGroupingStructure(), m.isParallel(), lazy);
			final double preferredHeight = element.getPreferredHeight(stringBounder);
			freeY2 = freeY2.add(preferredHeight, range);
		} else if (m.getType() == GroupingType.END) {
			if (m.isParallel()) {
				freeY2 = ((FrontierStack) freeY2).closeBar();
			}
			final GroupingGraphicalElementHeader groupingHeaderStart = (GroupingGraphicalElementHeader) drawableSet
					.getEvent(m.getGroupingStart());
			if (groupingHeaderStart != null) {
				groupingHeaderStart.setEndY(freeY2.getFreeY(range));
			}
			element = new GroupingGraphicalElementTail(freeY2.getFreeY(range),
					inGroupableStack.getTopGroupingStructure(), m.isParallel());
			final Component comp = drawableSet.getSkin().createComponent(ComponentType.GROUPING_SPACE, null, skinParam,
					Display.create(m.getComment()));
			final double preferredHeight = comp.getPreferredHeight(stringBounder);
			freeY2 = freeY2.add(preferredHeight, range);
			inGroupableStack.pop();
		} else {
			throw new IllegalStateException();
		}
		drawableSet.addEvent(m, element);

	}

	private void prepareNote(StringBounder stringBounder, Note n, ParticipantRange range) {
		final NoteBox noteBox = createNoteBox(stringBounder, n, range);
		inGroupableStack.addElement(noteBox);

		drawableSet.addEvent(n, noteBox);
		freeY2 = freeY2.add(noteBox.getPreferredHeight(stringBounder), range);
	}

	private NoteBox createNoteBox(StringBounder stringBounder, Note n, ParticipantRange range) {
		LivingParticipantBox p1 = drawableSet.getLivingParticipantBox(n.getParticipant());
		LivingParticipantBox p2;
		if (n.getParticipant2() == null) {
			p2 = null;
		} else {
			p2 = drawableSet.getLivingParticipantBox(n.getParticipant2());
			if (p1.getParticipantBox().getCenterX(stringBounder) > p2.getParticipantBox().getCenterX(stringBounder)) {
				final LivingParticipantBox tmp = p1;
				p1 = p2;
				p2 = tmp;
			}
		}
		final ISkinParam skinParam = new SkinParamBackcolored(drawableSet.getSkinParam(), n.getSpecificBackColor());
		final ComponentType type = getNoteComponentType(n.getStyle());
		final NoteBox noteBox = new NoteBox(freeY2.getFreeY(range), drawableSet.getSkin().createComponent(type, null,
				skinParam, n.getStrings()), p1, p2, n.getPosition(), n.getUrl());
		return noteBox;
	}

	private void prepareNotes(StringBounder stringBounder, Notes notes, ParticipantRange range) {
		final NotesBoxes notesBoxes = new NotesBoxes(freeY2.getFreeY(range));
		for (Note n : notes) {
			final NoteBox noteBox = createNoteBox(stringBounder, n, range);
			final ParticipantBox p1 = drawableSet.getLivingParticipantBox(n.getParticipant()).getParticipantBox();
			final ParticipantBox p2 = n.getParticipant2() == null ? null : drawableSet.getLivingParticipantBox(
					n.getParticipant2()).getParticipantBox();
			notesBoxes.add(noteBox, p1, p2);
		}
		notesBoxes.ensureConstraints(stringBounder, constraintSet);
		inGroupableStack.addElement(notesBoxes);

		drawableSet.addEvent(notes, notesBoxes);
		freeY2 = freeY2.add(notesBoxes.getPreferredHeight(stringBounder), range);
	}

	private ComponentType getNoteComponentType(NoteStyle noteStyle) {
		if (noteStyle == NoteStyle.HEXAGONAL) {
			return ComponentType.NOTE_HEXAGONAL;
		}
		if (noteStyle == NoteStyle.BOX) {
			return ComponentType.NOTE_BOX;
		}
		return ComponentType.NOTE;
	}

	private void prepareLiveEvent(StringBounder stringBounder, LifeEvent lifeEvent) {
		if (lifeEvent.getType() != LifeEventType.DESTROY && lifeEvent.getType() != LifeEventType.CREATE) {
			throw new IllegalStateException();
		}
	}

	private void prepareMessage(StringBounder stringBounder, Message m, ParticipantRange range) {
		final Step1Message step1Message = new Step1Message(range, stringBounder, m, drawableSet, freeY2);
		freeY2 = step1Message.prepareMessage(constraintSet, inGroupableStack);
	}

	private void prepareReference(StringBounder stringBounder, Reference reference, ParticipantRange range) {
		final LivingParticipantBox p1 = drawableSet.getLivingParticipantBox(drawableSet.getFirst(reference
				.getParticipant()));
		final LivingParticipantBox p2 = drawableSet.getLivingParticipantBox(drawableSet.getLast(reference
				.getParticipant()));
		final ISkinParam skinParam = new SkinParamBackcoloredReference(drawableSet.getSkinParam(),
				reference.getBackColorElement(), reference.getBackColorGeneral());

		Display strings = Display.empty();
		strings = strings.add("ref");
		strings = strings.addAll(reference.getStrings());
		final Component comp = drawableSet.getSkin().createComponent(ComponentType.REFERENCE, null, skinParam, strings);
		final GraphicalReference graphicalReference = new GraphicalReference(freeY2.getFreeY(range), comp, p1, p2,
				reference.getUrl());

		final ParticipantBox pbox1 = p1.getParticipantBox();
		final ParticipantBox pbox2 = p2.getParticipantBox();
		final double width = graphicalReference.getPreferredWidth(stringBounder)
				- pbox1.getPreferredWidth(stringBounder) / 2 - pbox2.getPreferredWidth(stringBounder) / 2;

		final Constraint constraint;
		if (p1 == p2) {
			constraint = constraintSet.getConstraintAfter(pbox1);
		} else {
			constraint = constraintSet.getConstraint(pbox1, pbox2);
		}
		constraint.ensureValue(width);

		inGroupableStack.addElement(graphicalReference);
		inGroupableStack.addElement(p1);
		if (p1 != p2) {
			inGroupableStack.addElement(p2);
		}

		freeY2 = freeY2.add(graphicalReference.getPreferredHeight(stringBounder), range);
		drawableSet.addEvent(reference, graphicalReference);
	}

	private void prepareMessageExo(StringBounder stringBounder, MessageExo m, ParticipantRange range) {
		final Step1MessageExo step1Message = new Step1MessageExo(range, stringBounder, m, drawableSet, freeY2);
		freeY2 = step1Message.prepareMessage(constraintSet, inGroupableStack);
	}

	private void prepareParticipant(StringBounder stringBounder, Participant p) {
		final ComponentType headType;
		final ComponentType tailType;
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
		HtmlColor specificBackColor = p.getSpecificBackColor();
		final boolean clickable = p.getUrl() != null;
		final HtmlColor stereoBackColor = drawableSet.getSkinParam().getHtmlColor(p.getBackgroundColorParam(),
				p.getStereotype(), clickable);
		if (stereoBackColor != null && specificBackColor == null) {
			specificBackColor = stereoBackColor;
		}

		final ISkinParam skinParam = new SkinParamBackcolored(drawableSet.getSkinParam(), specificBackColor, clickable);
		final Display participantDisplay = p.getDisplay(skinParam.forceSequenceParticipantUnderlined());
		final Component head = drawableSet.getSkin().createComponent(headType, null, skinParam, participantDisplay);
		final Component tail = drawableSet.getSkin().createComponent(tailType, null, skinParam, participantDisplay);
		final Component line = drawableSet.getSkin().createComponent(this.defaultLineType, null,
				drawableSet.getSkinParam(), participantDisplay);
		final Component delayLine = drawableSet.getSkin().createComponent(ComponentType.DELAY_LINE, null,
				drawableSet.getSkinParam(), participantDisplay);
		final ParticipantBox box = new ParticipantBox(head, line, tail, delayLine, this.freeX, p.getCode());

		final Component comp = drawableSet.getSkin().createComponent(ComponentType.ALIVE_BOX_CLOSE_CLOSE, null,
				drawableSet.getSkinParam(), null);

		final LifeLine lifeLine = new LifeLine(box, comp.getPreferredWidth(stringBounder), drawableSet.getSkinParam()
				.shadowing());
		drawableSet.setLivingParticipantBox(p, new LivingParticipantBox(box, lifeLine));

		this.freeX = box.getMaxX(stringBounder);
	}

	public void addParticipant(Participant p, ParticipantEnglober participantEnglober) {
		drawableSet.addParticipant(p, participantEnglober);
	}

	public void addEvent(Event event) {
		drawableSet.addEvent(event, null);
	}

}
