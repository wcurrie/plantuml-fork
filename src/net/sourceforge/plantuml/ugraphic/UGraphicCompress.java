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

public class UGraphicCompress implements UGraphic {

	private final UGraphic ug;
	private final CompressionTransform compressionTransform;

	public UGraphicCompress(UGraphic ug, CompressionTransform compressionTransform) {
		this.ug = ug;
		this.compressionTransform = compressionTransform;
	}

	public StringBounder getStringBounder() {
		return ug.getStringBounder();
	}

	public UParam getParam() {
		return ug.getParam();
	}

	public void draw(double x, double y, UShape shape) {
		if (shape instanceof ULine) {
			drawLine(x, y, (ULine) shape);
		} else {
			ug.draw(x, ct(y), shape);
		}
	}

	private void drawLine(double x, double y, ULine shape) {
		drawLine(x, ct(y), x + shape.getDX(), ct(y + shape.getDY()));
	}

	private double ct(double v) {
		return compressionTransform.transform(v);
	}

	private void drawLine(double x1, double y1, double x2, double y2) {
		final double xmin = Math.min(x1, x2);
		final double xmax = Math.max(x1, x2);
		final double ymin = Math.min(y1, y2);
		final double ymax = Math.max(y1, y2);
		ug.draw(xmin, ymin, new ULine(xmax - xmin, ymax - ymin));
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
		ug.writeImage(os, metadata, dpi);
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
