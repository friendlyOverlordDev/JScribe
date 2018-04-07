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

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import p1327.jscribe.io.data.Style;
import p1327.jscribe.util.Static;
import p1327.jscribe.util.Unserialzable;
import p1327.jscribe.util.data.BoolProperty;
import p1327.jscribe.util.data.IntProperty;
import p1327.jscribe.util.data.Property;

public class StyleElement extends JPanel implements Unserialzable{
	
	private static final Border border = new EmptyBorder(3, 3, 3, 3);
	
	private static final int minSize = 5,
							 maxSize = 200;
	
	public StyleElement(Style s, String name) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JLabel l = new JLabel(name);
		l.setHorizontalAlignment(SwingConstants.CENTER);
		JPanel wrapper = new JPanel(new BorderLayout());
		wrapper.setBorder(border);
		wrapper.add(l);
		add(wrapper);
		add(createDropDownMenu(s.font, s.useFont, Static.fonts));
		add(createSpinner(s.size, s.useSize, minSize, maxSize));
		add(createColor(s.outline, s.useOutline));
	}
	
	private static JPanel createColor(Property<Color> value, BoolProperty inUse) {
		ColorField cf = new ColorField(value.get());
		cf.color.add(e -> {
			value.set(e.newVal);
		});
		return makeCheckable(cf, inUse);
	}
	
	private static JPanel createDropDownMenu(Property<String> value, BoolProperty inUse, String[] data) {
		JComboBox<String> box = new JComboBox<>(data);
		box.setSelectedItem(value.get());
		return makeCheckable(box, inUse);
	}
	
	private static JPanel createSpinner(IntProperty value, BoolProperty inUse, int min, int max) {
		int v = value.get();
		if(v < min)
			v = min;
		if(v > max)
			v = max;
		JSpinner spinner = new JSpinner(new SpinnerNumberModel(v, min, max, 1));
		return makeCheckable(spinner, inUse);
	}

	private static JPanel makeCheckable(JComponent c, BoolProperty inUse) {
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		p.setBorder(border);
		
		JCheckBox used = new JCheckBox();
		used.setSelected(inUse.get());
		
		p.add(used);
		p.add(c);
		
		return p;
	}
}
