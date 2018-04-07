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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.border.Border;

import p1327.jscribe.util.Unserialzable;
import p1327.jscribe.util.data.Property;

public class ColorField extends JPanel implements Unserialzable{
	
	private static final Border border = BorderFactory.createLoweredBevelBorder();
	
	public final Property<Color> color;
	
	public ColorField() {
		this(Color.white);
	}
	
	public ColorField(Color c) {
		setLayout(new BorderLayout());
		setBorder(border);
		
		final JPanel inner = new JPanel();
		inner.setBackground(c);
		add(inner);
		
		color = new Property<>(c);
		color.add(e -> inner.setBackground(e.newVal));
		
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				Color c = JColorChooser.showDialog(null, "Select a Color", color.get());
				if(c != null)
					color.set(c);
			}
		});
	}
}
