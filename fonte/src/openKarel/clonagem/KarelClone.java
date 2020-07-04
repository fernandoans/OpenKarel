package openKarel.clonagem;

import java.awt.Color;

public class KarelClone {
	
	private byte rua;
	private byte avenida;
	private byte faceKarel;
	private Color corKarel;
	
	public KarelClone(byte rua, byte avenida, byte faceKarel, Color corKarel) {
		this.rua = rua;
		this.avenida = avenida;
		this.faceKarel = faceKarel;
		this.corKarel = corKarel;
	}
	
	public byte getRua() {
		return rua;
	}
	public byte getAvenida() {
		return avenida;
	}
	public byte getFaceKarel() {
		return faceKarel;
	}
	public Color getCorKarel() {
		return corKarel;
	}
}
