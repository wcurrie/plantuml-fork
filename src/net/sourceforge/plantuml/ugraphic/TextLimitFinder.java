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

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.graphic.StringBounder;

public class TextLimitFinder implements UGraphic {

	private final StringBounder stringBounder;

	public TextLimitFinder(StringBounder stringBounder) {
		this.stringBounder = stringBounder;
	}

	private final UParam param = new UParam();

	public StringBounder getStringBounder() {
		return stringBounder;
	}

	public UParam getParam() {
		return param;
	}

	public void draw(double x, double y, UShape shape) {
		if (shape instanceof UText) {
			drawText(x, y, (UText) shape);
		}
	}

	public void centerChar(double x, double y, char c, UFont font) {
		throw new UnsupportedOperationException();
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

	private double maxX;
	private double maxY;
	private double minX;
	private double minY;

	private void addPoint(double x, double y) {
		this.maxX = Math.max(x, maxX);
		this.maxY = Math.max(y, maxY);
		this.minX = Math.min(x, minX);
		this.minY = Math.min(y, minY);
	}

	private void drawText(double x, double y, UText text) {
		final Dimension2D dim = stringBounder.calculateDimension(text.getFontConfiguration().getFont(), text.getText());
		y -= dim.getHeight() - 1.5;
		addPoint(x, y);
		addPoint(x, y + dim.getHeight());
		addPoint(x + dim.getWidth(), y);
		addPoint(x + dim.getWidth(), y + dim.getHeight());
	}

	public double getMaxX() {
		return maxX;
	}

	public double getMaxY() {
		return maxY;
	}

	public double getMinX() {
		return minX;
	}

	public double getMinY() {
		return minY;
	}

}
