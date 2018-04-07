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

import p1327.jscribe.util.data.event.DoubleChangeEvent;
import p1327.jscribe.util.data.event.DoubleChangeListener;

public class DoubleProperty {
	
	private double d;
	private final Vector<DoubleChangeListener> listeners = new Vector<>();
	
	public DoubleProperty() {
		this.d = 0;
	}
	
	public DoubleProperty(double d) {
		this.d = d;
	}
	
	public double get() {
		return d;
	}
	
	public void set(double d) {
		if(this.d == d)
			return;
		DoubleChangeEvent e = new DoubleChangeEvent(this.d, d);
		this.d = d;
		for(DoubleChangeListener l : listeners)
			l.change(e);
	}
	
	public void add(DoubleChangeListener l) {
		addChangeListener(l);
	}
	
	public void addChangeListener(DoubleChangeListener l) {
		listeners.add(l);
	}
	
	public boolean remove(DoubleChangeListener l) {
		return removeChangeListener(l);
	}
	
	public boolean removeChangeListener(DoubleChangeListener l) {
		return listeners.remove(l);
	}
}
