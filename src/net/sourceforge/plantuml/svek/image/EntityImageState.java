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
 * Revision $Revision: 5183 $
 *
 */
package net.sourceforge.plantuml.svek.image;

import java.awt.geom.Dimension2D;
import java.util.List;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.SkinParamUtils;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Member;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.svek.AbstractEntityImage;
import net.sourceforge.plantuml.svek.ShapeType;
import net.sourceforge.plantuml.ugraphic.Shadowable;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class EntityImageState extends AbstractEntityImage {

	final private TextBlock desc;
	final private TextBlock fields;
	final private List<Url> url;

	final private static int MIN_WIDTH = 50;
	final private static int MIN_HEIGHT = 50;

	final private boolean withSymbol;

	final static private double smallRadius = 3;
	final static private double smallLine = 3;
	final static private double smallMarginX = 7;
	final static private double smallMarginY = 4;

	public EntityImageState(IEntity entity, ISkinParam skinParam) {
		super(entity, skinParam);
		final Stereotype stereotype = entity.getStereotype();
		this.withSymbol = stereotype != null && stereotype.isWithOOSymbol();

		this.desc = TextBlockUtils.create(entity.getDisplay(),
				new FontConfiguration(SkinParamUtils.getFont(getSkinParam(), FontParam.STATE, stereotype),
						SkinParamUtils.getFontColor(getSkinParam(), FontParam.STATE, stereotype)),
				HorizontalAlignement.CENTER, skinParam);

		Display list = new Display();
		for (Member att : entity.getFieldsToDisplay()) {
			list = list.addAll(Display.getWithNewlines(att.getDisplay(true)));
		}

		this.url = entity.getUrls();

		this.fields = TextBlockUtils.create(list,
				new FontConfiguration(SkinParamUtils.getFont(getSkinParam(), FontParam.STATE_ATTRIBUTE, stereotype),
						SkinParamUtils.getFontColor(getSkinParam(), FontParam.STATE_ATTRIBUTE, stereotype)),
				HorizontalAlignement.LEFT, skinParam);

	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		final Dimension2D dim = Dimension2DDouble.mergeTB(desc.calculateDimension(stringBounder),
				fields.calculateDimension(stringBounder));
		double heightSymbol = 0;
		if (withSymbol) {
			heightSymbol += 2 * smallRadius + smallMarginY;
		}
		final Dimension2D result = Dimension2DDouble.delta(dim, MARGIN * 2 + 2 * MARGIN_LINE + heightSymbol);
		return Dimension2DDouble.atLeast(result, MIN_WIDTH, MIN_HEIGHT);
	}

	final public void drawUNewWayINLINED(UGraphic ug) {
		if (url.size() > 0) {
			ug.startUrl(url.get(0));
		}
		final StringBounder stringBounder = ug.getStringBounder();
		final Dimension2D dimTotal = calculateDimension(stringBounder);
		final Dimension2D dimDesc = desc.calculateDimension(stringBounder);

		final double widthTotal = dimTotal.getWidth();
		final double heightTotal = dimTotal.getHeight();
		final Shadowable rect = new URectangle(widthTotal, heightTotal, CORNER, CORNER);
		if (getSkinParam().shadowing()) {
			rect.setDeltaShadow(4);
		}

		ug = ug.apply(new UStroke(1.5)).apply(
				new UChangeColor(SkinParamUtils.getColor(getSkinParam(), ColorParam.stateBorder, getStereo())));
		HtmlColor backcolor = getEntity().getSpecificBackColor();
		if (backcolor == null) {
			backcolor = SkinParamUtils.getColor(getSkinParam(), ColorParam.stateBackground, getStereo());
		}
		ug = ug.apply(new UChangeBackColor(backcolor));

		ug.drawOldWay(rect);

		final double yLine = MARGIN + dimDesc.getHeight() + MARGIN_LINE;
		ug.drawNewWay(0, yLine, new ULine(widthTotal, 0));

		ug = ug.apply(new UStroke());

		if (withSymbol) {
			final double xSymbol = dimTotal.getWidth();
			final double ySymbol = dimTotal.getHeight();
			drawSymbol(ug, xSymbol, ySymbol);
		}

		final double xDesc = (widthTotal - dimDesc.getWidth()) / 2;
		final double yDesc = MARGIN;
		desc.drawUNewWayINLINED(ug.apply(new UTranslate(xDesc, yDesc)));

		final double xFields = MARGIN;
		final double yFields = yLine + MARGIN_LINE;
		fields.drawUNewWayINLINED(ug.apply(new UTranslate(xFields, yFields)));

		if (url.size() > 0) {
			ug.closeAction();
		}
	}

	public static void drawSymbol(UGraphic ug, double xSymbol, double ySymbol) {
		xSymbol -= 4 * smallRadius + smallLine + smallMarginX;
		ySymbol -= 2 * smallRadius + smallMarginY;
		final UEllipse small = new UEllipse(2 * smallRadius, 2 * smallRadius);
		ug.drawNewWay(xSymbol, ySymbol, small);
		ug.drawNewWay(xSymbol + smallLine + 2 * smallRadius, ySymbol, small);
		ug.drawNewWay(xSymbol + 2 * smallRadius, ySymbol + smallLine, new ULine(smallLine, 0));
	}

	public ShapeType getShapeType() {
		return ShapeType.ROUND_RECTANGLE;
	}

	public int getShield() {
		return 0;
	}

}
