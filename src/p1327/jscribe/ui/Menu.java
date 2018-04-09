package p1327.jscribe.ui;

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

import java.awt.Event;
import java.awt.Toolkit;
import java.awt.event.ActionListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import p1327.jscribe.ui.listener.SimpleMouseClickListener;
import p1327.jscribe.util.UIText;
import p1327.jscribe.util.Unserialzable;

public class Menu extends JMenuBar implements Unserialzable{

	private static final short upper = 'A' - 'a';
	private static final Border border = new EmptyBorder(0, 5, 0, 5);
	
	public Menu(SubMenu...subMenus) {
		for(SubMenu sm : subMenus)
			add(sm).setBorder(border);
	}
	
	public static JPopupMenu newContextMenu(JMenuItem...items) {
		JPopupMenu menu = new JPopupMenu();
		for(JMenuItem i : items)
			if(i != null)
				menu.add(i);
			else
				menu.addSeparator();
		return menu;
	}
	
	public static class SubMenu extends JMenu implements Unserialzable{
		
		
		public SubMenu(String name, SimpleMouseClickListener l) {
			this(name);
			// click doesn't work optimally
			addMouseListener(l);
		}
		
		public SubMenu(String name, JMenuItem...items) {
			this(name);
			for(JMenuItem i : items)
				if(i != null)
					add(i);
				else
					addSeparator();
		}
		
		private SubMenu(String name) {
			super(name);
		}
	}
	
	public static class Item extends JMenuItem implements MenuItemInit, Unserialzable{
		
		public Item(String name, String tooltip, ActionListener listener) {
			super(name);
			init(tooltip, listener);
		}
		
		public Item(String name, char shortcut, String tooltip, ActionListener listener) {
			super(name);
			init(shortcut, tooltip, listener);
		}
	}
	
	public static class RadioItem extends JRadioButtonMenuItem implements MenuItemInit, Unserialzable{

		public RadioItem(String name, String tooltip, ActionListener listener) {
			super(name);
			init(tooltip, listener);
		}
		
		public RadioItem(String name, char shortcut, String tooltip, ActionListener listener) {
			super(name);
			init(shortcut, tooltip, listener);
		}
		
		public RadioItem setSelected() {
			setSelected(true);
			return this;
		}
	}
	
	public static class CheckItem extends JCheckBoxMenuItem implements MenuItemInit, Unserialzable{

		public CheckItem(String name, String tooltip, ActionListener listener) {
			super(name);
			init(tooltip, listener);
		}
		
		public CheckItem(String name, char shortcut, String tooltip, ActionListener listener) {
			super(name);
			init(shortcut, tooltip, listener);
		}
		
		public CheckItem setSelected() {
			setSelected(true);
			return this;
		}
	}
	
	private static interface MenuItemInit {
		
		void setToolTipText(String text);
		void addActionListener(ActionListener l);
		void setAccelerator(KeyStroke keyStroke);
		
		default void init(String tooltip, ActionListener listener) {
			setToolTipText(UIText.displayable(tooltip));
			addActionListener(listener);
		}
		
		default void init(char shortcut, String tooltip, ActionListener listener) {
			init(tooltip, listener);
			if(shortcut >= 'A' && shortcut <= 'Z') {
				setAccelerator(KeyStroke.getKeyStroke(shortcut, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() | Event.SHIFT_MASK));
				return;
			}
			if(shortcut >= 'a' && shortcut <= 'z')
				shortcut += upper;
			
			setAccelerator(KeyStroke.getKeyStroke(shortcut, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		}
	}
}
