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
import net.sourceforge.plantuml.ugraphic.MinMax;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UGraphicStencil;

public class SheetBlock implements TextBlock {

	private final Sheet sheet;
	private final Stencil stencil;
	private Map<Stripe, Double> heights;
	private Map<Atom, Position> positions;
	private MinMax minMax;

	public SheetBlock(Sheet sheet, Stencil stencil) {
		this.sheet = sheet;
		this.stencil = stencil;
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
			final double height = getMaxHeight(stringBounder, stripe.getAtoms());
			for (Atom atom : stripe.getAtoms()) {
				final Dimension2D dim = atom.calculateDimension(stringBounder);
				final Position position = new Position(x, y, dim);
				minMax = position.update(minMax);
				positions.put(atom, position);
				x += dim.getWidth();
			}
			heights.put(stripe, height);
			y += height;
		}
	}

	private double getMaxHeight(StringBounder stringBounder, List<Atom> atoms) {
		double height = 0;
		for (Atom atom : atoms) {
			final Dimension2D dim = atom.calculateDimension(stringBounder);
			final double h = dim.getHeight();
			if (h > height) {
				height = h;
			}
		}
		return height;
	}

	private double maxDeltaY(Stripe stripe, StringBounder stringBounder) {
		double result = 0;
		final double stripeHeight = heights.get(stripe);
		for (Atom atom : stripe.getAtoms()) {
			if (atom instanceof AtomText == false) {
				continue;
			}
			final Dimension2D dimBloc = atom.calculateDimension(stringBounder);
			final double deltaY = stripeHeight - dimBloc.getHeight() + ((AtomText) atom).getFontSize2D();
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
		// final Dimension2D dim = calculateDimension(ug.getStringBounder());
		if (stencil != null) {
			// ug = new UGraphicHorizontalLine(ug, 0, dim.getWidth(), new UStroke());
			ug = new UGraphicStencil(ug, stencil);
		}
		for (Stripe stripe : sheet) {
			final StringBounder stringBounder = ug.getStringBounder();
			final double deltaY = maxDeltaY(stripe, stringBounder);
			for (Atom atom : stripe.getAtoms()) {
				final Position position = positions.get(atom);
				if (atom instanceof AtomText) {
					atom.drawU(position.translate(ug));
					// position.drawDebug(ug);
				} else {
					atom.drawU(position.translate(ug));
					// position.drawDebug(ug);
				}
				// final double dy = stripeHeight - position.getHeight();
			}
		}
	}
}
