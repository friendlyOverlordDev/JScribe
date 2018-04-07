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

import java.awt.Color;
import java.awt.Font;

import org.json.JSONObject;

import p1327.jscribe.util.JSONable;
import p1327.jscribe.util.Static;
import p1327.jscribe.util.data.BoolProperty;
import p1327.jscribe.util.data.DoubleProperty;
import p1327.jscribe.util.data.IntProperty;
import p1327.jscribe.util.data.Property;

public class Style implements JSONable{

	public static final int PLAIN = Font.PLAIN,
							BOLD = Font.BOLD,
							ITALIC = Font.ITALIC,
							
							LEFT = -1,
							CENTER = 0,
							RIGHT = 1,
							
							TOP = -1,
							BOTTOM = 1;
	
	
	private static final String FONT = "font",
								SIZE = "size",
								STYLE = "style", // plain | bold | italic
								COLOR = "color",
								X_ALIGN = "xAlign",
								Y_ALIGN = "yAlign",
								OUTLINE = "outline", // color of outline
								OL_WIDTH = "olWidth",
								LINE_HEIGHT = "lineHeight",
								ROTATION = "rotation",
								USE_PREFIX = "use__";

	public final Property<String> font;
	public final Property<Color> color,
								 outline;
	public final DoubleProperty rotation,
								outlineWidth;
	public final IntProperty size,
							 style,
							 xAlign,
							 yAlign,
							 lineHeight;
	public final BoolProperty useFont,
							  useRotation,
							  useOutlineWidth,
							  useSize,
							  useStyle,
							  useColor,
							  useXAlign,
							  useYAlign,
							  useOutline,
							  useLineHeight;
	
	
	public Style() {
		font = new Property<>(Static.defaultRenderFont);
		rotation = new DoubleProperty(0);
		outlineWidth = new DoubleProperty(2);
		size = new IntProperty(16);
		style = new IntProperty(PLAIN);
		color = new Property<>(Color.black);
		xAlign = new IntProperty(CENTER);
		yAlign = new IntProperty(CENTER);
		outline = new Property<>(Color.white);
		lineHeight = new IntProperty(0);

		useFont = new BoolProperty();
		useRotation = new BoolProperty();
		useOutlineWidth = new BoolProperty();
		useSize = new BoolProperty();
		useStyle = new BoolProperty();
		useColor = new BoolProperty();
		useXAlign = new BoolProperty();
		useYAlign = new BoolProperty();
		useOutline = new BoolProperty();
		useLineHeight = new BoolProperty();
	}
	
	public Style(JSONObject data) {
		font = new Property<>(data.getString(FONT));
		color = new Property<>(new Color(data.getInt(COLOR)));
		outline = new Property<>(new Color(data.getInt(OUTLINE)));
		rotation = new DoubleProperty(data.getDouble(ROTATION));
		outlineWidth = new DoubleProperty(data.getDouble(OL_WIDTH));
		size = new IntProperty(data.getInt(SIZE));
		style = new IntProperty(data.getInt(STYLE));
		xAlign = new IntProperty(data.getInt(X_ALIGN));
		yAlign = new IntProperty(data.getInt(Y_ALIGN));
		lineHeight = new IntProperty(data.getInt(LINE_HEIGHT));

		useFont = new BoolProperty(data.getBoolean(USE_PREFIX + FONT));
		useRotation = new BoolProperty(data.getBoolean(USE_PREFIX + ROTATION));
		useOutlineWidth = new BoolProperty(data.getBoolean(USE_PREFIX + OL_WIDTH));
		useSize = new BoolProperty(data.getBoolean(USE_PREFIX + SIZE));
		useStyle = new BoolProperty(data.getBoolean(USE_PREFIX + STYLE));
		useColor = new BoolProperty(data.getBoolean(USE_PREFIX + COLOR));
		useXAlign = new BoolProperty(data.getBoolean(USE_PREFIX + X_ALIGN));
		useYAlign = new BoolProperty(data.getBoolean(USE_PREFIX + Y_ALIGN));
		useOutline = new BoolProperty(data.getBoolean(USE_PREFIX + OUTLINE));
		useLineHeight = new BoolProperty(data.getBoolean(USE_PREFIX + LINE_HEIGHT));
	}
	
	@Override
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		json.put(FONT, font.get());
		json.put(COLOR, color.get().getRGB());
		json.put(OUTLINE, outline.get().getRGB());
		json.put(ROTATION, rotation.get());
		json.put(OL_WIDTH, outlineWidth.get());
		json.put(SIZE, size.get());
		json.put(STYLE, style.get());
		json.put(X_ALIGN, xAlign.get());
		json.put(Y_ALIGN, yAlign.get());
		json.put(LINE_HEIGHT, lineHeight.get());
		
		json.put(USE_PREFIX + FONT, useFont.get());
		json.put(USE_PREFIX + ROTATION, useRotation.get());
		json.put(USE_PREFIX + OL_WIDTH, useOutlineWidth.get());
		json.put(USE_PREFIX + SIZE, useSize.get());
		json.put(USE_PREFIX + STYLE, useStyle.get());
		json.put(USE_PREFIX + COLOR, useColor.get());
		json.put(USE_PREFIX + X_ALIGN, useXAlign.get());
		json.put(USE_PREFIX + Y_ALIGN, useYAlign.get());
		json.put(USE_PREFIX + OUTLINE, useOutline.get());
		json.put(USE_PREFIX + LINE_HEIGHT, useLineHeight.get());
		return json;
	}
}
