package p1327.jscribe.ui.window;

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

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import p1327.jscribe.util.Static;
import p1327.jscribe.util.UIText;
import p1327.jscribe.util.Unserialzable;
import p1327.jscribe.util.Window;

public class About extends JDialog implements Unserialzable, Window{
	
	private static final Border border = new EmptyBorder(30, 30, 30, 30);
	private static final String info = UIText.displayable("<h2>JScribe</h2>Copyright (c) 2018 friendlyOverlordDev\n\n<b>Version:</b> " + Static.version+ "\n\n" +
			"JScribe is a free program, published under GPLv3, for adding texts to images.\nIf you like the program, please consider supporting the developer.");
	
	public About() {
		setModalityType(ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("About JScribe");
		
		JLabel l = new JLabel(info);
		l.setBorder(border);
		add(l);
		
		pack();
		center(getWidth(), getHeight());
		
		setVisible(true);
	}
}
