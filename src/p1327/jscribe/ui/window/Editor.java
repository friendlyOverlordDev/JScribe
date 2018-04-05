package p1327.jscribe.ui.window;

/*
 * Copyright (c) 2018 your friendly Overlord & friendlyOverlordDev
 * 
 * This file is part of JScribe.
 * 
 * JScribe is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * JScribe is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with JScribe.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

import p1327.jscribe.ui.Menu;
import p1327.jscribe.ui.Menu.Item;
import p1327.jscribe.ui.Menu.SubMenu;
import p1327.jscribe.util.Static;
import p1327.jscribe.util.Unserialzable;
import p1327.jscribe.util.Window;

public class Editor extends JFrame implements Unserialzable, Window {
	
	private final JFileChooser openJSC,
							   openArchive,
							   saveAsJSC,
							   saveAsArchive,
							   importImg,
							   importFolder,
							   exportImg,
							   exportFolder;
	
	public Editor() {
		setTitle("JScribe");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setLayout(null);
		center(1000, 900);
		
		openJSC = new JFileChooser();
		openJSC.setDialogTitle("Select .jsc-File to Open...");
		openJSC.setFileFilter(new FileNameExtensionFilter("JScribe-File", "jsc"));
		
		openArchive = new JFileChooser();
		openArchive.setDialogTitle("Select .jsa-Archive to Open...");
		openArchive.setFileFilter(new FileNameExtensionFilter("JScribe-Archive", "jsa"));
		
		saveAsJSC = new JFileChooser();
		saveAsJSC.setDialogTitle("Saving .jsc-File to...");
		saveAsJSC.setFileFilter(new FileNameExtensionFilter("JScribe-File", "jsc"));
		saveAsJSC.setDialogType(JFileChooser.SAVE_DIALOG);
		
		saveAsArchive = new JFileChooser();
		saveAsArchive.setDialogTitle("Saving .jsa-Archive to...");
		saveAsArchive.setFileFilter(new FileNameExtensionFilter("JScribe-Archive", "jsa"));
		saveAsArchive.setDialogType(JFileChooser.SAVE_DIALOG);
		
		importImg = new JFileChooser();
		importImg.setDialogTitle("Select Image to Import...");
		importImg.setFileFilter(new FileNameExtensionFilter("Graphics", Static.supportedTypeList));
		
		importFolder = new JFileChooser();
		importFolder.setDialogTitle("Select Image to Import...");
		importFolder.setFileFilter(new FileNameExtensionFilter("Graphics", Static.supportedTypeList));
		
		exportImg = new JFileChooser();
		exportImg.setDialogTitle("Export Image to...");
		exportImg.setFileFilter(new FileNameExtensionFilter("Graphics", Static.supportedTypeList));
		exportImg.setDialogType(JFileChooser.SAVE_DIALOG);
		
		exportFolder = new JFileChooser();
		exportFolder.setDialogTitle("Export Images to...");
		exportFolder.setFileFilter(new FileNameExtensionFilter("Graphics", Static.supportedTypeList));
		exportFolder.setDialogType(JFileChooser.SAVE_DIALOG);
		
		
		JButton b = new JButton("test");
		b.setLocation(10, 10);
		b.setSize(100, 25);
		add(b);

		setJMenuBar(new Menu(
				new SubMenu("File",
						new Item("Open JSC", 'o', "open existing .jsc-file", e -> System.out.println("test")),
						new Item("Open Archive", 'O', "open existing .jsa-file\n(archive with .jsc and images)", e -> System.out.println("test2")),
						null,
						new Item("Save", 's', "when already saved, overrides it,\notherwise saves as a new .jsc-file", e -> System.out.println("test")),
						new Item("Save As...", "saves project as .jsc or .jsa-file, forces the save dialog to open", e -> System.out.println("test2")),
						new Item("Save Archive", 'S', "saves the project as .jsa\n(archive with .jsc and images)", e -> System.out.println("test2")),
						null,
						new Item("Import Image", 'i', "creates a new project from an image", e -> {
							
						}),
						new Item("Import Folder", 'I', "creates a new project from all images in a folder\n(" + Static.supportedTypes + ")", e -> System.out.println("test2")),
						null,
						new Item("Export...", 'e', "exports the files by merging text and images\n(output is writting in a new file/files)", e -> System.out.println("test2")),
						null,
						new Item("Exit", "closes this program", e -> {
							if(requestExit())
								Editor.this.dispose();
						})
				)
		));
		
		addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {}
			
			@Override
			public void windowIconified(WindowEvent e) {}
			
			@Override
			public void windowDeiconified(WindowEvent e) {}
			
			@Override
			public void windowDeactivated(WindowEvent e) {}
			
			@Override
			public void windowClosing(WindowEvent e) {
				if(requestExit())
					Editor.this.setDefaultCloseOperation(EXIT_ON_CLOSE); // better than dispose();
			}
			
			@Override
			public void windowClosed(WindowEvent e) {}
			
			@Override
			public void windowActivated(WindowEvent e) {}
		});
		
		setVisible(true);
	}
	
	boolean requestExit() {
		return true;
	}
}
