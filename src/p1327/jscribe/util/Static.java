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

public class Static {
	
	private Static() {}
	
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
