package p1327.jscribe.time;

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

import java.util.Stack;
import java.util.Vector;

public class Time {
	
	private Time() {};
	
	private static final Stack<Element> past = new Stack<>(),
								 future = new Stack<>();
	
	public static final Vector<Action> timeEvent = new Vector<>();
	
	private static volatile boolean locked = false;
	
	public static void rec(Action action, Action undo) {
		if(locked)
			return;
		locked = true;
		past.push(new Element(action, undo));
		future.clear();
		fireTimeEvent();
		action.act();
		locked = false;
	}
	
	public static void undo() {
		if(!canUndo())
			return;
		locked = true;
		Element e = past.pop();
		future.push(e);
		fireTimeEvent();
		e.undo.act();
		locked = false;
	}
	
	public static void redo() {
		if(!canRedo())
			return;
		locked = true;
		Element e = future.pop();
		past.push(e);
		fireTimeEvent();
		e.redo.act();
		locked = false;
	}
	
	public static void resetTime() {
		past.clear();
		future.clear();
		fireTimeEvent();
	}
	
	public static boolean canUndo() {
		return !past.isEmpty();
	}
	
	public static boolean canRedo() {
		return !future.isEmpty();
	}
	
	private static void fireTimeEvent() {
		for(Action a : timeEvent)
			a.act();
	}
}
