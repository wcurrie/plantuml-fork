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
 * Revision $Revision: 11025 $
 *
 */
package net.sourceforge.plantuml.creole;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UText;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class ListNumber implements Atom {

	private final FontConfiguration fontConfiguration;
	private final int order;
	private final String label;

	public ListNumber(FontConfiguration fontConfiguration, int order, int localNumber) {
		this.fontConfiguration = fontConfiguration;
		this.order = order;
		this.label = "" + (localNumber + 1) + ".";
	}

	private double getWidth(StringBounder stringBounder, int pos) {
		final Dimension2D dim = stringBounder.calculateDimension(fontConfiguration.getFont(), "9. ");
		return dim.getWidth() * (pos + 1);
	}

	public void drawU(UGraphic ug) {
		final HtmlColor color = fontConfiguration.getColor();
		ug = ug.apply(new UChangeColor(color)).apply(new UChangeBackColor(color)).apply(new UStroke(0));

		ug = ug.apply(new UTranslate(getWidth(ug.getStringBounder(), order - 1), 0));
		ug.draw(new UText(label, fontConfiguration));
		// final double width = getWidth(ug.getStringBounder());
		// ug.draw(new UEllipse(5, 5));
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return new Dimension2DDouble(getWidth(stringBounder, order), 5);
	}

	public double getH1(StringBounder stringBounder) {
		return 5;
	}

}
