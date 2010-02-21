package ru.roulette.comm.mock;

import ru.roulette.comm.CommHandler;
import ru.roulette.comm.Identity;

public class CommHandlerMock implements CommHandler {
	
	private String lastMsg  = "Hello Russia!";

	public CommHandlerMock() {
		// TODO Auto-generated constructor stub
	}

	
	public String getMyMessages(int myid) {
		return this.lastMsg;
	}

	
	public int login(byte[] image) {
		return 4711;
	}

	
	public void message(int myid, int destid, String message) {
		this.lastMsg = message;

	}

	
	public Identity nextIdentity() {
		Identity identity = new Identity();
		identity.setId(4712);
		
//		getResources().getDrawable( R.drawable.defaultuserimage);
//		createBitmap(Bitmap src)
		//identity.setImage(image)
		return identity;
	}

	
	public void logoff(int myid) {
		// TODO Auto-generated method stub
		
	}

}
