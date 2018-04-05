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

import org.json.JSONException;
import org.json.JSONObject;

import p1327.jscribe.util.JSONable;

public class Note implements JSONable {
	
	private static final String INFO = "info",
								X = "x",
								Y = "y",
								CHECKED = "checked";
	
	public String info;
	public int x, y;
	public boolean checked;
	
	public Note(String info, int x, int y) {
		this.info = info;
		this.x = x;
		this.y = y;
		checked = false;
	}
	
	public Note(JSONObject note) throws JSONException {
		info = note.getString(INFO);
		x = note.getInt(X);
		y = note.getInt(Y);
		checked = note.getBoolean(CHECKED);
	}
	
	@Override
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		json.put(INFO, info);
		json.put(X, x);
		json.put(Y, y);
		json.put(CHECKED, checked);
		return json;
	}
}
