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
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.util.function.Consumer;

import javax.swing.JComponent;

import p1327.jscribe.io.data.Text;
import p1327.jscribe.io.data.prototype.DeletableElement;
import p1327.jscribe.ui.window.Editor;
import p1327.jscribe.ui.window.StyleEditor;
import p1327.jscribe.util.Renderer;
import p1327.jscribe.util.Static;
import p1327.jscribe.util.Unserialzable;
import p1327.jscribe.util.data.event.ChangeListener;
import p1327.jscribe.util.data.event.IntChangeListener;

public class TextLocation extends JComponent implements Unserialzable {
	
	private static final int border = 3;
	private static final int border2 = border * 2;
	private static final int resizearea = 5;
	
	static final Cursor defaultCursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
	
	final Text text;
	
	Direction dir = Direction.NONE;
	EditMode mode = EditMode.NONE;
	int startX, startY, // position on view
		startSX, startSY, // position on screen
		startLX, startLY; // position on element
	Point posSize, pos; //position+size
	
	private final Consumer<DeletableElement> deleteL;
	private final ChangeListener<String> textL;
	private final IntChangeListener xChangeL,
									yChangeL,
									wChangeL,
									hChangeL;

	public TextLocation(Text _text) {
		setSize(0, 0);
		setForeground(Color.white);
		setCursor(defaultCursor);
		
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				if(mode == EditMode.OPEN) {
					Editor.$.data.setActive(text);
					if(e.getClickCount() == 2) {
						StyleEditor se = new StyleEditor(Editor.$.getJSA().jsc.getTextStyle(_text));
						se.setVisible(true);
						Editor.$.viewer.repaint();
					}
				}
				mode = EditMode.NONE;
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				mode = EditMode.OPEN;
				startX = _text.x.get();
				startY = _text.y.get();
				startSX = e.getXOnScreen();
				startSY = e.getYOnScreen();
				if(zoomLevel == 0) {
					startLX = e.getX();
					startLY = e.getY();
				}else {
					startLX = (int) (e.getX() / zoomMultiplyer);
					startLY = (int) (e.getY() / zoomMultiplyer);
				}
				posSize = new Point(startX + _text.w.get(), startY + _text.h.get());
				pos = new Point(startX, startY);
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
				// the resizearea is independent of the zoom
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
							setCursor(defaultCursor);
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
				int x = startX + (int)((e.getXOnScreen() - startSX) / zoomMultiplyer) + startLX,
					y = startY + (int)((e.getYOnScreen() - startSY) / zoomMultiplyer) + startLY,
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
				int x = startX + (int)((e.getXOnScreen() - startSX) / zoomMultiplyer),
					y = startY + (int)((e.getYOnScreen() - startSY) / zoomMultiplyer),
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
		_text.x.add(xChangeL = e -> setLocation(e.newVal, _text.y.get()));
		_text.y.add(yChangeL = e -> setLocation(_text.x.get(), e.newVal));
		_text.w.add(wChangeL = e -> setSize(e.newVal, _text.h.get()));
		_text.h.add(hChangeL = e -> setSize(_text.w.get(), e.newVal));
		_text.text.add(textL = e -> repaint());
		
		_text.addDeleteListener(deleteL = n -> {
			Container c = getParent();
			if(c == null)
				return;
			c.remove(this);
			c.repaint();
		});
	}
	
	public void delete() {
		text.x.remove(xChangeL);
		text.y.remove(yChangeL);
		text.w.remove(wChangeL);
		text.h.remove(hChangeL);
		text.text.remove(textL);
		text.removeDeleteListener(deleteL);
	}
	
	public Text getText() {
		return text;
	}
	
	@Override
	public void setLocation(int x, int y) {
		super.setLocation((int)(x * zoomMultiplyer) - border, (int)(y * zoomMultiplyer) - border);
	}
	
	@Override
	public void setSize(int width, int height) {
		super.setSize((int)(width * zoomMultiplyer) + border2, (int)(height * zoomMultiplyer) + border2);
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
		if(Editor.$.viewer.areNonTextElementsVisible()) {
			int w = getWidth(),
				h = getHeight();
			g.setColor(Color.black);
			g.drawRect(0, 0, w - 1, h - 1);
			g.drawRect(2, 2, w - 5, h - 5);
			
			g.setColor(getForeground());
			g.drawRect(1, 1, w - 3, h - 3);
		}
		/*
		if(zoomLevel != 0) {
			Graphics2D g2d = (Graphics2D) g;
			AffineTransform transform = g2d.getTransform();
			transform.concatenate(zoomTransform);
			g2d.setTransform(zoomTransform);
		}*/
		
		// write the text last, since it enables a few features for better drawing which would interfere with the commands above
		Renderer tr = new Renderer(g);
		tr.setZoom(zoomTransform, zoomMultiplyer);
		tr.writeLocal(text, border, getWidth() - border2, getHeight() - border2, Editor.$.getJSA().jsc.getTextStyle(text).compile());
	}
	
	int zoomLevel = 0;
	double zoomMultiplyer = 1;
	private AffineTransform zoomTransform = new AffineTransform();
	
	public void setZoom(int zoomLevel) {
		this.zoomLevel = zoomLevel;
		zoomMultiplyer = Static.getZoomMultiplyer(zoomLevel);
		zoomTransform = new AffineTransform();
		zoomTransform.scale(zoomMultiplyer, zoomMultiplyer);
		setSize(text.w.get(), text.h.get());
		setLocation(text.x.get(), text.y.get());
		repaint();
	}
	
	static enum Direction{
		NONE, UP, DOWN, LEFT, RIGHT
	}
}
