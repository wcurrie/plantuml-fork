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
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TileText;
import net.sourceforge.plantuml.ugraphic.MinMax;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class SheetBlock implements TextBlock {

	private final Sheet sheet;
	private Map<Stripe, Double> heights;
	private Map<Atom, Position> positions;
	private MinMax minMax;

	public SheetBlock(Sheet sheet) {
		this.sheet = sheet;
	}

	private void initMap(StringBounder stringBounder) {
		if (positions != null) {
			return;
		}
		positions = new LinkedHashMap<Atom, Position>();
		heights = new LinkedHashMap<Stripe, Double>();
		minMax = MinMax.getEmpty(true);
		double y = 0;
		for (Stripe stripe : sheet) {
			double x = 0;
			double height = 0;
			for (Atom atom : stripe.getAtoms()) {
				final TextBlock tb = atom.asTextBlock();
				final Dimension2D dim = tb.calculateDimension(stringBounder);
				final Position position = new Position(x, y, dim);
				minMax = position.update(minMax);
				positions.put(atom, position);
				x += dim.getWidth();
				height = Math.max(0, dim.getHeight());
			}
			heights.put(stripe, height);
			y += height;
		}
	}

	private double maxDeltaY(Stripe stripe, StringBounder stringBounder) {
		double result = 0;
		final double stripeHeight = heights.get(stripe);
		for (Atom atom : stripe.getAtoms()) {
			final TextBlock tb = atom.asTextBlock();
			if (tb instanceof TileText == false) {
				continue;
			}
			final Dimension2D dimBloc = tb.calculateDimension(stringBounder);
			final double deltaY = stripeHeight - dimBloc.getHeight() + ((TileText) tb).getFontSize2D();
			result = Math.max(result, deltaY);
		}
		return result;
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		initMap(stringBounder);
		return minMax.getDimension();
	}

	public List<Url> getUrls(StringBounder stringBounder) {
		return Collections.emptyList();
	}

	public void drawU(UGraphic ug) {
		initMap(ug.getStringBounder());
		for (Stripe stripe : sheet) {
			final StringBounder stringBounder = ug.getStringBounder();
			final double deltaY = maxDeltaY(stripe, stringBounder);
			for (Atom atom : stripe.getAtoms()) {
				final Position position = positions.get(atom);
				final TextBlock tb = atom.asTextBlock();
				tb.drawU(position.translate(ug).apply(new UTranslate(0, deltaY)));
			}
		}
	}
}
