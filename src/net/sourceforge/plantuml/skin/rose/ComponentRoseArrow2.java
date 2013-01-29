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
 * Revision $Revision: 9696 $
 *
 */
package net.sourceforge.plantuml.skin.rose;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

import net.sourceforge.plantuml.OptionFlags;
import net.sourceforge.plantuml.SpriteContainer;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.ArrowConfiguration;
import net.sourceforge.plantuml.skin.ArrowDecoration;
import net.sourceforge.plantuml.skin.ArrowDirection;
import net.sourceforge.plantuml.skin.ArrowDressing;
import net.sourceforge.plantuml.skin.ArrowHead;
import net.sourceforge.plantuml.skin.ArrowPart;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.UStroke;

public class ComponentRoseArrow2 extends AbstractComponentRoseArrow {

	private final HorizontalAlignement messagePosition;

	public ComponentRoseArrow2(HtmlColor foregroundColor, HtmlColor fontColor, UFont font, Display stringsToDisplay,
			ArrowConfiguration arrowConfiguration, HorizontalAlignement messagePosition,
			SpriteContainer spriteContainer, HorizontalAlignement textHorizontalAlignement) {
		super(foregroundColor, fontColor, font, stringsToDisplay, arrowConfiguration, spriteContainer,
				textHorizontalAlignement);
		this.messagePosition = messagePosition;
	}

	private final double spaceCrossX = 6;
	private final double diamCircle = 8;
	private final double thinCircle = 1.5;

	@Override
	public void drawInternalU(UGraphic ug, Area area) {
		final Dimension2D dimensionToUse = area.getDimensionToUse();
		final StringBounder stringBounder = ug.getStringBounder();
		final int textHeight = (int) getTextHeight(stringBounder);
		ug.getParam().setColor(getForegroundColor());

		final ArrowDressing dressing1 = getArrowConfiguration().getDressing1();
		final ArrowDressing dressing2 = getArrowConfiguration().getDressing2();

		double start = 0;
		double len = dimensionToUse.getWidth() - 1;

		final double pos1 = start + 1;
		final double pos2 = len - 1;

		if (dressing2.getDecoration() == ArrowDecoration.CIRCLE && dressing2.getHead() == ArrowHead.NONE) {
			len -= diamCircle / 2;
		}
		if (dressing2.getDecoration() == ArrowDecoration.CIRCLE && dressing2.getHead() != ArrowHead.NONE) {
			len -= diamCircle / 2 + thinCircle;
		}

		if (dressing1.getDecoration() == ArrowDecoration.CIRCLE && dressing1.getHead() == ArrowHead.NONE) {
			start += diamCircle / 2;
			len -= diamCircle / 2;
		}
		if (dressing1.getDecoration() == ArrowDecoration.CIRCLE && dressing1.getHead() == ArrowHead.NORMAL) {
			start += diamCircle + thinCircle;
			len -= diamCircle + thinCircle;
		}

		drawDressing1(ug, pos1, dressing1);
		drawDressing2(ug, pos2, dressing2);

		if (dressing2.getPart() == ArrowPart.FULL && dressing2.getHead() == ArrowHead.NORMAL) {
			len -= getArrowDeltaX() / 2;
		}
		if (dressing1.getPart() == ArrowPart.FULL && dressing1.getHead() == ArrowHead.NORMAL) {
			start += getArrowDeltaX() / 2;
			len -= getArrowDeltaX() / 2;
		}

		if (dressing2.getHead() == ArrowHead.CROSSX) {
			len -= 2 * spaceCrossX;
		}
		if (dressing1.getHead() == ArrowHead.CROSSX) {
			start += 2 * spaceCrossX;
			len -= 2 * spaceCrossX;
		}

		if (getArrowConfiguration().isDotted()) {
			stroke(ug, 2, 2);
		}
		ug.draw(start, textHeight, new ULine(len, 0));
		if (getArrowConfiguration().isDotted()) {
			ug.getParam().setStroke(new UStroke());
		}

		final ArrowDirection direction2 = getDirection2();
		final double textPos;
		if (messagePosition == HorizontalAlignement.CENTER) {
			final double textWidth = getTextBlock().calculateDimension(stringBounder).getWidth();
			textPos = (dimensionToUse.getWidth() - textWidth) / 2;
		} else if (messagePosition == HorizontalAlignement.RIGHT) {
			final double textWidth = getTextBlock().calculateDimension(stringBounder).getWidth();
			textPos = dimensionToUse.getWidth() - textWidth - getMarginX2()
					- (direction2 == ArrowDirection.LEFT_TO_RIGHT_NORMAL ? getArrowDeltaX() : 0);
		} else {
			textPos = getMarginX1()
					+ (direction2 == ArrowDirection.RIGHT_TO_LEFT_REVERSE
							|| direction2 == ArrowDirection.BOTH_DIRECTION ? getArrowDeltaX() : 0);
		}
		getTextBlock().drawU(ug, textPos, 0);
	}

