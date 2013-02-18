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
 * Revision $Revision: 5183 $
 *
 */
package net.sourceforge.plantuml.ugraphic;

import java.io.IOException;
import java.io.OutputStream;

import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.graphic.StringBounder;

public class SlotFinder implements UGraphic {

	private final SlotSet yslot = new SlotSet();
	private final StringBounder stringBounder;

	public SlotFinder(StringBounder stringBounder) {
		this.stringBounder = stringBounder;
	}

	public StringBounder getStringBounder() {
		return stringBounder;
	}

	private final UParam param = new UParam();

	public UParam getParam() {
		return param;
	}

	public void draw(double x, double y, UShape shape) {
		if (shape instanceof URectangle) {
			drawRectangle(x, y, (URectangle) shape);
		} else if (shape instanceof UPolygon) {
			drawPolygon(x, y, (UPolygon) shape);
		} else if (shape instanceof UEllipse) {
			drawEllipse(x, y, (UEllipse) shape);
		}
	}

	private void drawEllipse(double x, double y, UEllipse shape) {
		yslot.addSlot(y, y + shape.getHeight());
	}

	private void drawPolygon(double x, double y, UPolygon shape) {
		yslot.addSlot(y + shape.getMinY(), y + shape.getMaxY());
	}

	private void drawRectangle(double x, double y, URectangle shape) {
		yslot.addSlot(y, y + shape.getHeight());
	}

	public void centerChar(double x, double y, char c, UFont font) {
	}

	public void translate(double dx, double dy) {
		throw new UnsupportedOperationException();
	}

	public void setTranslate(double dx, double dy) {
		throw new UnsupportedOperationException();
	}

	public double getTranslateX() {
		throw new UnsupportedOperationException();
	}

	public double getTranslateY() {
		throw new UnsupportedOperationException();
	}

	public void writeImage(OutputStream os, String metadata, int dpi) throws IOException {
		throw new UnsupportedOperationException();
	}

	public void setClip(UClip clip) {
		throw new UnsupportedOperationException();
	}

	public void setAntiAliasing(boolean trueForOn) {
		throw new UnsupportedOperationException();
	}

	public ColorMapper getColorMapper() {
		throw new UnsupportedOperationException();
	}

	public void startUrl(Url url) {
		throw new UnsupportedOperationException();
	}

	public void closeAction() {
		throw new UnsupportedOperationException();
	}

	public UGroup createGroup() {
		throw new UnsupportedOperationException();
	}

	public SlotSet getYSlotSet() {
		return yslot;
	}

}
