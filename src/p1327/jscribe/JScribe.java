package p1327.jscribe;

import javax.swing.UIManager;

import p1327.jscribe.ui.window.Editor;

public class JScribe {
	
	public static void main(String...args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch(Exception e) {
			e.printStackTrace();
		}
		new Editor();
	}
}
