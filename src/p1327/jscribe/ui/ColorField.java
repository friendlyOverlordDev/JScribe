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

import javax.swing.BorderFactory;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import p1327.jscribe.ui.listener.SimpleMouseListener;
import p1327.jscribe.util.Unserialzable;
import p1327.jscribe.util.data.Property;

public class ColorField extends JPanel implements Unserialzable{
	
	private static final Border border = BorderFactory.createLoweredBevelBorder();
	private static final Border disabled = new EmptyBorder(border.getBorderInsets(null)); 
	
	public final Property<Color> color;
	
	private final JPanel inner;
	
	public ColorField() {
		this(Color.white);
	}
	
	public ColorField(Color c) {
		setLayout(new BorderLayout());
		setBorder(border);
		
		inner = new JPanel();
		inner.setBackground(c);
		add(inner);
		
		color = new Property<>(c);
		color.add(e -> inner.setBackground(e.newVal));
		
		addMouseListener(new SimpleMouseListener(e -> {
			if(isEnabled()) {
				Color _c = JColorChooser.showDialog(null, "Select a Color", color.get());
				if(_c != null)
					color.set(_c);
			}
		}));
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		if(enabled) {
			setBorder(border);
			inner.setBackground(color.get());
		}else {
			setBorder(disabled);
			inner.setBackground(color.get().darker());
		}
	}
}
