package p1327.jscribe.ui;

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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import p1327.jscribe.io.data.JSImg;
import p1327.jscribe.io.data.Note;
import p1327.jscribe.io.data.Text;
import p1327.jscribe.util.UIText;
import p1327.jscribe.util.Unserialzable;

public class DataViewer extends JTabbedPane implements Unserialzable {
	
	private static final int maxWidth = 300;
	
	private final JPanel notes, texts;
	private final JLabel notesTitle, textsTitle;
	
	SwitchViewer active = null;
	
	public DataViewer() {
		
		notes = new JPanel();
		notes.setLayout(new BoxLayout(notes, BoxLayout.Y_AXIS));
		texts = new JPanel();
		texts.setLayout(new BoxLayout(texts, BoxLayout.Y_AXIS));
		
		JScrollPane sp = new JScrollPane(notes);
		sp.setBorder(null);
		notesTitle = addComplexTab("Notes", sp);
		sp = new JScrollPane(texts);
		sp.setBorder(null);
		textsTitle = addComplexTab("Text", sp);

		setPreferredSize(new Dimension(maxWidth, 0));
	}
	
	private JLabel addComplexTab(String name, JComponent c) {
		addTab(name, c);
		JLabel l = new JLabel(name);
		l.setFont(UIText.bold(getFont()));
		l.setPreferredSize(new Dimension(130, l.getPreferredSize().height));
		setTabComponentAt(getTabCount() - 1, l);
		return l;
	}
	
	public void setImage(JSImg img) {
		notes.removeAll();
		texts.removeAll();
		for(Note n : img.notes)
			notes.add(new NoteViewer(this, n));
		for(Text t : img.texts)
			texts.add(new TextViewer(this, t));
		notesTitle.setText("Notes (" + img.notes.size() + ")");
		textsTitle.setText("Text (" + img.texts.size() + ")");
	}
	
	private static class SwitchViewer extends JPanel implements Unserialzable{
		
		protected static final Dimension textDimension = new Dimension(0, 200);

		private final DataViewer parent;
		protected final JLabel l;
		protected final JPanel content;
		
		public SwitchViewer(DataViewer parent) {
			this.parent = parent;
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			l = new JLabel("test content");
			l.setAlignmentX(LEFT_ALIGNMENT);
			l.setBorder(new EmptyBorder(0, 10, 0, 0));
			l.setPreferredSize(new Dimension(maxWidth - 20, 30));
			content = new JPanel();
			content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
			content.setAlignmentX(LEFT_ALIGNMENT);
			content.setVisible(false);
			super.add(l);
			super.add(content);
			super.add(new JSeparator());
			
			addMouseListener(new MouseListener() {
				
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
					setActive();
				}
			});
		}
		
		@Override
		public Dimension getMaximumSize() {
			Dimension d = super.getMaximumSize();
			d.height = getPreferredSize().height;
			return d;
		}
		
		@Override
		public Component add(Component comp) {
			return content.add(comp);
		}
		
		public void setActive() {
			if(isActive())
				return;
			if(parent.active != null)
				parent.active.setInactive();
			parent.active = this;
			l.setVisible(false);
			content.setVisible(true);
//			revalidate();
		}
		
		public void setInactive() {
			l.setVisible(true);
			content.setVisible(false);
//			revalidate();
		}
		
		public boolean isActive() {
			return this == parent.active;
		}
	}
	
	private static class NoteViewer extends SwitchViewer{
		
		final JTextArea info;
		
		public NoteViewer(DataViewer parent, Note n) {
			super(parent);
			
			String s = n.info.get();
			l.setText(UIText.displayableSingleLine(s));
			
			info = new JTextArea();
			info.setText(s);
			info.getDocument().addDocumentListener(new DocumentListener() {
				
				@Override
				public void removeUpdate(DocumentEvent e) {
					update();
				}
				
				@Override
				public void insertUpdate(DocumentEvent e) {
					update();
				}
				
				@Override
				public void changedUpdate(DocumentEvent e) {
					update();
				}
				
				private void update() {
					String s = info.getText();
					n.info.set(s);
					l.setText(UIText.displayableSingleLine(s));
				}
			});
			
			add(new JScrollPane(info)).setPreferredSize(textDimension);
		}
	}
	
	private static class TextViewer extends SwitchViewer{
		
		final JTextArea text;
		
		public TextViewer(DataViewer parent, Text t) {
			super(parent);
			
			String s = t.text.get();
			l.setText(UIText.displayableSingleLine(s));
			
			text = new JTextArea();
			text.setText(s);
			text.getDocument().addDocumentListener(new DocumentListener() {
				
				@Override
				public void removeUpdate(DocumentEvent e) {
					update();
				}
				
				@Override
				public void insertUpdate(DocumentEvent e) {
					update();
				}
				
				@Override
				public void changedUpdate(DocumentEvent e) {
					update();
				}
				
				private void update() {
					String s = text.getText();
					t.text.set(s);
					l.setText(UIText.displayableSingleLine(s));
				}
			});

			add(new JScrollPane(text)).setPreferredSize(textDimension);
		}
	}
}
