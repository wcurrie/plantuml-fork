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
package net.sourceforge.plantuml.activitydiagram3.ftile;

import java.awt.geom.Dimension2D;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UShape;
import net.sourceforge.plantuml.ugraphic.UStroke;

public class FtileCircleStop implements Ftile {

	private static final int SIZE = 20;

	public void drawU(UGraphic ug, double xTheoricalPosition, double yTheoricalPosition) {

		xTheoricalPosition = Math.round(xTheoricalPosition);
		yTheoricalPosition = Math.round(yTheoricalPosition);

		final UEllipse circle = new UEllipse(SIZE, SIZE);
		if (SHADOWING) {
			circle.setDeltaShadow(3);
		}
		ug.getParam().setStroke(new UStroke());
		ug.getParam().setBackcolor(null);
		ug.getParam().setColor(HtmlColorUtils.BLACK);
		ug.draw(xTheoricalPosition, yTheoricalPosition, circle);
		ug.getParam().setStroke(new UStroke());

		final double delta = 4;
		final UEllipse circleSmall = new UEllipse(SIZE - delta * 2, SIZE - delta * 2);
		if (SHADOWING) {
			circleSmall.setDeltaShadow(3);
		}
		ug.getParam().setColor(null);
		ug.getParam().setBackcolor(HtmlColorUtils.BLACK);
		ug.draw(xTheoricalPosition + delta + .5, yTheoricalPosition + delta + .5, circleSmall);

	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return new Dimension2DDouble(SIZE, SIZE);
	}

	public List<Url> getUrls() {
		return Collections.emptyList();
	}
	
	public boolean isKilled() {
		return true;
	}


}
