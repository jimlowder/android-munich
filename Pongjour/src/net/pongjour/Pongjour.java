package net.pongjour;

import android.app.Activity;
import android.os.Bundle;

public class Pongjour extends Activity {

	private NetworkHandler _networkHandler;
	
	private Player _player;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		registerNetwork();
	}

	private void registerNetwork() {
//		WifiManager wifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
//		MulticastLock lock = wifi.createMulticastLock("mylock");
//		lock.acquire();

		Player.register("my-name-" + (System.currentTimeMillis() % 100));
		_networkHandler = new NetworkHandlerImp();
//		lock.release();
	}
	
	
}