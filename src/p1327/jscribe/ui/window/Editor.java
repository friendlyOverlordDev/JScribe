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
import java.awt.Component;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.inet.jortho.SpellChecker;

import p1327.jscribe.io.FileHandler;
import p1327.jscribe.io.JSArchive;
import p1327.jscribe.io.JSS;
import p1327.jscribe.io.data.JSImg;
import p1327.jscribe.time.Time;
import p1327.jscribe.ui.DataViewer;
import p1327.jscribe.ui.ImageViewer;
import p1327.jscribe.ui.Menu;
import p1327.jscribe.ui.Menu.CheckItem;
import p1327.jscribe.ui.Menu.Item;
import p1327.jscribe.ui.Menu.RadioItem;
import p1327.jscribe.ui.Menu.SubMenu;
import p1327.jscribe.ui.Pagination;
import p1327.jscribe.ui.PlacementMode;
import p1327.jscribe.ui.StatusBar;
import p1327.jscribe.util.Message;
import p1327.jscribe.util.Static;
import p1327.jscribe.util.UIText;
import p1327.jscribe.util.Unserialzable;
import p1327.jscribe.util.Window;

public class Editor extends JFrame implements Unserialzable, Window {
	
	private static final String[] saveAsOptions = {"JSC", "Archive"};
	
	private static final String dictDirectory = "./dict/";
	private static final String enDict = dictDirectory + "dictionary_en.ortho";
	
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
	
	public final boolean hasSpellCheck;
	
	public final ImageViewer viewer;
	private final Pagination pages;
	public final DataViewer data;
	
	private final SubMenu zoom;
	private final Item save, saveAs, saveArchive, export, iJSS, eJSS, zoomIn, zoomOut, pmAddImg, pmOverrideImg, pmCopyImg, pmRenameImg, pmDeleteImg, undo, redo;
	private final RadioItem placeNote, placeText;
	private final CheckItem spellChecking;
	private final StatusBar status;
	private final JScrollPane content;
	
	private JSArchive jsa = null;
	private int currentImg = 0;
	private boolean _unsaved = false;
	private File last = new File(".");
	
