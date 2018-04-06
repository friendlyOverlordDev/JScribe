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

import p1327.jscribe.util.data.event.ChangeEvent;
import p1327.jscribe.util.data.event.ChangeListener;

public class Property<T> {
	
	private T data;
	private final Vector<ChangeListener<T>> listeners = new Vector<>();
	
	public Property() {
		this.data = null;
	}
	
	public Property(T data) {
		this.data = data;
	}
	
	public T get() {
		return data;
	}
	
	public void set(T data) {
		if(this.data.equals(data))
			return;
		ChangeEvent<T> e = new ChangeEvent<T>(this.data, data);
		this.data = data;
		for(ChangeListener<T> l : listeners)
			l.change(e);
	}
	
	public void add(ChangeListener<T> l) {
		addChangeListener(l);
	}
	
	public void addChangeListener(ChangeListener<T> l) {
		listeners.add(l);
	}
	
	public boolean remove(ChangeListener<T> l) {
		return removeChangeListener(l);
	}
	
	public boolean removeChangeListener(ChangeListener<T> l) {
		return listeners.remove(l);
	}
}
