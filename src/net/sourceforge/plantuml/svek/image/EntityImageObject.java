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
import net.sourceforge.plantuml.cucadiagram.EntityPortion;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.PortionShower;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockEmpty;
import net.sourceforge.plantuml.graphic.TextBlockLineBefore2;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.svek.AbstractEntityImage;
import net.sourceforge.plantuml.svek.ShapeType;
import net.sourceforge.plantuml.ugraphic.PlacementStrategyY1Y2;
import net.sourceforge.plantuml.ugraphic.Shadowable;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UGraphicHorizontalLine;
import net.sourceforge.plantuml.ugraphic.ULayoutGroup;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UStroke;

public class EntityImageObject extends AbstractEntityImage {

	final private TextBlock name;
	final private TextBlock stereo;
	final private TextBlock fields;
	final private List<Url> url;
	final private double roundCorner;

	public EntityImageObject(ILeaf entity, ISkinParam skinParam) {
		super(entity, skinParam);
		final Stereotype stereotype = entity.getStereotype();
		this.roundCorner = skinParam.getRoundCorner();
		this.name = TextBlockUtils.withMargin(
				TextBlockUtils.create(entity.getDisplay(), new FontConfiguration(SkinParamUtils.getFont(getSkinParam(), FontParam.OBJECT, stereotype),
						SkinParamUtils.getFontColor(getSkinParam(), FontParam.OBJECT, stereotype)), HorizontalAlignement.CENTER, skinParam), 2, 2);
		if (stereotype == null || stereotype.getLabel() == null) {
			this.stereo = null;
		} else {
			this.stereo = TextBlockUtils.create(
					Display.getWithNewlines(stereotype.getLabel()),
					new FontConfiguration(SkinParamUtils.getFont(getSkinParam(), FontParam.OBJECT_STEREOTYPE, stereotype), SkinParamUtils.getFontColor(getSkinParam(), FontParam.OBJECT_STEREOTYPE, stereotype)), HorizontalAlignement.CENTER, skinParam);
		}

		if (entity.getFieldsToDisplay().size() == 0) {
			this.fields = new TextBlockLineBefore2(new TextBlockEmpty(10, 16));
		} else {
			// this.fields =
			// entity.getFieldsToDisplay().asTextBlock(FontParam.OBJECT_ATTRIBUTE,
			// skinParam);
			this.fields = entity.getBody(new PortionShower() {
				public boolean showPortion(EntityPortion portion, ILeaf entity) {
					return true;
				}
			}).asTextBlock(FontParam.OBJECT_ATTRIBUTE, skinParam);

		}
		this.url = entity.getUrls();

	}

	private int marginEmptyFieldsOrMethod = 13;

	@Override
	public Dimension2D getDimension(StringBounder stringBounder) {
		final Dimension2D dimTitle = getTitleDimension(stringBounder);
		final Dimension2D dimFields = fields.calculateDimension(stringBounder);
		final double width = Math.max(dimFields.getWidth(), dimTitle.getWidth() + 2 * xMarginCircle);

		final double height = getMethodOrFieldHeight(dimFields) + dimTitle.getHeight();
		return new Dimension2DDouble(width, height);
	}

	private double getMethodOrFieldHeight(final Dimension2D dim) {
		final double fieldsHeight = dim.getHeight();
		if (fieldsHeight == 0) {
			return marginEmptyFieldsOrMethod;
		}
		return fieldsHeight;
	}

	private int xMarginCircle = 5;

	private Dimension2D getTitleDimension(StringBounder stringBounder) {
		return getNameAndSteretypeDimension(stringBounder);
	}

	private Dimension2D getNameAndSteretypeDimension(StringBounder stringBounder) {
		final Dimension2D nameDim = name.calculateDimension(stringBounder);
		final Dimension2D stereoDim = stereo == null ? new Dimension2DDouble(0, 0) : stereo
				.calculateDimension(stringBounder);
		final Dimension2D nameAndStereo = new Dimension2DDouble(Math.max(nameDim.getWidth(), stereoDim.getWidth()),
				nameDim.getHeight() + stereoDim.getHeight());
		return nameAndStereo;
	}

	public void drawU(UGraphic ug, double xTheoricalPosition, double yTheoricalPosition) {
		final StringBounder stringBounder = ug.getStringBounder();
		final Dimension2D dimTotal = getDimension(stringBounder);
		final Dimension2D dimTitle = getTitleDimension(stringBounder);

		final double widthTotal = dimTotal.getWidth();
		final double heightTotal = dimTotal.getHeight();
		final Shadowable rect = new URectangle(widthTotal, heightTotal, roundCorner, roundCorner);
		if (getSkinParam().shadowing()) {
			rect.setDeltaShadow(4);
		}

		ug.getParam().setColor(SkinParamUtils.getColor(getSkinParam(), ColorParam.objectBorder, getStereo()));
		HtmlColor backcolor = getEntity().getSpecificBackColor();
		if (backcolor == null) {
			backcolor = SkinParamUtils.getColor(getSkinParam(), ColorParam.objectBackground, getStereo());
		}
		ug.getParam().setBackcolor(backcolor);
		if (url.size() > 0) {
			ug.startUrl(url.get(0));
		}

		double x = xTheoricalPosition;
		double y = yTheoricalPosition;
		ug.getParam().setStroke(new UStroke(1.5));
		ug.drawNewWay(x, y, rect);
		ug.getParam().setStroke(new UStroke());

		final ULayoutGroup header = new ULayoutGroup(new PlacementStrategyY1Y2(ug.getStringBounder()));
		if (stereo != null) {
			header.add(stereo);
		}
		header.add(name);
		header.drawU(ug, x, y, dimTotal.getWidth(), dimTitle.getHeight());

		y += dimTitle.getHeight();

		x = xTheoricalPosition;
		ug.getParam().setColor(SkinParamUtils.getColor(getSkinParam(), ColorParam.objectBorder, getStereo()));

		final UGraphic ug2 = new UGraphicHorizontalLine(ug, x, x + widthTotal);
		fields.drawU(ug2, x, y);
		
		if (url.size() > 0) {
			ug.closeAction();
		}

	}

	public ShapeType getShapeType() {
		return ShapeType.RECTANGLE;
	}

	public int getShield() {
		return 0;
	}

}
