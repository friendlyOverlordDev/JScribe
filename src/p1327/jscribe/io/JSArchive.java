package p1327.jscribe.io;

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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;

import org.json.JSONException;

import p1327.jscribe.io.data.JSImg;

public class JSArchive {
	
	private static final String data = "data.jsc"; 

	public File name = null;
	public File jscName = null;
	public final JSC jsc;
	private final Vector<BufferedImage> imgs = new Vector<>();
	
	final File baseDirectory; // don't save this 
	
	JSArchive(File archive, ZipFile zf) throws IOException{
		this.baseDirectory = archive.getParentFile();
		this.name = archive;
		
		ZipEntry entry = zf.getEntry(data);
		if(entry == null)
			throw new IOException("Invalid jsa; can't find data.jsc in it.");
		jsc = new JSC(zf.getInputStream(entry));
		for(JSImg img : jsc.imgs) {
			entry = zf.getEntry(img.img);
			if(entry == null)
				throw new IOException("Invalid jsa; can't find image " + img.img + " in it.");
			imgs.add(ImageIO.read(zf.getInputStream(entry)));
		}
	}
	
	JSArchive(File baseDirecty) {
		this.baseDirectory = baseDirecty;
		jsc = new JSC();
	}
	
	JSArchive(File name, JSC jsc) throws JSONException, IOException {
		this.baseDirectory = name.getParentFile();
		this.jscName = name;
		this.jsc = jsc;
		String parent = name.getParent();
		if(parent == null)
			throw new IOException(".jsc parent directory not found for " + name.getAbsolutePath());
		for(JSImg img : jsc.imgs)
			imgs.add(ImageIO.read(new File(parent + "/" + img.img)));
	}
	
	void write(ZipOutputStream zos, Charset charset) throws IOException{
		int l = imgs.size();
		if(l != jsc.imgs.size())
			throw new RuntimeException("Found " + l + " images and data for " + jsc.imgs.size() + " images...\nCritical Runtime Error");
		zos.putNextEntry(new ZipEntry(data));
		zos.write(jsc.toJSON().toString().getBytes(charset));
		
		String name, ext;
		int pos;
		for(int i = 0; i < l; i++) {
			zos.putNextEntry(new ZipEntry(name = jsc.imgs.get(i).img));
			pos = name.lastIndexOf('.') + 1;
			if(pos < 1)
				throw new RuntimeException("Found image without file-format " + name);
			ext = name.substring(pos);
			ImageIO.write(imgs.get(i), ext, zos);
		}
	}
	
	public int size() {
		return imgs.size();
	}
	
	public BufferedImage get(int index) throws ArrayIndexOutOfBoundsException {
		return imgs.get(index);
	}
	
	void add(String name, BufferedImage img){
		imgs.add(img);
		jsc.imgs.add(new JSImg(name));
	}
}
