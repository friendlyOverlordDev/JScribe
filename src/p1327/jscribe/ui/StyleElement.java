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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Vector;
import java.util.function.BiConsumer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import p1327.jscribe.io.data.Style;
import p1327.jscribe.io.data.TaggedStyle;
import p1327.jscribe.util.Static;
import p1327.jscribe.util.UIText;
import p1327.jscribe.util.Unserialzable;
import p1327.jscribe.util.data.BoolProperty;
import p1327.jscribe.util.data.DoubleProperty;
import p1327.jscribe.util.data.IntProperty;
import p1327.jscribe.util.data.Property;

public class StyleElement extends JPanel implements Unserialzable{
	
	private static final Border border = new EmptyBorder(3, 3, 3, 3);
	private static final Dimension nameSize = new Dimension(80, 0);
	private static final Font defaultFont = new JToggleButton().getFont();
	private static final Font bold = UIText.bold(defaultFont);
	private static final Font italic = UIText.italic(defaultFont);
	
	private static final int minSize = 5,
							 maxSize = 200,
							 lineDif = 100;
	
	private static final String[] xAlignVal = {"Left", "Center", "Right"},
								  yAlignVal = {"Top", "Center", "Bottom"};
	
	private final Vector<JCheckBox> boxes = new Vector<>();
	private final Vector<JLabel> names = new Vector<>();
	
	private JPanel inner = null,
				   currentElementList = null;
	
	public final JButton moveLeft, moveRight;
	private final JButton more;
	
	public final Vector<BiConsumer<Style, String>> removeListener = new Vector<>();
	
	public StyleElement(TaggedStyle ts) {
		this(ts.style, ts.tag.get());
	}
	
	public StyleElement(final Style s, final String name) {
		setLayout(new BorderLayout());
		
		JLabel l = new JLabel(name);
		l.setHorizontalAlignment(SwingConstants.CENTER);
		l.setPreferredSize(new Dimension(0, 30));
		add(l, BorderLayout.NORTH);
		
		inner = new JPanel();
		inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
		add(inner);

		createSeparator();
		createDropDownMenu("Font:", s.font, s.useFont, Static.fonts);
		createNumberField("Size:", s.size, s.useSize, minSize, maxSize);
		createFontStyle("Style:", s.style, s.useStyle);
		createColor("Color:", s.color, s.useColor);
		createSeparator();
		createColor("Outline:", s.outline, s.useOutline);
		createDecField("Outline Width:", s.outlineWidth, s.useOutlineWidth, 0, 100, 0.1);
		createSeparator();
		createIntDropDownMenu("Horizontal:", s.xAlign, s.useXAlign, xAlignVal, Style.LEFT, Style.CENTER);
		createIntDropDownMenu("Vertical:", s.yAlign, s.useYAlign, yAlignVal, Style.LEFT, Style.CENTER);
		createNumberField("Line Height:", s.lineHeight, s.useLineHeight, -lineDif, lineDif);
		createSeparator();
		createDecField("Rotation", s.rotation, s.useRotation, -360, 360, 0.05);

		JPanel wrapper = new JPanel();
		wrapper.setLayout(new BorderLayout());
		
//		JPopupMenu menu = Menu.newContextMenu(new Item("Remove", "Removes this style from this element\n(but doesn't delete the style)", e -> {
//			for(BiConsumer<Style, String> rl : removeListener)
//				rl.accept(s, name);
//		}));
		
		moveLeft = new JButton("<");
//		wrapper.add(moveLeft, BorderLayout.WEST);
		
//		more = new JButton("Menu");
//		more.setComponentPopupMenu(menu);
//		more.addActionListener(e->{
//			Point m = more.getMousePosition();
//			menu.show(more, m.x, m.y);
//		});
//		wrapper.add(more);
		
		moveRight = new JButton(">");
//		wrapper.add(moveRight, BorderLayout.EAST);
		
		more = new JButton("Remove");
		more.addActionListener(e -> {
			for(BiConsumer<Style, String> rl : removeListener)
				rl.accept(s, name);
		});
		wrapper.add(more);
		
		add(wrapper, BorderLayout.SOUTH);
	}
	
	public StyleElement setMain(boolean b) {
		boolean v = !b;
		for(JCheckBox box : boxes) {
//			box.setVisible(v);
			box.setEnabled(v);
			box.setSelected(b);
		}
		for(JLabel l : names)
			l.setVisible(b);
		
		moveLeft.setEnabled(v);
		more.setEnabled(v);
		moveRight.setEnabled(v);
		
		return this;
	}
	
