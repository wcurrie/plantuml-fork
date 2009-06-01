/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques (for Atos Origin).
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
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
 * Original Author:  Arnaud Roques (for Atos Origin).
 *
 */
package net.sourceforge.plantuml.swing;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.prefs.Preferences;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.sourceforge.plantuml.DirWatcher;
import net.sourceforge.plantuml.GeneratedImage;

public class MainWindow extends JFrame {

	final private static Preferences prefs = Preferences
			.userNodeForPackage(MainWindow.class);
	final private static String KEY_DIR = "cur";

	private final JList jList1 = new JList();
	private final JScrollPane scrollPane;

	final private List<SimpleLine> currentDirectoryListing = new ArrayList<SimpleLine>();
	final private Set<ImageWindow> openWindows = new HashSet<ImageWindow>();

	private File dir;
	private DirWatcher dirWatcher;

	public MainWindow() {
		super("PlantUML");
		scrollPane = new JScrollPane(jList1);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		setSize(320, 200);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				init();

				tick();
				final Timer timer = new Timer(3000, new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						tick();
					}
				});
				timer.start();
			}
		});

		final MouseListener mouseListener = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					final int index = jList1.locationToIndex(e.getPoint());
					doubleClick((SimpleLine) jList1.getModel().getElementAt(
							index));
				}
			}
		};
		jList1.addMouseListener(mouseListener);

	}

	private void init() {
		final JFileChooser chooser = new JFileChooser();
		chooser.setDialogType(JFileChooser.CUSTOM_DIALOG);
		chooser.setDialogTitle("Directory to watch:");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		final String currentPath = prefs.get(KEY_DIR, ".");
		chooser.setCurrentDirectory(new File(currentPath));
		final int returnVal = chooser.showOpenDialog(MainWindow.this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			dir = chooser.getSelectedFile();
		} else {
			System.exit(0);
		}

		setTitle(dir.getAbsolutePath());
		prefs.put(KEY_DIR, dir.getAbsolutePath());
		dirWatcher = new DirWatcher(dir);
	}

	private void doubleClick(SimpleLine simpleLine) {
		for (ImageWindow win : openWindows) {
			if (win.getSimpleLine().equals(simpleLine)) {
				win.setVisible(true);
				win.setExtendedState(Frame.NORMAL);
				return;
			}
		}
		openWindows.add(new ImageWindow(simpleLine, this));
	}

	private void tick() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					final boolean changed = refreshDir();
					if (changed) {
						jList1.setListData(new Vector<SimpleLine>(
								currentDirectoryListing));
						jList1.setVisible(true);
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private boolean refreshDir() throws IOException, InterruptedException {
		final Collection<GeneratedImage> createdFiles2 = dirWatcher
				.buildCreatedFiles();

		boolean changed = false;

		for (GeneratedImage g : createdFiles2) {
			final SimpleLine simpleLine = new SimpleLine(g);
			mayRefreshImageWindow(g.getPngFile());
			if (currentDirectoryListing.contains(simpleLine) == false) {
				removeAllThatUseThisFile(g.getPngFile());
				currentDirectoryListing.add(simpleLine);
				changed = true;
			}
		}
		// for (SimpleLine simpleLine : currentDirectoryListing) {
		// final String newDesc = createdFiles.get(simpleLine.getFireResult());
		// if (newDesc != null) {
		// simpleLine.setDesc(newDesc);
		// mayRefreshImageWindow(simpleLine);
		// changed = true;
		// }
		//
		// }
		for (final Iterator<SimpleLine> it = currentDirectoryListing.iterator(); it
				.hasNext();) {
			final SimpleLine s = it.next();
			if (s.exists() == false) {
				it.remove();
				changed = true;
			}

		}
		Collections.sort(currentDirectoryListing);
		return changed;
	}

	private void removeAllThatUseThisFile(File pngFile) {
		for (final Iterator<SimpleLine> it = currentDirectoryListing.iterator(); it
				.hasNext();) {
			final SimpleLine line = it.next();
			if (line.getGeneratedImage().getPngFile().equals(pngFile)) {
				it.remove();
			}

		}

	}

	private void mayRefreshImageWindow(File pngFile) {
		for (ImageWindow win : openWindows) {
			if (pngFile.equals(win.getSimpleLine().getGeneratedImage()
					.getPngFile())) {
				win.refreshImage();
			}
		}

	}

	public void closing(ImageWindow imageWindow) {
		final boolean ok = openWindows.remove(imageWindow);
		if (ok == false) {
			throw new IllegalStateException();
		}
	}

	public static void main(String[] args) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException,
			UnsupportedLookAndFeelException {

		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
		}

		new MainWindow();

	}

}