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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;

import org.json.JSONException;

import p1327.jscribe.io.data.JSImg;

public class JSArchive {

	public File name = null;
	public File jscName = null;
	public final JSC jsc;
	public final Vector<BufferedImage> imgs = new Vector<>();
	
	JSArchive() {
		jsc = new JSC();
	}
	
	JSArchive(File name, JSC jsc) throws JSONException, IOException {
		this.jscName = name;
		this.jsc = jsc;
		String parent = name.getParent();
		if(parent == null)
			throw new IOException(".jsc parent directory not found for " + name.getAbsolutePath());
		for(JSImg img : jsc.imgs)
			imgs.add(ImageIO.read(new File(parent + "/" + img.img)));
	}
}
