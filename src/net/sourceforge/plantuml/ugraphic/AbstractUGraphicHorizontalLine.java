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
 * Revision $Revision: 8033 $
 *
 */
package net.sourceforge.plantuml.ugraphic;

import java.io.IOException;
import java.io.OutputStream;

import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.graphic.StringBounder;

public abstract class AbstractUGraphicHorizontalLine implements UGraphic {

	private UGraphic ug;
	private UTranslate translate = new UTranslate();

	public UGraphic apply(UChange change) {
		final AbstractUGraphicHorizontalLine result;
		if (change instanceof UTranslate) {
			result = copy(ug);
			result.translate = this.translate.compose((UTranslate) change);
		} else {
			result = copy(ug.apply(change));
			result.translate = this.translate;
		}
		return result;
	}

	protected abstract AbstractUGraphicHorizontalLine copy(UGraphic ug);

	protected AbstractUGraphicHorizontalLine(UGraphic ug) {
		this.ug = ug;
	}

	public StringBounder getStringBounder() {
		return ug.getStringBounder();
	}

	public UParam getParam() {
		return ug.getParam();
	}

	protected abstract void drawHline(UGraphic ug, UHorizontalLine line, UTranslate translate);

	public void draw(UShape shape) {
		if (shape instanceof UHorizontalLine) {
			drawHline(ug, (UHorizontalLine) shape, new UTranslate(0, translate.getDy()));
		} else {
			ug.apply(translate).draw(shape);
		}
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

	public void writeImage(OutputStream os, String metadata, int dpi) throws IOException {
		throw new UnsupportedOperationException();
	}

}
