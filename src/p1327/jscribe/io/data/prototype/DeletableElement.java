package p1327.jscribe.io.data.prototype;

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
import java.util.function.Consumer;

public class DeletableElement {

	private final Vector<Consumer<DeletableElement>> deleteListener = new Vector<>();
	
	public boolean addDeleteListener(Consumer<DeletableElement> l) {
		return deleteListener.add(l);
	}
	
	public boolean removeDeleteListener(Consumer<DeletableElement> l) {
		return deleteListener.remove(l);
	}
	
	/**
	 * doesn't actually delete the note, however invokes all the delete listeners which should handle it.
	 */
	public void delete() {
		@SuppressWarnings("unchecked")
		Consumer<DeletableElement>[] dL = (Consumer<DeletableElement>[]) deleteListener.toArray(new Consumer<?>[deleteListener.size()]);
		for(Consumer<DeletableElement> l : dL)
			l.accept(this);
		deleteListener.clear();
	}
}
