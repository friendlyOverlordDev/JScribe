package p1327.jscribe.util;

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

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Vector;
import java.util.function.Function;

import org.json.JSONArray;
import org.json.JSONObject;

public class JSON {
	
	private JSON() {}
	
	public static Vector<String> extractStrings(JSONArray array){
		int l = array.length();
		Vector<String> out = new Vector<String>(l);
		for(int i = 0; i < l; i++)
			out.add(array.getString(i));
		return out;
	}
	
	public static <T> Vector<T> extractAsJSONObjectToVector(JSONArray array, Function<JSONObject, T> converter) {
		int l = array.length();
		Vector<T> out = new Vector<T>(l);
		for(int i = 0; i < l; i++)
			out.add(converter.apply(array.getJSONObject(i)));
		return out;
	}
	
	public static <T> HashMap<String, T> extractAsJSONObjectToMap(JSONObject obj, Function<JSONObject, T> converter){
		int l = obj.length();
		HashMap<String, T> map = new HashMap<>(l);
		for(String key : obj.keySet()) 
			map.put(key, converter.apply(obj.getJSONObject(key)));
		return map;
	}
	
	public static JSONArray packStrings(Vector<String> data) {
		JSONArray array = new JSONArray();
		for(String s : data)
			array.put(s);
		return array;
	}
	
	public static JSONArray packJSONableVector(Vector<? extends JSONable> data) {
		JSONArray array = new JSONArray();
		for(JSONable j : data)
			array.put(j.toJSON());
		return array;
	}
	
	public static JSONObject packJSONableMap(HashMap<String, ? extends JSONable> data) {
		JSONObject o = new JSONObject();
		for(Entry<String, ? extends JSONable> entry : data.entrySet())
			o.put(entry.getKey(), entry.getValue().toJSON());
		return o;
	}
}
