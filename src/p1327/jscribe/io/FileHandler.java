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
import java.nio.charset.Charset;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;

import p1327.jscribe.util.Message;
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
}
