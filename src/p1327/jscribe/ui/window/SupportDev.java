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

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import p1327.jscribe.util.UIText;
import p1327.jscribe.util.Unserialzable;
import p1327.jscribe.util.Window;

public class SupportDev extends JDialog implements Window, Unserialzable{
	
	private static final String message = UIText.displayable("Thank you for considering to donate.\nJScribe offers currently the following options:");
	private static final Dimension optNameSize = new Dimension(80, 0);
	private static final Dimension buttonWidth = new Dimension(100, 27);
	private static final Border margin = new EmptyBorder(10, 10, 10, 10);
	private static final Border optBorder = new EmptyBorder(3, 0, 3, 0);
	private static final Cursor textCursor = Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR); 
	
	private static final String[][] options = {
			{"Bitcoin (BTC):", "1PPDyhR5fcybRtBf8fntwAtRWVCGakkjZR"},
			{"Litecoin (LTC):", "Lcs9kuZNtPEC7qMpawTPPeVLka8XZpM46Z"},
			{"Dash:", "Xg3Z6S4Bn2pLRVfebZR1e94MdnmtmmQZMd"}
	};
	
	public SupportDev() {
		setModalityType(ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Donate to Support the Developer");
		
		JPanel root = new JPanel();
		root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
		root.setBorder(margin);
		
		JLabel info = new JLabel(message);
		JPanel wrapper = new JPanel(new BorderLayout());
		wrapper.setBorder(optBorder);
		wrapper.add(info);
		root.add(wrapper);
		
		JPanel o;
		JLabel name;
		for(String[] opt : options) {
			o = new JPanel();
			o.setLayout(new BoxLayout(o, BoxLayout.X_AXIS));
			o.setBorder(optBorder);
			
			name = new JLabel(opt[0]);
			name.setPreferredSize(optNameSize);
			o.add(name);
			o.add(immutableText(opt[1]));
			
			root.add(o);
		}
		
		JButton done = new JButton("Done");
		done.setPreferredSize(buttonWidth);
		done.addActionListener(e -> dispose());
		root.add(done);
		
		add(root);
		
		pack();
		center();
		setVisible(true);
	}
	
	private static JTextField immutableText(final String text) {
		JTextField field = new JTextField(text);
		field.setEditable(false);
		field.setCursor(textCursor);
		field.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				field.setSelectionStart(0);
				field.setSelectionEnd(text.length());
			}
		});
		return field;
	}
}
