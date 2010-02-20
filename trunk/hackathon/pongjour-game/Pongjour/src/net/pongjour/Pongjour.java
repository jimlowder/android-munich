package net.pongjour;

import java.util.ArrayList;
import java.util.List;

import net.pongjour.network.NetworkHandler;
import net.pongjour.network.NetworkHandlerImp;
import net.pongjour.notifications.Notification;
import net.pongjour.notifications.NotificationCenter;
import net.pongjour.notifications.NotificationReceiver;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Pongjour extends Activity implements NotificationReceiver {

	private NetworkHandler networkHandler;
	private String msg;
	private PlayerAdaptor playerAdaptor;
	private LayoutInflater inflater;
	private List<Player> players = new ArrayList<Player>();

	private class PlayerAdaptor extends ArrayAdapter<Player> {

		public PlayerAdaptor(Context context) {
			super(context, 0, players);
			inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = inflater.inflate(R.layout.row, null);
			TextView name = (TextView) view.findViewById(R.id.Name);
			TextView host = (TextView) view.findViewById(R.id.Host);
			Player player = getItem(position);
			name.setText(player.getName());
			host.setText(player.getHost());
			return view;
		}
		
		private void reload() {
			List<Player> active = new ArrayList<Player>(networkHandler.getPlayers());
			players.clear();
			players.addAll(active);
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					notifyDataSetChanged();
				}
			});
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		NotificationCenter.addReceiver(this, NetworkHandler.Notifications.PLAYERS_CHANGED, null);
		setContentView(R.layout.main);
		ListView list = (ListView) findViewById(R.id.PlayerList);
		playerAdaptor = new PlayerAdaptor(this);
		list.setAdapter(playerAdaptor);

		registerNetwork();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (msg != null) {
			Toast.makeText(Pongjour.this, msg, 2000).show();
			msg = null;
		}
	}
	
	private void registerNetwork() {
		if (networkHandler == null) {
			Player.register("my-name-" + (System.currentTimeMillis() % 100));
			networkHandler = new NetworkHandlerImp();
			networkHandler.startListening();
		}
	}

	@Override
	public void receive(final Notification notification) {
		Log.d("Pongjour", notification.getName() + "->" + notification.getUserInfo("object"));
		Player player = (Player) notification.getUserInfo("object");
		if (player != null) {
			msg = "Player: " + player.getName();
		} else {
			msg = "Should have changed...";
		}
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(Pongjour.this, msg, 2000).show();
				playerAdaptor.reload();
			}
		});
	}
}