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
 * Revision $Revision: 8033 $
 *
 */
package net.sourceforge.plantuml.ugraphic;

import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.graphic.StringBounder;

public abstract class AbstractUGraphicHorizontalLine implements UGraphic {

	private final UGraphic ug;

	public AbstractUGraphicHorizontalLine(UGraphic ug) {
		this.ug = ug;
	}

	public StringBounder getStringBounder() {
		return ug.getStringBounder();
	}

	public UParam getParam() {
		return ug.getParam();
	}
	
	protected abstract void drawHline(UGraphic ug, double x, double y, UHorizontalLine line);

	public void draw(double x, double y, UShape shape) {
		if (shape instanceof UHorizontalLine) {
			drawHline(ug, x, y, (UHorizontalLine) shape);
		} else {
			ug.draw(x, y, shape);
		}
	}

	public void centerChar(double x, double y, char c, UFont font) {
		ug.centerChar(x, y, c, font);
	}

	public void translate(double dx, double dy) {
		ug.translate(dx, dy);
	}

	public void setTranslate(double dx, double dy) {
		ug.setTranslate(dx, dy);
	}

	public double getTranslateX() {
		return ug.getTranslateX();
	}

	public double getTranslateY() {
		return ug.getTranslateY();
	}

	public void setClip(UClip clip) {
		ug.setClip(clip);
	}

	public void setAntiAliasing(boolean trueForOn) {
		ug.setAntiAliasing(trueForOn);
	}

	public ColorMapper getColorMapper() {
		return ug.getColorMapper();
	}

	public void startUrl(Url url) {
		ug.startUrl(url);
	}

	public void closeAction() {
		ug.closeAction();
	}

	public UGroup createGroup() {
		return ug.createGroup();
	}

}
