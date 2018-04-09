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

import java.util.Vector;

import org.json.JSONObject;
import org.json.JSONTokener;

import p1327.jscribe.io.data.JSImg;
import p1327.jscribe.io.data.Text;
import p1327.jscribe.io.data.TextStyle;
import p1327.jscribe.util.JSON;
import p1327.jscribe.util.JSONable;
import p1327.jscribe.util.data.Property;

public class JSC implements JSONable{
	
	private static final int version = 1;
	
	private static final String VERSION = "version",
								IMGS = "imgs",
								STYLES = "styls";
	
	public final Vector<JSImg> imgs;
	public final Property<JSS> jss; 
	
	public JSC() {
		imgs = new Vector<>();
		jss = new Property<>(new JSS());
	}
	
	public JSC(InputStream is) {
		this(new JSONObject(new JSONTokener(is)));
	}
	
	private JSC(JSONObject jsc) {
		if(jsc.getInt(VERSION) == 0)
			jsc.put(STYLES, new JSS().toJSON());
		imgs = JSON.extractAsJSONObjectToVector(jsc.getJSONArray(IMGS), o -> new JSImg(o));
		jss = new Property<>(new JSS(jsc.getJSONObject(STYLES)));
	}
	
	@Override
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		json.put(VERSION, version);
		json.put(IMGS, JSON.packJSONableVector(imgs));
		json.put(STYLES, jss.get().toJSON());
		return json;
	}
	
	public TextStyle getTextStyle(Text t) {
		return new TextStyle(jss.get(), t.tags);
	}
}
