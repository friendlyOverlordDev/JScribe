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

public class TextNote implements JSONable {
	
	private static final String INFO = "info",
								CHECKED = "checked";
	
	public String info;
	public boolean checked;
	
	public TextNote(String info) {
		this.info = info;
		checked = false;
	}
	
	public TextNote(JSONObject note) throws JSONException {
		info = note.getString(INFO);
		checked = note.getBoolean(CHECKED);
	}
	
	@Override
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		json.put(INFO, info);
		json.put(CHECKED, checked);
		return json;
	}
}
