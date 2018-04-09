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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;

import p1327.jscribe.io.data.JSImg;
import p1327.jscribe.io.data.Text;
import p1327.jscribe.util.Message;
import p1327.jscribe.util.Renderer;
import p1327.jscribe.util.Static;

public class FileHandler {
	
	private static final Charset charset = Charset.forName("UTF-8");
	
	private FileHandler() {};
	
	// open
	public static JSArchive openJSC(File jsc) {
		try(FileInputStream fis = new FileInputStream(jsc)){
			JSArchive jsa = new JSArchive(jsc, new JSC(fis));
			fis.close();
			return jsa;
		}catch(Exception e) {
			Message.error("Load Error", e);
		}
		return null;
	}
	
	public static JSArchive openJSA(File archive) {
		try(ZipFile zf = new ZipFile(archive, charset)){
			JSArchive jsa = new JSArchive(archive, zf);
			zf.close();
			return jsa;
		}catch(Exception e) {
			Message.error("Load Error", e);
		}
		return null;
	}
	
	// save
	public static boolean save(JSArchive jsa) {
		try {
			if(jsa.name != null) {
				try(ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(jsa.name), charset)){
					jsa.write(zos, charset);
					zos.close();
					return true;
				}catch(Exception e) {
					throw e;
				}
			} else if(jsa.jscName != null) {
				try(FileWriter fw = new FileWriter(jsa.jscName)){
					jsa.jsc.toJSON().write(fw);
					fw.close();
					return true;
				}catch(Exception e) {
					throw e;
				}
			} else
				throw new IOException("Found unsolved timeloop.");
		}catch(Exception e) {
			Message.error("Save Error", e);
		}
		return false;
	}
	
	// import
	public static JSArchive importImg(File img) {
		try{
			if(!img.isFile())
				throw new IOException(img.getAbsolutePath() + " isn't a file");
			BufferedImage bi = ImageIO.read(img);
			if(bi == null)
				throw new IOException("Failed to open Image...");
			JSArchive jsa = new JSArchive(img.getParentFile());
			jsa.add(img.getName(), bi);
			return jsa;
		}catch(Exception e) {
			Message.error("Import Error", e);
		}
		return null;
	}
	
	public static JSArchive importFolder(File dir) {
		try{
			if(!dir.isDirectory())
				throw new IOException(dir.getAbsolutePath() + " isn't a directory");
			File[] imgs = dir.listFiles((d, name) -> {
					int pos = name.lastIndexOf('.');
					if(pos == -1)
						return false;
					name = name.substring(pos + 1);
					for(String ext : Static.supportedTypeList)
						if(ext.equals(name))
							return true;
					return false;
			});
			if(imgs.length < 1)
				throw new IOException("No importable Images in " + dir.getAbsolutePath());
			JSArchive jsa = new JSArchive(dir);
			for(File img : imgs) {
				BufferedImage bi = ImageIO.read(img);
				if(bi == null)
					throw new IOException("Failed to open Image " + img.getAbsolutePath());
				jsa.add(img.getName(), bi);
			}
			return jsa;
		}catch(Exception e) {
			Message.error("Import Error", e);
		}
		return null;
	}
	
	// export
	public static boolean exportImg(JSArchive jsa, File img) {
		try {
			String name = img.getName();
			String ext = name.substring(name.lastIndexOf('.') + 1);
			boolean hasExt = false;
			JSImg jsimg = jsa.jsc.imgs.get(0);
			for(String _ext : Static.supportedTypeList)
				if(_ext.equals(ext)) {
					hasExt = true;
					break;
				}
			if(!hasExt) {
				String iname = jsimg.img;
				int pos = iname.lastIndexOf('.') + 1;
				if(pos < 1)
					throw new RuntimeException("Found image without file-format " + name);
				ext = iname.substring(pos);
				name = name + "." + ext;
			}
			
			BufferedImage out = Renderer.copy(jsa.get(0));
			Renderer r = new Renderer(out.getGraphics());
			for(Text t : jsimg.texts)
				r.write(t, jsa.jsc.getTextStyle(t).compile());
			r.finish();
			writeImage(out, ext, img);
		}catch(Exception e) {
			Message.error("Import Error", e);
		}
		return false;
	}
	
	public static boolean exportFolder(JSArchive jsa, File dir) {
		try {
			if(!dir.isDirectory())
				throw new IOException(dir.getAbsolutePath() + " isn't a directory");
			String name, ext;
			BufferedImage bi;
			JSImg jsimg;
			int pos;
			for(int i = 0, l = jsa.size(); i < l; i++) {
				jsimg = jsa.jsc.imgs.get(i);
				name = jsimg.img;
				pos = name.lastIndexOf('.') + 1;
				if(pos < 1)
					throw new RuntimeException("Found image without file-format " + name);
				ext = name.substring(pos);

				bi = Renderer.copy(jsa.get(i));
				Renderer r = new Renderer(bi.getGraphics());
				for(Text t : jsimg.texts)
					r.write(t, jsa.jsc.getTextStyle(t).compile());
				r.finish();
				
				writeImage(bi, ext, new File(dir.getAbsolutePath() + "/" + name));
				
			}
		}catch(Exception e) {
			Message.error("Import Error", e);
		}
		return false;
	}
	
	// jss
	public static JSS importJSS(File jss) {
		try(FileInputStream fis = new FileInputStream(jss)){
			JSS _jss = new JSS(fis);
			fis.close();
			return _jss;
		}catch(Exception e) {
			Message.error("Failed to Import JSS-File", e);
		}
		return null;
	}
	
	public static boolean exportJSS(JSS jss, File out) {
		String name = out.getName();
		if(!name.endsWith(".jss"))
			out = new File(out.getParent() + "/" + name + ".jss");
		try(FileWriter fw = new FileWriter(out)){
			
			jss.toJSON().write(fw);
			fw.close();
			return true;
		}catch(Exception e) {
			Message.error("Failed to Export JSS-File", e);
		}
		return false;
	}
	
	public static void writeImage(BufferedImage img, String ext, File f) throws IOException {
		try(FileImageOutputStream fios = new FileImageOutputStream(f)){
			writeImage(img, ext, fios);
		} catch(IOException e){
			throw e;
		}
	}
	
	public static void writeImage(BufferedImage img, String ext, OutputStream out) throws IOException {
			writeImage(img, ext, ImageIO.createImageOutputStream(out));
	}
	
	public static void writeImage(BufferedImage img, String ext, ImageOutputStream out) throws IOException {
		ImageWriter writer = ImageIO.getImageWritersByFormatName(ext).next();
		ImageWriteParam writerParam = writer.getDefaultWriteParam();
		if(!ext.equals("png")) {
			writerParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			writerParam.setCompressionQuality(1f);
		}
		
		writer.setOutput(out);
		writer.write(null, new IIOImage(img, null, null), writerParam);
		writer.dispose();
	}
}
