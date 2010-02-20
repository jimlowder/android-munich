package net.pongjour;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;

import android.util.Log;

public class NetworkHandlerImp extends NetworkHandler {

	public static void log(String msg) {
		Log.i(NetworkHandler.class.getSimpleName(), msg);
	}

	private static final int PONGJOUR_PORT = 8080;

	private static final String PONGJOUR_ID = "_pongjour._tcp.local.";

	private JmDNS _jmdns;

	private List<Player> _players = new ArrayList<Player>();

	public NetworkHandlerImp() {
		ServiceListener listener = new ServiceListener() {

			@Override
			public void serviceAdded(ServiceEvent evt) {
				String host = evt.getDNS().getHostName();
				// _players.add(Player.fromString(info.toString()));
				Player player = getPlayerFromEvent(evt);
				if(!_players.contains(player)) {
					_players.add(player);
				}
				log("serviceAdded:" + host + "->" + player);
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
					info = _jmdns.getServiceInfo(PONGJOUR_ID, info.getName());
				}
				if (info != null) {
					for (Enumeration<String> iterator = info.getPropertyNames(); iterator.hasMoreElements();) {
						String key = (String) iterator.nextElement();
						props.put(key, info.getPropertyString(key));
					}
				}
				return props;
			}

			@Override
			public void serviceRemoved(ServiceEvent evt) {
				String host = evt.getDNS().getHostName();
				Player player = getPlayerFromEvent(evt);
				_players.remove(player);
				log("serviceRemoved: " + host + "->" +player);
			}

			@Override
			public void serviceResolved(ServiceEvent info) {
				String host = info.getDNS().getHostName();
				log("serviceResolved: " + host);
			}
		};

		try {
			Player player = Player.self();
			String name = player.getName() + "." + PONGJOUR_ID;
			_jmdns = JmDNS.create(InetAddress.getByName(Util.getLocalIpAddress()));
			ServiceInfo info = ServiceInfo.create(PONGJOUR_ID, player.getName(), 1268, 0, 0, player.getServiceInfo());
			_jmdns.registerService(info);
			_jmdns.addServiceListener(PONGJOUR_ID, listener);
			ServiceInfo si = _jmdns.getServiceInfo(PONGJOUR_ID, name);
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
