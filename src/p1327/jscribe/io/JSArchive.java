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
import p1327.jscribe.util.Renderer;

public class JSArchive {
	
	private static final String data = "data.jsc"; 

	public File name = null;
	public File jscName = null;
	public final JSC jsc;
	private final Vector<BufferedImage> imgs = new Vector<>();
	final Vector<File> imgFiles = new Vector<>();
	
	
	JSArchive(File archive, ZipFile zf) throws IOException{
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
			imgFiles.add(null);
		}
	}
	
	JSArchive() {
		jsc = new JSC();
	}
	
	JSArchive(File name, JSC jsc) throws JSONException, IOException {
		this.jscName = name;
		this.jsc = jsc;
		String parent = name.getParent();
		if(parent == null)
			throw new IOException(".jsc parent directory not found for " + name.getAbsolutePath());
		File f;
		for(JSImg img : jsc.imgs) {
			f = new File(parent + "/" + img.img);
			imgs.add(ImageIO.read(f));
			imgFiles.add(f);
		}
	}
	
	void write(ZipOutputStream zos, Charset charset) throws IOException{
		int l = imgs.size();
		if(l != jsc.imgs.size())
			throw new RuntimeException("Found " + l + " images and data for " + jsc.imgs.size() + " images...\nCritical Runtime Error");
		zos.putNextEntry(new ZipEntry(data));
		zos.write(jsc.toJSON().toString().getBytes(charset));
		
		String name;
//		String ext;
		int pos;
		for(int i = 0; i < l; i++) {
			zos.putNextEntry(new ZipEntry(name = jsc.imgs.get(i).img));
			pos = name.lastIndexOf('.') + 1;
			if(pos < 1)
				throw new RuntimeException("Found image without file-format " + name);
//			ext = name.substring(pos); // force "png"
			FileHandler.writeImage(imgs.get(i), "png", zos);
			imgFiles.set(i, null);
		}
	}
	
	public int size() {
		return imgs.size();
	}
	
	public BufferedImage get(int index) throws ArrayIndexOutOfBoundsException {
		return imgs.get(index);
	}
	
	public BufferedImage set(int index, BufferedImage img) throws ArrayIndexOutOfBoundsException {
		return imgs.set(index, img);
	}
	
	public File getFile(int index) throws ArrayIndexOutOfBoundsException {
		return imgFiles.get(index);
	}
	
	public File setFile(int index, File f) throws ArrayIndexOutOfBoundsException{
		return imgFiles.set(index, f);
	}
	
	public void add(File f) throws IOException {
		String name = f.getName();
		for(JSImg i : jsc.imgs) {
			if(i.img.equals(name))
				throw new IOException("An image with the name " + name + " already exists.");
		}
		BufferedImage img = ImageIO.read(f);
		if(img == null)
			throw new IOException("Failed to open Image...");
		imgs.add(img);
		imgFiles.add(jscName == null ? null : f);
		jsc.imgs.add(new JSImg(name));
	}
	
	public void copy(int index, String newName, File newFile) throws ArrayIndexOutOfBoundsException {
		imgs.add(Renderer.copy(imgs.get(index)));
		imgFiles.add(newFile);
		JSImg nImg = new JSImg(jsc.imgs.get(index).toJSON());
		nImg.img = newName;
		jsc.imgs.add(nImg);
	}
	
	public void remove(int index) throws ArrayIndexOutOfBoundsException {
		jsc.imgs.remove(index);
		imgs.remove(index);
		imgFiles.remove(index);
	}
}
