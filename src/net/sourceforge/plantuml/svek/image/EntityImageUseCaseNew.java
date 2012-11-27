/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2012, Arnaud Roques
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
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.svek.AbstractEntityImage;
import net.sourceforge.plantuml.svek.ShapeType;
import net.sourceforge.plantuml.ugraphic.TextBlockInEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UStroke;

public class EntityImageUseCaseNew extends AbstractEntityImage {

	final private TextBlock desc;

	final private List<Url> url;

	public EntityImageUseCaseNew(ILeaf entity, ISkinParam skinParam) {
		super(entity, skinParam);
		final Stereotype stereotype = entity.getStereotype();

		final TextBlock tmp = TextBlockUtils.create(
				entity.getDisplay(),
				new FontConfiguration(getFont(FontParam.USECASE, stereotype), getFontColor(FontParam.USECASE,
						stereotype)), HorizontalAlignement.CENTER, skinParam);

		if (stereotype == null || stereotype.getLabel() == null) {
			this.desc = tmp;
		} else {
			final TextBlock stereo = TextBlockUtils.create(
					Display.getWithNewlines(stereotype.getLabel()),
					new FontConfiguration(getFont(FontParam.USECASE_ACTOR_STEREOTYPE, stereotype), getFontColor(
							FontParam.USECASE_ACTOR_STEREOTYPE, null)), HorizontalAlignement.CENTER, skinParam);
			this.desc = TextBlockUtils.mergeTB(stereo, tmp, HorizontalAlignement.CENTER);
		}
		this.url = entity.getUrls();

	}

	@Override
	public Dimension2D getDimension(StringBounder stringBounder) {
		return new TextBlockInEllipse(desc, stringBounder).calculateDimension(stringBounder);
	}

	public void drawU(UGraphic ug, double xTheoricalPosition, double yTheoricalPosition) {
		final StringBounder stringBounder = ug.getStringBounder();
		final TextBlockInEllipse ellipse = new TextBlockInEllipse(desc, stringBounder);
		if (getSkinParam().shadowing()) {
			ellipse.setDeltaShadow(3);
		}

		if (url.size() > 0) {
			ug.startUrl(url.get(0));
		}

		ug.getParam().setStroke(new UStroke(1.5));
		ug.getParam().setColor(getColor(ColorParam.usecaseBorder, getStereo()));
		HtmlColor backcolor = getEntity().getSpecificBackColor();
		if (backcolor == null) {
			backcolor = getColor(ColorParam.usecaseBackground, getStereo());
		}
		ug.getParam().setBackcolor(backcolor);
		ellipse.drawU(ug, xTheoricalPosition, yTheoricalPosition);
		ug.getParam().setStroke(new UStroke());

		if (url.size() > 0) {
			ug.closeAction();
		}

	}

	public ShapeType getShapeType() {
		return ShapeType.OVAL;
	}

	public int getShield() {
		return 0;
	}

}
