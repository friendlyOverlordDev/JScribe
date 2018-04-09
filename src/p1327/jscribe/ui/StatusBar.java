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

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import p1327.jscribe.util.Unserialzable;

public class StatusBar extends JPanel implements Unserialzable{
	
	private static final Border border = BorderFactory.createLoweredBevelBorder();
	private static final Border innerBorder = new EmptyBorder(3, 5, 3, 3);
	
	private final JLabel text;
	
	public StatusBar() {
		setBorder(border);
		setLayout(new GridLayout(0, 1));
		
		text = new JLabel(" ");
		text.setBorder(innerBorder);
		add(text);
	}
	
	public StatusBar(String text) {
		this();
		setText(text);
	}
	
	public void setText(String text) {
		this.text.setText(text);
	}
	
	public String getText() {
		return text.getText();
	}
}
