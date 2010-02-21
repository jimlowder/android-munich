package ru.roulette.comm;

import android.util.Log;

public class AndroidCommHandler implements CommHandler {

	private HttpServiceHandler serviceHandler;

	public AndroidCommHandler() {
		this.serviceHandler = new HttpServiceHandler();
	}


	public String getMyMessages(int myid) {
		String s = this.serviceHandler.getString(myid,
				HttpServiceHandler.SERVERNAME + "poll");
		String deb = "null";
		if (s != null)
			deb = s;

		Log.d("getMyMessages: ", deb);
		return s;
	}


	public int login(byte[] image) {
		int id = this.serviceHandler.postData(image,
				HttpServiceHandler.SERVERNAME + "logon");
		Log.d("login", "My id : " + id);
		return id;
	}

	public void message(int myid, int destid, String message) {
		serviceHandler.sendMessage(myid, destid, message,
				HttpServiceHandler.SERVERNAME + "message");
		Log.d("message", "send message " + message);
	}


	public Identity nextIdentity() {
		Identity identity = serviceHandler.nextIdentity(HttpServiceHandler.SERVERNAME + "next");
		int id = identity.getId();
		byte[] image = serviceHandler.getImage(HttpServiceHandler.SERVERNAME + "image/"+id);
		identity.setImage(image);
		return identity;
	}

	
	public void logoff(int myid) {
		serviceHandler.sendID(myid,HttpServiceHandler.SERVERNAME + "logoff");

	}

}
