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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;

import p1327.jscribe.io.data.Note;
import p1327.jscribe.ui.window.NoteWindow;
import p1327.jscribe.util.Unserialzable;

public class NotePoint extends JComponent implements Unserialzable {
	
	private static final int size = 9;
	private static final int move = size / 2;
	
	private Note note;
	
	// todo: limit locatio to window
	
	public NotePoint() {
		setSize(size, size);
		setForeground(Color.red);
		note = new Note("", 0, 0);
		
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
	
	public NotePoint(Note note) {
		this();
		setLocation(note.x, note.y);
		this.note = note; // set local note later, so we don't accidently update it with wrong values
	}
	
	public Note getNote() {
		return note;
	}
	
	public void openWindow() {
		new NoteWindow(note);
	}
	
	@Override
	public void setLocation(int x, int y) {
		super.setLocation(x - move, y - move);
		// don't chanage x/y for note, since they are already adjusted for that
		note.x = x;
		note.y = y;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
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
