package net.pongjour;

import net.pongjour.network.NetworkHandler;
import net.pongjour.network.NetworkHandlerImp;
import net.pongjour.notifications.Notification;
import net.pongjour.notifications.NotificationCenter;
import net.pongjour.notifications.NotificationReceiver;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class Pongjour extends Activity implements NotificationReceiver {

	private NetworkHandler _networkHandler;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		registerNetwork();
	}

	private void registerNetwork() {
		NotificationCenter.addReceiver(this, NetworkHandler.Notifications.PLAYERS_CHANGED, null);
		Player.register("my-name-" + (System.currentTimeMillis() % 100));
		_networkHandler = new NetworkHandlerImp();
	}

	@Override
	public void receive(Notification notification) {
		Log.d("Pongjour", notification.getName() +"->"+ notification.getUserInfo("object"));
	}
}