	public Editor() {
		$ = this;
		setTitle();
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		center(1200, 900);
		
		boolean sc = false;
		try {
			File dictDir = new File(dictDirectory);
			if(!dictDir.isDirectory())
				throw new IOException("Can't find dictionary directory " + dictDir.getAbsolutePath());
			if(!new File(enDict).isFile())
				throw new IOException("Can't find dictionary at " + dictDir.getAbsolutePath());
			Locale.setDefault(Locale.forLanguageTag("en"));
			SpellChecker.registerDictionaries(new File("./dict/").toURI().toURL(), "en", "en");
			sc = true;
		}catch(Exception e) {
			Message.error("Failed to register Dictionary", e);
		}
		hasSpellCheck = sc;

		
		viewer = new ImageViewer();
		content = new JScrollPane(viewer);
		content.getVerticalScrollBar().setUnitIncrement(16);
		content.getHorizontalScrollBar().setUnitIncrement(16);
		add(content);
		
		JPanel wrapper = new JPanel(new BorderLayout());
		pages = new Pagination();
		pages.currentPage.add(e -> {
			setImgActive(e.newVal - 1);
		});
		wrapper.add(pages, BorderLayout.NORTH);
		
		data = new DataViewer();
		wrapper.add(data);
		add(wrapper, BorderLayout.EAST);
		
		status = new StatusBar();
		add(status, BorderLayout.SOUTH);
		
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
						new Item("Open JSC", 'o', "Open existing .jsc-file", e -> {
							if(isUnsavedAndCancel())
								return;
							
							openJSC.setCurrentDirectory(last);
							if(openJSC.showOpenDialog(Editor.this) == JFileChooser.APPROVE_OPTION)
								updateJSA(FileHandler.openJSC(last = openJSC.getSelectedFile()));
						}),
						new Item("Open Archive", 'O', "Open existing .jsa-file\n(archive with .jsc and images)", e -> {
							if(isUnsavedAndCancel())
								return;
							
							openArchive.setCurrentDirectory(last);
							if(openArchive.showOpenDialog(Editor.this) == JFileChooser.APPROVE_OPTION) 
								updateJSA(FileHandler.openJSA(last = openArchive.getSelectedFile()));
						}),
						null,
						save = new Item("Save", 's', "When already saved, overrides it,\notherwise saves as a new .jsc-file", e -> {
							save();
						}),
						saveAs = new Item("Save As...", "Saves project as .jsc or .jsa-file, forces the save dialog to open", e -> {
							if(jsa == null)
								return;
							saveAs();
						}),
						saveArchive = new Item("Save Archive", 'S', "Saves the project as .jsa\n(archive with .jsc and images)", e -> {
							if(jsa == null)
								return;
							if(jsa.name != null) {
								if(FileHandler.save(jsa))
									setSaved();
								return;
							}
							saveAsArchive.setCurrentDirectory(last);
							if(saveAsArchive.showSaveDialog(Editor.this) == JFileChooser.APPROVE_OPTION) {
								last =
								jsa.name = saveAsArchive.getSelectedFile();
								if(FileHandler.save(jsa))
									setSaved();
							}
						}),
						null,
						new Item("Import Image", 'i', "Creates a new project from an image", e -> {
							if(isUnsavedAndCancel())
								return;
							
							importImg.setCurrentDirectory(last);
							if(importImg.showOpenDialog(Editor.this) == JFileChooser.APPROVE_OPTION)
								updateJSA(FileHandler.importImg(last = importImg.getSelectedFile()));
						}),
						new Item("Import Folder", 'I', "Creates a new project from all images in a folder\n(" + Static.supportedTypes + ")", e -> {
							if(isUnsavedAndCancel())
								return;
							
							importFolder.setCurrentDirectory(last);
							if(importFolder.showOpenDialog(Editor.this) == JFileChooser.APPROVE_OPTION)
								updateJSA(FileHandler.importFolder(last = importFolder.getSelectedFile()));
						}),
						null,
						export = new Item("Export...", 'e', "Exports the files by merging text and images\n(output is writting in a new file/files)", e -> {
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
						new Item("Exit", 'q', "Closes this program", e -> {
							if(requestExit())
								Editor.this.dispose();
						})
				),
				new SubMenu("Edit",
						undo = new Item("Undo", 'z', "Undo the last action", e -> {
							Time.undo();
						}),
						redo = new Item("Redo", 'y', "Redo the last undone action", e-> {
							Time.redo();
						}),
						null,
						placeNote = new RadioItem("Place Notes", 'n', "Clicking on the image creates notes", e -> {
							viewer.setMode(PlacementMode.NOTE);
						}),
						placeText = new RadioItem("Place Text", 't', "Clicking and dragging on the image creates text", e -> {
							viewer.setMode(PlacementMode.TEXT);
						}),
						null,
						spellChecking = new CheckItem("Use Spell-Checking", "Enable or disable Spell-Checking (in english)", e -> {
							data.setSpellChecking(useSpellChecking());
						}).setSelected(),
						null,
						iJSS = new Item("Import JSS", "Import and override currently used style definitions", e -> {
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
						eJSS = new Item("Export JSS", "Export/save currently used style definitions", e -> {
							if(jsa == null)
								return;
							
							exportJSS.setCurrentDirectory(last);
							if(exportJSS.showSaveDialog(Editor.this) == JFileChooser.APPROVE_OPTION)
								FileHandler.exportJSS(jsa.jsc.jss.get(), last = exportJSS.getSelectedFile());
						}),
						null,
						pmAddImg = new Item("Add Image", "Adds a new image to the current project", e -> {
							importImg.setCurrentDirectory(last);
							if(importImg.showOpenDialog(Editor.this) == JFileChooser.APPROVE_OPTION) {
								last = importImg.getSelectedFile();
								JSArchive jsa = getJSA();
								try{
									jsa.add(last);
									pages.maxPage.set(jsa.size());
								}catch(Exception ex) {
									Message.error("Failed to add Image", ex);
								}
							}
						}),
						pmOverrideImg = new Item("Override Image", "Replaces the current image with a new one\n(content only; the text, notes and names will stay)", e -> {
							importImg.setCurrentDirectory(last);
							if(importImg.showOpenDialog(Editor.this) == JFileChooser.APPROVE_OPTION) {
								JSArchive njsa = FileHandler.importImg(last = importImg.getSelectedFile()),
										  jsa = getJSA();
								if(jsa.jscName != null) {
									try {
										FileHandler.copy(njsa.getFile(0), jsa.getFile(currentImg));
									}catch(Exception ex) {
										Message.error("Failed to override Image", ex);
										return;
									}
								}
								jsa.set(currentImg, njsa.get(0));
								viewer.repaint();
							}
						}),
						pmCopyImg = new Item("Copy Image", "Copies the current image, including notes and texts",  e -> {
							if(!Message.yesno("Save to copy Image", "All changes need to be saved to copy an image. Continue?"))
								return;
							save();
							JSArchive jsa = getJSA();
							JSImg img = jsa.jsc.imgs.get(currentImg);
							String old = img.img;
							String newName = Message.input("Copy Image", "The new name for the copy of " + old + " (without file extensions):");
							if(newName == null)
								return;
							if(newName.length() < 1 || newName.indexOf('\\') > -1 || newName.indexOf('/') > -1 || !Static.validateFile(newName)) {
								Message.error("Invalid Name", "Failed to copy the file, the name " + newName + " was invalid");
								return;
							}
							String ext = old.substring(old.lastIndexOf('.'));
							newName += ext;
							for(JSImg i : jsa.jsc.imgs)
								if(i.img.equals(newName)) {
									Message.error("Invalid Name", "File " + newName + " is already part of the project");
									return;
								}
							File newFile = null;
							if(jsa.jscName != null) {
								File current = jsa.getFile(currentImg);
								File parent = current.getParentFile();
								newFile = new File(parent.getAbsolutePath() + "/" + newName);
								if(newFile.exists()) {
									Message.error("Invalid Name", "File " + newFile + " already exists and isn't part of the project");
									return;
								}
								try {
									FileHandler.copy(jsa.getFile(currentImg), newFile);
								}catch(Exception ex) {
									Message.error("Copy Failed", ex);
									return;
								}
							}
							jsa.copy(currentImg, newName, newFile);
							updateJSA(jsa);
						}),
						pmRenameImg = new Item("Rename Image", "Renames the current image", e -> {
							if(!Message.yesno("Save to rename Image", "All changes need to be saved to rename an image. Continue?"))
								return;
							save();
							JSArchive jsa = getJSA();
							JSImg img = jsa.jsc.imgs.get(currentImg);
							String old = img.img;
							String newName = Message.input("Rename Image", "The new name for " + old + " (without file extensions):");
							if(newName == null)
								return;
							if(newName.length() < 1 || newName.indexOf('\\') > -1 || newName.indexOf('/') > -1 || !Static.validateFile(newName)) {
								Message.error("Invalid Name", "Failed to rename the file, the name '" + newName + "' was invalid");
								return;
							}
							String ext = old.substring(old.lastIndexOf('.'));
							newName += ext;
							for(JSImg i : jsa.jsc.imgs)
								if(i.img.equals(newName)) {
									Message.error("Invalid Name", "File " + newName + " is already part of the project");
									return;
								}
							if(jsa.jscName != null) {
								File current = jsa.getFile(currentImg);
								File parent = current.getParentFile();
								File newFile = new File(parent.getAbsolutePath() + "/" + newName);
								if(newFile.exists()) {
									Message.error("Invalid Name", "File " + newFile + " already exists and isn't part of the project");
									return;
								}
								current.renameTo(newFile);
								jsa.setFile(currentImg, newFile);
							}
							img.img = newName;
							setTitle();
						}),
						pmDeleteImg = new Item("Delete Image", "Deletes the current image", e -> {
							JSArchive jsa = getJSA();
							if(jsa.size() < 2) {
								Message.error("Delete Error", "Can't delete the last File in a Project");
								return;
							}
							String name = jsa.jsc.imgs.get(currentImg).img;
							if(Message.yesno("Delete Image?", "Are you sure you want to delete " + name + "?\nTexts and notes will be lost forever.")) {
								jsa.remove(currentImg);
								updateJSA(jsa);
							}
						})
				),
				zoom = new SubMenu("Zoom",
						zoomIn = new Item("Zoom in", (char)KeyEvent.VK_PLUS, "Increases the visual size of the image and text (doesn't apply to the export)",  e -> {
							updateZoom(1);
						}),
						zoomOut = new Item("Zoom out", '-', "Decreases the visual size of the image and text (doesn't apply to the export)", e -> {
							updateZoom(-1);
						}),
						null,
						new Item("Reset", '0', "Resets the zoom to the default", e -> {
							updateZoom(0);
						}) 
				),
				new SubMenu("Window",
						new CheckItem("show Non-Text-Elements", 'h', "If disabled, hides notes and the border around text-elements", e -> {
							CheckItem i = (CheckItem)e.getSource();
							viewer.showNonTextElements(i.isSelected());
						}).setSelected()//,
//						new CheckItem("enable Highlighting", 'H', "Darkens the unfocused areas", e -> {
//							CheckItem i = (CheckItem)e.getSource();
//						})
				),
				new SubMenu("Help",
						new Item("Homepage (GitHub)", "Opens the project website in your browser", e -> {
							try {
								Desktop.getDesktop().browse(new URI("https://github.com/friendlyOverlordDev/JScribe"));
							} catch (Exception ex) {
								Message.error("Failed to open the Homepage!", ex);
							}
						}),
						new Item("Donate", "Support the developer with a donation", e -> {
							new SupportDev();
						}),
						null,
						new Item("About JScribt", "Opens a window with general infos about this program", e -> {
							new About();
						})
				)
		));
		
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
			int k = e.getKeyCode();
			int t = e.getID();
			
			if(t == KeyEvent.KEY_PRESSED && (k == KeyEvent.VK_F5 || k == KeyEvent.VK_F1)) {
				LinkedList<Component> comps = new LinkedList<>();
				comps.push(Editor.this);
				Component c;
				while(!comps.isEmpty()) {
					c = comps.pop();
					c.validate();
					c.revalidate();
					if(c instanceof Container)
						comps.addAll(Arrays.asList(((Container) c).getComponents()));
				}
				repaint();
			}
			return false; // true consumes the event 
		});
		
