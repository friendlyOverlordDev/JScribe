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
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import javax.swing.JComponent;

import p1327.jscribe.io.data.JSImg;
import p1327.jscribe.io.data.Note;
import p1327.jscribe.io.data.Text;
import p1327.jscribe.util.Unserialzable;

public class ImageViewer extends JComponent implements Unserialzable{
	
	Image img = null;
	JSImg jsimg = null;
	
	final Vector<NotePoint> points;
	NotePoint newPoint = null;
	final Vector<TextLocation> locations;
	TextLocation newText = null;
	Point start = null;
	
	PlacementMode pMode = PlacementMode.TEXT;
	
	// todo: reset mode on esc-key;
	// todo set unsaved
	
	public ImageViewer() {
		this(null, null);
	}
	
	public ImageViewer(Image _img, JSImg _jsimg) {
		setLayout(null);
		setBackground(Color.black);
		points = new Vector<>();
		locations = new Vector<>();
		
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				if(e.getButton() != MouseEvent.BUTTON1)
					return;
				if(pMode == PlacementMode.NOTE) {
					if(newPoint == null)
						return;
					newPoint.setLocation(e.getPoint());
					newPoint.openWindow();
				}else if(pMode == PlacementMode.TEXT){
					if(newText == null)
						return;
					newText.calcSize(start, e.getPoint());
					newText.openWindow();
				}
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				if(img == null || jsimg == null )
					return;
				if(e.getButton() != MouseEvent.BUTTON1)
					return;
				if(pMode == PlacementMode.NOTE) {
					newPoint = new NotePoint();
					newPoint.setLocation(e.getPoint());
					add(newPoint);
					points.add(newPoint);
					jsimg.notes.add(newPoint.getNote());
					newPoint.repaint();
				}else if(pMode == PlacementMode.TEXT){
					newText = new TextLocation();
					newText.setLocation(start = e.getPoint());
					add(newText);
					locations.add(newText);
					jsimg.texts.add(newText.getText());
					newText.repaint();
				}
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
				if(newPoint != null)
					newPoint.setLocation(e.getPoint());
				if(newText != null)
					newText.calcSize(start, e.getPoint());
			}
		});
		
		if(_img != null && _jsimg != null)
			setImage(_img, _jsimg);
	}
	
	public void setImage(Image img, JSImg jsimg) {
		this.img = img;
		this.jsimg = jsimg;
		NotePoint np;
		for(Note n : jsimg.notes) {
			np = new NotePoint(n);
			add(np);
			points.addElement(np);
		}
		TextLocation tl;
		for(Text t : jsimg.texts) {
			tl = new TextLocation(t);
			add(tl);
			locations.add(tl);
		}
		this.setPreferredSize(new Dimension(img.getWidth(this), img.getHeight(this)));
		this.repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(img != null)
			g.drawImage(img, 0, 0, this);
	}
}
