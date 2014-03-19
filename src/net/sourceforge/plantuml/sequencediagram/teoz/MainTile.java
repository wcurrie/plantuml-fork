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
 * Revision $Revision: 9591 $
 *
 */
package net.sourceforge.plantuml.sequencediagram.teoz;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;
import net.sourceforge.plantuml.skin.Skin;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class MainTile implements Tile {

	private final Real alpha;
	private final Real omega;
	private double height;

	private final List<Tile> tiles = new ArrayList<Tile>();

	public MainTile(SequenceDiagram diagram, Skin skin, Real alpha, Real omega,
			Map<Participant, LivingSpace> livingSpaces) {
		final StringBounder stringBounder = TextBlockUtils.getDummyStringBounder();

		final ISkinParam skinParam = diagram.getSkinParam();

		this.alpha = alpha;
		this.omega = omega;

		final TileArguments tileArguments = new TileArguments(stringBounder, alpha, omega, livingSpaces, skin,
				skinParam);

		tiles.addAll(TileBuilder.buildSeveral(diagram.events().iterator(), tileArguments));

		for (Tile tile : tiles) {
			height += tile.getPreferredHeight(stringBounder);
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
		return alpha;
	}

	public Real getMaxX(StringBounder stringBounder) {
		return omega;
	}

}
