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
 * Revision $Revision: 10104 $
 *
 */
package net.sourceforge.plantuml.ugraphic;

import net.sourceforge.plantuml.ugraphic.txt.UGraphicTxt;

public abstract class AbstractCommonUGraphic extends UGraphic {

	private/* final */UParam param;
	private double dx;
	private double dy;

	private final ColorMapper colorMapper;
	private UClip clip;

	@Override
	public UGraphic apply(UChange change) {
		final AbstractCommonUGraphic copy = copyUGraphic();
		if (change instanceof UTranslate) {
			final double x = ((UTranslate) change).getDx();
			final double y = ((UTranslate) change).getDy();
			copy.dx += x;
			copy.dy += y;
		} else if (change instanceof UClip) {
			copy.clip = (UClip) change;
			copy.clip = copy.clip.translate(getTranslateXTOBEREMOVED(), getTranslateYTOBEREMOVED());
		}
		return copy;
	}

	final public UClip getClip() {
		return clip;
	}

	public AbstractCommonUGraphic(ColorMapper colorMapper) {
		this.colorMapper = colorMapper;
		this.param = new UParam();
	}

	protected AbstractCommonUGraphic(AbstractCommonUGraphic other) {
		this.colorMapper = other.colorMapper;
		this.dx = other.dx;
		this.dy = other.dy;
		this.param = other.param.copy();
		this.clip = other.clip;
	}

	protected abstract AbstractCommonUGraphic copyUGraphic();

	final public UParam getParam() {
		return param;
	}

	final public void setTranslateTOBEREMOVED(double dx, double dy) {
		if (this instanceof UGraphicTxt == false) {
			throw new UnsupportedOperationException();
		}
		this.dx = dx;
		this.dy = dy;
	}

	final protected double getTranslateXTOBEREMOVED() {
		return dx;
	}

	final protected double getTranslateYTOBEREMOVED() {
		return dy;
	}

	final public ColorMapper getColorMapper() {
		return colorMapper;
	}
}
