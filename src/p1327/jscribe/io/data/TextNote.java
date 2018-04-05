package p1327.jscribe.io.data;

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
