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

import p1327.jscribe.util.data.event.IntChangeEvent;
import p1327.jscribe.util.data.event.IntChangeListener;

public class IntProperty {
	
	private int i;
	private final Vector<IntChangeListener> listeners = new Vector<>();
	
	public IntProperty() {
		this.i = 0;
	}
	
	public IntProperty(int i) {
		this.i = i;
	}
	
	public int get() {
		return i;
	}
	
	public void set(int i) {
		if(this.i == i)
			return;
		IntChangeEvent e = new IntChangeEvent(this.i, i);
		this.i = i;
		for(IntChangeListener l : listeners)
			l.change(e);
	}
	
	public void add(IntChangeListener l) {
		addChangeListener(l);
	}
	
	public void addChangeListener(IntChangeListener l) {
		listeners.add(l);
	}
	
	public boolean remove(IntChangeListener l) {
		return removeChangeListener(l);
	}
	
	public boolean removeChangeListener(IntChangeListener l) {
		return listeners.remove(l);
	}
}
