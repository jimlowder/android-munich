package ru.roulette.comm;

public interface CommHandler {
	/**
	 * logs into server with my image
	 * @param image 
	 * @return my personal id to use for this chat session
	 */
		public int login(byte[] image) ;
	
	/**
	 * gets next chat partner with picture and id
	 * @return Identity of next chat partner
	 */
	public Identity nextIdentity ();
	
	/**
	 * sends message to partner
	 * @param myid my personal id
	 * @param destid id of my chat partner
	 * @param message message to be sent to chat partner
	 */
	public void message(int myid, int destid, String message);
	
	/**
	 * polls for messages from server
	 * @param myid my personal id
	 * @return null if none available, otherwise next available message in database;
	 */
	public String getMyMessages(int myid);
}
