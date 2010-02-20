package ru.roulette.comm;

import android.util.Log;

public class AndroidCommHandler implements CommHandler {

	private HttpServiceHandler serviceHandler;

	public AndroidCommHandler() {
		this.serviceHandler = new HttpServiceHandler();
	}

	@Override
	public String getMyMessages(int myid) {
		return this.serviceHandler.getString(myid,
				HttpServiceHandler.SERVERNAME + "poll");

	}

	@Override
	public int login(byte[] image) {
		int id = this.serviceHandler.postData(image,
				HttpServiceHandler.SERVERNAME + "logon");
		Log.d("CommHandler", "My id : " + id);
		return id;
	}

	@Override
	public void message(int myid, int destid, String message) {
		serviceHandler.sendMessage(myid, destid, message,
				HttpServiceHandler.SERVERNAME + "message");
	}

	@Override
	public Identity nextIdentity() {
		return serviceHandler.getImage(HttpServiceHandler.SERVERNAME + "next");
	}

}
