package net.pongjour;

import java.util.List;

public abstract class NetworkHandler {

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
}
