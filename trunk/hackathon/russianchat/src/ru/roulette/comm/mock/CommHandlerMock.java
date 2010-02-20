package ru.roulette.comm.mock;

import ru.roulette.chat.R;
import ru.roulette.comm.CommHandler;
import ru.roulette.comm.Identity;

public class CommHandlerMock implements CommHandler {
	
	private String lastMsg  = "Hello Russia!";

	public CommHandlerMock() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getMyMessages(int myid) {
		return this.lastMsg;
	}

	@Override
	public int login(byte[] image) {
		return 4711;
	}

	@Override
	public void message(int myid, int destid, String message) {
		this.lastMsg = message;

	}

	@Override
	public Identity nextIdentity() {
		Identity identity = new Identity();
		identity.setId(4712);
		
//		getResources().getDrawable( R.drawable.defaultuserimage);
//		createBitmap(Bitmap src)
		//identity.setImage(image)
		return identity;
	}

	@Override
	public void logoff(int myid) {
		// TODO Auto-generated method stub
		
	}

}
