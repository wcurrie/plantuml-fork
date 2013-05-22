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

import net.sourceforge.plantuml.activitydiagram3.ftile.vcompact.ConnectionVerticalDown;
import net.sourceforge.plantuml.ugraphic.UGraphic;

public class ConnectionCross extends AbstractConnection {

	private final Connection connection;

	public ConnectionCross(Connection connection) {
		super(connection.getFtile1(), connection.getFtile2());
		this.connection = connection;
	}

	public void drawU(UGraphic ug) {
		if (connection instanceof ConnectionVerticalDown) {
			final Swimlane swimlane1 = getFtile1().getSwimlaneOut();
			final Swimlane swimlane2 = getFtile2().getSwimlaneIn();
			((ConnectionVerticalDown) connection).drawTranslate(ug, swimlane1.getTranslate(), swimlane2.getTranslate());
		}
	}
}
