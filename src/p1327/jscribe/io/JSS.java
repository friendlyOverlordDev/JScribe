package p1327.jscribe.io;

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

import java.io.InputStream;
import java.util.HashMap;

import org.json.JSONObject;
import org.json.JSONTokener;

import p1327.jscribe.io.data.Style;
import p1327.jscribe.util.JSON;
import p1327.jscribe.util.JSONable;

public class JSS implements JSONable {

	private static final String MAIN = "main",
								TAGS = "tags";
	
	public final Style main;
	public final HashMap<String, Style> tags;
	
	public JSS() {
		main = new Style();
		tags = new HashMap<>();
	}
	
	public JSS(InputStream is) {
		this(new JSONObject(new JSONTokener(is)));
	}
	
	public JSS(JSONObject jss) {
		main = new Style(jss.getJSONObject(MAIN));
		tags = JSON.extractAsJSONObjectToMap(jss.getJSONObject(TAGS), o -> new Style(o));
	}
	
	@Override
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		json.put(MAIN, main.toJSON());
		json.put(TAGS, JSON.packJSONableMap(tags));
		return json;
	}
}
