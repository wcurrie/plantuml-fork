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

import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileFactory;
import net.sourceforge.plantuml.cucadiagram.Display;

public class InstructionIf implements Instruction {

	private final InstructionList thenList = new InstructionList();
	private final InstructionList elseList = new InstructionList();
	private final Instruction parent;
	private final Display labelTest;
	private final Display whenThen;
	private Display whenElse;
	private InstructionList current = thenList;

	public InstructionIf(Instruction parent, Display labelTest, Display whenThen) {
		this.parent = parent;
		this.labelTest = labelTest;
		this.whenThen = whenThen;
	}

	public void add(Instruction ins) {
		current.add(ins);
	}

	public Ftile createFtile(FtileFactory factory) {
		return factory.createIf(thenList.createFtile(factory), elseList.createFtile(factory), labelTest, whenThen,
				whenElse);
	}

	public Instruction getParent() {
		return parent;
	}

	public void swithToElse(Display whenElse) {
		this.whenElse = whenElse;
		this.current = elseList;
	}

	public boolean kill() {
		return current.kill();
	}

}
