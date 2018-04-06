package p1327.jscribe.util.data;

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

import java.util.Vector;

import p1327.jscribe.util.data.event.BoolChangeEvent;
import p1327.jscribe.util.data.event.BoolChangeListener;

public class BoolProperty {
	
	private boolean b;
	private final Vector<BoolChangeListener> listeners = new Vector<>();
	
	public BoolProperty() {
		this.b = false;
	}
	
	public BoolProperty(boolean b) {
		this.b = b;
	}
	
	public boolean get() {
		return b;
	}
	
	public void set(boolean b) {
		if(this.b == b)
			return;
		BoolChangeEvent e = new BoolChangeEvent(this.b, b);
		this.b = b;
		for(BoolChangeListener l : listeners)
			l.change(e);
	}
	
	public void add(BoolChangeListener l) {
		addChangeListener(l);
	}
	
	public void addChangeListener(BoolChangeListener l) {
		listeners.add(l);
	}
	
	public boolean remove(BoolChangeListener l) {
		return removeChangeListener(l);
	}
	
	public boolean removeChangeListener(BoolChangeListener l) {
		return listeners.remove(l);
	}
}
