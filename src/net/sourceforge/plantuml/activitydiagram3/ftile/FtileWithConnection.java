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
 * Revision $Revision: 8475 $
 *
 */
package net.sourceforge.plantuml.activitydiagram3.ftile;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.activitydiagram3.LinkRendering;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

class FtileWithConnection implements Ftile {

	private final Ftile ftile;
	private final List<Connection> connections = new ArrayList<Connection>();

	FtileWithConnection(Ftile ftile, Collection<Connection> connections) {
		if (connections == null || connections.size() == 0) {
			throw new IllegalArgumentException();
		}
		this.ftile = ftile;
		this.connections.addAll(connections);
	}

	@Override
	public String toString() {
		return "FtileWithConnection " + ftile + " " + connections;
	}

	public FtileWithConnection(Ftile ftile, Connection connection) {
		this(ftile, Arrays.asList(connection));
		if (connection == null) {
			throw new IllegalArgumentException();
		}
	}

	public UTranslate getTranslateFor(Ftile child, StringBounder stringBounder) {
		return this.ftile.getTranslateFor(child, stringBounder);
	}

	public boolean isKilled() {
		return ftile.isKilled();
	}

	public LinkRendering getInLinkRendering() {
		return ftile.getInLinkRendering();
	}

	public LinkRendering getOutLinkRendering() {
		return ftile.getOutLinkRendering();
	}

	public Point2D getPointIn(StringBounder stringBounder) {
		return ftile.getPointIn(stringBounder);
	}

	public Point2D getPointOut(StringBounder stringBounder) {
		return ftile.getPointOut(stringBounder);
	}

	public TextBlock asTextBlock() {
		final TextBlock original = ftile.asTextBlock();
		return new TextBlock() {

			public void drawU(UGraphic ug) {
				original.drawU(ug);
				for (Connection c : connections) {
					ug.draw(c);
				}
			}

			public List<Url> getUrls() {
				return original.getUrls();
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				return original.calculateDimension(stringBounder);
			}
		};
	}

	public Collection<Connection> getInnerConnections() {
		final List<Connection> result = new ArrayList<Connection>(ftile.getInnerConnections());
		result.addAll(connections);
		return Collections.unmodifiableList(connections);
	}

	public Set<Swimlane> getSwimlanes() {
		return ftile.getSwimlanes();
	}
	
	public Swimlane getSwimlaneIn() {
		return ftile.getSwimlaneIn();
	}

	public Swimlane getSwimlaneOut() {
		return ftile.getSwimlaneOut();
	}


}
