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
 * Revision $Revision: 6577 $
 *
 */
package net.sourceforge.plantuml.graphic;

import java.awt.geom.Dimension2D;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UHorizontalLine;
import net.sourceforge.plantuml.ugraphic.UStroke;

public class TextBlockLineBefore2 implements TextBlock {

	private final TextBlock textBlock;
	private final char separator;
	private final TextBlock title;

	public TextBlockLineBefore2(TextBlock textBlock, char separator, TextBlock title) {
		this.textBlock = textBlock;
		this.separator = separator;
		this.title = title;
	}

	public TextBlockLineBefore2(TextBlock textBlock, char separator) {
		this(textBlock, separator, null);
	}

	public TextBlockLineBefore2(TextBlock textBlock) {
		this(textBlock, '\0');
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		final Dimension2D dim = textBlock.calculateDimension(stringBounder);
		if (title != null) {
			final Dimension2D dimTitle = title.calculateDimension(stringBounder);
			return Dimension2DDouble.atLeast(dim, dimTitle.getWidth() + 8, dimTitle.getHeight());
		}
		return dim;
	}

	private void drawLine(UGraphic ug, double y, UHorizontalLine line) {
		if (separator == '=') {
			ug.drawNewWay(0, y, line);
			ug.drawNewWay(0, y + 2, line.blankTitle());
		} else {
			ug.drawNewWay(0, y, line);
		}
	}

	private UStroke getStroke() {
		if (separator == '\0') {
			return null;
		} else if (separator == '=') {
			return new UStroke();
		} else if (separator == '.') {
			return new UStroke(1, 2, 1);
		} else if (separator == '-') {
			return new UStroke();
		} else {
			return new UStroke(1.5);
		}
	}

	public void drawUNewWayINLINED(UGraphic ug) {
		final HtmlColor color = ug.getParam().getColor();
		if (title == null) {
			drawLine(ug, 0, UHorizontalLine.infinite(1, 1, getStroke()));
		}
		textBlock.drawUNewWayINLINED(ug);
		ug = ug.apply(new UChangeColor(color));
		if (title != null) {
			drawLine(ug, 0, UHorizontalLine.infinite(1, 1, title, getStroke()));
		}
	}

	public List<Url> getUrls() {
		return textBlock.getUrls();
	}

}