	private void drawDressing1(UGraphic ug, double x, ArrowDressing dressing) {
		final StringBounder stringBounder = ug.getStringBounder();
		final int textHeight = (int) getTextHeight(stringBounder);

		if (dressing.getDecoration() == ArrowDecoration.CIRCLE) {
			ug.getParam().setStroke(new UStroke(thinCircle));
			ug.getParam().setColor(getForegroundColor());
			ug.getParam().setBackcolor(null);
			final UEllipse circle = new UEllipse(diamCircle, diamCircle);
			ug.draw(x - diamCircle / 2 - thinCircle, textHeight - diamCircle / 2 - thinCircle / 2, circle);
			ug.getParam().setStroke(new UStroke());
			x += diamCircle / 2 + thinCircle;
		}

		if (dressing.getHead() == ArrowHead.ASYNC) {
			if (dressing.getPart() != ArrowPart.BOTTOM_PART) {
				ug.draw(x - 1, textHeight, new ULine(getArrowDeltaX(), -getArrowDeltaY()));
			}
			if (dressing.getPart() != ArrowPart.TOP_PART) {
				ug.draw(x - 1, textHeight, new ULine(getArrowDeltaX(), getArrowDeltaY()));
			}
		} else if (dressing.getHead() == ArrowHead.CROSSX) {
			ug.getParam().setStroke(new UStroke(2));
			ug.draw(spaceCrossX, textHeight - getArrowDeltaX() / 2, new ULine(getArrowDeltaX(), getArrowDeltaX()));
			ug.draw(spaceCrossX, textHeight + getArrowDeltaX() / 2, new ULine(getArrowDeltaX(), -getArrowDeltaX()));
			ug.getParam().setStroke(new UStroke());
		} else if (dressing.getHead() == ArrowHead.NORMAL) {
			ug.getParam().setBackcolor(getForegroundColor());
			final UPolygon polygon = getPolygonReverse(dressing.getPart(), textHeight);
			ug.draw(x, 0, polygon);
			ug.getParam().setBackcolor(null);
		}

	}

	private void drawDressing2(UGraphic ug, double x, ArrowDressing dressing) {
		final StringBounder stringBounder = ug.getStringBounder();
		final int textHeight = (int) getTextHeight(stringBounder);

		if (dressing.getDecoration() == ArrowDecoration.CIRCLE) {
			ug.getParam().setStroke(new UStroke(thinCircle));
			ug.getParam().setColor(getForegroundColor());
			ug.getParam().setBackcolor(null);
			final UEllipse circle = new UEllipse(diamCircle, diamCircle);
			ug.draw(x - diamCircle / 2 + thinCircle, textHeight - diamCircle / 2 - thinCircle / 2, circle);
			ug.getParam().setStroke(new UStroke());
			x -= diamCircle / 2 + thinCircle;
		}

		if (dressing.getHead() == ArrowHead.ASYNC) {
			if (dressing.getPart() != ArrowPart.BOTTOM_PART) {
				ug.draw(x, textHeight, new ULine(-getArrowDeltaX(), -getArrowDeltaY()));
			}
			if (dressing.getPart() != ArrowPart.TOP_PART) {
				ug.draw(x, textHeight, new ULine(-getArrowDeltaX(), getArrowDeltaY()));
			}
		} else if (dressing.getHead() == ArrowHead.CROSSX) {
			ug.getParam().setStroke(new UStroke(2));
			ug.draw(x - spaceCrossX - getArrowDeltaX(), textHeight - getArrowDeltaX() / 2, new ULine(getArrowDeltaX(),
					getArrowDeltaX()));
			ug.draw(x - spaceCrossX - getArrowDeltaX(), textHeight + getArrowDeltaX() / 2, new ULine(getArrowDeltaX(),
					-getArrowDeltaX()));
			ug.getParam().setStroke(new UStroke());
		} else if (dressing.getHead() == ArrowHead.NORMAL) {
			ug.getParam().setBackcolor(getForegroundColor());
			final UPolygon polygon = getPolygonNormal(dressing.getPart(), textHeight, x);
			ug.draw(0, 0, polygon);
			ug.getParam().setBackcolor(null);
		}

	}

