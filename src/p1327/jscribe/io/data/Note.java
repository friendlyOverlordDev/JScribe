package p1327.jscribe.io.data;

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