	private void createFontStyle(String name, IntProperty value, BoolProperty inUse) {
		final JPanel wrapper = new JPanel();
		wrapper.setLayout(new GridLayout(1, 0));
		
		int val = value.get();
		
		final JToggleButton tBold = new JToggleButton("Bold");
		final JToggleButton tItalic = new JToggleButton("Italic");
		
		tBold.setFont(bold);
		tBold.setSelected((val & Style.BOLD) > 0);
		tBold.addActionListener(e -> {
			value.set((tBold.isSelected() ? Style.BOLD : 0) | (tItalic.isSelected() ? Style.ITALIC : 0));
		});
		wrapper.add(tBold);
		
		tItalic.setFont(italic);
		tItalic.setSelected((val & Style.ITALIC) > 0);
		tItalic.addActionListener(e -> {
			value.set((tBold.isSelected() ? Style.BOLD : 0) | (tItalic.isSelected() ? Style.ITALIC : 0));
		});
		wrapper.add(tItalic);
		
		wrapper.addPropertyChangeListener(e -> {
			boolean b = wrapper.isEnabled();
			tBold.setEnabled(b);
			tItalic.setEnabled(b);
		});
		
		makeCheckable(name, wrapper, inUse);
	}
	
	private void createColor(String name, Property<Color> value, BoolProperty inUse) {
		ColorField cf = new ColorField(value.get());
		cf.color.add(e -> {
			value.set(e.newVal);
		});
		makeCheckable(name, cf, inUse);
	}
	
	/**
	 * Creates a DropDownMenu for numeric values with a defined name.
	 * @param name - the name of this menu
	 * @param value - the value property
	 * @param inUse - the in use property
	 * @param data - data used to convert the value into a string
	 * @param diff - the lowest possible value 
	 * @param fallback - the default value
	 */
	private void createIntDropDownMenu(String name, IntProperty value, BoolProperty inUse, String[] data, int diff, int fallback) {
		final JComboBox<String> box = new JComboBox<>(data);
		int v = value.get();
		v -= diff;
		if(v < 0 || v >= data.length)
			v = fallback - diff;
		box.setSelectedItem(data[v]);
		box.addItemListener(e -> {
			value.set(box.getSelectedIndex() + diff);
		});
		makeCheckable(name, box, inUse);
	}
	
	private void createDropDownMenu(String name, Property<String> value, BoolProperty inUse, String[] data) {
		final JComboBox<String> box = new JComboBox<>(data);
		box.setSelectedItem(value.get());
		box.addItemListener(e -> {
			value.set(box.getSelectedItem().toString());
		});
		makeCheckable(name, box, inUse);
	}
	
	private void createDecField(String name, DoubleProperty value, BoolProperty inUse, double min, double max, double step) {
		double v = value.get();
		if(v < min)
			v = min;
		if(v > max)
			v = max;
		JSpinner spinner = new JSpinner(new SpinnerNumberModel(v, min, max, step));
		spinner.addChangeListener(e -> {
			value.set((double)spinner.getValue());
		});
		makeCheckable(name, spinner, inUse);
	}
	
	private void createNumberField(String name, IntProperty value, BoolProperty inUse, int min, int max) {
		int v = value.get();
		if(v < min)
			v = min;
		if(v > max)
			v = max;
		JSpinner spinner = new JSpinner(new SpinnerNumberModel(v, min, max, 1));
		spinner.addChangeListener(e -> {
			value.set((int)spinner.getValue());
		});
		makeCheckable(name, spinner, inUse);
	}
	
	private void createSeparator() {
		inner.add(new JSeparator());
//		currentElementList = new JPanel(new GridLayout(0, 1));
		currentElementList = new JPanel();
		currentElementList.setLayout(new BoxLayout(currentElementList, BoxLayout.Y_AXIS));
		inner.add(currentElementList);
	}

	private void makeCheckable(String name, JComponent c, BoolProperty inUse) {
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		p.setBorder(border);
		
		JLabel l = new JLabel(name);
		l.setPreferredSize(nameSize);
		l.setVisible(false);
		names.add(l);
		
		final JCheckBox used = new JCheckBox();
		used.setSelected(inUse.get());
		c.setEnabled(inUse.get());
		used.addItemListener(e -> {
			boolean b = used.isSelected();
			inUse.set(b);
			c.setEnabled(b);
		});
		boxes.add(used);
		
		p.add(used);
		p.add(l);
		p.add(c);
		
		currentElementList.add(p);
	}
}
