package net.pongjour.network;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;

import net.pongjour.Player;
import net.pongjour.Util;
import net.pongjour.notifications.NotificationCenter;
import android.util.Log;

public class NetworkHandlerImp extends NetworkHandler {

	private final class Listener implements ServiceListener {
		@Override
		public void serviceAdded(ServiceEvent evt) {
			String host = evt.getDNS().getHostName();
			Player player = getPlayerFromEvent(evt);
			if (player != null && !_players.contains(player)) {
				_players.add(player);
				notifyPlayersChanged(player);
			}
			log("serviceAdded:" + host + "->" + player);
		}

		@Override
		public void serviceRemoved(ServiceEvent evt) {
			String host = evt.getDNS().getHostName();
			Player player = getPlayerFromEvent(evt);
			if (player != null) {
				_players.remove(player);
				notifyPlayersChanged(player);
			}
			log("serviceRemoved: " + host + "->" + player);
		}

		@Override
		public void serviceResolved(ServiceEvent info) {
			String host = info.getDNS().getHostName();
			log("serviceResolved: " + host);
		}

	}

	private Player getPlayerFromEvent(ServiceEvent evt) {
		Hashtable<String, String> properties = getPropertiesFromEvent(evt);
		Player player = Player.fromServiceInfo(properties);
		return player;
	}

	private Hashtable<String, String> getPropertiesFromEvent(ServiceEvent evt) {
		ServiceInfo info = evt.getInfo();
		Hashtable<String, String> props = new Hashtable<String, String>();
		if (info == null) {
			info = _jmdns.getServiceInfo(PONGJOUR_ID, evt.getName());
		}
		if (info != null) {
			for (Enumeration<String> iterator = info.getPropertyNames(); iterator.hasMoreElements();) {
				String key = (String) iterator.nextElement();
				props.put(key, info.getPropertyString(key));
			}
		}
		return props;
	}

	private void notifyPlayersChanged(Player player) {
		Map<String, Object> userInfo = new HashMap<String, Object>();
		userInfo.put("object", player);
		NotificationCenter.postNotification(Notifications.PLAYERS_CHANGED, NetworkHandlerImp.this, userInfo);
	}
	
	public static void log(String msg) {
		Log.i(NetworkHandler.class.getSimpleName(), msg);
	}

	private static final int PONGJOUR_PORT = 8080;

	private static final String PONGJOUR_ID = "_pongjour._tcp.local.";

	private JmDNS _jmdns;

	private List<Player> _players = new ArrayList<Player>();


	
	private class SimpleFetch implements Runnable {

		@Override
		public void run() {

			while(true) {
				ServiceInfo infos[] = _jmdns.list(PONGJOUR_ID);
				for (int i = 0; i < infos.length; i++) {
					ServiceInfo serviceInfo = infos[i];
					Hashtable<String, String> props = new Hashtable<String, String>();
					for (Enumeration<String> iterator = serviceInfo.getPropertyNames(); iterator.hasMoreElements();) {
						String key = (String) iterator.nextElement();
						props.put(key, serviceInfo.getPropertyString(key));
					}
					Player player = Player.fromServiceInfo(props);

					log(i+ " -> " + player);
				}
				try {
					log("Waiting");
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					break;
				}
			}
		}
		
	}
	
	public NetworkHandlerImp() {
	}

	public void startListening() {
		ServiceListener listener = new Listener();

		try {
			Player player = Player.self();
			String name = player.getName() + "." + PONGJOUR_ID;
			_jmdns = JmDNS.create(InetAddress.getByName(Util.getLocalIpAddress()));
			ServiceInfo info = ServiceInfo.create(PONGJOUR_ID, player.getName(), 1268, 0, 0, player.getServiceInfo());
			_jmdns.registerService(info);
			_jmdns.addServiceListener(PONGJOUR_ID, listener);
			new Thread(new SimpleFetch()).start();
			//ServiceInfo si = _jmdns.getServiceInfo(PONGJOUR_ID, name);
			// log(si.getTextString());
		} catch (IOException e) {
			throw new RuntimeException("Can't register", e);
		}
	}

	@Override
	public List<Player> getPlayers() {
		return _players;
	}

	@Override
	public boolean invite(String host) {
		return true;
	}
}
