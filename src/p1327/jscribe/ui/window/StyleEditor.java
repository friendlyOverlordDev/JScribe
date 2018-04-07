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

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import p1327.jscribe.io.data.Style;
import p1327.jscribe.ui.StyleElement;
import p1327.jscribe.util.Unserialzable;
import p1327.jscribe.util.Window;

public class StyleEditor extends JDialog implements Window, Unserialzable {
	
	public StyleEditor() {
		setTitle("Style Editor");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModalityType(ModalityType.APPLICATION_MODAL);
		JPanel wrapper = new JPanel();
		wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.X_AXIS));
		
		wrapper.add(new StyleElement(new Style(), "main"));
		wrapper.add(new ArrowSeparator());
		wrapper.add(new StyleElement(new Style(), "second"));
		
		
		add(wrapper);
		
		pack();
		center();
		setVisible(true);
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
