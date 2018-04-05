package p1327.jscribe.io.data;

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
