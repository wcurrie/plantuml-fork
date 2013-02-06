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
 * Revision $Revision: 9786 $
 *
 */
package net.sourceforge.plantuml.activitydiagram3;

import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import net.sourceforge.plantuml.CMapData;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.UmlDiagramInfo;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.StringBounderUtils;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UGraphicUtils;

public class ActivityDiagram3 extends UmlDiagram {

	private static final StringBounder dummyStringBounder;

	static {
		final BufferedImage imDummy = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
		dummyStringBounder = StringBounderUtils.asStringBounder(imDummy.createGraphics());
	}

	public String getDescription() {
		return "activity3";
	}

	@Override
	public UmlDiagramType getUmlDiagramType() {
		return UmlDiagramType.ACTIVITY;
	}

	@Override
	protected UmlDiagramInfo exportDiagramInternal(OutputStream os, CMapData cmap, int index,
			FileFormatOption fileFormatOption, List<BufferedImage> flashcodes) throws IOException {
		final Ftile result = getFtile();
		final ISkinParam skinParam = getSkinParam();
		final Dimension2D dim = Dimension2DDouble.delta(result.calculateDimension(dummyStringBounder), 20);

		final double dpiFactor = getDpiFactor(fileFormatOption);

		final UGraphic ug = fileFormatOption.createUGraphic(skinParam.getColorMapper(), dpiFactor, dim, getSkinParam()
				.getBackgroundColor(), isRotation());

		result.drawU(ug, 8, 5);
		UGraphicUtils.writeImage(os, ug, getMetadata(), getDpi(fileFormatOption));
		// final BufferedImage im = ((UGraphicG2d) ug).getBufferedImage();
		// PngIO.write(im, os, getMetadata(), getDpi(fileFormatOption));
		return new UmlDiagramInfo(dim.getWidth());
	}

	private Instruction current = new InstructionList();

	public void addActivity(Display activity) {
		current.add(new InstructionSimple(activity));
	}

	private Ftile getFtile() {
		return current.createFtile();
	}

	public void fork() {
		final InstructionFork instructionFork = new InstructionFork(current);
		current.add(instructionFork);
		current = instructionFork;

	}

	public void forkAgain() {
		((InstructionFork) current).forkAgain();
	}

	public void endFork() {
		current = ((InstructionFork) current).getParent();
	}

	public void startIf(String test, Display whenThen) {
		final InstructionIf instructionIf = new InstructionIf(current, test, whenThen);
		current.add(instructionIf);
		current = instructionIf;
	}

	public void endif() {
		current = ((InstructionIf) current).getParent();
	}

	public void else2(Display whenElse) {
		((InstructionIf) current).swithToElse(whenElse);
	}

	public void startRepeat() {
		final InstructionRepeat instructionRepeat = new InstructionRepeat(current);
		current.add(instructionRepeat);
		current = instructionRepeat;

	}

	public void repeatWhile(Display label) {
		final InstructionRepeat instructionRepeat = (InstructionRepeat) current;
		instructionRepeat.setTest(label);
		current = instructionRepeat.getParent();

	}

	public void doWhile(Display test) {
		final InstructionWhile instructionWhile = new InstructionWhile(current, test);
		current.add(instructionWhile);
		current = instructionWhile;
	}

	public void endwhile() {
		current = ((InstructionWhile) current).getParent();
	}

	public void start() {
		current.add(new InstructionStart());
	}

	public void stop() {
		current.add(new InstructionStop());
	}

}
