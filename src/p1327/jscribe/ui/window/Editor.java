package p1327.jscribe.ui.window;

/*
 * Copyright (c) 2018 friendlyOverlordDev
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

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.net.URI;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import p1327.jscribe.io.FileHandler;
import p1327.jscribe.io.JSArchive;
import p1327.jscribe.io.JSS;
import p1327.jscribe.io.data.JSImg;
import p1327.jscribe.ui.DataViewer;
import p1327.jscribe.ui.ImageViewer;
import p1327.jscribe.ui.Menu;
import p1327.jscribe.ui.Menu.CheckItem;
import p1327.jscribe.ui.Menu.Item;
import p1327.jscribe.ui.Menu.RadioItem;
import p1327.jscribe.ui.Menu.SubMenu;
import p1327.jscribe.ui.Pagination;
import p1327.jscribe.ui.PlacementMode;
import p1327.jscribe.util.Message;
import p1327.jscribe.util.Static;
import p1327.jscribe.util.Unserialzable;
import p1327.jscribe.util.Window;

public class Editor extends JFrame implements Unserialzable, Window {
	
	private static final String[] saveAsOptions = {"JSC", "Archive"};
	
	public static Editor $;
	
	private final JFileChooser openJSC,
							   openArchive,
							   saveAsJSC,
							   saveAsArchive,
							   importImg,
							   importFolder,
							   exportImg,
							   exportFolder,
							   importJSS,
							   exportJSS;
	
	public final ImageViewer viewer;
	private final Pagination pages;
	public final DataViewer data;
	
	private final Item save, saveAs, saveArchive, export, iJSS, eJSS;
	private final RadioItem placeNote, placeText;
	
	private JSArchive jsa = null;
	private boolean unsaved = false;
	private File last = new File(".");
	
	public Editor() {
		$ = this;
		setTitle("JScribe");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		center(1200, 900);
		
		viewer = new ImageViewer();
		add(new JScrollPane(viewer));
		
		JPanel wrapper = new JPanel(new BorderLayout());
		pages = new Pagination();
		pages.currentPage.add(e -> {
			setImgActive(jsa.jsc.imgs.get(e.newVal - 1));
		});
		wrapper.add(pages, BorderLayout.NORTH);
		
		data = new DataViewer();
		wrapper.add(data);
		add(wrapper, BorderLayout.EAST);
		
		openJSC = new JFileChooser();
		openJSC.setDialogTitle("Select .jsc-File to Open...");
		openJSC.setFileFilter(new FileNameExtensionFilter("JScribe-File (jsc)", "jsc"));
		
		openArchive = new JFileChooser();
		openArchive.setDialogTitle("Select .jsa-Archive to Open...");
		openArchive.setFileFilter(new FileNameExtensionFilter("JScribe-Archive (jsa)", "jsa"));
		
		saveAsJSC = new JFileChooser();
		saveAsJSC.setDialogTitle("Saving .jsc-File to...");
		saveAsJSC.setFileFilter(new FileNameExtensionFilter("JScribe-File (jsc)", "jsc"));
		saveAsJSC.setDialogType(JFileChooser.SAVE_DIALOG);
		
		saveAsArchive = new JFileChooser();
		saveAsArchive.setDialogTitle("Saving .jsa-Archive to...");
		saveAsArchive.setFileFilter(new FileNameExtensionFilter("JScribe-Archive (jsa)", "jsa"));
		saveAsArchive.setDialogType(JFileChooser.SAVE_DIALOG);
		
		importImg = new JFileChooser();
		importImg.setDialogTitle("Select Image to Import...");
		importImg.setFileFilter(new FileNameExtensionFilter("Graphics (" + Static.supportedTypes + ")", Static.supportedTypeList));
		
		importFolder = new JFileChooser();
		importFolder.setDialogTitle("Select Image-Directory to Import...");
		importFolder.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		exportImg = new JFileChooser();
		exportImg.setDialogTitle("Export Image to...");
		exportImg.setFileFilter(new FileNameExtensionFilter("Graphics (" + Static.supportedTypes + ")", Static.supportedTypeList));
		exportImg.setDialogType(JFileChooser.SAVE_DIALOG);
		
		exportFolder = new JFileChooser();
		exportFolder.setDialogTitle("Export Images to ... (Directory)");
		exportFolder.setDialogType(JFileChooser.SAVE_DIALOG);
		exportFolder.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		importJSS = new JFileChooser();
		importJSS.setDialogTitle("Select Style Definitions (JSS) to Import...");
		importJSS.setFileFilter(new FileNameExtensionFilter("JScribe Style Definitions (jss)", "jss"));
		
		exportJSS = new JFileChooser();
		exportJSS.setDialogTitle("Export Style Definitions (JSS) to...");
		exportJSS.setFileFilter(new FileNameExtensionFilter("Graphics (jss)", "jss"));
		exportJSS.setDialogType(JFileChooser.SAVE_DIALOG);
		
		setJMenuBar(new Menu(
				new SubMenu("File",
						new Item("Open JSC", 'o', "open existing .jsc-file", e -> {
							if(isUnsavedAndCancel())
								return;
							
							openJSC.setCurrentDirectory(last);
							if(openJSC.showOpenDialog(Editor.this) == JFileChooser.APPROVE_OPTION)
								updateJSA(FileHandler.openJSC(last = openJSC.getSelectedFile()));
						}),
						new Item("Open Archive", 'O', "open existing .jsa-file\n(archive with .jsc and images)", e -> {
							if(isUnsavedAndCancel())
								return;
							
							openArchive.setCurrentDirectory(last);
							if(openArchive.showOpenDialog(Editor.this) == JFileChooser.APPROVE_OPTION) 
								updateJSA(FileHandler.openJSA(last = openArchive.getSelectedFile()));
						}),
						null,
						save = new Item("Save", 's', "when already saved, overrides it,\notherwise saves as a new .jsc-file", e -> {
							if(jsa == null)
								return;
							if(jsa.name != null || jsa.jscName != null) {
								if(FileHandler.save(jsa))
									unsaved = false;
								return;
							}
							
							// file was never saved
							saveAs();
								
						}),
						saveAs = new Item("Save As...", "saves project as .jsc or .jsa-file, forces the save dialog to open", e -> {
							if(jsa == null)
								return;
							saveAs();
						}),
						saveArchive = new Item("Save Archive", 'S', "saves the project as .jsa\n(archive with .jsc and images)", e -> {
							if(jsa == null)
								return;
							if(jsa.name != null)
								if(FileHandler.save(jsa)) {
									unsaved = false;
									return;
								}
							saveAsArchive.setCurrentDirectory(last);
							if(saveAsArchive.showSaveDialog(Editor.this) == JFileChooser.APPROVE_OPTION) {
								last =
								jsa.name = saveAsArchive.getSelectedFile();
								if(FileHandler.save(jsa))
									unsaved = false;
							}
						}),
						null,
						new Item("Import Image", 'i', "creates a new project from an image", e -> {
							if(isUnsavedAndCancel())
								return;
							
							importImg.setCurrentDirectory(last);
							if(importImg.showOpenDialog(Editor.this) == JFileChooser.APPROVE_OPTION)
								updateJSA(FileHandler.importImg(last = importImg.getSelectedFile()));
						}),
						new Item("Import Folder", 'I', "creates a new project from all images in a folder\n(" + Static.supportedTypes + ")", e -> {
							if(isUnsavedAndCancel())
								return;
							
							importFolder.setCurrentDirectory(last);
							if(importFolder.showOpenDialog(Editor.this) == JFileChooser.APPROVE_OPTION)
								updateJSA(FileHandler.importFolder(last = importFolder.getSelectedFile()));
						}),
						null,
						export = new Item("Export...", 'e', "exports the files by merging text and images\n(output is writting in a new file/files)", e -> {
							if(jsa == null)
								return;
							if(jsa.name != null) {
								exportFolder.setCurrentDirectory(last);
								if(exportFolder.showSaveDialog(Editor.this) == JFileChooser.APPROVE_OPTION)
									FileHandler.exportFolder(jsa, last = exportFolder.getSelectedFile());
							}else if(jsa.jscName != null || jsa.size() == 1) {
								exportImg.setCurrentDirectory(last);
								if(exportImg.showSaveDialog(Editor.this) == JFileChooser.APPROVE_OPTION)
									FileHandler.exportImg(jsa, last = exportImg.getSelectedFile());
							} else {
								exportFolder.setCurrentDirectory(last);
								if(exportFolder.showSaveDialog(Editor.this) == JFileChooser.APPROVE_OPTION)
									FileHandler.exportFolder(jsa, last = exportFolder.getSelectedFile());
							}
						}),
						null,
						new Item("Exit", 'q', "closes this program", e -> {
							if(requestExit())
								Editor.this.dispose();
						})
				),
				new SubMenu("Edit",
						iJSS = new Item("Import JSS", "import and override currently used style definitions", e -> {
							if(jsa == null)
								return;
							if(!Message.yesno("Import JSS", "You are about to import a JSS-File, this will remove all other Styles.\nContinue?"))
								return;
							
							importJSS.setCurrentDirectory(last);
							if(importJSS.showOpenDialog(Editor.this) == JFileChooser.APPROVE_OPTION) {
								JSS jss = FileHandler.importJSS(last = importJSS.getSelectedFile());
								if(jss != null) {
									jsa.jsc.jss.set(jss);
									viewer.repaint();
								}
							}
						}),
						eJSS = new Item("Export JSS", "export/save currently used style definitions", e -> {
							if(jsa == null)
								return;
							
							exportJSS.setCurrentDirectory(last);
							if(exportJSS.showSaveDialog(Editor.this) == JFileChooser.APPROVE_OPTION)
								FileHandler.exportJSS(jsa.jsc.jss.get(), last = exportJSS.getSelectedFile());
						}),
						null,
						placeNote = new RadioItem("Place Notes", 'n', "clicking on the image creates notes", e -> {
							viewer.setMode(PlacementMode.NOTE);
						}),
						placeText = new RadioItem("Place Text", 't', "clicking and dragging on the image creates text", e -> {
							viewer.setMode(PlacementMode.TEXT);
						})
						// add another image
				),
				new SubMenu("Window",
						new CheckItem("show Non-Text-Elements", 'h', "if disabled, hides notes and the border around text-elements", e -> {
							CheckItem i = (CheckItem)e.getSource();
							viewer.showNonTextElements(i.isSelected());
						}).setSelected(),
						new CheckItem("enable Highlighting", 'H', "darkens the unfocused areas", e -> {
							CheckItem i = (CheckItem)e.getSource();
							System.out.println(i.isSelected());
						})
				),
				new SubMenu("Help",
						new Item("Homepage (GitHub)", "opens the project website in your browser", e -> {
							try {
								Desktop.getDesktop().browse(new URI("https://github.com/friendlyOverlordDev/JScribe"));
							} catch (Exception ex) {
								Message.error("Failed to open the Homepage!", ex);
							}
						}),
						null,
						new Item("About JScribt", "opens a window with general infos about this program", e -> {
							new About();
						})
				),
				new SubMenu("Donate", e -> {
					new SupportDev();
				}),
				new SubMenu("test", e -> {
					new StyleEditor();
				})
		));
		
		save.setEnabled(false);
		saveAs.setEnabled(false);
		saveArchive.setEnabled(false);
		export.setEnabled(false);
		iJSS.setEnabled(false);
		eJSS.setEnabled(false);
		
		
		ButtonGroup group = new ButtonGroup();
		group.add(placeNote);
		group.add(placeText);
		
		placeText.setSelected(true);
		
		
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
		
		updateJSA(FileHandler.openJSA(new File("./test/doge.jsa")));
		
		setVisible(true);
	}
	
	/**
	 * Checkes if there are currently unsaved changes in the project.<br>
	 * If unsaved changes are found it asks the user if they want to continue.
	 * @return Returns true if the user wants to cancel the current task and continue with the loaded project. Otherwise false.
	 */
	boolean isUnsavedAndCancel() {
		if(!unsaved)
			return false;
		
		// x and cancel will preserve changes
		return !Message.okcancel("Continue without saving?",
				"There are unsaved chages! If you continue, you will lose your changes.\nOk -> Discard changes and continue.\nCancel -> Cancel task and preserve changes.");
	}
	
	/**
	 * checks if we can exit
	 * @return true if we can, otherwise false
	 */
	boolean requestExit() {
		if(!unsaved)
			return true;
		return Message.yesno("Close without saving?", "There are unsaved chages!\nClose the Program anyway?");
	}
	
	void updateJSA(JSArchive jsa) {
		if(jsa == null)
			return;
		this.jsa = jsa;
		
		Vector<JSImg> imgs = jsa.jsc.imgs;
		
		pages.maxPage.set(imgs.size());
		pages.currentPage.set(1);
		
		setImgActive(imgs.get(0));
		
		unsaved = false;
		
		save.setEnabled(true);
		saveAs.setEnabled(true);
		saveArchive.setEnabled(true);
		export.setEnabled(true);
		iJSS.setEnabled(true);
		eJSS.setEnabled(true);
	}
	
	public void setImgActive(JSImg i) {
		viewer.setImage(jsa.get(0), i);
		data.setImage(i);
		setTitle("JScribe - " + i.img);
	}
	
	void saveAs() {
		int result = JOptionPane.showOptionDialog(null, "Save text alone (JSC) or bundle with images (Archive; JSA)?", "JSC or Archive?",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, saveAsOptions, null);
		if(result == 0) { // == JSC
			saveAsJSC.setCurrentDirectory(last);
			if(saveAsJSC.showSaveDialog(Editor.this) == JFileChooser.APPROVE_OPTION) {
				File f = saveAsJSC.getSelectedFile();
				String name = f.getName();
				if(!name.endsWith(".jsc"))
					f = new File(f.getParent() + "/" + name + ".jsc");
				last =
				jsa.jscName = f;
				jsa.name = null;
				if(FileHandler.save(jsa))
					unsaved = false;
			}
		} else if(result == 1) { // == Archive
			saveAsArchive.setCurrentDirectory(last);
			if(saveAsArchive.showSaveDialog(Editor.this) == JFileChooser.APPROVE_OPTION) {
				File f = saveAsArchive.getSelectedFile();
				String name = f.getName();
				if(!name.endsWith(".jsa"))
					f = new File(f.getParent() + "/" + name + ".jsa");
				last =
				jsa.name = f;
				jsa.jscName = null;
				if(FileHandler.save(jsa))
					unsaved = false;
			}
		}
		// nothing was selected, the dialog was x-ed
	}
}
