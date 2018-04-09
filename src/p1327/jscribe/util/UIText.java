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

import java.awt.Font;
import java.time.LocalTime;

public class UIText {
	
	private UIText() {}
	
	/**
	 * Makes a given string displayable by converting it to html-string in case it is necessary (e.g. for linebreaks to work)
	 * @param str - the string to convert
	 * @return the converted string
	 */
	public static String displayable(String str) {
		boolean convert = str.indexOf('\n') > -1;
		if(convert)
			str = "<html>" + str.replace("\n", "<br>") + "</html>";
		return str;
	}

	public static String displayableSingleLine(String str) {
		return str.replace('\n', ' ');
	}
	
	public static Font bold(Font f) {
		return alter(f, Font.BOLD);
	}
	
	public static Font italic(Font f) {
		return alter(f, Font.ITALIC);
	}
	
	public static Font alter(Font f, int flags) {
		return new Font(f.getFontName(), flags, f.getSize());
	}
	
	public static String getTime() {
		return LocalTime.now().toString();
	}
}
