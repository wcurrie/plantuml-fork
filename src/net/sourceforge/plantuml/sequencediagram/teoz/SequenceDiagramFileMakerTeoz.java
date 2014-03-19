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
 * Revision $Revision: 9591 $
 *
 */
package net.sourceforge.plantuml.sequencediagram.teoz;

import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.api.ImageDataSimple;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.real.RealUtils;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;
import net.sourceforge.plantuml.sequencediagram.graphic.FileMaker;
import net.sourceforge.plantuml.skin.Skin;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class SequenceDiagramFileMakerTeoz implements FileMaker {

	private final SequenceDiagram diagram;
	private final FileFormatOption fileFormatOption;
	private final Skin skin;

	private final Map<Participant, LivingSpace> livingSpaces = new LinkedHashMap<Participant, LivingSpace>();

	public SequenceDiagramFileMakerTeoz(SequenceDiagram sequenceDiagram, Skin skin, FileFormatOption fileFormatOption,
			List<BufferedImage> flashcodes) {
		this.diagram = sequenceDiagram;
		this.fileFormatOption = fileFormatOption;
		this.skin = skin;

	}

	public ImageData createOne(OutputStream os, int index, boolean isWithMetadata) throws IOException {
		StringBounder stringBounder = TextBlockUtils.getDummyStringBounder();

		final ISkinParam skinParam = diagram.getSkinParam();

		final Real origin = RealUtils.createOrigin();
		final Real alpha = origin.subAtLeast(0);
		Real currentPos = origin;
		double headHeight = 0;
		LivingSpace previous = null;
		for (Participant p : diagram.participants().values()) {
			final LivingSpace livingSpace = new LivingSpace(p, diagram.getEnglober(p), skin, skinParam, currentPos,
					previous);
			if (previous != null) {
				previous.setNext(livingSpace);
			}
			previous = livingSpace;
			livingSpaces.put(p, livingSpace);
			final Dimension2D headDim = livingSpace.getHeadPreferredDimension(stringBounder);
			currentPos = livingSpace.getPosD(stringBounder).addAtLeast(0);
			headHeight = Math.max(headHeight, headDim.getHeight());
		}
		final Real omega = previous.getPosD(stringBounder).addAtLeast(0);

		final MainTile mainTile = new MainTile(diagram, skin, alpha, omega, Collections.unmodifiableMap(livingSpaces));
		mainTile.compile(stringBounder);
		origin.compile();

		final double height = mainTile.getPreferredHeight(stringBounder) + 2 * headHeight;

		System.err.println("alpha=" + alpha.getCurrentValue());
		System.err.println("omega=" + omega.getCurrentValue());

		final Dimension2D dim = new Dimension2DDouble(omega.getCurrentValue() - alpha.getCurrentValue(), height);
		final UGraphic ug = fileFormatOption.createUGraphic(dim).apply(new UTranslate(-alpha.getCurrentValue(), 0));
		stringBounder = ug.getStringBounder();

		drawHeads(ug);
		mainTile.drawU(ug.apply(new UTranslate(0, headHeight)));
		drawHeads(ug.apply(new UTranslate(0, mainTile.getPreferredHeight(stringBounder) + headHeight)));

		ug.writeImage(os, isWithMetadata ? diagram.getMetadata() : null, diagram.getDpi(fileFormatOption));
		final Dimension2D info = new Dimension2DDouble(dim.getWidth(), dim.getHeight());

		// if (fileFormatOption.getFileFormat() == FileFormat.PNG && ug instanceof UGraphicG2d) {
		// final Set<Url> urls = ((UGraphicG2d) ug).getAllUrlsEncountered();
		// if (urls.size() > 0) {
		// if (scale == 0) {
		// throw new IllegalStateException();
		// }
		// final CMapData cmap = CMapData.cmapString(urls, scale);
		// return new ImageDataComplex(info, cmap, null);
		// }
		// }
		return new ImageDataSimple(info);
	}

	private void drawHeads(final UGraphic ug) {
		for (LivingSpace livingSpace : livingSpaces.values()) {
			System.err.println("drawing " + livingSpace);
			final double x = livingSpace.getPosB().getCurrentValue();
			livingSpace.drawHead(ug.apply(new UTranslate(x, 0)));
		}
	}

	public int getNbPages() {
		return 1;
	}

}
