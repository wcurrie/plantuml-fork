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
 * Revision $Revision: 8475 $
 *
 */
package net.sourceforge.plantuml.activitydiagram3.ftile;

import java.awt.geom.Dimension2D;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.plantuml.activitydiagram3.LinkRendering;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class FtileAssemblySimple4747bis implements Ftile {

	private final Ftile tile1;
	private final Ftile tile2;

	@Override
	public String toString() {
		return "FtileAssemblySimple " + tile1 + " && " + tile2;
	}

	public FtileAssemblySimple4747bis(Ftile tile1, Ftile tile2) {
		this.tile1 = tile1;
		this.tile2 = tile2;
	}

	public Swimlane getSwimlaneIn() {
		return tile1.getSwimlaneIn();
	}

	public Swimlane getSwimlaneOut() {
		return tile2.getSwimlaneOut();
	}

	public UTranslate getTranslateFor(Ftile child, StringBounder stringBounder) {
		if (child == tile1) {
			return getTranslated1(stringBounder);
		}
		if (child == tile2) {
			return getTranslated2(stringBounder);
		}
		UTranslate tmp = tile1.getTranslateFor(child, stringBounder);
		if (tmp != null) {
			return tmp.compose(getTranslated1(stringBounder));
		}
		tmp = tile2.getTranslateFor(child, stringBounder);
		if (tmp != null) {
			return tmp.compose(getTranslated2(stringBounder));
		}
		throw new UnsupportedOperationException();
	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		ug.apply(getTranslated1(stringBounder)).draw(tile1);
		ug.apply(getTranslated2(stringBounder)).draw(tile2);
	}

	// private double left1 = Double.MIN_VALUE;
	// private double right1 = Double.MIN_VALUE;
	// private double left2 = Double.MIN_VALUE;
	// private double right2 = Double.MIN_VALUE;
	//
	// private double getLeft1(StringBounder stringBounder) {
	// if (left1 == Double.MIN_VALUE) {
	// final FtileGeometry geo = tile1.calculateDimension(stringBounder);
	// left1 = geo.getLeft();
	// }
	// return left1;
	// }
	//
	// private double getRight1(StringBounder stringBounder) {
	// if (right1 == Double.MIN_VALUE) {
	// final Dimension2D dim = tile1.calculateDimension(stringBounder);
	// right1 = dim.getWidth() - getLeft1(stringBounder);
	// }
	// return right1;
	// }
	//
	// private double getLeft2(StringBounder stringBounder) {
	// if (left2 == Double.MIN_VALUE) {
	// left2 = tile2.calculateDimension(stringBounder).getLeft();
	// }
	// return left2;
	// }
	//
	// private double getRight2(StringBounder stringBounder) {
	// if (right2 == Double.MIN_VALUE) {
	// final Dimension2D dim = tile2.calculateDimension(stringBounder);
	// right2 = dim.getWidth() - getLeft2(stringBounder);
	// }
	// return right2;
	// }

	public boolean isKilled() {
		return tile1.isKilled() || tile2.isKilled();
	}

	public LinkRendering getInLinkRendering() {
		return tile1.getInLinkRendering();
	}

	public LinkRendering getOutLinkRendering() {
		return null;
	}

	private FtileGeometry calculateDimension;

	public FtileGeometry calculateDimension(StringBounder stringBounder) {
		if (calculateDimension == null) {
			calculateDimension = tile1.calculateDimension(stringBounder).appendBottom(
					tile2.calculateDimension(stringBounder));
		}
		return calculateDimension;
	}

	private UTranslate getTranslated1(StringBounder stringBounder) {
		final double left = calculateDimension(stringBounder).getLeft();
		return new UTranslate(left - tile1.calculateDimension(stringBounder).getLeft(), 0);
	}

	private UTranslate getTranslated2(StringBounder stringBounder) {
		final Dimension2D dim1 = tile1.calculateDimension(stringBounder);
		final double left = calculateDimension(stringBounder).getLeft();
		return new UTranslate(left - tile2.calculateDimension(stringBounder).getLeft(), dim1.getHeight());
	}

	public Collection<Connection> getInnerConnections() {
		return Collections.emptyList();
	}

	public Set<Swimlane> getSwimlanes() {
		final Set<Swimlane> result = new HashSet<Swimlane>();
		result.addAll(tile1.getSwimlanes());
		result.addAll(tile2.getSwimlanes());
		return Collections.unmodifiableSet(result);
	}

	public boolean shadowing() {
		return tile1.shadowing() || tile2.shadowing();
	}

}
