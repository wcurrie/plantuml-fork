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
 * Revision $Revision: 5207 $
 * 
 */
package net.sourceforge.plantuml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.plantuml.acearth.PSystemXearthFactory1317;
import net.sourceforge.plantuml.activitydiagram.ActivityDiagramFactory1317;
import net.sourceforge.plantuml.activitydiagram3.ActivityDiagramFactory3_1317;
import net.sourceforge.plantuml.api.PSystemFactory1317;
import net.sourceforge.plantuml.classdiagram.ClassDiagramFactory1317;
import net.sourceforge.plantuml.compositediagram.CompositeDiagramFactory1317;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.DiagramType;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.creole.PSystemCreoleFactory1317;
import net.sourceforge.plantuml.descdiagram.DescriptionDiagramFactory1317;
import net.sourceforge.plantuml.directdot.PSystemDotFactory1317;
import net.sourceforge.plantuml.ditaa.PSystemDitaaFactory1317;
import net.sourceforge.plantuml.donors.PSystemDonorsFactory1317;
import net.sourceforge.plantuml.eggs.PSystemAppleTwoFactory1317;
import net.sourceforge.plantuml.eggs.PSystemEggFactory1317;
import net.sourceforge.plantuml.eggs.PSystemLostFactory1317;
import net.sourceforge.plantuml.eggs.PSystemPathFactory1317;
import net.sourceforge.plantuml.eggs.PSystemRIPFactory1317;
import net.sourceforge.plantuml.flowdiagram.FlowDiagramFactory1317;
import net.sourceforge.plantuml.font.PSystemListFontsFactory1317;
import net.sourceforge.plantuml.jcckit.PSystemJcckitFactory1317;
import net.sourceforge.plantuml.logo.PSystemLogoFactory1317;
import net.sourceforge.plantuml.objectdiagram.ObjectDiagramFactory1317;
import net.sourceforge.plantuml.oregon.PSystemOregonFactory1317;
import net.sourceforge.plantuml.postit.PostIdDiagramFactory1317;
import net.sourceforge.plantuml.printskin.PrintSkinFactory1317;
import net.sourceforge.plantuml.project2.PSystemProjectFactory2_1317;
import net.sourceforge.plantuml.salt.PSystemSaltFactory1317;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagramFactory1317;
import net.sourceforge.plantuml.statediagram.StateDiagramFactory1317;
import net.sourceforge.plantuml.sudoku.PSystemSudokuFactory1317;
import net.sourceforge.plantuml.turing.PSystemTuringFactory1317;
import net.sourceforge.plantuml.version.License;
import net.sourceforge.plantuml.version.PSystemLicenseFactory1317;
import net.sourceforge.plantuml.version.PSystemVersionFactory1317;

public class PSystemBuilder1317 {

	final public Diagram createPSystem(final List<? extends CharSequence> strings) {

		final List<PSystemFactory1317> factories = getAllFactories();

		final DiagramType type = DiagramType.getTypeFromArobaseStart(strings.get(0).toString());

		final UmlSource umlSource = new UmlSource(strings, type == DiagramType.UML);
		final DiagramType diagramType = umlSource.getDiagramType();
		final List<PSystemError> errors = new ArrayList<PSystemError>();
		for (PSystemFactory1317 systemFactory : factories) {
			if (diagramType != systemFactory.getDiagramType()) {
				continue;
			}
			final Diagram sys = systemFactory.createSystem(umlSource);
			if (isOk(sys)) {
				return sys;
			}
			errors.add((PSystemError) sys);
		}

		final PSystemError err = merge(errors);
		if (OptionFlags.getInstance().isQuiet() == false) {
			err.print(System.err);
		}
		return err;

	}

	private List<PSystemFactory1317> getAllFactories() {
		final List<PSystemFactory1317> factories = new ArrayList<PSystemFactory1317>();
		factories.add(new SequenceDiagramFactory1317());
		factories.add(new ClassDiagramFactory1317());
		factories.add(new ActivityDiagramFactory1317());
		factories.add(new DescriptionDiagramFactory1317());
		factories.add(new StateDiagramFactory1317());
		factories.add(new ActivityDiagramFactory3_1317());
		factories.add(new CompositeDiagramFactory1317());
		factories.add(new ObjectDiagramFactory1317());
		factories.add(new PostIdDiagramFactory1317());
		factories.add(new PrintSkinFactory1317());
		factories.add(new PSystemLicenseFactory1317());
		factories.add(new PSystemVersionFactory1317());
		factories.add(new PSystemDonorsFactory1317());
		factories.add(new PSystemListFontsFactory1317());
		factories.add(new PSystemSaltFactory1317(DiagramType.SALT));
		factories.add(new PSystemSaltFactory1317(DiagramType.UML));
		factories.add(new PSystemDotFactory1317(DiagramType.DOT));
		factories.add(new PSystemDotFactory1317(DiagramType.UML));
		if (License.getCurrent() == License.GPL) {
			factories.add(new PSystemDitaaFactory1317(DiagramType.DITAA));
			factories.add(new PSystemDitaaFactory1317(DiagramType.UML));
			factories.add(new PSystemJcckitFactory1317(DiagramType.JCCKIT));
			factories.add(new PSystemJcckitFactory1317(DiagramType.UML));
			factories.add(new PSystemLogoFactory1317());
			factories.add(new PSystemSudokuFactory1317());
			factories.add(new PSystemTuringFactory1317());
		}
		factories.add(new PSystemCreoleFactory1317());
		factories.add(new PSystemEggFactory1317());
		factories.add(new PSystemAppleTwoFactory1317());
		factories.add(new PSystemRIPFactory1317());
		factories.add(new PSystemLostFactory1317());
		factories.add(new PSystemPathFactory1317());
		factories.add(new PSystemOregonFactory1317());
		if (License.getCurrent() == License.GPL) {
			factories.add(new PSystemXearthFactory1317());
		}
		factories.add(new PSystemProjectFactory2_1317());
		factories.add(new FlowDiagramFactory1317());
		return factories;
	}

	private PSystemError merge(Collection<PSystemError> ps) {
		UmlSource source = null;
		final List<ErrorUml> errors = new ArrayList<ErrorUml>();
		for (PSystemError system : ps) {
			if (system.getSource() != null && source == null) {
				source = system.getSource();
			}
			errors.addAll(system.getErrorsUml());
		}
		if (source == null) {
			throw new IllegalStateException();
		}
		return new PSystemError(source, errors);
	}

	private boolean isOk(Diagram ps) {
		if (ps == null || ps instanceof PSystemError) {
			return false;
		}
		return true;
	}

}
