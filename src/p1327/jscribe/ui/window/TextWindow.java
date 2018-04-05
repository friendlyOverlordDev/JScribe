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

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import p1327.jscribe.io.data.Text;
import p1327.jscribe.util.Unserialzable;
import p1327.jscribe.util.Window;

public class TextWindow extends JFrame implements Unserialzable, Window{
	
//	private final Text t;
	
	final JTextField text;
	
	// todo implement check able
	// todo implement convert to Text
	// todo set unsaved
	
	public TextWindow(Text t) {
//		this.t = t;
		setTitle("Text (" + t.x + "|" + t.y + ")");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		spawnUnderMouse();
		setLayout(null);
		setSize(200, 100);
		
		text = new JTextField();
		text.setText(t.text);
		text.setLocation(10, 10);
		text.setSize(180, 25);
		text.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				updated();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				updated();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				updated();
			}
			
			private void updated() {
				t.text = text.getText();
			}
		});
		add(text);
		
		setVisible(true);
	}
}
