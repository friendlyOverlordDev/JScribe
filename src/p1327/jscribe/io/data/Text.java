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

import java.awt.Dimension;
import java.awt.Point;

import java.util.Vector;

import org.json.JSONException;
import org.json.JSONObject;

import p1327.jscribe.util.JSON;
import p1327.jscribe.util.JSONable;
import p1327.jscribe.util.data.IntProperty;
import p1327.jscribe.util.data.Property;

public class Text implements JSONable {
	
	private static final String TEXT = "text",
								X = "x",
								Y = "y",
								W = "w",
								H = "h",
								NOTES = "notes";

	public final Property<String> text;
	public final IntProperty x, y, w, h;
	public final Vector<SimpleNote> notes;
	
	public Text(String text, int x, int y, int w, int h) {
		this.text = new Property<>(text);
		this.x = new IntProperty(x);
		this.y = new IntProperty(y);
		this.w = new IntProperty(w);
		this.h = new IntProperty(h);
		notes = new Vector<>();
	}
	
	public Text(JSONObject text) throws JSONException {
		this.text = new Property<>(text.getString(TEXT));
		x = new IntProperty(text.getInt(X));
		y = new IntProperty(text.getInt(Y));
		w = new IntProperty(text.getInt(W));
		h = new IntProperty(text.getInt(H));
		notes = JSON.extractAsJSONObjectToVector(text.getJSONArray(NOTES), o -> new SimpleNote(o));
	}
	
	@Override
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		json.put(TEXT, text.get());
		json.put(X, x.get());
		json.put(Y, y.get());
		json.put(W, w.get());
		json.put(H, h.get());
		json.put(NOTES, JSON.packJSONableVector(notes));
		return json;
	}
	
	public void setLocation(Point p) {
		x.set(p.x);
		y.set(p.y);
	}
	
	public void setSize(Dimension d) {
		w.set(d.width);
		h.set(d.height);
	}
}
