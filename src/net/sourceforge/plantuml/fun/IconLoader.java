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
 */
package net.sourceforge.plantuml.fun;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class IconLoader {

	public static BufferedImage getRandom() {
		return addTransparent(getIcon("sprite013.png"));
		// return addTransparent(getIcon(getSomeQuote()));
	}

	private static String getSomeQuote() {
		final int v = (int) (System.currentTimeMillis() / 1000L);
		final int n = v % 10;
		return "sprite" + String.format("%03d", n) + ".png";
	}

	private static BufferedImage getIcon(String name) {
		try {
			final InputStream is = IconLoader.class.getResourceAsStream(name);
			final BufferedImage image = ImageIO.read(is);
			is.close();
			return image;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static BufferedImage addTransparent(BufferedImage ico) {
		final BufferedImage transparentIcon = new BufferedImage(ico.getWidth(), ico.getHeight(),
				BufferedImage.TYPE_INT_ARGB_PRE);
		for (int i = 0; i < ico.getWidth(); i++) {
			for (int j = 0; j < ico.getHeight(); j++) {
				final int col = ico.getRGB(i, j);
				if (col != ico.getRGB(0, 0)) {
					transparentIcon.setRGB(i, j, col);
				}
			}
		}
		return transparentIcon;
	}

}
