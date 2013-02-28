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
 */
package net.sourceforge.plantuml.ugraphic.txt;

import java.awt.geom.Dimension2D;
import java.io.IOException;
import java.io.OutputStream;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.asciiart.TextStringBounder;
import net.sourceforge.plantuml.asciiart.TranslatedCharArea;
import net.sourceforge.plantuml.asciiart.UmlCharArea;
import net.sourceforge.plantuml.asciiart.UmlCharAreaImpl;
import net.sourceforge.plantuml.graphic.FontStyle;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.AbstractCommonUGraphic;
import net.sourceforge.plantuml.ugraphic.ColorMapperIdentity;
import net.sourceforge.plantuml.ugraphic.UClip;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGroup;
import net.sourceforge.plantuml.ugraphic.UGroupNull;
import net.sourceforge.plantuml.ugraphic.UShape;
import net.sourceforge.plantuml.ugraphic.UText;

public class UGraphicTxt extends AbstractCommonUGraphic {

	private /*final*/ UmlCharArea charArea = new UmlCharAreaImpl();
	private int lastPrint = 0;

	@Override
	protected AbstractCommonUGraphic copyUGraphic() {
		// return new UGraphicTxt(this);
		return this;
	}


	private UGraphicTxt(UGraphicTxt other) {
		super(other);
		this.charArea = other.charArea;
		this.lastPrint = other.lastPrint;
	}

	public UGraphicTxt() {
		super(new ColorMapperIdentity());
	}



	public StringBounder getStringBounder() {
		return new TextStringBounder();
	}

	public void drawOldWay(UShape shape) {
		if (shape instanceof UText) {
			final UText txt = (UText) shape;
			charArea.drawStringLR(txt.getText(), 0, lastPrint);
			lastPrint++;
			if (txt.getFontConfiguration().containsStyle(FontStyle.WAVE)) {
				charArea.drawHLine('^', lastPrint, 0, txt.getText().length());
				lastPrint++;
			}
			return;
		}
		throw new UnsupportedOperationException();
	}

	public void centerChar(double x, double y, char c, UFont font) {
		throw new UnsupportedOperationException();
	}

	public final UmlCharArea getCharArea() {
		return new TranslatedCharArea(charArea, (int) getTranslateXTOBEREMOVED(), (int) getTranslateYTOBEREMOVED());
	}

	public void startUrl(Url url) {
	}
	
	public void closeAction() {
	}


	public UGroup createGroup() {
		return new UGroupNull();
	}

	public void writeImage(OutputStream os, String metadata, int dpi) throws IOException {
		throw new UnsupportedOperationException();
	}


	public Dimension2D getDimension() {
		return new Dimension2DDouble(0, 0);
	}

}
