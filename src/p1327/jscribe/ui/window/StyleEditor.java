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
import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import p1327.jscribe.io.data.TaggedStyle;
import p1327.jscribe.io.data.TextStyle;
import p1327.jscribe.ui.StyleElement;
import p1327.jscribe.util.Message;
import p1327.jscribe.util.Unserialzable;
import p1327.jscribe.util.Window;

public class StyleEditor extends JDialog implements Window, Unserialzable {
	
	private static final Border division = new EmptyBorder(0, 10, 0, 0); 
	
	private final JPanel addButtonWrapper;
	
	private final TextStyle ts;
	
	public StyleEditor(final TextStyle ts) {
		this.ts = ts;
		
		setTitle("Style Editor");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setResizable(false);
		final JPanel wrapper = new JPanel();
		wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.X_AXIS));
		
		wrapper.add(new StyleElement(ts.main(), "Main").setMain(true));
		for(TaggedStyle s : ts)
			createExtensionStyleElement(s, wrapper);

		addButtonWrapper = new JPanel(new BorderLayout());
		addButtonWrapper.setBorder(division);
		
		JButton addButton = new JButton("+");
		addButton.addActionListener(e -> {
			String[] options = ts.jss.getStyleList();
			String o = Message.choice("Select Style", "Chose the style to add:", options);
			if(o == null)
				return;
			if(o == options[0]) {
				String result = Message.input("New Style", "Enter the name of the new style:");
				if(result == null)
					return;
				if(result.length() < 1) {
					Message.error("Invalid Name", "The given name was to short... (one character or more needed)");
					return;
				}
				if(result.indexOf('<') > -1 || result.indexOf(' ') > -1) {
					Message.error("Invalid Name", "The given name contained invalid characters... ('<', ' ')");
					return;
				}
				o = result;
			}

			wrapper.remove(addButtonWrapper);
			createExtensionStyleElement(ts.add(o), wrapper);
			wrapper.add(addButtonWrapper);
			pack();
			repaint();
		});
		
		addButtonWrapper.add(addButton);
		wrapper.add(addButtonWrapper);
		
		add(wrapper);
		
		pack();
		center();
	}
	
	private void createExtensionStyleElement(TaggedStyle ts, final JComponent parent) {
		ArrowSeparator as = new ArrowSeparator();
		StyleElement se = new StyleElement(ts);
		se.removeListener.add((style, tag) -> {
			StyleEditor.this.ts.remove(style);
			
			parent.remove(as);
			parent.remove(se);
			
			pack();
			repaint();
		});
		parent.add(as);
		parent.add(se);
	}
	
	private static class ArrowSeparator extends JPanel implements Unserialzable{
		
		private static final Border border = new EmptyBorder(3, 3, 3, 3);
		
		public ArrowSeparator() {
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			setBorder(border);
			
			JSeparator s = new JSeparator(JSeparator.VERTICAL);
			s.setAlignmentX(.5f);
			add(s);
			JLabel arrow = new JLabel(">");
			arrow.setForeground(Color.gray);
			add(arrow);
			add(new JSeparator(JSeparator.VERTICAL));
		}
	}
}
