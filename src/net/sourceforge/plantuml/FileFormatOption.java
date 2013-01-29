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
 * Revision $Revision: 5333 $
 *
 */
package net.sourceforge.plantuml;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;

import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorGradient;
import net.sourceforge.plantuml.graphic.HtmlColorSimple;
import net.sourceforge.plantuml.graphic.HtmlColorTransparent;
import net.sourceforge.plantuml.ugraphic.ColorMapper;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.g2d.UGraphicG2d;

public class FileFormatOption {

	private final FileFormat fileFormat;
	private final AffineTransform affineTransform;

	public FileFormatOption(FileFormat fileFormat) {
		this(fileFormat, null);
	}

	public FileFormatOption(FileFormat fileFormat, AffineTransform at) {
		this.fileFormat = fileFormat;
		this.affineTransform = at;
	}

	public final FileFormat getFileFormat() {
		return fileFormat;
	}

	public AffineTransform getAffineTransform() {
		return affineTransform;
	}

	public UGraphic createUGraphic(ColorMapper colorMapper, double dpiFactor, final Dimension2D dim,
			HtmlColor mybackcolor, boolean rotation) {
		switch (fileFormat) {
		case PNG:
			return createUGraphicPNG(colorMapper, dpiFactor, dim, mybackcolor, rotation);
		default:
			throw new UnsupportedOperationException();
		}

	}

	private UGraphic createUGraphicPNG(ColorMapper colorMapper, double dpiFactor, final Dimension2D dim,
			HtmlColor mybackcolor, boolean rotation) {
		Color backColor = Color.WHITE;
		if (mybackcolor instanceof HtmlColorSimple) {
			backColor = colorMapper.getMappedColor(mybackcolor);
		} else if (mybackcolor instanceof HtmlColorTransparent) {
			backColor = null;
		}

		final EmptyImageBuilder builder;
		final Graphics2D graphics2D;
		if (rotation) {
			builder = new EmptyImageBuilder((int) (dim.getHeight() * dpiFactor), (int) (dim.getWidth() * dpiFactor),
					backColor);
			graphics2D = builder.getGraphics2D();
			graphics2D.rotate(-Math.PI / 2);
			graphics2D.translate(-builder.getBufferedImage().getHeight(), 0);
		} else {
			builder = new EmptyImageBuilder((int) (dim.getWidth() * dpiFactor), (int) (dim.getHeight() * dpiFactor),
					backColor);
			graphics2D = builder.getGraphics2D();

		}
		final UGraphicG2d ug = new UGraphicG2d(colorMapper, graphics2D, dpiFactor);
		ug.setBufferedImage(builder.getBufferedImage());
		final BufferedImage im = ((UGraphicG2d) ug).getBufferedImage();
		if (mybackcolor instanceof HtmlColorGradient) {
			ug.getParam().setBackcolor(mybackcolor);
			ug.draw(0, 0, new URectangle(im.getWidth(), im.getHeight()));
			ug.getParam().setBackcolor(null);
		}

		return ug;
	}
}
