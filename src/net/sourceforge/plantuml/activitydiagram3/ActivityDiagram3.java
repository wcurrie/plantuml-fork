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
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.vertical.VerticalFactory;
import net.sourceforge.plantuml.api.ImageData;
import net.sourceforge.plantuml.api.ImageDataSimple;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.StringBounderUtils;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.svek.DecorateEntityImage2;
import net.sourceforge.plantuml.ugraphic.CompressionTransform;
import net.sourceforge.plantuml.ugraphic.SlotFinder;
import net.sourceforge.plantuml.ugraphic.SlotSet;
import net.sourceforge.plantuml.ugraphic.TextLimitFinder;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UGraphicCompress;

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

	// @Override
	private Dimension2D exportDiagramInternalUnused(OutputStream os, CMapData cmap, int index,
			FileFormatOption fileFormatOption, List<BufferedImage> flashcodes) throws IOException {
		final TextBlock result = getResult();

		final TextLimitFinder limitFinder = new TextLimitFinder(dummyStringBounder, true);
		result.drawU(limitFinder, 0, 0);
		final double negX = Math.min(0, limitFinder.getMinX());
		final double negY = Math.min(0, limitFinder.getMinY());
		assert negX <= 0;
		assert negY <= 0;

		final ISkinParam skinParam = getSkinParam();
		Dimension2D dim = result.calculateDimension(dummyStringBounder);
		dim = new Dimension2DDouble(Math.max(dim.getWidth(), limitFinder.getMaxX() - negX), Math.max(dim.getHeight(),
				limitFinder.getMaxY() - negY));
		dim = Dimension2DDouble.delta(dim, 20);

		final double dpiFactor = getDpiFactor(fileFormatOption);

		final UGraphic ug = fileFormatOption.createUGraphic(skinParam.getColorMapper(), dpiFactor, dim, getSkinParam()
				.getBackgroundColor(), isRotation());

		final double posy = 5 - negY;
		result.drawU(ug, 8 - negX, posy);
		ug.writeImage(os, getMetadata(), getDpi(fileFormatOption));

		return new Dimension2DDouble(dim.getWidth(), dim.getHeight());
	}

	protected ImageData exportDiagramInternal(OutputStream os, int index, FileFormatOption fileFormatOption,
			List<BufferedImage> flashcodes) throws IOException {
		final TextBlock result = getResult();

		final TextLimitFinder limitFinder = new TextLimitFinder(dummyStringBounder, true);
		result.drawU(limitFinder, 0, 0);
		final double negX = Math.min(0, limitFinder.getMinX());
		final double negY = Math.min(0, limitFinder.getMinY());
		assert negX <= 0;
		assert negY <= 0;

		final SlotFinder slotFinder = new SlotFinder(dummyStringBounder);
		result.drawU(slotFinder, 0, 0);
		final SlotSet ysSlotSet = slotFinder.getYSlotSet().reverse().smaller(5.0);

		final ISkinParam skinParam = getSkinParam();
		Dimension2D dim = result.calculateDimension(dummyStringBounder);
		final CompressionTransform compressionTransform = new CompressionTransform(ysSlotSet);
		dim = new Dimension2DDouble(Math.max(dim.getWidth(), limitFinder.getMaxX() - negX),
				compressionTransform.transform(Math.max(dim.getHeight(), limitFinder.getMaxY() - negY)));
		dim = Dimension2DDouble.delta(dim, 20);

		final double dpiFactor = getDpiFactor(fileFormatOption);

		UGraphic ug = fileFormatOption.createUGraphic(skinParam.getColorMapper(), dpiFactor, dim, getSkinParam()
				.getBackgroundColor(), isRotation());
		// ug = new UGraphicFilter(ug, URectangle.class, UEllipse.class, UPolygon.class);

		// for (Slot sl : ysSlotSet) {
		// ug.getParam().setBackcolor(HtmlColorUtils.RED);
		// ug.draw(0, posy + sl.getStart(), new URectangle(dim.getWidth(), sl.size()));
		// }

		ug = new UGraphicCompress(ug, compressionTransform);
		result.drawU(ug, 8 - negX, 0);
		ug.writeImage(os, getMetadata(), getDpi(fileFormatOption));

		return new ImageDataSimple((int) dim.getWidth(), (int) dim.getHeight());
	}

	private Instruction current = new InstructionList();

	public void addActivity(Display activity, HtmlColor color) {
		current.add(new InstructionSimple(activity, color, nextLinkRenderer));
		nextLinkRenderer = null;
	}

	private Ftile getFtile() {
		return current.createFtile(new VerticalFactory(getSkinParam()));
	}

	private TextBlock getResult() {
		TextBlock result = getFtile();
		result = addTitle(result);
		result = addHeaderAndFooter(result);
		return result;
	}

	private TextBlock addTitle(TextBlock original) {
		final Display title = getTitle();
		if (title == null) {
			return original;
		}
		final TextBlock text = TextBlockUtils.create(title, new FontConfiguration(getFont(FontParam.TITLE),
				getFontColor(FontParam.TITLE, null)), HorizontalAlignement.CENTER, getSkinParam());

		return new DecorateEntityImage2(original, text, HorizontalAlignement.CENTER);
	}

	private TextBlock addHeaderAndFooter(TextBlock original) {
		final Display footer = getFooter();
		final Display header = getHeader();
		if (footer == null && header == null) {
			return original;
		}
		final TextBlock textFooter = footer == null ? null : TextBlockUtils
				.create(footer, new FontConfiguration(getFont(FontParam.FOOTER), getFontColor(FontParam.FOOTER, null)),
						getFooterAlignement(), getSkinParam());
		final TextBlock textHeader = header == null ? null : TextBlockUtils
				.create(header, new FontConfiguration(getFont(FontParam.HEADER), getFontColor(FontParam.HEADER, null)),
						getHeaderAlignement(), getSkinParam());

		return new DecorateEntityImage2(original, textHeader, getHeaderAlignement(), textFooter, getFooterAlignement());
	}

	private final UFont getFont(FontParam fontParam) {
		final ISkinParam skinParam = getSkinParam();
		return skinParam.getFont(fontParam, null);
	}

	private final HtmlColor getFontColor(FontParam fontParam, String stereo) {
		final ISkinParam skinParam = getSkinParam();
		return skinParam.getFontHtmlColor(fontParam, stereo);
	}

	public void fork() {
		final InstructionFork instructionFork = new InstructionFork(current);
		current.add(instructionFork);
		current = instructionFork;

	}

	public CommandExecutionResult forkAgain() {
		if (current instanceof InstructionFork) {
			((InstructionFork) current).forkAgain();
			return CommandExecutionResult.ok();
		}
		return CommandExecutionResult.error("Cannot find fork");
	}

	public CommandExecutionResult endFork() {
		if (current instanceof InstructionFork) {
			current = ((InstructionFork) current).getParent();
			return CommandExecutionResult.ok();
		}
		return CommandExecutionResult.error("Cannot find fork");
	}

	public void startIf(Display test, Display whenThen) {
		final InstructionIf instructionIf = new InstructionIf(current, test, whenThen, nextLinkRenderer);
		current.add(instructionIf);
		current = instructionIf;
	}

	public CommandExecutionResult endif() {
		if (current instanceof InstructionIf) {
			((InstructionIf) current).endif(nextLinkRenderer);
			nextLinkRenderer = null;
			current = ((InstructionIf) current).getParent();
			return CommandExecutionResult.ok();
		}
		return CommandExecutionResult.error("Cannot find if");
	}

	public CommandExecutionResult else2(Display whenElse) {
		if (current instanceof InstructionIf) {
			((InstructionIf) current).swithToElse(whenElse, nextLinkRenderer);
			nextLinkRenderer = null;
			return CommandExecutionResult.ok();
		}
		return CommandExecutionResult.error("Cannot find if");
	}

	public void startRepeat() {
		final InstructionRepeat instructionRepeat = new InstructionRepeat(current, nextLinkRenderer);
		current.add(instructionRepeat);
		current = instructionRepeat;

	}

	public CommandExecutionResult repeatWhile(Display label) {
		if (current instanceof InstructionRepeat) {
			final InstructionRepeat instructionRepeat = (InstructionRepeat) current;
			instructionRepeat.setTest(label, nextLinkRenderer);
			current = instructionRepeat.getParent();
			this.nextLinkRenderer = null;
			return CommandExecutionResult.ok();
		}
		return CommandExecutionResult.error("Cannot find repeat");

	}

	public void doWhile(Display test) {
		final InstructionWhile instructionWhile = new InstructionWhile(current, test, nextLinkRenderer);
		current.add(instructionWhile);
		current = instructionWhile;
	}

	public CommandExecutionResult endwhile() {
		if (current instanceof InstructionWhile) {
			((InstructionWhile) current).endwhile(nextLinkRenderer);
			nextLinkRenderer = null;
			current = ((InstructionWhile) current).getParent();
			return CommandExecutionResult.ok();
		}
		return CommandExecutionResult.error("Cannot find while");
	}

	public void start() {
		current.add(new InstructionStart());
	}

	public void stop() {
		current.add(new InstructionStop());
	}

	public CommandExecutionResult kill() {
		if (current.kill() == false) {
			return CommandExecutionResult.error("kill cannot be used here");
		}
		return CommandExecutionResult.ok();
	}

	public void startGroup(Display name) {
		final InstructionGroup instructionGroup = new InstructionGroup(current, name);
		current.add(instructionGroup);
		current = instructionGroup;
	}

	public CommandExecutionResult endGroup() {
		if (current instanceof InstructionGroup) {
			current = ((InstructionGroup) current).getParent();
			return CommandExecutionResult.ok();
		}
		return CommandExecutionResult.error("Cannot find group");
	}

	private LinkRendering nextLinkRenderer;

	public void setNextLinkRenderer(LinkRendering linkRenderer) {
		if (current instanceof InstructionList) {
			final Instruction last = ((InstructionList) current).getLast();
			if (last instanceof InstructionWhile) {
				((InstructionWhile) last).afterEndwhile(linkRenderer);
			}
		}
		this.nextLinkRenderer = linkRenderer;
	}

}
