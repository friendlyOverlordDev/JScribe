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

import java.awt.Point;

import org.json.JSONException;
import org.json.JSONObject;

import p1327.jscribe.util.data.IntProperty;

public class Note extends SimpleNote {
	
	private static final String X = "x",
								Y = "y";
	
	public final IntProperty x, y;
	
	public Note(String info, int x, int y) {
		super(info);
		this.x = new IntProperty(x);
		this.y = new IntProperty(y);
	}
	
	public Note(JSONObject note) throws JSONException {
		super(note);
		x = new IntProperty(note.getInt(X));
		y = new IntProperty(note.getInt(Y));
	}
	
	@Override
	public JSONObject toJSON() {
		JSONObject json = super.toJSON();
		json.put(X, x.get());
		json.put(Y, y.get());
		return json;
	}
	
	public void setLocation(Point p) {
		x.set(p.x);
		y.set(p.y);
	}
}