	private UPolygon getPolygonNormal(ArrowPart part, final int textHeight, final double x2) {
		final UPolygon polygon = new UPolygon();
		if (part == ArrowPart.TOP_PART) {
			polygon.addPoint(x2 - getArrowDeltaX(), textHeight - getArrowDeltaY());
			polygon.addPoint(x2, textHeight);
			polygon.addPoint(x2 - getArrowDeltaX(), textHeight);
		} else if (part == ArrowPart.BOTTOM_PART) {
			polygon.addPoint(x2 - getArrowDeltaX(), textHeight + 1);
			polygon.addPoint(x2, textHeight + 1);
			polygon.addPoint(x2 - getArrowDeltaX(), textHeight + getArrowDeltaY() + 1);
		} else {
			polygon.addPoint(x2 - getArrowDeltaX(), textHeight - getArrowDeltaY());
			polygon.addPoint(x2, textHeight);
			polygon.addPoint(x2 - getArrowDeltaX(), textHeight + getArrowDeltaY());
			if (OptionFlags.NICE_ARROW) {
				polygon.addPoint(x2 - getArrowDeltaX() + 4, textHeight);
			}
		}
		return polygon;
	}

	private UPolygon getPolygonReverse(ArrowPart part, final int textHeight) {
		final UPolygon polygon = new UPolygon();
		if (part == ArrowPart.TOP_PART) {
			polygon.addPoint(getArrowDeltaX(), textHeight - getArrowDeltaY());
			polygon.addPoint(0, textHeight);
			polygon.addPoint(getArrowDeltaX(), textHeight);
		} else if (part == ArrowPart.BOTTOM_PART) {
			polygon.addPoint(getArrowDeltaX(), textHeight + 1);
			polygon.addPoint(0, textHeight + 1);
			polygon.addPoint(getArrowDeltaX(), textHeight + getArrowDeltaY() + 1);
		} else {
			polygon.addPoint(getArrowDeltaX(), textHeight - getArrowDeltaY());
			polygon.addPoint(0, textHeight);
			polygon.addPoint(getArrowDeltaX(), textHeight + getArrowDeltaY());
			if (OptionFlags.NICE_ARROW) {
				polygon.addPoint(getArrowDeltaX() - 4, textHeight);
			}
		}
		return polygon;
	}

	public Point2D getStartPoint(StringBounder stringBounder, Dimension2D dimensionToUse) {
		final int textHeight = (int) getTextHeight(stringBounder);
		if (getDirection2() == ArrowDirection.LEFT_TO_RIGHT_NORMAL) {
			return new Point2D.Double(getPaddingX(), textHeight + getPaddingY());
		}
		return new Point2D.Double(dimensionToUse.getWidth() + getPaddingX(), textHeight + getPaddingY());
	}

	public Point2D getEndPoint(StringBounder stringBounder, Dimension2D dimensionToUse) {
		final int textHeight = (int) getTextHeight(stringBounder);
		if (getDirection2() == ArrowDirection.LEFT_TO_RIGHT_NORMAL) {
			return new Point2D.Double(dimensionToUse.getWidth() + getPaddingX(), textHeight + getPaddingY());
		}
		return new Point2D.Double(getPaddingX(), textHeight + getPaddingY());
	}

	final private ArrowDirection getDirection2() {
		return getArrowConfiguration().getArrowDirection();
	}

	@Override
	public double getPreferredHeight(StringBounder stringBounder) {
		return getTextHeight(stringBounder) + getArrowDeltaY() + 2 * getPaddingY();
	}

	@Override
	public double getPreferredWidth(StringBounder stringBounder) {
		return getTextWidth(stringBounder) + getArrowDeltaX();
	}

}
