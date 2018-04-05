package p1327.jscribe.util;

/*
 * Copyright (c) 2018 your friendly Overlord & friendlyOverlordDev
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
}
