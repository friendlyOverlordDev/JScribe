package p1327.jscribe.util;

public class Static {
	
	private Static() {}
	
	public static String[] supportedTypeList = {"png", "jpg", "jpeg", "gif"};
	public static String supportedTypes = String.join(", ", supportedTypeList);
}
