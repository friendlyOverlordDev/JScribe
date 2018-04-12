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
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.JPanel;

import p1327.jscribe.io.data.JSImg;
import p1327.jscribe.io.data.Note;
import p1327.jscribe.io.data.Text;
import p1327.jscribe.time.Time;
import p1327.jscribe.ui.window.Editor;
import p1327.jscribe.util.Static;
import p1327.jscribe.util.Unserialzable;

public class ImageViewer extends JPanel implements Unserialzable{
	
	private static final int minTextSize = 10;
	
	BufferedImage img = null;
	JSImg jsimg = null;
	
	private int imgW = 0, imgH = 0;
	
	final Vector<NotePoint> points;
	NotePoint newPoint = null;
	final Vector<TextLocation> locations;
	TextLocation newText = null;
	Point start = null;
	private boolean nonTextElementsVisible = true;
	
	PlacementMode pMode = PlacementMode.TEXT;
	
	public ImageViewer() {
		this(null, null);
	}
	
	public ImageViewer(BufferedImage _img, JSImg _jsimg) {
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
					// on delete the position isn't lost, so probably no recording is needed...
					Editor.$.data.setActive(newPoint.note);
				}else if(pMode == PlacementMode.TEXT){
					if(newText == null)
						return;
					final Text t = newText.text;
					final Point p = new Point(t.x.get(), t.y.get());
					final Dimension d = new Dimension(t.w.get(), t.h.get());
					final BufferedImage cImg = img;
					if(d.width < minTextSize || d.height < minTextSize)
						TextLocation.findBorders(t, cImg, p);
					// on delete the position and size isn't lost, so probably no recording is needed...
					Editor.$.data.setActive(newText.text);
				}
				newPoint = null;
				newText = null;
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				if(img == null || jsimg == null )
					return;
				if(e.getButton() != MouseEvent.BUTTON1)
					return;
				
				final Point m = e.getPoint();
				m.x /= zoomMultiplyer;
				m.y /= zoomMultiplyer;
				int h = getImgHeight() - 1, w = getImgWidth() - 1;
				if(m.x < 0 || m.y < 0 || m.x > w || m.y > h)
					return;

				final JSImg cImg = jsimg;
				if(pMode == PlacementMode.NOTE) {
					final Note n = new Note("", m.x, m.y);
					Time.rec(() -> {
						n.addDeleteListener(cImg.notes::remove);
						cImg.notes.add(n);
						
						if(cImg != jsimg)
							return;
						
						newPoint = new NotePoint(n);
						newPoint.setZoom(zoomLevel);
						add(newPoint);
						points.add(newPoint);
						Editor.$.data.addNote(n);
						
						newPoint.repaint();
					}, () -> {
						n.delete();
					});
				}else if(pMode == PlacementMode.TEXT){
					final Text t = new Text("", m.x, m.y, 0, 0);
					start = m;
					Time.rec(() -> {
						t.addDeleteListener(cImg.texts::remove);
						cImg.texts.add(t);
						
						if(cImg != jsimg)
							return;
						
						newText = new TextLocation(t);
						newText.setZoom(zoomLevel);
						add(newText);
						locations.add(newText);
						Editor.$.data.addText(t);
						
						newText.repaint();
					}, () -> {
						t.delete();
					});
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
				if(pMode == PlacementMode.NOTE && newPoint != null)
					newPoint.note.setLocation(fixPoint(e.getPoint()));
				if(pMode == PlacementMode.TEXT && newText != null)
					newText.calcSize(start, fixPoint(e.getPoint()));
			}
		});
		
		if(_img != null && _jsimg != null)
			setImage(_img, _jsimg);
	}
	
	public void setImage(BufferedImage img, JSImg jsimg) {
		for(NotePoint p : points)
			p.destroy();
		points.clear();
		for(TextLocation l : locations)
			l.destroy();
		locations.clear();
		
		this.img = img;
		this.jsimg = jsimg;
		NotePoint np;
		for(Note n : jsimg.notes) {
			np = new NotePoint(n);
			np.setZoom(zoomLevel);
			add(np);
			points.addElement(np);
		}
		TextLocation tl;
		for(Text t : jsimg.texts) {
			tl = new TextLocation(t);
			tl.setZoom(zoomLevel);
			add(tl);
			locations.add(tl);
		}
		this.setPreferredSize(new Dimension((int) ((imgW = img.getWidth(this)) * zoomMultiplyer), (int)((imgH = img.getHeight(this)) * zoomMultiplyer)));
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
		p.x /= zoomMultiplyer;
		p.y /= zoomMultiplyer;
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
		if(img != null) {
			if(zoomLevel != 0) {
				Graphics2D g2d = (Graphics2D) g;
				g2d.drawImage(img, zoomTransform, this);
			}else
			g.drawImage(img, 0, 0, this);
		}
	}
	
	int zoomLevel = 0;
	double zoomMultiplyer = 1;
	private AffineTransform zoomTransform = new AffineTransform();
	
	public void setZoom(int zoomLevel) {
		this.zoomLevel = zoomLevel;
		zoomMultiplyer = Static.getZoomMultiplyer(zoomLevel);
		zoomTransform = new AffineTransform();
		zoomTransform.scale(zoomMultiplyer, zoomMultiplyer);
		setPreferredSize(new Dimension((int) (imgW * zoomMultiplyer), (int)(imgH * zoomMultiplyer)));
		for(NotePoint np : points)
			np.setZoom(zoomLevel);
		for(TextLocation tl : locations)
			tl.setZoom(zoomLevel);
		repaint();
	}
	
	public void setMode(PlacementMode m) {
		pMode = m;
	}
	
	public void showNonTextElements(boolean b) {
		if(b == nonTextElementsVisible)
			return;
		nonTextElementsVisible = b;
		repaint();
	}
	
	public boolean areNonTextElementsVisible() {
		return nonTextElementsVisible;
	}

//	private static final int area = 100;
	public Text convertNoteToText(final Note n) {
//		int x = n.x.get(),
//			y = n.y.get();
//		x -= area / 2;
//		y -= area / 2;
//		if(x < 0)
//			x = 0;
//		if(y < 0)
//			y = 0;
//		int w = x + area,
//			h = y + area,
//			wImg = getImgWidth(),
//			hImg = getImgHeight();
//		if(w > wImg)
//			x -= w - wImg;
//		if(h > hImg)
//			y -= h - hImg;
//		w = area;
//		h = area;
//		
//		Text t = new Text(n.info.get(), x, y, w, h);
		final Text t = new Text(n.info.get(), 0, 0, 0, 0);
		TextLocation.findBorders(t, img, new Point(n.x.get(), n.y.get()));
		final JSImg cImg = jsimg;
		
		Time.rec(() -> {
			t.addDeleteListener(cImg.texts::remove);
			cImg.texts.add(t);
			n.delete();
			
			if(cImg != jsimg)
				return;
			
			TextLocation nt = new TextLocation(t);
			nt.setZoom(zoomLevel);
			add(nt);
			locations.add(nt);
			Editor.$.data.addText(t);
			
		}, () -> {
			n.addDeleteListener(cImg.notes::remove);
			cImg.notes.add(n);
			t.delete();
			
			if(cImg != jsimg)
				return;
			
			NotePoint np = new NotePoint(n);
			np.setZoom(zoomLevel);
			add(np);
			points.add(np);
			Editor.$.data.addNote(n);
			
		});
		
		return t;
	}
}
