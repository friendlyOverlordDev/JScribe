package p1327.jscribe.util;

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
