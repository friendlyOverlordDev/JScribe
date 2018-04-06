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
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;

import p1327.jscribe.io.data.Text;
import p1327.jscribe.ui.window.Editor;
import p1327.jscribe.util.Renderer;
import p1327.jscribe.util.Unserialzable;

public class TextLocation extends JComponent implements Unserialzable {
	
	private static final int border = 3;
	private static final int border2 = border * 2;
	private static final int resizearea = 5;
	
	final Text text;
	
	Direction dir = Direction.NONE;
	EditMode mode = EditMode.NONE;
	int startX, startY, // position on view
		startSX, startSY, // position on screen
		startLX, startLY; // position on element
	Point posSize, pos; //position+size
	
	// todo: limit locatio to window
	// todo: add mouse icons depending on location of mouse to field
	
	public TextLocation(Text _text) {
		setSize(0, 0);
		setForeground(Color.white);
		
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				mode = EditMode.NONE;
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				mode = EditMode.OPEN;
				startX = getX() + border;
				startY = getY() + border;
				startSX = e.getXOnScreen();
				startSY = e.getYOnScreen();
				startLX = e.getX();
				startLY = e.getY();
				pos = new Point(startX, startY);
				posSize = new Point(startX + getWidth() - border2, startY + getHeight() - border2);
			}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {}
		});
		
		addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				Direction d = Direction.NONE;
				int x = e.getX(), y = e.getY(), w = getWidth() - resizearea, h = getHeight() - resizearea;
				if(x < resizearea)
					d = Direction.LEFT;
				else if(x >= w)
					d = Direction.RIGHT;
				else if(y < resizearea)
					d = Direction.UP;
				else if(y >= h)
					d = Direction.DOWN;
				if(d != dir) {
					dir = d;
					switch(d) {
						case LEFT:
							setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
							break;
						case RIGHT:
							setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
							break;
						case UP:
							setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
							break;
						case DOWN:
							setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
							break;
						default:
							setCursor(Cursor.getDefaultCursor());
							break;
					}
				}
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				if(mode == EditMode.OPEN)
					mode = dir == Direction.NONE ? EditMode.MOVE : EditMode.RESIZE;
				if(mode == EditMode.MOVE)
					text.setLocation(getResizePoint(e, true));
				else if(mode == EditMode.RESIZE) {
					Point m;
					switch(dir) {
						case LEFT:
							m = getResizePoint(e, false);
							calcSize(posSize, new Point(m.x, startY));
							break;
						case RIGHT:
							m = getResizePointWithSize(e);
							calcSize(pos, new Point(m.x, posSize.y));
							break;
						case UP:
							m = getResizePoint(e, false);
							calcSize(posSize, new Point(startX, m.y));
							break;
						case DOWN:
							m = getResizePointWithSize(e);
							calcSize(pos, new Point(posSize.x, m.y));
							break;
						default:
							break;
					}
				}
			}
			
			private Point getResizePointWithSize(MouseEvent e) {
				int x = startX + e.getXOnScreen() - startSX + startLX,
					y = startY + e.getYOnScreen() - startSY + startLY,
					w = Editor.$.viewer.getImgWidth(),
					h = Editor.$.viewer.getImgHeight();
				if(x < 0)
					x = 0;
				if(y < 0)
					y = 0;
				if(x > w)
					x = w;
				if(y > h)
					y = h;
				return new Point(x, y);
			}
			
			private Point getResizePoint(MouseEvent e, boolean includeSize) {
				int x = startX + e.getXOnScreen() - startSX,
					y = startY + e.getYOnScreen() - startSY,
					w = Editor.$.viewer.getImgWidth(),
					h = Editor.$.viewer.getImgHeight();
				if(includeSize) {
					w -= text.w.get();
					h -= text.h.get();
				}
				if(x < 0)
					x = 0;
				if(y < 0)
					y = 0;
				if(x > w)
					x = w;
				if(y > h)
					y = h;
				return new Point(x, y);
			}
		});
		
		setLocation(_text.x.get(), _text.y.get());
		setSize(_text.w.get(), _text.h.get());
		this.text = _text;
		_text.x.add(e -> setLocation(e.newVal, getY() + border));
		_text.y.add(e -> setLocation(getX() + border, e.newVal));
		_text.w.add(e -> setSize(e.newVal, getHeight() - border2));
		_text.h.add(e -> setSize(getWidth() - border2, e.newVal));
		_text.text.add(e -> repaint());
	}
	
	public Text getText() {
		return text;
	}
	
	@Override
	public void setLocation(int x, int y) {
		super.setLocation(x - border, y - border);
	}
	
	@Override
	public void setSize(int width, int height) {
		super.setSize(width + border2, height + border2);
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
		text.x.set(x);
		text.y.set(y);
		text.w.set(w);
		text.h.set(h);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.black);
		int w = getWidth(),
			h = getHeight();
		g.drawRect(0, 0, w - 1, h - 1);
		g.drawRect(2, 2, w - 5, h - 5);
		
		
		
		g.setColor(getForeground());
		g.drawRect(1, 1, w - 3, h - 3);
		
		// write the text last, since it enables a few features for better drawing which would interfere with the commands above
		Renderer tr = new Renderer(g);
		tr.writeLocal(text, border);
	}
	
	enum Direction{
		NONE, UP, DOWN, LEFT, RIGHT
	}
}
