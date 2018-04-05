package p1327.jscribe.io;

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
