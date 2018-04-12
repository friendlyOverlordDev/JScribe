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

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.inet.jortho.SpellChecker;

import p1327.jscribe.io.data.JSImg;
import p1327.jscribe.io.data.Note;
import p1327.jscribe.io.data.SimpleNote;
import p1327.jscribe.io.data.Text;
import p1327.jscribe.io.data.prototype.DeletableElement;
import p1327.jscribe.time.Time;
import p1327.jscribe.ui.window.Editor;
import p1327.jscribe.ui.window.StyleEditor;
import p1327.jscribe.util.Message;
import p1327.jscribe.util.UIText;
import p1327.jscribe.util.Unserialzable;
import p1327.jscribe.util.data.event.BoolChangeListener;
import p1327.jscribe.util.data.event.ChangeListener;
import p1327.jscribe.util.data.event.IntChangeListener;

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
		
		setSelectedIndex(1);
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
		// prepare ui-elements for removal by removing listeners
		Component[] cs = notes.getComponents();
		for(Component c : cs)
			if(c instanceof NoteViewer)
				((NoteViewer)c).destroy();
		cs = texts.getComponents();
		for(Component c : cs)
			if(c instanceof TextViewer)
				((TextViewer)c).destroy();
		
		// remove old ui
		notes.removeAll();
		texts.removeAll();
		
		// add new ui
		for(Note n : img.notes)
			notes.add(new NoteViewer(this, n));
		for(Text t : img.texts)
			texts.add(new TextViewer(this, t));
		notesTitle.setText("Notes (" + img.notes.size() + ")");
		textsTitle.setText("Text (" + img.texts.size() + ")");
		repaint();
	}
	
	public void addNote(Note n) {
		notes.add(new NoteViewer(this, n));
		updateNotesTitle();
		revalidate();
		repaint();
	}
	
	void updateNotesTitle() {
		notesTitle.setText("Notes (" + notes.getComponentCount() + ")");
	}
	
	public void addText(Text t) {
		texts.add(new TextViewer(this, t));
		updateTextsTitle();
		revalidate();
		repaint();
	}
	
	void updateTextsTitle() {
		textsTitle.setText("Text (" + texts.getComponentCount() + ")");
	}
	
	public void setActive(Note n) {
		Component[] cs = notes.getComponents();
		NoteViewer nv;
		for(Component c : cs) {
			if(c instanceof NoteViewer) {
				nv = (NoteViewer)c;
				if(nv.n == n) {
					setSelectedIndex(0);
					nv.setActive();
					nv.info.grabFocus();
					nv.info.setCaretPosition(nv.info.getText().length());
					break;
				}
			}
		}
	}
	
	public void setActive(Text t) {
		Component[] cs = texts.getComponents();
		TextViewer tv;
		for(Component c : cs) {
			if(c instanceof TextViewer) {
				tv = (TextViewer)c;
				if(tv.t == t) {
					setSelectedIndex(1);
					tv.setActive();
					tv.text.grabFocus();
					tv.text.setCaretPosition(tv.text.getText().length());
					break;
				}
			}
		}
	}
	
	public void setSpellChecking( boolean enable) {
		Component[] cs = notes.getComponents();
		for(Component c : cs)
			if(c instanceof NoteViewer)
				((NoteViewer)c).setSpellChecking(enable);
		cs = texts.getComponents();
		for(Component c : cs)
			if(c instanceof TextViewer)
				((TextViewer)c).setSpellChecking(enable);
	}
	
	public void updateTextNotes(Text t) {
		Component[] cs = texts.getComponents();
		for(Component c : cs)
			if(c instanceof TextViewer) {
				TextViewer tv = (TextViewer) c;
				if(tv.t == t) {
					tv.updateNotes();
					return;
				}
			}
	}
	
	
	
	private static class SwitchViewer extends JPanel implements Unserialzable{
		
		protected static final Dimension textDimension = new Dimension(0, 200),
										 labelDimension = new Dimension(maxWidth - 50, 30);
		protected static final Border titleBorder = new EmptyBorder(0, 10, 0, 0),
									  textBorder = new EmptyBorder(3, 5, 3, 0);
		final Color normalBackground,
					darkenBackground,
					lightenBackground;

		protected final DataViewer parent;
		protected final JLabel l;
		protected final JPanel content;
		
		public SwitchViewer(DataViewer parent) {
			this.parent = parent;
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			normalBackground = getBackground();
			darkenBackground = normalBackground.darker();
			lightenBackground = normalBackground.brighter();
			
			l = new JLabel("");
			l.setAlignmentX(LEFT_ALIGNMENT);
			l.setBorder(titleBorder);
			l.setPreferredSize(labelDimension);
			
			content = new JPanel();
			content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
			content.setAlignmentX(LEFT_ALIGNMENT);
			content.setVisible(false);
			super.add(l);
			super.add(content);
			super.add(new JSeparator());
			
			addMouseListener(new MouseListener() {
				
				private boolean isOver = false,
								down = false;
				
				@Override
				public void mouseReleased(MouseEvent e) {
					if(down)
						setActive();
					down = false;
					if(isOver)
						setBackground(lightenBackground);
				}
				
				@Override
				public void mousePressed(MouseEvent e) {
					down = true;
					if(isOver)
						setBackground(darkenBackground);
				}
				
				@Override
				public void mouseExited(MouseEvent e) {
					down = false;
					isOver = false;
					setBackground(normalBackground);
				}
				
				@Override
				public void mouseEntered(MouseEvent e) {
					isOver = true;
					setBackground(lightenBackground);
				}
				
				@Override
				public void mouseClicked(MouseEvent e) {}
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
			if(comp instanceof JComponent)
				((JComponent) comp).setAlignmentX(LEFT_ALIGNMENT);
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
			revalidate();
		}
		
		public void setInactive() {
			l.setVisible(true);
			content.setVisible(false);
			revalidate();
		}
		
		public boolean isActive() {
			return this == parent.active;
		}
	}
	
	
	
	private static class NoteViewer extends SwitchViewer{
		
		private static final Color uncheckedColor = Color.red,
								   checkedColor = Color.black;
		
		final Note n;
		
		final JTextArea info;
		
		private boolean checkSpelling = true;
		
		private final Consumer<DeletableElement> deleteL;
		private final ChangeListener<String> infoL;
		private final BoolChangeListener checkedL;
		private final IntChangeListener posL;
		
		public NoteViewer(DataViewer parent, Note n) {
			super(parent);
			this.n = n;
			n.addDeleteListener(deleteL = e -> {
				destroy();
			});
			
			String s = n.info.get();
			l.setText(UIText.displayableSingleLine(s));
			l.setForeground(n.checked.get() ? checkedColor : uncheckedColor);
			
			info = new JTextArea();
			Editor $ = Editor.$;
			if($.hasSpellCheck && $.useSpellChecking())
				SpellChecker.register(info);
			else
				checkSpelling = false;
			
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
					final String str = info.getText(),
								 old = n.info.get();
					Time.rec(() -> {
						n.info.set(str);
					}, () -> {
						n.info.set(old);
					});
				}
			});
			n.info.add(infoL = e -> {
				l.setText(UIText.displayableSingleLine(e.newVal));
				try {
					info.setText(e.newVal);
				}catch(Exception ex) {
					// exception will be thrown when setting Text while handling update event
				}
			});
			
			add(new JScrollPane(info)).setPreferredSize(textDimension);
			
			JLabel pos = new JLabel();
			pos.setText(createPositionText());
			pos.setBorder(textBorder);
			add(pos);
			posL = e -> pos.setText(createPositionText());
			n.x.add(posL);
			n.y.add(posL);

			JCheckBox checked = new JCheckBox("Checked?");
			checked.setBorder(textBorder);
			checked.setSelected(n.checked.get());
			checked.addActionListener(e ->{
				final boolean nVal = checked.isSelected(),
							  oVal = n.checked.get();
				Time.rec(() -> {
					n.checked.set(nVal);
				}, () -> {
					n.checked.set(oVal);
				});
			});
			add(checked);
			n.checked.add(checkedL = e -> {
				l.setForeground(e.newVal ? checkedColor : uncheckedColor);
				checked.setSelected(e.newVal);
			});
			
			JButton convert = new JButton();
			convert.setText("Convert to Text");
			convert.addActionListener(e -> {
				Editor.$.viewer.convertNoteToText(n);
			});
			
			JButton delete = new JButton();
			delete.setText("Delete");
			delete.addActionListener(e -> {
				if(Message.yesno("Delete Note?", "Delete the Note?\n" + n.info.get()))
					n.delete();
			});
			
			JPanel buttonList = new JPanel();
			buttonList.setLayout(new GridLayout(1, 0));
			buttonList.add(convert);
			buttonList.add(delete);
			add(buttonList);
		}
		
		String createPositionText() {
			return "Location: (" + n.x.get() + "|" + n.y.get() + ")";
		}
		
		public void destroy() {
			n.info.remove(infoL);
			n.x.remove(posL);
			n.y.remove(posL);
			n.checked.remove(checkedL);
			n.removeDeleteListener(deleteL);

			parent.updateNotesTitle();
			Container c = getParent(); // don't confuse this with parent, which would be the DataViewer and not the containing container
			if(c == null)
				return;
			c.remove(this);
			c.revalidate();
			c.repaint();
		}
		
		public void setSpellChecking(boolean enable) {
			if(Editor.$.hasSpellCheck && (enable != checkSpelling)) {
				if(enable) {
					SpellChecker.register(info);
				} else {
					SpellChecker.unregister(info);
				}
			}
			
			checkSpelling = enable;
		}
	}
	
	
	
	private static class TextViewer extends SwitchViewer{
		
		private static final Border notesBorder = BorderFactory.createCompoundBorder(new EmptyBorder(5, 5, 5, 5), BorderFactory.createLoweredBevelBorder());
		private static final Color invalidColor = Color.red,
								   titleColor = Color.black;
		
		final Color textColor;

		final Text t;
		
		final JTextArea text;
		
		SimpleNoteViewer active = null;
		
		private boolean checkSpelling = true;
		
		private final JPanel notes,
							 noteButtons;
		final JButton minimize;
		
		private final Consumer<DeletableElement> deleteL;
		private final ChangeListener<String> textL;
		private final BoolChangeListener invalidL;
		private final IntChangeListener posL,
										sizeL;
		
		public TextViewer(DataViewer parent, final Text t) {
			super(parent);
			this.t = t;
			t.addDeleteListener(deleteL = e -> {
				destroy();
			});
			
			String s = t.text.get();
			l.setText(UIText.displayableSingleLine(s));
			
			text = new JTextArea();
			Editor $ = Editor.$;
			if($.hasSpellCheck && $.useSpellChecking())
				SpellChecker.register(text);
			else
				checkSpelling = false;
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
					final String str = text.getText(),
								 old = t.text.get();
					
					Time.rec(() -> {
						t.text.set(str);
					}, () -> {
						t.text.set(old);
					});
				}
			});
			textColor = text.getForeground();
			
			t.text.add(textL = e -> {
				l.setText(UIText.displayableSingleLine(e.newVal));
				try {
					text.setText(e.newVal);
				}catch(Exception ex) {
					// exception will be thrown when setting Text while handling update event
				}
			});
			t.isInvalid.add(invalidL = e -> {
				if(e.newVal) {
					text.setForeground(invalidColor);
					l.setForeground(invalidColor);
				}else {
					text.setForeground(textColor);
					l.setForeground(titleColor);
				}
			});

			add(new JScrollPane(text)).setPreferredSize(textDimension);
			
			JButton changeStyle = new JButton("change Style");
			changeStyle.addActionListener(e -> {
				StyleEditor se = new StyleEditor(Editor.$.getJSA().jsc.getTextStyle(t));
				se.setVisible(true);
				Editor.$.viewer.repaint();
			});
			
			JPanel buttonList = new JPanel();
			buttonList.setLayout(new GridLayout(1, 0));
			buttonList.add(changeStyle);
			add(buttonList);
			
			JLabel pos = new JLabel();
			pos.setText(createPositionText());
			pos.setBorder(textBorder);
			add(pos);
			posL = e -> pos.setText(createPositionText());
			t.x.add(posL);
			t.y.add(posL);

			JLabel size = new JLabel();
			size.setText(createSizeText());
			size.setBorder(textBorder);
			add(size);
			sizeL = e -> size.setText(createSizeText());
			t.w.add(sizeL);
			t.h.add(sizeL);
			
			JButton delete = new JButton("Delete");
			delete.addActionListener(e -> {
				if(Message.yesno("Delete Text?", "Delete the Text?\n" + t.text.get()))
					t.delete();
			});
			
			notes = new JPanel();
			notes.setLayout(new BoxLayout(notes, BoxLayout.Y_AXIS));
			notes.setBorder(notesBorder);
			
			noteButtons = new JPanel(new GridLayout(1, 0));
			noteButtons.setAlignmentX(LEFT_ALIGNMENT);

			JButton addNote = new JButton("Add Note");
			addNote.addActionListener(e -> {
				final SimpleNote sn = new SimpleNote("");
				Time.rec(() -> {
					t.notes.add(sn);
					sn.addDeleteListener(t.notes::remove);
					Editor.$.data.updateTextNotes(t);
				}, () -> {
					sn.delete();
				});
			});
			noteButtons.add(addNote);
			
			minimize = new JButton("minimize");
			minimize.addActionListener(e -> {
				minimize.setEnabled(false);
				if(active == null)
					return;
				active.setInactive();
				active = null;
				parent.revalidate();
				parent.repaint();
			});
			minimize.setEnabled(false);
			noteButtons.add(minimize);
			
			updateNotes();
			
			add(notes);
			
			buttonList = new JPanel();
			buttonList.setLayout(new GridLayout(1, 0));
			buttonList.add(delete);
			add(buttonList);
		}
		
		void updateNotes() {
			Component[] cs = notes.getComponents();
			for(Component c : cs)
				if(c instanceof SimpleNoteViewer)
					((SimpleNoteViewer) c).destroy();
			for(SimpleNote n : t.notes)
				notes.add(new SimpleNoteViewer(this, n));
			notes.add(noteButtons);
			
			notes.revalidate();
			notes.repaint();
		}
		
		String createPositionText() {
			return "Location: (" + t.x.get() + "|" + t.y.get() + ")";
		}
		
		String createSizeText() {
			return "Size: " + t.w.get() + "x" + t.h.get() + "";
		}
		
		public void destroy() {
			t.text.remove(textL);
			t.x.remove(posL);
			t.y.remove(posL);
			t.w.remove(sizeL);
			t.h.remove(sizeL);
			t.isInvalid.remove(invalidL);
			t.removeDeleteListener(deleteL);
			
			Component[] cs = notes.getComponents();
			for(Component c : cs)
				if(c instanceof SimpleNoteViewer)
					((SimpleNoteViewer) c).destroy();
			
			parent.updateTextsTitle();
			Container c = getParent(); // don't confuse this with parent, which would be the DataViewer and not the containing container
			if(c == null)
				return;
			c.remove(this);
			c.revalidate();
			c.repaint();
		}
		
		public void setSpellChecking(boolean enable) {
			if(Editor.$.hasSpellCheck && (enable != checkSpelling)) {
				if(enable) {
					SpellChecker.register(text);
				} else {
					SpellChecker.unregister(text);
				}
			}
			
			for(Component c : notes.getComponents())
				if(c instanceof SimpleNoteViewer)
					((SimpleNoteViewer) c).setSpellChecking(enable);
			
			checkSpelling = enable;
		}
		
		
		
		private static class SimpleNoteViewer extends SwitchViewer{

			private static final Dimension labelDimension = new Dimension(maxWidth - 100, 30);
			private static final Color uncheckedColor = Color.red,
									   checkedColor = Color.black;
			
			private final TextViewer parent;
			
			final SimpleNote n;
			
			final JTextArea info;
			
			private boolean checkSpelling = true;
			
			private final Consumer<DeletableElement> deleteL;
			private final ChangeListener<String> infoL;
			private final BoolChangeListener checkedL;
			
			public SimpleNoteViewer(TextViewer parent, SimpleNote n) {
				super(null);
				this.parent = parent;
				this.n = n;
				n.addDeleteListener(deleteL = e -> {
					destroy();
				});
				
				String s = n.info.get();
				l.setPreferredSize(labelDimension);
				l.setText(UIText.displayableSingleLine(s));
				l.setForeground(n.checked.get() ? checkedColor : uncheckedColor);
				
				info = new JTextArea();
				Editor $ = Editor.$;
				if($.hasSpellCheck && $.useSpellChecking())
					SpellChecker.register(info);
				else
					checkSpelling = false;
				
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
						final String str = info.getText(),
									 old = n.info.get();
						Time.rec(() -> {
							n.info.set(str);
						}, () -> {
							n.info.set(old);
						});
					}
				});
				n.info.add(infoL = e -> {
					l.setText(UIText.displayableSingleLine(e.newVal));
					try {
						info.setText(e.newVal);
					}catch(Exception ex) {
						// exception will be thrown when setting Text while handling update event
					}
				});
				
				add(new JScrollPane(info)).setPreferredSize(textDimension);
				
				JCheckBox checked = new JCheckBox("Checked?");
				checked.setBorder(textBorder);
				checked.setSelected(n.checked.get());
				checked.addActionListener(e ->{
					final boolean nVal = checked.isSelected(),
								  oVal = n.checked.get();
					Time.rec(() -> {
						n.checked.set(nVal);
					}, () -> {
						n.checked.set(oVal);
					});
				});
				add(checked);
				n.checked.add(checkedL = e -> {
					l.setForeground(e.newVal ? checkedColor : uncheckedColor);
					checked.setSelected(e.newVal);
				});
				
				JButton delete = new JButton();
				delete.setText("Delete");
				delete.addActionListener(e -> {
					if(Message.yesno("Delete Note?", "Delete the Note?\n" + n.info.get()))
						n.delete();
				});
				
				JPanel buttonList = new JPanel();
				buttonList.setLayout(new GridLayout(1, 0));
				buttonList.add(delete);
				add(buttonList);
			}
			
			public void destroy() {
				n.info.remove(infoL);
				n.checked.remove(checkedL);
				n.removeDeleteListener(deleteL);

				Container c = getParent(); // don't confuse this with parent, which would be the DataViewer and not the containing container
				if(c == null)
					return;
				c.remove(this);
				c.revalidate();
				c.repaint();
			}
			
			public void setSpellChecking(boolean enable) {
				if(Editor.$.hasSpellCheck && (enable != checkSpelling)) {
					if(enable) {
						SpellChecker.register(info);
					} else {
						SpellChecker.unregister(info);
					}
				}
				
				checkSpelling = enable;
			}
			
			@Override
			public void setActive() {
				if(isActive())
					return;
				if(parent.active != null)
					parent.active.setInactive();
				parent.active = this;
				l.setVisible(false);
				content.setVisible(true);
				parent.minimize.setEnabled(true);
				revalidate();
			}
			
			@Override
			public boolean isActive() {
				return this == parent.active;
			}
		}
	}
}
