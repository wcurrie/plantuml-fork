/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
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
 * Revision $Revision: 4636 $
 *
 */
package net.sourceforge.plantuml.real;

import java.util.Arrays;
import java.util.Collection;

public class RealUtils {

	public static Real createOrigin() {
		final RealLine line = new RealLine();
		final RealImpl result = new RealImpl("O", line, 0);
		return result;
	}

	public static Real middle(Real r1, Real r2) {
		return new RealMiddle((RealMoveable) r1, (RealMoveable) r2);
	}

	public static Real max(Collection<Real> reals) {
		double v = -Double.MAX_VALUE;
		final RealLine line = ((RealMoveable) reals.iterator().next()).getLine();
		final RealImpl result = new RealImpl("max", line, 0);
		for (Real r : reals) {
			final RealImpl rr = (RealImpl) r;
			if (rr.getLine() != line) {
				throw new IllegalArgumentException();
			}
			if (rr.getCurrentValue() > v) {
				v = rr.getCurrentValue();
			}
			line.addForce(new PositiveForce(rr, result, 0));
		}
		result.move(v);
		return result;
	}

	public static Real max(Real... reals) {
		return max(Arrays.asList(reals));
	}

	public static Real min(Collection<Real> reals) {
		double v = Double.MAX_VALUE;
		final RealLine line = ((RealImpl) reals.iterator().next()).getLine();
		final RealImpl result = new RealImpl("min", line, 0);
		for (Real r : reals) {
			final RealImpl rr = (RealImpl) r;
			if (rr.getLine() != line) {
				throw new IllegalArgumentException();
			}
			if (rr.getCurrentValue() < v) {
				v = rr.getCurrentValue();
			}
			line.addForce(new NegativeForce(rr, result, 0));
		}
		result.move(v);
		return result;
	}

	public static Real min(Real... reals) {
		return min(Arrays.asList(reals));
	}

}
