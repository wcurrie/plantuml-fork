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
 * Revision $Revision: 10766 $
 *
 */
package net.sourceforge.plantuml.cucadiagram.dot;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.OptionFlags;
import net.sourceforge.plantuml.StringUtils;

abstract class AbstractGraphviz implements Graphviz {

	private final File dotExe;
	private final String dotString;
	private final String[] type;

	static boolean isWindows() {
		return File.separatorChar == '\\';
	}

	AbstractGraphviz(String dotString, String... type) {
		if (type == null) {
			throw new IllegalArgumentException();
		}
		this.dotExe = searchDotExe();
		this.dotString = dotString;
		this.type = type;
	}

	private File searchDotExe() {
		if (OptionFlags.getInstance().getDotExecutable() == null) {
			final String getenv = GraphvizUtils.getenvGraphvizDot();
			if (getenv == null) {
				return specificDotExe();
			}
			return new File(getenv);
		}

		return new File(OptionFlags.getInstance().getDotExecutable());

	}

	abstract protected File specificDotExe();

	final public void createFile(OutputStream os) throws IOException, InterruptedException {
		if (dotString == null) {
			throw new IllegalArgumentException();
		}

		if (illegalDotExe()) {
			// createPngNoGraphviz(os, new FileFormatOption(FileFormat.valueOf(type[0].toUpperCase())));
			throw new IllegalStateException();
		}
		final String cmd[] = getCommandLine();
		ProcessRunner p = null;
		try {
			Log.info("Starting Graphviz process " + cmd);
			Log.info("DotString size: " + dotString.length());
			p = new ProcessRunner(cmd);
			p.run(dotString.getBytes(), os);
			Log.info("Ending process ok");
		} catch (InterruptedException e) {
			Log.error("Interrupted");
		} catch (Throwable e) {
			e.printStackTrace();
			Log.error("Error: " + e);
			Log.error("The command was " + cmd);
			Log.error("");
			Log.error("Try java -jar plantuml.jar -testdot to figure out the issue");
			Log.error("");
		} finally {
			Log.info("Ending Graphviz process");
		}
		if (OptionFlags.getInstance().isCheckDotError() && p != null && p.getError().length() > 0) {
			Log.error("GraphViz error stream : " + p.getError());
			if (OptionFlags.getInstance().isCheckDotError()) {
				throw new IllegalStateException("Dot error " + p.getError());
			}
		}
		if (OptionFlags.getInstance().isCheckDotError() && p != null && p.getOut().length() > 0) {
			Log.error("GraphViz out stream : " + p.getOut());
			if (OptionFlags.getInstance().isCheckDotError()) {
				throw new IllegalStateException("Dot out " + p.getOut());
			}
		}

	}

	private boolean illegalDotExe() {
		return dotExe == null || dotExe.isFile() == false || dotExe.canRead() == false;
	}

	final public String dotVersion() throws IOException, InterruptedException {
		final String cmd[] = getCommandLineVersion();
		return executeCmd(cmd);
	}

	private String executeCmd(final String cmd[]) throws IOException, InterruptedException {
		final ProcessRunner p = new ProcessRunner(cmd);
		p.run(null, null);
		final StringBuilder sb = new StringBuilder();
		if (StringUtils.isNotEmpty(p.getOut())) {
			sb.append(p.getOut());
		}
		if (StringUtils.isNotEmpty(p.getError())) {
			if (sb.length() > 0) {
				sb.append(' ');
			}
			sb.append(p.getError());
		}
		return sb.toString().replace('\n', ' ').trim();
	}

	final String[] getCommandLine() {
		final String[] result = new String[type.length + 1];
		result[0] = getDotExe().getAbsolutePath();
		for (int i = 0; i < type.length; i++) {
			result[i + 1] = "-T" + type[i];
		}
		return result;
	}

	final String[] getCommandLineVersion() {
		return new String[] { getDotExe().getAbsolutePath(), "-V" };
	}

	public final File getDotExe() {
		return dotExe;
	}

	public final String getDotString() {
		return dotString;
	}

	public final List<String> getType() {
		return Arrays.asList(type);
	}

}
