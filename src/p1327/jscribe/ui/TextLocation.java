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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;

import p1327.jscribe.io.data.Text;
import p1327.jscribe.ui.window.TextWindow;
import p1327.jscribe.util.Renderer;
import p1327.jscribe.util.Unserialzable;

public class TextLocation extends JComponent implements Unserialzable {
	
	private static final int border = 3;
	
	private Text text;
	
	// todo: limit locatio to window
	// todo: add mouse icons depending on location of mouse to field
	
	public TextLocation() {
		text = new Text("", 0, 0, 100, 100);
		setSize(0, 0);
		setForeground(Color.white);
		
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				openWindow();
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {}
		});
	}
	
	public TextLocation(Text text) {
		this();
		setLocation(text.x, text.y);
		setSize(text.w, text.h);
		this.text = text; // set local note later, so we don't accidently update it with wrong values
	}
	
	public Text getText() {
		return text;
	}
	
	public void openWindow() {
		new TextWindow(text);
	}
	
	@Override
	public void setLocation(int x, int y) {
		super.setLocation(x - border, y - border);
		text.x = x;
		text.y = y;
	}
	
	@Override
	public void setSize(int width, int height) {
		super.setSize(width + border * 2, height + border * 2);
		text.w = width;
		text.h = height;
	}
	
	public void calcSize(Point p1, Point p2) {
		int x, y, w, h;
		if(p1.x < p2.x) {
			x = p1.x;
			w = p2.x - x;
		} else {
			x = p2.x;
			w = p1.x - x;
		}
		if(p1.y < p2.y) {
			y = p1.y;
			h = p2.y - y;
		}else {
			y = p2.y;
			h = p1.y - y;
		}
		setLocation(x, y);
		setSize(w, h);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.black);
		int w = getWidth(),
			h = getHeight();
		g.drawRect(0, 0, w - 1, h - 1);
		g.drawRect(2, 2, w - 5, h - 5);
		
		
		Renderer tr = new Renderer(g);
		tr.writeLocal(text, border);
		
		g.setColor(getForeground());
		g.drawRect(1, 1, w - 3, h - 3);
	}
}
