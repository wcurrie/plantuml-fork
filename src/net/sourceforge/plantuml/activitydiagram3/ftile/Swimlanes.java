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

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.SpriteContainerEmpty;
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
import net.sourceforge.plantuml.activitydiagram3.ftile.vcompact.UGraphicInterceptorOneSwimlane;
import net.sourceforge.plantuml.activitydiagram3.ftile.vcompact.VCompactFactory;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.StringBounderUtils;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockInterceptorTextBlockable;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.graphic.UGraphicDelegator;
import net.sourceforge.plantuml.svek.UGraphicLineMerger;
import net.sourceforge.plantuml.ugraphic.LimitFinder;
import net.sourceforge.plantuml.ugraphic.MinMax;
import net.sourceforge.plantuml.ugraphic.UChange;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UShape;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class Swimlanes implements TextBlock {

	private static final StringBounder dummyStringBounder;

	static {
		final BufferedImage imDummy = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
		dummyStringBounder = StringBounderUtils.asStringBounder(imDummy.createGraphics());
	}

	private final ISkinParam skinParam;;

	private final List<Swimlane> swinlanes = new ArrayList<Swimlane>();
	private Swimlane currentSwimlane = null;

	private final Instruction root = new InstructionList();
	private Instruction currentInstruction = root;

	private LinkRendering nextLinkRenderer;

	public Swimlanes(ISkinParam skinParam) {
		this.skinParam = skinParam;
	}

	private FtileFactory getFtileFactory() {
		FtileFactory factory = new VCompactFactory(skinParam, dummyStringBounder);
		factory = new FtileFactoryDelegatorAssembly(factory, skinParam);
		factory = new FtileFactoryDelegatorIf(factory, skinParam);
		factory = new FtileFactoryDelegatorWhile(factory, skinParam);
		factory = new FtileFactoryDelegatorRepeat(factory, skinParam);
		factory = new FtileFactoryDelegatorCreateFork(factory, skinParam);
		factory = new FtileFactoryDelegatorCreateSplit(factory, skinParam);
		factory = new FtileFactoryDelegatorAddNote(factory, skinParam);
		factory = new FtileFactoryDelegatorCreateGroup(factory, skinParam);
		return factory;
	}

	public void swimlane(String name, HtmlColor color, Display label) {
		currentSwimlane = getOrCreate(name);
		if (color != null) {
			currentSwimlane.setSpecificBackcolor(color);
		}
		if (label != null) {
			currentSwimlane.setDisplay(label);
		}
	}

	private Swimlane getOrCreate(String name) {
		for (Swimlane s : swinlanes) {
			if (s.getName().equals(name)) {
				return s;
			}
		}
		final Swimlane result = new Swimlane(name);
		swinlanes.add(result);
		return result;
	}

	class Cross extends UGraphicDelegator {

		private Cross(UGraphic ug) {
			super(ug);
		}

		@Override
		public void draw(UShape shape) {
			if (shape instanceof Ftile) {
				final Ftile tile = (Ftile) shape;
				tile.asTextBlock().drawU(this);
			} else if (shape instanceof Connection) {
				final Connection connection = (Connection) shape;
				final Ftile tile1 = connection.getFtile1();
				final Ftile tile2 = connection.getFtile2();

				if (tile1 == null || tile2 == null) {
					return;
				}
				final Swimlane swimlane1 = tile1.getSwimlaneOut();
				final Swimlane swimlane2 = tile2.getSwimlaneIn();
				if (swimlane1 != swimlane2) {
					final ConnectionCross connectionCross = new ConnectionCross(connection);
					connectionCross.drawU(getUg());
				}
			}
		}

		public UGraphic apply(UChange change) {
			return new Cross(getUg().apply(change));
		}

	}

	public void drawU(final UGraphic ug) {
		final FtileFactory factory = getFtileFactory();
		TextBlock full = root.createFtile(factory).asTextBlock();
		if (swinlanes.size() <= 1) {
			full = new TextBlockInterceptorTextBlockable(full);
			// BUG42
			// full.drawU(ug);
			final UGraphicLineMerger ugLineMerger = new UGraphicLineMerger(ug);
			full.drawU(ugLineMerger);
			ugLineMerger.draw(new ULineFlush());
			return;
		}

		final StringBounder stringBounder = ug.getStringBounder();
		final Dimension2D dimensionFull = full.calculateDimension(stringBounder);

		final UFont font = skinParam.getFont(FontParam.TITLE, null);
		final FontConfiguration fc = new FontConfiguration(font, HtmlColorUtils.BLACK);

		double titlesHeight = 0;
		for (Swimlane swimlane : swinlanes) {
			final TextBlock swTitle = TextBlockUtils.create(swimlane.getDisplay(), fc, HorizontalAlignment.LEFT,
					new SpriteContainerEmpty());

			titlesHeight = Math.max(titlesHeight, swTitle.calculateDimension(stringBounder).getHeight());
		}
		double x = 0;
		final double separationMargin = 10;

		final UTranslate titleHeightTranslate = new UTranslate(0, titlesHeight);
		for (Swimlane swimlane : swinlanes) {
			final TextBlock swTitle = TextBlockUtils.create(swimlane.getDisplay(), fc, HorizontalAlignment.LEFT,
					new SpriteContainerEmpty());

			final LimitFinder limitFinder = new LimitFinder(stringBounder, false);
			full.drawU(new UGraphicInterceptorOneSwimlane(limitFinder, swimlane));
			final MinMax minMax = limitFinder.getMinMax();
			// System.err.println("minMax=" + minMax);

			final double drawingWidth = minMax.getWidth() + 2 * separationMargin;
			final double titleWidth = swTitle.calculateDimension(stringBounder).getWidth();
			final double totalWidth = Math.max(drawingWidth, titleWidth + 2 * separationMargin);

			if (swimlane.getSpecificBackColor() != null) {
				final UGraphic background = ug.apply(new UChangeBackColor(swimlane.getSpecificBackColor()))
						.apply(new UChangeColor(swimlane.getSpecificBackColor())).apply(new UTranslate(x, 0));
				background.draw(new URectangle(totalWidth, dimensionFull.getHeight() + titlesHeight));
			}

			final UTranslate translate = new UTranslate(x - minMax.getMinX() + separationMargin
					+ (totalWidth - drawingWidth) / 2.0, 0);
			swimlane.setTranslate(translate);

			final double posTitle = x + (totalWidth - titleWidth) / 2;
			swTitle.drawU(ug.apply(new UTranslate(posTitle, 0)));

			drawSeparation(ug.apply(new UTranslate(x, 0)), dimensionFull.getHeight() + titlesHeight);

			final UGraphic ugOneSwimlane = new UGraphicInterceptorOneSwimlane(ug, swimlane);

			full.drawU(ugOneSwimlane.apply(translate).apply(titleHeightTranslate));

			x += totalWidth;
		}
		drawSeparation(ug.apply(new UTranslate(x, 0)), dimensionFull.getHeight() + titlesHeight);
		full.drawU(new Cross(ug.apply(titleHeightTranslate)));
	}

	private void drawSeparation(UGraphic ug, double height) {
		ug.apply(new UStroke(2)).apply(new UChangeColor(HtmlColorUtils.BLACK)).draw(new ULine(0, height));
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return TextBlockUtils.getMinMax(this, stringBounder).getDimension();
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

	public Swimlane getCurrentSwimlane() {
		return currentSwimlane;
	}

}
