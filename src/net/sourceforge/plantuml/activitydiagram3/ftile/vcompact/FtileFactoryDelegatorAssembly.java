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

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.activitydiagram3.ftile.Connection;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileEmpty;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactory;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactoryDelegator;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileUtils;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class FtileFactoryDelegatorAssembly extends FtileFactoryDelegator {

	public FtileFactoryDelegatorAssembly(FtileFactory factory, ISkinParam skinParam) {
		super(factory, skinParam);
	}

	@Override
	public Ftile assembly(final Ftile tile1, final Ftile tile2) {
		final Ftile space = new FtileEmpty(getFactory().shadowing(), 1, 35);
		final Ftile tile1andSpace = super.assembly(tile1, space);
		final Ftile result = super.assembly(tile1andSpace, tile2);
		final StringBounder stringBounder = getStringBounder();
		final UTranslate translate1 = result.getTranslateFor(tile1, stringBounder);
		final Point2D pointOut = tile1.getPointOut(stringBounder);
		if (pointOut == null) {
			return result;
		}
		final Point2D p1 = translate1.getTranslated(pointOut);
		final UTranslate translate2 = result.getTranslateFor(tile2, stringBounder);
		final Point2D p2 = translate2.getTranslated(tile2.getPointIn(stringBounder));

		final HtmlColor color = getInLinkRenderingColor(tile2);

		final Connection connection = new ConnectionVerticalDown(tile1, tile2, p1, p2, color);
		return FtileUtils.addConnection(result, connection);
	}
}
