package p1327.jscribe.util;

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

import javax.swing.JOptionPane;

public class Message {
	
	public static final int YES = JOptionPane.YES_OPTION,
							NO = JOptionPane.NO_OPTION,
							OK = JOptionPane.OK_OPTION;

	public static void error(String title, Exception e) {
		JOptionPane.showMessageDialog(null, e.getMessage(), title, JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * Asks a question, if answered with yes, returns true, otherwise false
	 * @param title - the title of the dialog
	 * @param message - the message/question to show
	 * @return true if yes, othererwise false
	 */
	public static boolean yesno(String title, String message) {
		return JOptionPane.showOptionDialog(null, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null) == YES;
	}
	
	/**
	 * Asks a question, if answered with ok, returns true, otherwise false
	 * @param title - the title of the dialog
	 * @param message - the message/question to show
	 * @return true if ok, othererwise false
	 */
	public static boolean okcancel(String title, String message) {
		return JOptionPane.showOptionDialog(null, message, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null) == OK;
	}
	
	public static int options(String title, String message, String...options) {
		return JOptionPane.showOptionDialog(null, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);
	}
}
