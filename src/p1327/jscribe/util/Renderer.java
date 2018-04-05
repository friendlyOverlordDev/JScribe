package p1327.jscribe.util;

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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;

import p1327.jscribe.io.data.Text;

public class Renderer {
	
	private final Graphics2D g2d;
	private Font font = new Font("Comic Sans", Font.PLAIN, 10);
	private Color color = Color.black;
	
	public Renderer(Graphics g) {
		this((Graphics2D) g);
	}
	
	public Renderer(Graphics2D g2d) {
		this.g2d = g2d;
	}
	
	public void writeLocal(Text t, int offset) {
		// search for a better solution
		g2d.setFont(font);
		GlyphVector gv = font.createGlyphVector(g2d.getFontMetrics().getFontRenderContext(), t.text);
		Shape s = gv.getOutline(offset, offset + font.getSize2D());
		g2d.setStroke(new BasicStroke(2f));
		g2d.setColor(Color.red);
		g2d.fill(s);
		g2d.setColor(color);
		g2d.drawString(t.text, offset, offset + font.getSize2D());
		//font.layoutGlyphVector(g2d.getFontMetrics().getFontRenderContext(), t.text.toCharArray(), 0, t.text.length(), 0);
	}
	
	public void write(Text t) {
		// search for a better solution
		g2d.setFont(font);
		GlyphVector gv = font.createGlyphVector(g2d.getFontMetrics().getFontRenderContext(), t.text);
		Shape s = gv.getOutline(t.x, t.y + font.getSize2D());
		g2d.setStroke(new BasicStroke(2f));
		g2d.setColor(Color.red);
		g2d.fill(s);
		g2d.setColor(color);
		g2d.drawString(t.text, t.x, t.y + font.getSize2D());
		//font.layoutGlyphVector(g2d.getFontMetrics().getFontRenderContext(), t.text.toCharArray(), 0, t.text.length(), 0);
	}
	
	public void finish() {
		g2d.dispose();
	}
	
	public static BufferedImage copy(BufferedImage img) {
		return new BufferedImage(img.getColorModel(), img.copyData(null), img.isAlphaPremultiplied(), null);
	}
}
