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

import java.util.Vector;
import java.util.function.Function;

import org.json.JSONArray;
import org.json.JSONObject;

public class JSON {
	
	private JSON() {}
	
	public static <T> Vector<T> extractAsJSONObjectToVector(JSONArray array, Function<JSONObject, T> converter) {
		int l = array.length();
		Vector<T> out = new Vector<T>(l);
		for(int i = 0; i < l; i++)
			out.add(converter.apply(array.getJSONObject(i)));
		return out;
	}
	
	public static JSONArray packJSONableVector(Vector<? extends JSONable> data) {
		JSONArray array = new JSONArray();
		for(int i = 0, l = data.size(); i < l; i++)
			array.put(data.get(i).toJSON());
		return array;
	}
}
