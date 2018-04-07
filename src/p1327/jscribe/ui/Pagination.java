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

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import p1327.jscribe.util.Unserialzable;
import p1327.jscribe.util.data.IntProperty;

public class Pagination extends JPanel implements Unserialzable {
	
	private final JButton left, right;
	private final JLabel text;
	
	public final IntProperty currentPage = new IntProperty(),
							 maxPage = new IntProperty();
	
	public Pagination() {
		setLayout(new GridLayout(1, 0));
		
		left = new JButton("<");
		left.addActionListener(e -> {
			int p = currentPage.get();
			if(p > 1)
				currentPage.set(p - 1);
		});
		add(left);
		
		text = new JLabel();
		text.setHorizontalAlignment(SwingConstants.CENTER);
		add(text);
		
		right = new JButton(">");
		right.addActionListener(e -> {
			int p = currentPage.get();
			if(p < maxPage.get())
				currentPage.set(p + 1);
		});
		add(right);
		
		currentPage.add(e -> {
			updateText();
			left.setEnabled(e.newVal > 1);
			right.setEnabled(e.newVal < maxPage.get());
		});
		
		maxPage.add(e -> {
			updateText();
			int cur = currentPage.get();
			if(cur < e.newVal)
				right.setEnabled(true);
			else if(cur >= e.newVal) {
				currentPage.set(e.newVal);
				right.setEnabled(false); 
			}
		});
		currentPage.set(1);
	}
	
	private void updateText() {
		text.setText(currentPage.get() + "/" + maxPage.get());
	}
}
