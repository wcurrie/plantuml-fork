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
 * Revision $Revision: 5079 $
 *
 */
package net.sourceforge.plantuml.hector;

import net.sourceforge.plantuml.geom.LineSegmentDouble;

class UnlinarCompressedPlan {

	private final UnlinearCompression compX;
	private final UnlinearCompression compY;

	public UnlinarCompressedPlan(double inner, double outer) {
		this(inner, outer, inner, outer);
	}

	public UnlinarCompressedPlan(double innerx, double outerx, double innery, double outery) {
		this.compX = new UnlinearCompression(innerx, outerx);
		this.compY = new UnlinearCompression(innery, outery);
	}

	public HectorPath uncompress(LineSegmentDouble segment) {
		double x1 = segment.getX1();
		double y1 = segment.getY1();
		final double x2 = segment.getX2();
		final double y2 = segment.getY2();
		final HectorPath result = new HectorPath();
		final double x[] = compX.encounteredSingularities(x1, x2);
		if (x.length == 0) {
			result.add(getUncompressedSegment(x1, y1, x2, y2));
			return result;
		}
		for (int i = 0; i < x.length; i++) {
			final double y = segment.getIntersectionVertical(x[i]);
			result.add(getUncompressedSegment(x1, y1, x[i], y));
			x1 = x[i];
			y1 = y;
		}
		result.add(getUncompressedSegment(x1, y1, x2, y2));
		return result;
	}

	private LineSegmentDouble getUncompressedSegment(final double x1, final double y1, final double x2, final double y2) {
		final LineSegmentDouble un1 = new LineSegmentDouble(compX.uncompress(x1), compY.uncompress(y1),
				compX.uncompress(x2), compY.uncompress(y2));
		return un1;
	}

	// private LineSegmentDouble getUncompressedSegmentRoundBefore(final double x1, final double y1, final double x2,
	// final double y2) {
	// final LineSegmentDouble un1 = new LineSegmentDouble(compX.uncompress(x1), compY.uncompress(y1),
	// compX.uncompress(x2) - compX.innerSize(), compY.uncompress(y2));
	// return un1;
	// }

}
