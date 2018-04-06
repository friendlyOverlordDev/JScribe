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

import javax.swing.JPanel;

import p1327.jscribe.io.data.JSImg;
import p1327.jscribe.io.data.Note;
import p1327.jscribe.io.data.Text;
import p1327.jscribe.util.Unserialzable;

public class ImageViewer extends JPanel implements Unserialzable{
	
	Image img = null;
	JSImg jsimg = null;
	
	private int imgW = 0, imgH = 0;
	
	final Vector<NotePoint> points;
	NotePoint newPoint = null;
	final Vector<TextLocation> locations;
	TextLocation newText = null;
	Point start = null;
	
//	PlacementMode pMode = PlacementMode.NOTE;	
	PlacementMode pMode = PlacementMode.TEXT;
	
	public ImageViewer() {
		this(null, null);
	}
	
	public ImageViewer(Image _img, JSImg _jsimg) {
		setLayout(null);
		setBackground(new Color(0x333333));
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
					newPoint.note.setLocation(e.getPoint());
//					newPoint.openWindow();
				}else if(pMode == PlacementMode.TEXT){
					if(newText == null)
						return;
					newText.calcSize(start, fixPoint(e.getPoint()));
//					newText.openWindow();
				}
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				if(img == null || jsimg == null )
					return;
				if(e.getButton() != MouseEvent.BUTTON1)
					return;
				
				Point m = e.getPoint();
				int h = getImgHeight() - 1, w = getImgWidth() - 1;
				if(m.x < 0 || m.y < 0 || m.x > w || m.y > h)
					return;
				
				if(pMode == PlacementMode.NOTE) {
					Note n = new Note("", 0, 0);
					newPoint = new NotePoint(n);
					n.setLocation(m);
					add(newPoint);
					points.add(newPoint);
					jsimg.notes.add(newPoint.getNote());
					newPoint.repaint();
				}else if(pMode == PlacementMode.TEXT){
					Text t = new Text("", 0, 0, 0, 0);
					newText = new TextLocation(t);
					t.setLocation(start = m);
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
					newPoint.note.setLocation(fixPoint(e.getPoint()));
				if(newText != null)
					newText.calcSize(start, fixPoint(e.getPoint()));
			}
		});
		
		if(_img != null && _jsimg != null)
			setImage(_img, _jsimg);
	}
	
	public void setImage(Image img, JSImg jsimg) {
		for(NotePoint p : points)
			remove(p);
		points.clear();
		for(TextLocation l : locations)
			remove(l);
		locations.clear();
		
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
		this.setPreferredSize(new Dimension(imgW = img.getWidth(this), imgH = img.getHeight(this)));
		this.repaint();
	}
	
	public int getImgWidth() {
		return imgW;
	}
	
	public int getImgHeight() {
		return imgH;
	}
	
	Point fixPoint(Point p) {
		int h = imgH, w = imgW;
		if(p.x < 0)
			p.x = 0;
		if(p.y < 0)
			p.y = 0;
		if(p.x > w)
			p.x = w;
		if(p.y > h)
			p.y = h;
		return p;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(img != null)
			g.drawImage(img, 0, 0, this);
	}
}
