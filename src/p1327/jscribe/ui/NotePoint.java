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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;

import p1327.jscribe.io.data.Note;
import p1327.jscribe.ui.window.Editor;
import p1327.jscribe.util.UIText;
import p1327.jscribe.util.Unserialzable;

public class NotePoint extends JComponent implements Unserialzable {
	
	private static final int size = 9;
	private static final int move = size / 2;
	
	final Note note;
	EditMode mode = EditMode.NONE;
	int startX, startY, // position on view
		startSX, startSY; // position on screen
	
	public NotePoint(Note _note) {
		setSize(size, size);
		setForeground(Color.red);
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				if(mode == EditMode.OPEN)
					Editor.$.data.setActive(note);
				mode = EditMode.NONE;
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				mode = EditMode.OPEN;
				startX = getX() + move;
				startY = getY() + move;
				startSX = e.getXOnScreen();
				startSY = e.getYOnScreen();
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
			public void mouseMoved(MouseEvent e) {}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				if(mode == EditMode.OPEN)
					mode = EditMode.MOVE;
				if(mode == EditMode.MOVE) {
					int x = startX + e.getXOnScreen() - startSX,
						y = startY + e.getYOnScreen() - startSY,
						w = Editor.$.viewer.getImgWidth() - 1,
						h = Editor.$.viewer.getImgHeight() - 1;
					if(x < 0)
						x = 0;
					if(y < 0)
						y = 0;
					if(x > w)
						x = w;
					if(y > h)
						y = h;
					note.x.set(x);
					note.y.set(y);
				}
			}
		});
		
		
		setLocation(_note.x.get(), _note.y.get());
		setToolTipText(UIText.displayable(_note.info.get()));
		this.note = _note;
		_note.x.add(e -> setLocation(e.newVal, getY() + move));
		_note.y.add(e -> setLocation(getX() + move, e.newVal));
		_note.info.add(e -> setToolTipText(UIText.displayable(e.newVal)));
		
		_note.addDeleteListener(n -> {
			Container c = getParent();
			c.remove(this);
			c.repaint();
		});
	}
	
	public Note getNote() {
		return note;
	}
	
	@Override
	public void setLocation(int x, int y) {
		super.setLocation(x - move, y - move);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		if(!Editor.$.viewer.areNonTextElementsVisible())
			return;
		super.paintComponent(g);
		g.setColor(Color.black);
		g.drawLine(1, 0, size - 1, size - 2);
		g.drawLine(0, 0, size - 1, size - 1);
		g.drawLine(0, 1, size - 2, size - 1);
		g.drawLine(1, size - 1, size - 1, 1);
		g.drawLine(0, size - 1, size - 1, 0);
		g.drawLine(0, size - 2, size - 2, 0);
		g.setColor(getForeground());
		g.drawLine(1, 1, size - 2, size - 2);
		g.drawLine(1, size - 2, size - 2, 1);
	}
}
