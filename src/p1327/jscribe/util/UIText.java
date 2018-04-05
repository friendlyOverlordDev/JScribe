package p1327.jscribe.util;

public class UIText {
	
	private UIText() {}
	
	/**
	 * Makes a given string displayable by converting it to html-string in case it is necessary (e.g. for linebreaks to work)
	 * @param str - the string to convert
	 * @return the converted string
	 */
	public static String displayable(String str) {
		boolean convert = str.indexOf('\n') > -1;
		if(convert)
			str = "<html>" + str.replace("\n", "<br>") + "</html>";
		return str;
	}
}
