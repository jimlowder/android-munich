package net.pongjour.network;

import java.util.List;

import net.pongjour.Player;

public abstract class NetworkHandler {

	public interface Notifications {
		
		public static String PLAYERS_CHANGED = "PLAYERS_CHANGED";
	}
	
	
	/**
	 * Return the list of currently supported hosts (might use a callback?)
	 * @return
	 */
	public abstract List<Player> getPlayers();
	
	/**
	 * Should block until accepted
	 * @param host
	 * @return
	 */
	public abstract boolean invite(String host);
	
	public abstract void startListening();
}
