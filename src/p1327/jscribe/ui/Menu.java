package p1327.jscribe.ui;

import java.awt.Event;
import java.awt.Toolkit;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import p1327.jscribe.util.UIText;
import p1327.jscribe.util.Unserialzable;

public class Menu extends JMenuBar implements Unserialzable{
	
	public Menu(SubMenu...subMenus) {
		for(SubMenu sm : subMenus)
			add(sm);
	}
	
	public static class SubMenu extends JMenu implements Unserialzable{
		
		public SubMenu(String name, ActionListener listener) {
			super(name);
			addActionListener(listener);
		}
		
		public SubMenu(String name, Item...items) {
			super(name);
			for(Item i : items)
				if(i != null)
					add(i);
				else
					addSeparator();
		}
	}
	
	public static class Item extends JMenuItem implements Unserialzable{
		
		private static final short upper = 'A' - 'a';
		
		public Item(String name, String tooltip, ActionListener listener) {
			super(name);
			setToolTipText(UIText.displayable(tooltip));
			addActionListener(listener);
		}
		
		public Item(String name, char shortcut, String tooltip, ActionListener listener) {
			super(name);
			setToolTipText(UIText.displayable(tooltip));
			addActionListener(listener);
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
