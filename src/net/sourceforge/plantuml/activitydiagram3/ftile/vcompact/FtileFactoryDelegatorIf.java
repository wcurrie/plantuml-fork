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

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.activitydiagram3.ftile.Connection;
import net.sourceforge.plantuml.activitydiagram3.ftile.Diamond;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactory;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactoryDelegator;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileUtils;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class FtileFactoryDelegatorIf extends FtileFactoryDelegator {

	public FtileFactoryDelegatorIf(FtileFactory factory) {
		super(factory);
	}

	@Override
	public Ftile createIf(final Ftile tile1, final Ftile tile2, Display labelTest, Display label1, Display label2) {
		final FtileIf2 result = new FtileIf2(tile1, tile2, HtmlColorUtils.BLUE, HtmlColorUtils.LIGHT_GRAY);
		final StringBounder stringBounder = getStringBounder();

		final UTranslate translate1 = result.getTranslateFor(tile1, stringBounder);
		final UTranslate translate2 = result.getTranslateFor(tile2, stringBounder);

		final Point2D p1in = translate1.getTranslated(tile1.getPointIn(stringBounder));
		final Point2D p2in = translate2.getTranslated(tile2.getPointIn(stringBounder));
		final Point2D p1out = translate1.getTranslated(tile1.getPointOut(stringBounder));
		final Point2D p2out = translate2.getTranslated(tile2.getPointOut(stringBounder));

		final Dimension2D dim = result.asTextBlock().calculateDimension(stringBounder);
		final Point2D pA1 = new Point2D.Double(dim.getWidth() / 2 - Diamond.diamondHalfSize, Diamond.diamondHalfSize);
		final Point2D pA2 = new Point2D.Double(dim.getWidth() / 2 + Diamond.diamondHalfSize, Diamond.diamondHalfSize);
		final Point2D pB1 = new Point2D.Double(dim.getWidth() / 2 - Diamond.diamondHalfSize, dim.getHeight()
				- Diamond.diamondHalfSize);
		final Point2D pB2 = new Point2D.Double(dim.getWidth() / 2 + Diamond.diamondHalfSize, dim.getHeight()
				- Diamond.diamondHalfSize);

		final List<Connection> conns = new ArrayList<Connection>();
		conns.add(new ConnectionHorizontalThenVertical(pA1, p1in, HtmlColorUtils.GREEN));
		conns.add(new ConnectionHorizontalThenVertical(pA2, p2in, HtmlColorUtils.GREEN));
		conns.add(new ConnectionVerticalDown(p1out, pB1, HtmlColorUtils.GREEN));
		conns.add(new ConnectionVerticalDown(p2out, pB2, HtmlColorUtils.GREEN));
		return FtileUtils.addConnection(result, conns);
	}

}
