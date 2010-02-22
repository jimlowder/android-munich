package org.hackathon2010.duell;

public interface Transmitter {
	public void localGunDown();
	public void localGunUp();
	public void localGunIntermediate();
	public void localGunShot();
	public void close();
	public void connect() throws Exception;
}
