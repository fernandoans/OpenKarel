package openKarel.objetos;

import java.awt.Color;

/**
 * Criar o objeto Parede
 * 
 * @author Fernando Anselmo
 * @version 1.0
 */
public class WallObject extends ObjetoDoMundo {

	private static final long serialVersionUID = 2925564835365372450L;
	private final byte TAM = 31;
	private boolean emPe = true;

	public WallObject(byte fator) {
		super(fator);
		this.setBackground(Color.BLACK);
		this.setSize(6, fator * TAM);
	}

	@Override
	public void setFator(byte fator) {
		super.setFator(fator);
		mudarTam();
	}

	@Override
	public void setAvenida(byte avenida, byte totAvenida) {
		super.setAvenida(avenida, totAvenida);
		if (emPe) {
			this.setLocTop(30 * getFator() * (totAvenida + 1 - avenida) - (getFator() * 29));
		} else {
			this.setLocTop(30 * getFator() * (totAvenida + 2 - avenida) - (getFator() * 30));
		}
	}

	@Override
	public void setRua(byte rua) {
		super.setRua(rua);
		if (emPe) {
			this.setLocEsq(30 * getFator() * rua - (15 * getFator()));
		} else {
			this.setLocEsq(30 * getFator() * rua - (15 * getFator()));
		}
	}

	public boolean isEmPe() {
		return emPe;
	}

	public void setEmPe(boolean emPe) {
		this.emPe = emPe;
		mudarTam();
	}

	private void mudarTam() {
		if (emPe) {
			this.setSize(4, getFator() * TAM);
		} else {
			this.setSize(getFator() * TAM, 4);
		}
	}
	
	@Override
	public String toString() {
		return "Wall: (" + getRua() + ", " + getAvenida() + ") " + (emPe?"west":"south");
	}
}
