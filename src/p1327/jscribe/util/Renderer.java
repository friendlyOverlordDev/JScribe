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
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;

import p1327.jscribe.io.data.Text;

public class Renderer {
	
	private final Graphics2D g2d;
	private Font font = new Font(Static.defaultRenderFont, Font.PLAIN, 20);
	private Color color = Color.black;
	
	public Renderer(Graphics g) {
		this((Graphics2D) g);
	}
	
	public Renderer(Graphics2D g2d) {
		this.g2d = g2d;
	}
	
	public void writeLocal(Text t, int offset) {
		write(t.text.get(), offset, offset);
	}
	
	public void write(Text t) {
		write(t.text.get(), t.x.get(), t.y.get());
	}
	
	public void write(String text, int x, int y) {
		setUp();
		// search for a better solution
		g2d.setFont(font);
		GlyphVector gv = font.createGlyphVector(g2d.getFontMetrics().getFontRenderContext(), text);
		Shape s = gv.getOutline(x, y + font.getSize2D());
		g2d.setStroke(new BasicStroke(2f));
		g2d.setColor(Color.red);
		g2d.draw(s);
		g2d.setColor(color);
		g2d.drawString(text, x, y + font.getSize2D());
		//font.layoutGlyphVector(g2d.getFontMetrics().getFontRenderContext(), t.text.toCharArray(), 0, t.text.length(), 0);
	}
	
	public void setUp() {
	    g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
	    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
	    g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
	    g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
	    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	    g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
	}
	
	public void finish() {
		g2d.dispose();
	}
	
	public static BufferedImage copy(BufferedImage img) {
		return new BufferedImage(img.getColorModel(), img.copyData(null), img.isAlphaPremultiplied(), null);
	}
}
