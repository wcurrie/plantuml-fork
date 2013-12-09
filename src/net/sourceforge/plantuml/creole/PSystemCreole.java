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
 * Revision $Revision: 4041 $
 *
 */
package net.sourceforge.plantuml.creole;

import java.awt.Font;
import java.awt.geom.Dimension2D;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.api.ImageDataSimple;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.DiagramDescriptionImpl;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.ugraphic.ColorMapperIdentity;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UStroke;

public class PSystemCreole extends AbstractPSystem {

	private final List<String> lines = new ArrayList<String>();

	public PSystemCreole() {
	}

	public DiagramDescription getDescription() {
		return new DiagramDescriptionImpl("(Creole)", getClass());
	}

	public void doCommandLine(String line) {
		lines.add(line);
	}

	public ImageData exportDiagram(OutputStream os, int num, FileFormatOption fileFormat) throws IOException {
		final Display display = new Display(lines);
		final UFont font = new UFont("Serif", Font.PLAIN, 14);
		final FontConfiguration fontConfiguration = new FontConfiguration(font, HtmlColorUtils.BLACK);
		final Sheet sheet = new CreoleParser(fontConfiguration, null).createSheet(display);
		final SheetBlock sheetBlock = new SheetBlock(sheet, null, new UStroke());
		final Dimension2D dim = TextBlockUtils.getDimension(sheetBlock);
		final UGraphic ug = fileFormat.createUGraphic(new ColorMapperIdentity(), 1, dim, null, false);
		// sheetBlock.drawU(ug.apply(new UTranslate(0, 10)));
		sheetBlock.drawU(ug);
		ug.writeImage(os, null, 96);
		return new ImageDataSimple(dim);
	}
}
