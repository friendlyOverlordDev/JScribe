package p1327.jscribe.net;

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

import java.awt.Desktop;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONObject;
import org.json.JSONTokener;

import p1327.jscribe.util.Message;
import p1327.jscribe.util.Static;

public class UpdateCheckThread extends Thread{
	
	private static final int timeout = 30000;
	
	private final boolean inform;
	
	public UpdateCheckThread() {
		this(false);
	}
	
	public UpdateCheckThread(boolean inform) {
		setName("Check4Updates");
		setDaemon(true);
		start();
		this.inform = inform;
	}
	
	@Override
	public void run() {
		try {
			HttpsURLConnection con = (HttpsURLConnection)new URL(Static.githubVersion).openConnection();
			con.setConnectTimeout(timeout);
			con.setReadTimeout(timeout);
			con.setUseCaches(false);
			con.setRequestProperty("Pragma", "no-cache");
			con.setRequestProperty("Cache-Control", "no-cache");
			con.setRequestProperty("Accept", "application/vnd.github.jean-grey-preview+json");

			int status = con.getResponseCode();
			if(status == 200){
				try(InputStream is = con.getInputStream()){
					String onlineVersion = new JSONObject(new JSONTokener(is)).getString("tag_name");
				
				// simple version with own version file; has issues with slow updates
//				try(ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
//					InputStream is = con.getInputStream()){
//					int b;
//					while((b = is.read()) > -1)
//						baos.write(b);
//					String onlineVersion = new String(baos.toByteArray()).trim();
					is.close();
					if(!onlineVersion.equals(Static.version)) {
						if(Message.options("Update found!", "JScribe " + Static.version + " can be updated to version " + onlineVersion, "Update", "Later...") == 0){
							try {
								Desktop.getDesktop().browse(new URI("https://github.com/friendlyOverlordDev/JScribe/releases"));
							} catch (Exception ex) {
								Message.error("Failed to open the Release-Page!\nhttps://github.com/friendlyOverlordDev/JScribe/releases", ex);
							}
						}
					} else if(inform) {
						Message.ok("No Update found", "You have the newest version (" + Static.version + "). No updates needed.");
					}
				}catch(Exception e) {
					throw e;
				}
			}else if(status == 404 || (status < 400 && status >= 300)){
				throw new IOException("Resource not found!\nYour version is probably outdated, but you can try to update here:\nhttps://github.com/friendlyOverlordDev/JScribe/releases");
			}else {
				throw new IOException("Invalid Update Source Status " + status + "...");
			}
		}catch(Exception e) {
			Message.error("Update Error", e);
		}
	}
}
