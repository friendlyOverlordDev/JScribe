package p1327.jscribe.io.data;

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

import java.util.Vector;

import org.json.JSONException;
import org.json.JSONObject;

import p1327.jscribe.util.JSON;
import p1327.jscribe.util.JSONable;

public class JSImg implements JSONable {
	
	private static final String IMG = "img",
								TEXTS = "texts",
								NOTES = "notes";
	
	public String img;
	public final Vector<Text> texts;
	public final Vector<Note> notes;
	
	public JSImg(String img) {
		this.img = img; 
		texts = new Vector<>();
		notes = new Vector<>();
	}
	
	public JSImg(JSONObject img) throws JSONException {
		this.img = img.getString(IMG);
		texts = JSON.extractAsJSONObjectToVector(img.getJSONArray(TEXTS), o -> new Text(o));
		notes = JSON.extractAsJSONObjectToVector(img.getJSONArray(NOTES), o -> new Note(o));
	}
	
	@Override
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		json.put(IMG, img);
		json.put(TEXTS, JSON.packJSONableVector(texts));
		json.put(NOTES, JSON.packJSONableVector(notes));
		return json;
	}
}