		MouseWheelListener mwl = e -> {
			if(e.isControlDown()) {
				updateZoom(e.getWheelRotation());
				e.consume();
			}
		};
		
		addMouseWheelListener(mwl);
		content.addMouseWheelListener(mwl);
		viewer.addMouseWheelListener(mwl);
		viewer.addMouseWheelListener(e -> {
			if(e.isControlDown())
				return;
			JScrollBar v = content.getVerticalScrollBar();
			if(v.isVisible()) {
				v.setValue(v.getValue() + v.getUnitIncrement() * e.getWheelRotation() * 3);
				e.consume();
				return;
			}
			JScrollBar h = content.getHorizontalScrollBar();
			if(h.isVisible()) {
				h.setValue(h.getValue() + h.getUnitIncrement() * e.getWheelRotation() * 3);
			}
			e.consume();
		});
		content.setWheelScrollingEnabled(true);
		
		undo.setEnabled(false);
		redo.setEnabled(false);
		Time.timeEvent.add(()->{
			boolean u = Time.canUndo(),
					r = Time.canRedo();
			undo.setEnabled(u);
			redo.setEnabled(r);
			if(u || r)
				setUnsaved();
		});
		
		save.setEnabled(false);
		saveAs.setEnabled(false);
		saveArchive.setEnabled(false);
		export.setEnabled(false);
		iJSS.setEnabled(false);
		eJSS.setEnabled(false);
		
