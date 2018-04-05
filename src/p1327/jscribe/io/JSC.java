package p1327.jscribe.io;

import java.util.Vector;

import org.json.JSONObject;

import p1327.jscribe.io.data.JSImg;
import p1327.jscribe.util.JSON;
import p1327.jscribe.util.JSONable;

public class JSC implements JSONable{
	
	private static final String IMGS = "imgs";
	
	public final Vector<JSImg> imgs;
	
	public JSC() {
		imgs = new Vector<>();
	}
	
	public JSC(JSONObject jsc) {
		imgs = JSON.extractAsJSONObjectToVector(jsc.getJSONArray(IMGS), o -> new JSImg(o));
	}
	
	@Override
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		json.put(IMGS, JSON.packJSONableVector(imgs));
		return json;
	}
}
