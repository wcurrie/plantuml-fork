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

import net.sourceforge.plantuml.api.PSystemFactory1317;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.DiagramType;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagramFactory1317;
import net.sourceforge.plantuml.version.PSystemLicenseFactory1317;

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
		// factories.add(new ClassDiagramFactory());
		// factories.add(new ActivityDiagramFactory());
		// factories.add(new DescriptionDiagramFactory());
		// factories.add(new StateDiagramFactory());
		// factories.add(new ActivityDiagramFactory3());
		// factories.add(new CompositeDiagramFactory());
		// factories.add(new ObjectDiagramFactory());
		// factories.add(new PostIdDiagramFactory());
		// factories.add(new PrintSkinFactory());
		factories.add(new PSystemLicenseFactory1317());
		// factories.add(new PSystemVersionFactory());
		// factories.add(new PSystemDonorsFactory());
		// factories.add(new PSystemListFontsFactory());
		// factories.add(new PSystemSaltFactory(DiagramType.SALT));
		// factories.add(new PSystemSaltFactory(DiagramType.UML));
		// factories.add(new PSystemDotFactory(DiagramType.DOT));
		// factories.add(new PSystemDotFactory(DiagramType.UML));
		// if (License.isCloseSource() == false) {
		// factories.add(new PSystemDitaaFactory(DiagramType.DITAA));
		// factories.add(new PSystemDitaaFactory(DiagramType.UML));
		// factories.add(new PSystemJcckitFactory(DiagramType.JCCKIT));
		// factories.add(new PSystemJcckitFactory(DiagramType.UML));
		// factories.add(new PSystemLogoFactory());
		// factories.add(new PSystemSudokuFactory());
		// factories.add(new PSystemTuringFactory());
		// }
		// factories.add(new PSystemEggFactory());
		// factories.add(new PSystemAppleTwoFactory());
		// factories.add(new PSystemRIPFactory());
		// factories.add(new PSystemLostFactory());
		// factories.add(new PSystemPathFactory());
		// factories.add(new PSystemOregonFactory());
		// if (License.isCloseSource() == false) {
		// factories.add(new PSystemXearthFactory());
		// }
		// factories.add(new PSystemProjectFactory2());
		// factories.add(new FlowDiagramFactory());
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