		pmAddImg.setEnabled(false);
		pmOverrideImg.setEnabled(false);
		pmCopyImg.setEnabled(false);
		pmRenameImg.setEnabled(false);
		pmDeleteImg.setEnabled(false);
		
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
		
		setVisible(true);
	}
	
	public void setUnsaved() {
		if(_unsaved == true)
			return;
		_unsaved = true;
		setTitle();
	}
	
	public void setTitle() {
		super.setTitle((isUnsaved() ? "*" : "") + "JScribe" + (jsa == null ? "" : " - " + jsa.jsc.imgs.get(currentImg).img));
	}
	
	private void setSaved() {
		_unsaved = false;
		setTitle();
		showMessage("Saved! (" + UIText.getTime() + ")");
	}
	
	public boolean isUnsaved() {
		return _unsaved;
	}
	
	/**
	 * Checkes if there are currently unsaved changes in the project.<br>
	 * If unsaved changes are found it asks the user if they want to continue.
	 * @return Returns true if the user wants to cancel the current task and continue with the loaded project. Otherwise false.
	 */
	boolean isUnsavedAndCancel() {
		if(!isUnsaved())
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
		if(!isUnsaved())
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

		_unsaved = false;
		
		setImgActive(0);
		
		save.setEnabled(true);
		saveAs.setEnabled(true);
		saveArchive.setEnabled(true);
		export.setEnabled(true);
		iJSS.setEnabled(true);
		eJSS.setEnabled(true);
		
		pmAddImg.setEnabled(true);
		pmOverrideImg.setEnabled(true);
		pmCopyImg.setEnabled(true);
		pmRenameImg.setEnabled(true);
		pmDeleteImg.setEnabled(true);
		
		Time.resetTime();
	}
	
	public JSArchive getJSA() {
		return jsa;
	}
	
	public void setImgActive(int index) {
		JSImg img = jsa.jsc.imgs.get(index);
		viewer.setImage(jsa.get(index), img);
		data.setImage(img);
		content.getVerticalScrollBar().setValue(0);
		content.getHorizontalScrollBar().setValue(0);
		content.revalidate();
		setTitle();
		currentImg = index;
	}
	
	public void showMessage(String msg) {
		status.setText(msg);
	}
	
	
	private int zoomLevel = 0;
	/**
	 * updates the zoom;<br>
	 * zoomlevels are: <br>
	 * -  12.5%
	 * -  25%
	 * -  50%
	 * - 100%
	 * - 200%
	 * - 400%
	 * - 800%
	 * @param step 1 = zoom in, -1 = zoom out, 0 = reset zoom
	 */
	private void updateZoom(int step) {
		if(step == 0)
			zoomLevel = 0;
		else
			zoomLevel += step;
		if(zoomLevel <= Static.minZoom) {
			zoomLevel = Static.minZoom;
			zoomOut.setEnabled(false);
		}else
			zoomOut.setEnabled(true);
		if(zoomLevel >= Static.maxZoom) {
			zoomLevel = Static.maxZoom;
			zoomIn.setEnabled(false);
		}else
			zoomIn.setEnabled(true);
		
		if(zoomLevel == 0)
			zoom.setText("Zoom");
		else
			zoom.setText("Zoom (" + UIText.getPercentage(Math.pow(2, zoomLevel), 1) + ")");
		
		viewer.setZoom(zoomLevel);
		content.revalidate();
	}
	
	public boolean useSpellChecking() {
		return spellChecking.isSelected();
	}
	
	void save() {
		if(jsa == null)
			return;
		if(jsa.name != null || jsa.jscName != null) {
			if(FileHandler.save(jsa))
				setSaved();
			return;
		}
		
		// file was never saved
		saveAs();
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
					setSaved();
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
					setSaved();
			}
		}
		// nothing was selected, the dialog was x-ed
	}
}
