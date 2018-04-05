package p1327.jscribe.io.data;

import java.util.Vector;

import org.json.JSONException;
import org.json.JSONObject;

import p1327.jscribe.util.JSON;
import p1327.jscribe.util.JSONable;

public class Text implements JSONable {
	
	private static final String TEXT = "text",
								X = "x",
								Y = "y",
								W = "w",
								H = "h",
								NOTES = "notes";
	
	public String text;
	public int x, y, w, h;
	public final Vector<TextNote> notes;
	
	public Text(String text, int x, int y, int w, int h) {
		this.text = text;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		notes = new Vector<>();
	}
	
	public Text(JSONObject text) throws JSONException {
		this.text = text.getString(TEXT);
		x = text.getInt(X);
		y = text.getInt(Y);
		w = text.getInt(W);
		h = text.getInt(H);
		notes = JSON.extractAsJSONObjectToVector(text.getJSONArray(NOTES), o -> new TextNote(o));
	}
	
	@Override
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		json.put(TEXT, text);
		json.put(X, x);
		json.put(Y, y);
		json.put(W, w);
		json.put(H, h);
		json.put(NOTES, JSON.packJSONableVector(notes));
		return json;
	}
}
