package net.pongjour;

import java.util.Hashtable;

public class Player {

	private String name;
	private String host;
	private int port;
	private int score;
	private static Player self;
	
	public Player(String name, String host) {
		this.name = name;
		this.host = host;
		this.score = 0;
		this.port = 8080;
	}
	
	public String getName() {
		return name;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public int getScore() {
		return score;
	}

	public Hashtable getServiceInfo() {
		Hashtable<String, Object> props = new Hashtable();
		props.put("name", name);
		props.put("host", host);
		props.put("score", score+"");
		props.put("port", port+"");
		return props;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Player) {
			Player player = (Player) o;
			return player.host.equals(host) && player.name.equals(name); 
		}
		return false;
	}
	
	public static Player fromServiceInfo(Hashtable<String, String> props) {
		if (props.size() > 0) {
			Player player = new Player(props.get("name"), props.get("host"));
			player.port = Integer.valueOf(props.get("port"));
			player.score = Integer.valueOf(props.get("score"));
			return player;
		}
		return null;
	}
	
	public static void register(String name) {
		self = new Player(name, Util.getLocalIpAddress());
	}
	
	public static Player self() {
		return self;
	}
}
