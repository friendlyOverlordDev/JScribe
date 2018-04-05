package p1327.jscribe.util;

import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

public interface Window {
	
	public static final int bottomSpace = 100;
	
	default void center(int width, int height) {
		Rectangle rect = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getBounds();
		if(width > rect.width)
			width = rect.width;
		if(height > rect.height)
			height = rect.height - bottomSpace;
		int x = (rect.x + rect.width - width) / 2;
		int y = (rect.y + rect.height - height) / 2;
		setSize(width, height);
		setLocation(x, y);
	}
	
	void setSize(int width, int height);
	
	void setLocation(int x, int y);
}
