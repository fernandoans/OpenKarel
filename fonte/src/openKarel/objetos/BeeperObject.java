package openKarel.objetos;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * Criar o objeto de Beeper
 * 
 * @author Fernando Anselmo
 * @version 1.0
 */
public class BeeperObject extends ObjetoDoMundo {

	private static final long serialVersionUID = -4680390452485265994L;
	private final byte TAM = 26;

	public BeeperObject(byte fator) {
		super(fator);
		this.setBackground(Color.WHITE);
		this.setSize(fator * TAM, fator * TAM);
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		super.paintComponent(g);
		// Desenha Beeper
		g2d.setStroke(new BasicStroke(getFator() * 1.2f));
		short p1 = (short) (getFator() * 2);
		// Corpo
		g2d.drawPolygon(new int[] { p1 * 7, p1 * 11, p1 * 7, p1 * 3, p1 * 7 },
				new int[] { p1 * 3, p1 * 7, p1 * 11, p1 * 7, p1 * 3 }, 5);
		g2d.setColor(Color.YELLOW);
		g2d.fillPolygon(new int[] { p1 * 7, p1 * 11 - getFator(), p1 * 7, p1 * 3 + 1, p1 * 7 },
				new int[] { p1 * 3 + 1, p1 * 7, p1 * 11 - getFator(), p1 * 7, p1 * 3 + 1 }, 5);
	}

	@Override
	public void setFator(byte fator) {
		super.setFator(fator);
		this.setSize(fator * TAM, fator * TAM);
	}

	@Override
	public String toString() {
		return "Beeper: (" + getRua() + ", " + getAvenida() + ") 1";
	}
}