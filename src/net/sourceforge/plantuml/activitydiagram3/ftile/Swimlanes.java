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
package net.sourceforge.plantuml.activitydiagram3.ftile;

import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.activitydiagram3.Instruction;
import net.sourceforge.plantuml.activitydiagram3.InstructionList;
import net.sourceforge.plantuml.activitydiagram3.LinkRendering;
import net.sourceforge.plantuml.activitydiagram3.ftile.vcompact.FtileFactoryDelegatorAddNote;
import net.sourceforge.plantuml.activitydiagram3.ftile.vcompact.FtileFactoryDelegatorAssembly;
import net.sourceforge.plantuml.activitydiagram3.ftile.vcompact.FtileFactoryDelegatorCreateFork;
import net.sourceforge.plantuml.activitydiagram3.ftile.vcompact.FtileFactoryDelegatorCreateGroup;
import net.sourceforge.plantuml.activitydiagram3.ftile.vcompact.FtileFactoryDelegatorCreateSplit;
import net.sourceforge.plantuml.activitydiagram3.ftile.vcompact.FtileFactoryDelegatorIf;
import net.sourceforge.plantuml.activitydiagram3.ftile.vcompact.FtileFactoryDelegatorRepeat;
import net.sourceforge.plantuml.activitydiagram3.ftile.vcompact.FtileFactoryDelegatorWhile;
import net.sourceforge.plantuml.activitydiagram3.ftile.vcompact.VCompactFactory;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.StringBounderUtils;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.UGraphic;

public class Swimlanes implements TextBlock {

	private static final StringBounder dummyStringBounder;
	
	static {
		final BufferedImage imDummy = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
		dummyStringBounder = StringBounderUtils.asStringBounder(imDummy.createGraphics());
	}

	private final ISkinParam skinParam;;

	private final List<Swimlane> swinlanes = new ArrayList<Swimlane>();
	private Instruction currentInstruction = new InstructionList();
	private LinkRendering nextLinkRenderer;

	public Swimlanes(ISkinParam skinParam) {
		this.skinParam = skinParam;
	}

	public Ftile getFtile() {
		FtileFactory factory = new VCompactFactory(skinParam, dummyStringBounder);
		factory = new FtileFactoryDelegatorAssembly(factory, skinParam);
		factory = new FtileFactoryDelegatorIf(factory, skinParam);
		factory = new FtileFactoryDelegatorWhile(factory, skinParam);
		factory = new FtileFactoryDelegatorRepeat(factory, skinParam);
		factory = new FtileFactoryDelegatorCreateFork(factory, skinParam);
		factory = new FtileFactoryDelegatorCreateSplit(factory, skinParam);
		factory = new FtileFactoryDelegatorAddNote(factory, skinParam);
		factory = new FtileFactoryDelegatorCreateGroup(factory, skinParam);
		return getCurrent().createFtile(factory);
	}

	public void swimlane(String name) {
		// TODO Auto-generated method stub

	}

	public void drawU(UGraphic ug) {
		getFtile().asTextBlock().drawU(ug);
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return getFtile().asTextBlock().calculateDimension(stringBounder);
	}

	public List<Url> getUrls() {
		throw new UnsupportedOperationException();
	}

	public Instruction getCurrent() {
		return currentInstruction;
	}

	public void setCurrent(Instruction current) {
		this.currentInstruction = current;
	}

	public LinkRendering nextLinkRenderer() {
		return nextLinkRenderer;
	}

	public void setNextLinkRenderer(LinkRendering link) {
		this.nextLinkRenderer = link;
	}

}
