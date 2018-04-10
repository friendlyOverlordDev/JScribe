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

import java.util.Iterator;
import java.util.Vector;

import p1327.jscribe.io.JSS;

public class TextStyle implements Iterable<TaggedStyle>{
	
	public final JSS jss;
	
	private final Vector<String> tags;
	final Vector<TaggedStyle> styles = new Vector<>();
	
	public TextStyle(JSS parent, Vector<String> tags){
		this.jss = parent;
		this.tags = tags;
		for(String tag : tags)
			styles.add(new TaggedStyle(tag, parent));
	}
	
	public Style main() {
		return jss.main;
	}
	
	public TaggedStyle add(String tag) {
		TaggedStyle ts = new TaggedStyle(tag, jss);
		styles.add(ts);
		tags.add(tag);
		return ts;
	}
	
	public boolean remove(Style s) {
		int pos = -1;
		for(int i = 0, l = styles.size(); i < l; i++)
			if(styles.get(i).style == s) {
				pos = i;
				break;
			}
		if(pos < 0)
			return false;
		styles.remove(pos);
		tags.remove(pos);
		return true;
	}

	@Override
	public Iterator<TaggedStyle> iterator() {
		return new Iterator<TaggedStyle>() {
			
			private int i = 0;
			
			@Override
			public boolean hasNext() {
				return i < styles.size();
			}

			@Override
			public TaggedStyle next() {
				return styles.get(i++);
			}
		};
	}
	
	public Style compile() {
		Style s = new Style(), tmp;
		boolean font = true,
				color = true,
				size = true,
				style = true,
				outline = true,
				olWidth = true,
				xAlign = true,
				yAlign = true,
				line = true,
				rotation = true,
				caps = true;
		
		for(int i = styles.size() - 1; i > -1; i--) {
			tmp = styles.get(i).style;
			if(font && (tmp.useFont.get())) {
				font = false;
				s.font.set(tmp.font.get());
			}
			if(color && (tmp.useColor.get())) {
				color = false;
				s.color.set(tmp.color.get());
			}
			if(size && (tmp.useSize.get())) {
				size = false;
				s.size.set(tmp.size.get());
			}
			if(style && (tmp.useStyle.get())) {
				style = false;
				s.style.set(tmp.style.get());
			}
			if(outline && (tmp.useOutline.get())) {
				outline = false;
				s.outline.set(tmp.outline.get());
			}
			if(olWidth && (tmp.useOutlineWidth.get())) {
				olWidth = false;
				s.outlineWidth.set(tmp.outlineWidth.get());
			}
			if(xAlign && (tmp.useXAlign.get())) {
				xAlign = false;
				s.xAlign.set(tmp.xAlign.get());
			}
			if(yAlign && (tmp.useYAlign.get())) {
				yAlign = false;
				s.yAlign.set(tmp.yAlign.get());
			}
			if(line && (tmp.useLineHeight.get())) {
				line = false;
				s.lineHeight.set(tmp.lineHeight.get());
			}
			if(rotation && (tmp.useRotation.get())) {
				rotation = false;
				s.rotation.set(tmp.rotation.get());
			}
			if(caps && (tmp.useCaps.get())) {
				caps = false;
				s.caps.set(tmp.caps.get());
			}
		}
		tmp = jss.main;
		if(font) {
			s.font.set(tmp.font.get());
		}
		if(color) {
			s.color.set(tmp.color.get());
		}
		if(size) {
			s.size.set(tmp.size.get());
		}
		if(style) {
			s.style.set(tmp.style.get());
		}
		if(outline) {
			s.outline.set(tmp.outline.get());
		}
		if(olWidth) {
			s.outlineWidth.set(tmp.outlineWidth.get());
		}
		if(xAlign) {
			s.xAlign.set(tmp.xAlign.get());
		}
		if(yAlign) {
			s.yAlign.set(tmp.yAlign.get());
		}
		if(line) {
			s.lineHeight.set(tmp.lineHeight.get());
		}
		if(rotation) {
			s.rotation.set(tmp.rotation.get());
		}
		if(caps) {
			s.caps.set(tmp.caps.get());
		}
		
		return s;
	}
}
