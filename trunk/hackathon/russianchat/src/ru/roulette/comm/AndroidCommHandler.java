package ru.roulette.comm;

import android.util.Log;

public class AndroidCommHandler implements CommHandler {

	private HttpServiceHandler serviceHandler;

	public AndroidCommHandler() {
		this.serviceHandler = new HttpServiceHandler();
	}

	@Override
	public String getMyMessages(int myid) {
		String s = this.serviceHandler.getString(myid,
				HttpServiceHandler.SERVERNAME + "poll");
		String deb = "null";
		if (s != null)
			deb = s;

		Log.d("getMyMessages: ", deb);
		return s;
	}

	@Override
	public int login(byte[] image) {
		int id = this.serviceHandler.postData(image,
				HttpServiceHandler.SERVERNAME + "logon");
		Log.d("login", "My id : " + id);
		return id;
	}

	@Override
	public void message(int myid, int destid, String message) {
		serviceHandler.sendMessage(myid, destid, message,
				HttpServiceHandler.SERVERNAME + "message");
		Log.d("message", "send message " + message);
	}

	@Override
	public Identity nextIdentity() {
		return serviceHandler.getImage(HttpServiceHandler.SERVERNAME + "next");
	}

	@Override
	public void logoff(int myid) {
		serviceHandler.sendID(myid,HttpServiceHandler.SERVERNAME + "logoff");

	}

}
