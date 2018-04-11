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
import java.io.File;
import java.io.IOException;

public class Static {
	
	private Static() {}
	
	public static final String version = "0.7.1";
	
	public static final int zoomSteps = 1;
	public static final int minZoom = -3;
	public static final int maxZoom = 3;
	
	public static final double getZoomMultiplyer(int zoomLevel) {
		if(zoomLevel == 0)
			return 1;
		return Math.pow(2, zoomLevel * zoomSteps);
	}
	
	public static boolean validateFile(String file) {
		try {
			File f = new File(file);
			f.getCanonicalPath();
			return true;
		}catch(IOException e) {
		}
		
		return false;
	}
	
	public static final String[] supportedTypeList = {"png", "jpg", "jpeg", "gif"};
	public static final String supportedTypes = String.join(", ", supportedTypeList);
	
	public static final String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
	public static final String defaultRenderFont;
	
	static {
		String font = "Comic Sans".toLowerCase(), drf = null;
		for(String f : fonts)
			if(f.toLowerCase().startsWith(font)) {
				drf= f;
			}
		if(drf == null)
			drf = "Comic Sans MS";
		defaultRenderFont = drf;
	}
}
