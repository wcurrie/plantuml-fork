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
import java.io.IOException;
import java.io.OutputStream;

import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.graphic.StringBounder;

public class TextLimitFinder extends UGraphic {

	static class MinMax {
		private double maxX;
		private double maxY;
		private double minX;
		private double minY;

		MinMax(boolean initToZero) {
			if (initToZero) {
				minX = 0;
				maxX = 0;
				minY = 0;
				maxY = 0;
			} else {
				minX = Double.MAX_VALUE;
				maxX = -Double.MAX_VALUE;
				minY = Double.MAX_VALUE;
				maxY = -Double.MAX_VALUE;
			}
		}

		private void addPoint(double x, double y) {
			this.maxX = Math.max(x, maxX);
			this.maxY = Math.max(y, maxY);
			this.minX = Math.min(x, minX);
			this.minY = Math.min(y, minY);
		}

	}

	@Override
	public UGraphic apply(UChange change) {
		if (change instanceof UTranslate) {
			return new TextLimitFinder(stringBounder, minmax, translate.compose((UTranslate) change));
		} else if (change instanceof UStroke) {
			return new TextLimitFinder(this);
		} else if (change instanceof UChangeBackColor) {
			return new TextLimitFinder(this);
		} else if (change instanceof UChangeColor) {
			return new TextLimitFinder(this);
		}
		throw new UnsupportedOperationException();
	}

	private final StringBounder stringBounder;
	private final UTranslate translate;
	private final MinMax minmax;

	public TextLimitFinder(StringBounder stringBounder, boolean initToZero) {
		this(stringBounder, new MinMax(initToZero), new UTranslate());
	}

	private TextLimitFinder(StringBounder stringBounder, MinMax minmax, UTranslate translate) {
		this.stringBounder = stringBounder;
		this.minmax = minmax;
		this.translate = translate;
	}

	private TextLimitFinder(TextLimitFinder other) {
		this(other.stringBounder, other.minmax, other.translate);
	}

	public StringBounder getStringBounder() {
		return stringBounder;
	}

	public UParam getParam() {
		return new UParamNull();
	}

	public void drawOldWay(UShape shape) {
		if (shape instanceof UText) {
			final double x = translate.getDx();
			final double y = translate.getDy();
			drawText(x, y, (UText) shape);
		}
	}

	public ColorMapper getColorMapper() {
		throw new UnsupportedOperationException();
	}

	public void startUrl(Url url) {
	}

	public void closeAction() {
	}

	public void writeImage(OutputStream os, String metadata, int dpi) throws IOException {
		throw new UnsupportedOperationException();
	}

	private void drawText(double x, double y, UText text) {
		final Dimension2D dim = stringBounder.calculateDimension(text.getFontConfiguration().getFont(), text.getText());
		y -= dim.getHeight() - 1.5;
		minmax.addPoint(x, y);
		minmax.addPoint(x, y + dim.getHeight());
		minmax.addPoint(x + dim.getWidth(), y);
		minmax.addPoint(x + dim.getWidth(), y + dim.getHeight());
	}

	public double getMaxX() {
		return minmax.maxX;
	}

	public double getMaxY() {
		return minmax.maxY;
	}

	public double getMinX() {
		return minmax.minX;
	}

	public double getMinY() {
		return minmax.minY;
	}

}
