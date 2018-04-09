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
import java.util.Collections;
import java.util.HashMap;
import java.util.Vector;

import org.json.JSONObject;
import org.json.JSONTokener;

import p1327.jscribe.io.data.Style;
import p1327.jscribe.util.JSON;
import p1327.jscribe.util.JSONable;

public class JSS implements JSONable {

	private static final String MAIN = "main",
								TAGS = "tags";
	
	public final Style main;
	private final HashMap<String, Style> tags;
	private String[] tagNames = null;
	
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
	
	public Style getStyle(String tag) {
		Style s = tags.get(tag);
		if(s == null)
			return createStyle(tag);
		return s;
	}
	
	public Style createStyle(String tag) {
		Style s = new Style();
		tags.put(tag, s);
		tagNames = null;
		return s;
	}
	
	public String[] getStyleList() {
		if(tagNames == null) {
			Vector<String> v = new Vector<>(tags.keySet());
			Collections.sort(v);
			v.add(0, "<add new...>");
			tagNames = v.toArray(new String[v.size()]);
		}
		
		return tagNames;
	}
}
