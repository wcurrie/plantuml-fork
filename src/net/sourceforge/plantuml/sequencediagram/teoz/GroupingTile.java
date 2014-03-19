/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
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
 * Revision $Revision: 4636 $
 *
 */
package net.sourceforge.plantuml.sequencediagram.teoz;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.sequencediagram.GroupingStart;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class GroupingTile implements Tile {

	private final List<Tile> tiles = new ArrayList<Tile>();
	private final Real min;
	private final Real max;

	private double height;

	public GroupingTile(Iterator<Event> it, GroupingStart start, TileArguments tileArguments) {

		final StringBounder stringBounder = tileArguments.getStringBounder();
		this.min = tileArguments.getOmega().subAtLeast(0);
		this.max = tileArguments.getAlpha().addAtLeast(0);

		while (it.hasNext()) {
			final Event ev = it.next();
			final Tile tile = TileBuilder.buildOne(it, tileArguments, ev);
			if (tile != null) {
				tiles.add(tile);
				min.ensureLowerThan(tile.getMinX(stringBounder));
				max.ensureBiggerThan(tile.getMaxX(stringBounder));
				height += tile.getPreferredHeight(stringBounder);
			}
		}

	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		double h = 0;
		for (Tile tile : tiles) {
			tile.drawU(ug.apply(new UTranslate(0, h)));
			h += tile.getPreferredHeight(stringBounder);
		}

	}

	public double getPreferredHeight(StringBounder stringBounder) {
		return height;
	}

	public void compile(StringBounder stringBounder) {
		for (Tile tile : tiles) {
			tile.compile(stringBounder);
		}
	}

	public Real getMinX(StringBounder stringBounder) {
		return min;
	}

	public Real getMaxX(StringBounder stringBounder) {
		return max;
	}

}
