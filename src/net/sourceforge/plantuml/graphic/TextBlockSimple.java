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
 * Revision $Revision: 10583 $
 *
 */
package net.sourceforge.plantuml.graphic;

import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.EmbededDiagram;
import net.sourceforge.plantuml.SpriteContainer;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

class TextBlockSimple implements TextBlock {

	private List<Line> lines2;

	private final Display texts;
	private final FontConfiguration fontConfiguration;
	private final HorizontalAlignement horizontalAlignement;
	private final SpriteContainer spriteContainer;
	private final double maxMessageSize;

	protected TextBlockSimple(Display texts, FontConfiguration fontConfiguration,
			HorizontalAlignement horizontalAlignement, SpriteContainer spriteContainer, double maxMessageSize) {
		this.texts = texts;
		this.fontConfiguration = fontConfiguration;
		this.horizontalAlignement = horizontalAlignement;
		this.spriteContainer = spriteContainer;
		this.maxMessageSize = maxMessageSize;
	}

	private List<Line> getLines(StringBounder stringBounder) {
		if (lines2 == null) {
			if (stringBounder == null) {
				throw new IllegalStateException();
			}
			this.lines2 = new ArrayList<Line>();
			for (CharSequence s : texts) {
				if (s instanceof Stereotype) {
					lines2.addAll(createLinesForStereotype(fontConfiguration, (Stereotype) s, horizontalAlignement,
							spriteContainer));
				} else if (s instanceof EmbededDiagram) {
					lines2.add(new EmbededSystemLine((EmbededDiagram) s));
				} else {
					addInLines(stringBounder, s.toString());
				}
			}
		}
		return lines2;
	}

	private void addInLines(StringBounder stringBounder, String s) {
		if (maxMessageSize == 0) {
			addSingleLine(s);
		} else if (maxMessageSize > 0) {
			final StringTokenizer st = new StringTokenizer(s, " ", true);
			final StringBuilder currentLine = new StringBuilder();
			while (st.hasMoreTokens()) {
				final String token = st.nextToken();
				final double w = getTextWidth(stringBounder, currentLine + token);
				if (w > maxMessageSize) {
					addSingleLineNoSpace(currentLine.toString());
					currentLine.setLength(0);
					if (token.startsWith(" ") == false) {
						currentLine.append(token);
					}
				} else {
					currentLine.append(token);
				}
			}
			addSingleLineNoSpace(currentLine.toString());
		} else if (maxMessageSize < 0) {
			final StringBuilder currentLine = new StringBuilder();
			for (int i = 0; i < s.length(); i++) {
				final char c = s.charAt(i);
				final double w = getTextWidth(stringBounder, currentLine.toString() + c);
				if (w > -maxMessageSize) {
					addSingleLineNoSpace(currentLine.toString());
					currentLine.setLength(0);
					if (c != ' ') {
						currentLine.append(c);
					}
				} else {
					currentLine.append(c);
				}
			}
			addSingleLineNoSpace(currentLine.toString());
		}
	}

	private void addSingleLineNoSpace(String s) {
		if (s.length() == 0 || s.matches("^\\s*$ ")) {
			return;
		}
		lines2.add(new SingleLine(s, fontConfiguration, horizontalAlignement, spriteContainer));
	}

	private void addSingleLine(String s) {
		lines2.add(new SingleLine(s, fontConfiguration, horizontalAlignement, spriteContainer));
	}

	private double getTextWidth(StringBounder stringBounder, String s) {
		final Line line = new SingleLine(s, fontConfiguration, horizontalAlignement, spriteContainer);
		return line.calculateDimension(stringBounder).getWidth();
	}

	public List<Url> getUrls() {
		final List<Url> result = new ArrayList<Url>();
		for (Line line : getLines(null)) {
			result.addAll(line.getUrls());
		}
		return Collections.unmodifiableList(result);
	}

	private List<SingleLine> createLinesForStereotype(FontConfiguration fontConfiguration, Stereotype s,
			HorizontalAlignement horizontalAlignement, SpriteContainer spriteContainer) {
		assert s.getLabel() != null;
		final List<SingleLine> result = new ArrayList<SingleLine>();
		for (String st : s.getLabels()) {
			result.add(new SingleLine(st, fontConfiguration.add(FontStyle.ITALIC), horizontalAlignement,
					spriteContainer));
		}
		return Collections.unmodifiableList(result);
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return getTextDimension(stringBounder);
	}

	protected final Dimension2D getTextDimension(StringBounder stringBounder) {
		double width = 0;
		double height = 0;
		for (Line line : getLines(stringBounder)) {
			final Dimension2D size2D = line.calculateDimension(stringBounder);
			height += size2D.getHeight();
			width = Math.max(width, size2D.getWidth());
		}
		return new Dimension2DDouble(width, height);
	}

	public void drawUNewWayINLINED(UGraphic ug) {
		double y = 0;
		final Dimension2D dimText = getTextDimension(ug.getStringBounder());

		for (Line line : getLines(ug.getStringBounder())) {
			final HorizontalAlignement lineHorizontalAlignement = line.getHorizontalAlignement();
			double deltaX = 0;
			if (lineHorizontalAlignement == HorizontalAlignement.CENTER) {
				final double diff = dimText.getWidth() - line.calculateDimension(ug.getStringBounder()).getWidth();
				deltaX = diff / 2.0;
			} else if (lineHorizontalAlignement == HorizontalAlignement.RIGHT) {
				final double diff = dimText.getWidth() - line.calculateDimension(ug.getStringBounder()).getWidth();
				deltaX = diff;
			}
			line.drawUNewWayINLINED(ug.apply(new UTranslate(deltaX, y)));
			y += line.calculateDimension(ug.getStringBounder()).getHeight();
		}
	}

}
