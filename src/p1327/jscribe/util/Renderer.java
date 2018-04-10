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
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import p1327.jscribe.io.data.Style;
import p1327.jscribe.io.data.Text;

public class Renderer {
	
	private final Graphics2D g2d;
	
	private AffineTransform zoom = null;
	private double multiplyer = 1;
	
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

	}
	
	public void writeLocal(Text t, int offset, int w, int h, Style s) {
		t.isInvalid.set(!write(t.text.get(), offset, offset, w, h, s));
	}
	
	public void write(Text t, Style s) {
		t.isInvalid.set(!write(t.text.get(), t.x.get(), t.y.get(), t.w.get(), t.h.get(), s));
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
	public boolean write(String text, int x, int y, int w, int h, Style s) {
		if(s.caps.get())
			text = text.toUpperCase();
		Font f = new Font(s.font.get(), s.style.get(), s.size.get());
		FontMetrics fm = g2d.getFontMetrics(f);
		FontRenderContext frc = fm.getFontRenderContext();
		String[] lines = text.split("\n"), words;
		char[] chars;
		int height = (int)((fm.getHeight() + s.lineHeight.get()) * multiplyer),
			lineCount = 0;
		double maxTxtW = w / multiplyer;
		String newLine, oldNewLine;
		Area a = new Area();
		for(String line : lines) {
			int lineLength = fm.stringWidth(line);
			if(lineLength > maxTxtW) {
				words = line.split(" ");
				line = "";
				for(int i = 0, l = words.length; i < l; i++) {
					newLine = line + words[i];
					while(fm.stringWidth(newLine) > maxTxtW) {
						oldNewLine = newLine;
						if(line.length() > 0) {
							a.add(xAlign(prepareLine(line, x, y + height * lineCount, f, frc), w, s.xAlign.get()));
							lineCount++;
							newLine = words[i];
							line = ""; // empty it since we enter a new line
						}else {
							chars = newLine.toCharArray();
//							line = ""; // is always the case here
							for(int j = 0, cc = chars.length; j < cc; j++) {
								newLine = line + chars[j] + "-";
								if(fm.stringWidth(newLine) > maxTxtW) {
									if(line.length() > 0) {
										a.add(xAlign(prepareLine(line + "-", x, y + height * lineCount, f, frc), w, s.xAlign.get()));
										lineCount++;
										line = Character.toString(chars[j]);
									} else {
										a.add(xAlign(prepareLine(newLine, x, y + height * lineCount, f, frc), w, s.xAlign.get()));
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
			a.add(xAlign(prepareLine(line, x, y + height * lineCount, f, frc), w, s.xAlign.get()));
			lineCount++;
		}
		yAlign(a, h, s.yAlign.get());
		rotate(a, s.rotation.get());
		
//		g2d.setStroke(new BasicStroke((float) (s.outlineWidth.get() * multiplyer), BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER)); // is the default, but generates weird spikes on "W"
		g2d.setStroke(new BasicStroke((float) (s.outlineWidth.get() * multiplyer), BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
		g2d.setColor(s.outline.get());
		g2d.draw(a);
		g2d.setColor(s.color.get());
		g2d.fill(a);
		return (f.getSize2D() + height * (lineCount - 1)) < h;
	}
	
	private Area prepareLine(String text, int x, int y, Font f, FontRenderContext frc) {
		if(text.length() < 1)
			text = " ";
		GlyphVector gv = f.createGlyphVector(frc, text);
		Area a = new Area(gv.getOutline(x, y + f.getSize2D()));
		if(zoom != null)
			a.transform(zoom);
		return a;
	}
	
	private Area xAlign(Area a, int width, int alignment) {
		if(alignment == Style.BOTTOM) {
			Rectangle2D rect = a.getBounds2D();
			AffineTransform t = new AffineTransform();
			t.translate(width - rect.getWidth(), 0);
			a.transform(t);
		} else if(alignment == Style.CENTER) {
			Rectangle2D rect = a.getBounds2D();
			AffineTransform t = new AffineTransform();
			t.translate((width - rect.getWidth()) / 2, 0);
			a.transform(t);
		}
		return a;
	}
	
	private Area rotate(Area a, double rotation) {
		if(-0.001 < rotation && rotation < 0.001)
			return a;
		Rectangle2D rect = a.getBounds2D();
		double x = rect.getCenterX(),
			   y = rect.getCenterY();
		AffineTransform transform = new AffineTransform();
		transform.translate(-x, -y);
		a.transform(transform);
		transform = new AffineTransform();
		transform.rotate(rotation * Math.PI / 180);
		a.transform(transform);
		transform = new AffineTransform();
		transform.translate(x, y);
		a.transform(transform);
		return a;
	}
	
	private Area yAlign(Area a, int height, int alignment) {

		if(alignment == Style.RIGHT) {
			Rectangle2D rect = a.getBounds2D();
			AffineTransform t = new AffineTransform();
			t.translate(0, height - rect.getHeight());
			a.transform(t);
		} else if(alignment == Style.CENTER) {
			Rectangle2D rect = a.getBounds2D();
			AffineTransform t = new AffineTransform();
			t.translate(0, (height - rect.getHeight()) / 2);
			a.transform(t);
		}
		return a;
	}
	
	public void setZoom(AffineTransform transform, double multiplyer) {
		zoom = transform;
		this.multiplyer = multiplyer;
	}
	
	public void finish() {
		g2d.dispose();
	}
	
	public static BufferedImage copy(BufferedImage img) {
		return new BufferedImage(img.getColorModel(), img.copyData(null), img.isAlphaPremultiplied(), null);
	}
}
