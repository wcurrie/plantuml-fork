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
package net.sourceforge.plantuml.activitydiagram3.ftile.vcompact;

import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.Set;

import net.sourceforge.plantuml.activitydiagram3.LinkRendering;
import net.sourceforge.plantuml.activitydiagram3.ftile.Connection;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.Swimlane;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.UTranslate;

class FtileFoo implements Ftile {

	public TextBlock asTextBlock() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean shadowing() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isKilled() {
		// TODO Auto-generated method stub
		return false;
	}

	public LinkRendering getInLinkRendering() {
		// TODO Auto-generated method stub
		return null;
	}

	public LinkRendering getOutLinkRendering() {
		// TODO Auto-generated method stub
		return null;
	}

	public Point2D getPointIn(StringBounder stringBounder) {
		// TODO Auto-generated method stub
		return null;
	}

	public Point2D getPointOut(StringBounder stringBounder) {
		// TODO Auto-generated method stub
		return null;
	}

	public UTranslate getTranslateFor(Ftile child, StringBounder stringBounder) {
		// TODO Auto-generated method stub
		return null;
	}

	public Collection<Connection> getInnerConnections() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<Swimlane> getSwimlanes() {
		// TODO Auto-generated method stub
		return null;
	}

	public Swimlane getSwimlaneIn() {
		// TODO Auto-generated method stub
		return null;
	}

	public Swimlane getSwimlaneOut() {
		// TODO Auto-generated method stub
		return null;
	}

}
