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
 * Revision $Revision: 10266 $
 *
 */
package net.sourceforge.plantuml.graphic;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.ugraphic.UGraphic;

public class TextBlockInterceptorTextBlockable implements TextBlock {

	private final TextBlock textBlock;

	public TextBlockInterceptorTextBlockable(TextBlock textBlock) {
		this.textBlock = textBlock;
	}

	public void drawU(UGraphic ug) {
		textBlock.drawU(new UGraphicInterceptorTextBlockable(ug));
		ug.flushUg();
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return TextBlockUtils.getMinMax(this, stringBounder).getDimension();
	}

}