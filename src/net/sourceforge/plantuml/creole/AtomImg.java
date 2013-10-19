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
 * Revision $Revision: 11025 $
 *
 */
package net.sourceforge.plantuml.creole;

import java.awt.Font;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.imageio.ImageIO;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FileSystem;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.ImgValign;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UImage;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class AtomImg implements Atom {

	// private final TextBlock tileImage;
	private final BufferedImage image;

	private AtomImg(BufferedImage image) {
		this.image = image;
	}

	public static Atom create(String src, final ImgValign valign, final int vspace) {
		final UFont font = new UFont("Monospaced", Font.PLAIN, 14);
		final FontConfiguration fc = new FontConfiguration(font, HtmlColorUtils.BLACK);
		try {
			final File f = FileSystem.getInstance().getFile(src);
			if (f.exists() == false) {
				// Check if valid URL
				if (src.startsWith("http:") || src.startsWith("https:")) {
					final byte image[] = getFile(src);
					final BufferedImage read = ImageIO.read(new ByteArrayInputStream(image));
					if (read == null) {
						return AtomText.create("(Cannot decode: " + src + ")", fc);
					}
					return new AtomImg(read);
				}
				return AtomText.create("(File not found: " + f + ")", fc);
			}
			if (f.getName().endsWith(".svg")) {
				// return new AtomImg(new TileImageSvg(f));
				throw new UnsupportedOperationException();
			}
			final BufferedImage read = ImageIO.read(f);
			if (read == null) {
				return AtomText.create("(Cannot decode: " + f + ")", fc);
			}
			return new AtomImg(ImageIO.read(f));
		} catch (IOException e) {
			return AtomText.create("ERROR " + e.toString(), fc);
		}
	}

	// Added by Alain Corbiere
	static byte[] getFile(String host) throws IOException {
		final ByteArrayOutputStream image = new ByteArrayOutputStream();
		InputStream input = null;
		try {
			final URL url = new URL(host);
			final URLConnection connection = url.openConnection();
			input = connection.getInputStream();
			final byte[] buffer = new byte[1024];
			int read;
			while ((read = input.read(buffer)) > 0) {
				image.write(buffer, 0, read);
			}
			image.close();
			return image.toByteArray();
		} finally {
			if (input != null) {
				input.close();
			}
		}
	}

	// End

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return new Dimension2DDouble(image.getWidth(), image.getHeight());
	}

	public double getH1(StringBounder stringBounder) {
		return image.getWidth();
	}

	public void drawU(UGraphic ug) {
		// final double h = calculateDimension(ug.getStringBounder()).getHeight();
		ug.draw(new UImage(image));
		// tileImage.drawU(ug.apply(new UTranslate(0, -h)));
	}

}
