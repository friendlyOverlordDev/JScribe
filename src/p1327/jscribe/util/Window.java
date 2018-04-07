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

import java.awt.GraphicsEnvironment;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;

public interface Window {
	
	public static final int bottomSpace = 100;
	public static final int topLeftSpace = 16;
	
	default void spawnUnderMouse() {
		Point p = MouseInfo.getPointerInfo().getLocation();
		setLocation(p.x - topLeftSpace, p.y - topLeftSpace);
	}
	
	default void center() {
		center(getWidth(), getHeight());
	}
	
	default void center(int width, int height) {
		Rectangle rect = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getBounds();
		if(width > rect.width)
			width = rect.width;
		if(height > rect.height)
			height = rect.height - bottomSpace;
		int x = (rect.x + rect.width - width) / 2;
		int y = (rect.y + rect.height - height) / 2;
		setSize(width, height);
		setLocation(x, y);
	}
	
	void setSize(int width, int height);
	void setLocation(int x, int y);
	
	int getWidth();
	int getHeight();
}
