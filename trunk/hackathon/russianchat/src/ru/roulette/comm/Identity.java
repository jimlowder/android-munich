package ru.roulette.comm;

public class Identity {
	private int id;
	private byte[] image;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setId(String s) {
		this.id = Integer.parseInt(s.substring(0,s.indexOf(".")));
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}
}
