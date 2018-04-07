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
import java.awt.FontMetrics;
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
	private FontMetrics fm;
	
	public Renderer(Graphics g) {
		this((Graphics2D) g);
	}
	
	public Renderer(Graphics2D g2d) {
		this.g2d = g2d;
	    g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
	    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
	    g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
	    g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
	    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	    g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

		g2d.setFont(font);
	    fm = g2d.getFontMetrics();
	}
	
	public void writeLocal(Text t, int offset, int w, int h) {
		t.isInvalid.set(!write(t.text.get(), offset, offset, w, h));
	}
	
	public void write(Text t) {
		t.isInvalid.set(!write(t.text.get(), t.x.get(), t.y.get(), t.w.get(), t.h.get()));
	}

	/**
	 * draws a text in a given rect<br>
	 * displays linebreaks and uses wordwrap
	 * @param text - the text to display
	 * @param x - left side of the rect
	 * @param y - top side of the rect
	 * @param w - width of the rect
	 * @param h - height of the rect
	 * @return true, if the text fitts into the rect, false if the height of the text is bigger than the given height.
	 */
	public boolean write(String text, int x, int y, int w, int h) {
		String[] lines = text.split("\n"), words;
		char[] chars;
		int height = fm.getHeight(), lineCount = 0;
		String newLine, oldNewLine;
		for(String line : lines) {
			int lineLength = fm.stringWidth(line);
			if(lineLength > w) {
				words = line.split(" ");
				line = "";
				for(int i = 0, l = words.length; i < l; i++) {
					newLine = line + words[i];
					while(fm.stringWidth(newLine) > w) {
						oldNewLine = newLine;
						if(line.length() > 0) {
							writeLine(line, x, y + height * lineCount);
							lineCount++;
							newLine = words[i];
							line = ""; // empty it since we enter a new line
						}else {
							chars = newLine.toCharArray();
//							line = ""; // is always the case here
							for(int j = 0, cc = chars.length; j < cc; j++) {
								newLine = line + chars[j] + "-";
								if(fm.stringWidth(newLine) > w) {
									if(line.length() > 0) {
										writeLine(line + "-", x, y + height * lineCount);
										lineCount++;
										line = Character.toString(chars[j]);
									} else {
										writeLine(newLine, x, y + height * lineCount);
										lineCount++;
										line = "";
									}
								}else
									line += chars[j];
							}
							newLine = line;
						}
						if(oldNewLine == newLine)
							break;
					}
					line = newLine + " ";
				}
			}
			writeLine(line, x, y + height * lineCount);
			lineCount++;
		}
		return y + font.getSize2D() + height * (lineCount - 1) < h;
	}
	
	public void writeLine(String text, int x, int y) {
		GlyphVector gv = font.createGlyphVector(fm.getFontRenderContext(), text);
		Shape s = gv.getOutline(x, y + font.getSize2D());
		g2d.setStroke(new BasicStroke(2f));
		g2d.setColor(Color.red);
		g2d.draw(s);
		g2d.setColor(color);
		g2d.fill(s);
//		g2d.drawString(text, x, y + font.getSize2D());
	}
	
	public void finish() {
		g2d.dispose();
	}
	
	public static BufferedImage copy(BufferedImage img) {
		return new BufferedImage(img.getColorModel(), img.copyData(null), img.isAlphaPremultiplied(), null);
	}
}
