/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2012, Arnaud Roques
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
 * Revision $Revision: 6577 $
 *
 */
package net.sourceforge.plantuml.graphic;

import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.ugraphic.UGraphic;

public class TextBlockVertical2 implements TextBlock {

	private final List<TextBlock> blocks = new ArrayList<TextBlock>();
	private final HorizontalAlignement horizontalAlignement;

	public TextBlockVertical2(TextBlock b1, TextBlock b2, HorizontalAlignement horizontalAlignement) {
		this.blocks.add(b1);
		this.blocks.add(b2);
		this.horizontalAlignement = horizontalAlignement;
	}

	public TextBlockVertical2(List<TextBlock> all, HorizontalAlignement horizontalAlignement) {
		if (all.size() < 2) {
			throw new IllegalArgumentException();
		}
		this.blocks.addAll(all);
		this.horizontalAlignement = horizontalAlignement;
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		Dimension2D dim = blocks.get(0).calculateDimension(stringBounder);
		for (int i = 1; i < blocks.size(); i++) {
			dim = Dimension2DDouble.mergeTB(dim, blocks.get(i).calculateDimension(stringBounder));
		}
		return dim;
	}

	public void drawU(UGraphic ug, double x, double y) {
		final HtmlColor color = ug.getParam().getColor();
		final Dimension2D dimtotal = calculateDimension(ug.getStringBounder());
		for (TextBlock b : blocks) {
			final Dimension2D dimb = b.calculateDimension(ug.getStringBounder());
			if (horizontalAlignement == HorizontalAlignement.LEFT) {
				b.drawU(ug, x, y);
				ug.getParam().setColor(color);
			} else if (horizontalAlignement == HorizontalAlignement.CENTER) {
				final double dx = (dimtotal.getWidth() - dimb.getWidth()) / 2;
				b.drawU(ug, x + dx, y);
				ug.getParam().setColor(color);
			} else {
				throw new UnsupportedOperationException();
			}
			y += dimb.getHeight();
		}
	}

	public List<Url> getUrls() {
		return Collections.emptyList();
	}

}