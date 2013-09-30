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
 * Revision $Revision: 10266 $
 *
 */
package net.sourceforge.plantuml.svek;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.activitydiagram3.ftile.ULineFlush;
import net.sourceforge.plantuml.activitydiagram3.ftile.ULineMergeable;
import net.sourceforge.plantuml.graphic.UGraphicDelegator;
import net.sourceforge.plantuml.ugraphic.UChange;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UParam;
import net.sourceforge.plantuml.ugraphic.UShape;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class UGraphicLineMerger extends UGraphicDelegator {

	static class MyLine {
		private final Line2D line;
		private final UParam param;

		private MyLine(Line2D line, UParam param) {
			this.line = line;
			this.param = param;
		}

		private MyLine merge(MyLine b) {
			if (this.getX1() == b.getX1() && this.getY1() == b.getY1()) {
				return new MyLine(new Line2D.Double(this.getX2(), this.getY2(), b.getX2(), b.getY2()), param);
			}
			if (this.getX1() == b.getX2() && this.getY1() == b.getY2()) {
				return new MyLine(new Line2D.Double(this.getX2(), this.getY2(), b.getX1(), b.getY1()), param);
			}
			return null;
		}

		private double getX1() {
			return line.getX1();
		}

		private double getY1() {
			return line.getY1();
		}

		private double getX2() {
			return line.getX2();
		}

		private double getY2() {
			return line.getY2();
		}

		public String toString() {
			return "" + getX1() + "-" + getY1() + " --> " + getX2() + "-" + getY2();
		}

	}

	private final double dx;
	private final double dy;
	private List<MyLine> pendingLines;

	public UGraphicLineMerger(UGraphic ug) {
		this(ug, 0, 0, new ArrayList<MyLine>());
	}

	public UGraphicLineMerger(UGraphic ug, double dx, double dy, List<MyLine> pendingLine) {
		super(ug);
		this.dx = dx;
		this.dy = dy;
		this.pendingLines = pendingLine;
	}

	public void draw(UShape shape) {
		if (shape instanceof ULineMergeable) {
			final MyLine li = new MyLine(getLine((ULineMergeable) shape), getParam());
			addLine(li);
		} else if (shape instanceof ULineFlush) {
			printPendingLine();
		} else {
			getUg().draw(shape);
		}
	}

	private void addLine(final MyLine myline) {
		if (pendingLines.size() == 0) {
			this.pendingLines.add(myline);
			return;
		}
		final int posLast = pendingLines.size() - 1;
		final MyLine last = pendingLines.get(posLast);
		final MyLine merged = myline.merge(last);
		if (merged == null) {
			pendingLines.add(myline);
		} else {
			pendingLines.set(posLast, merged);
		}
	}

	private void printPendingLine() {
		for (MyLine line : pendingLines) {
			printLine(getUg().apply(new UTranslate(-dx, -dy)), line);
		}
		this.pendingLines.clear();
	}

	private static void printLine(UGraphic ug, MyLine line) {
		// ug = ug.apply(new UChangeColor(HtmlColorUtils.BLACK)).apply(new UStroke());
		//
		// final ULine uline = new ULine(line.getX2() - line.getX1(), line.getY2() - line.getY1());
		// ug.apply(new UTranslate(line.getX1(), line.getY1())).draw(uline);

		ug = ug.apply(new UChangeColor(line.param.getColor())).apply(line.param.getStroke());
		final ULine hline = new ULine(line.getX2() - line.getX1(), 0);
		final ULine vline = new ULine(0, line.getY2() - line.getY1());
		ug.apply(new UTranslate(line.getX1(), line.getY1())).draw(hline);
		ug.apply(new UTranslate(line.getX2(), line.getY1())).draw(vline);
	}

	private Line2D getLine(ULineMergeable line) {
		return new Line2D.Double(dx, dy, dx + line.getDX(), dy + line.getDY());
	}

	public UGraphic apply(UChange change) {
		double newdx = dx;
		double newdy = dy;
		if (change instanceof UTranslate) {
			newdx += ((UTranslate) change).getDx();
			newdy += ((UTranslate) change).getDy();
		}
		return new UGraphicLineMerger(getUg().apply(change), newdx, newdy, pendingLines);
	}

}