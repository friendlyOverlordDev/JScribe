package p1327.jscribe.io;

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
