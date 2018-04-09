package p1327.jscribe.io.data;

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

import p1327.jscribe.io.JSS;
import p1327.jscribe.util.data.Property;

public class TaggedStyle {
	
	public final Property<String> tag;
	public final Style style;
	
	TaggedStyle(String tag, JSS jss) {
		this.tag = new Property<String>(tag);
		this.style = jss.getStyle(tag);
	}
}
