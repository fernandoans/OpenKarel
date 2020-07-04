package openKarel.objetos;

import javax.swing.JPanel;

/**
 * Classe mãe dos objetos existentes no mundo
 * 
 * @author Fernando Anselmo
 * @version 1.0
 */
public class ObjetoDoMundo extends JPanel {

	private static final long serialVersionUID = -3834309912186278308L;
	private byte fator = 2;
	private int locEsq = 0;
	private int locTop = 0;
	private byte rua = 0;
	private byte avenida = 0;

	protected ObjetoDoMundo(byte fator) {
		this.fator = fator;
	}

	protected byte getFator() {
		return fator;
	}

	protected void setFator(byte fator) {
		if (this.fator != fator) {
			this.fator = fator;
			this.setRua(rua);
		}
	}

	public void setRua(byte rua) {
		this.rua = rua;
		this.locEsq = 30 * fator * rua - (12 * fator);
	}

	public byte getRua() {
		return rua;
	}

	public int getLocEsq() {
		return locEsq;
	}

	protected void setLocEsq(int locEsq) {
		this.locEsq = locEsq;
	}

	public void setAvenida(byte avenida, byte totAvenida) {
		this.avenida = avenida;
		this.locTop = 30 * fator * (totAvenida + 1 - avenida) - (fator * 27);
	}

	public byte getAvenida() {
		return avenida;
	}

	public int getLocTop() {
		return locTop;
	}

	protected void setLocTop(int locTop) {
		this.locTop = locTop;
	}
}