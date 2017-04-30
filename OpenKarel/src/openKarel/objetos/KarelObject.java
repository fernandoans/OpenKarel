package openKarel.objetos;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * Criar o objeto de Karel
 * 
 * @author Fernando Anselmo
 * @version 1.0
 */
public class KarelObject extends ObjetoDoMundo {

	private static final long serialVersionUID = -5867005403211882906L;
	private byte faceKarel = 1;
	private Color corKarel = Color.BLACK;
	private final byte TAM = 26;

	public KarelObject(byte fator, byte faceKarel) {
		super(fator);
		this.setOpaque(false);
		this.faceKarel = faceKarel;
		this.setBackground(Color.WHITE);
		this.setSize(fator * TAM, fator * TAM);
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		switch (faceKarel) {
			case 2: // Cima
				g2d.rotate(-Math.PI / 2, getWidth() / 2, getHeight() / 2);
				break;
			case 3: // Trás
				g2d.rotate(-Math.PI, getWidth() / 2, getHeight() / 2);
				break;
			case 4: // Baixo
				g2d.rotate(Math.PI / 2, getWidth() / 2, getHeight() / 2);
				break;
		}
		super.paintComponent(g);
		// Desenha Karel
		g2d.setStroke(new BasicStroke(getFator()));
		short p1 = (short) (getFator() * 3);
		// Corpo
		g2d.drawLine(p1 * 2, 1, p1 * 6, 1); // -- cima
		g2d.drawLine(p1 * 6, 1, p1 * 7, p1); // \
		g2d.drawLine(p1 * 7, p1, p1 * 7, p1 * 7); // | frente
		g2d.drawLine(p1 * 2, 0, p1 * 2, p1 * 6); // | trás
		g2d.drawLine(p1 * 2, p1 * 6, p1 * 3, p1 * 7); // \ baixo
		g2d.drawLine(p1 * 3, p1 * 7, p1 * 7, p1 * 7); // -- baixo
		// Quadrado
		g.setColor(corKarel);
		g2d.fillRect(p1 * 3, p1, p1 * 3, p1 * 4);
		g.setColor(Color.BLACK);
		// Boca
		g2d.drawLine(p1 * 4, p1 * 6, p1 * 6, p1 * 6);
		// Pes
		g2d.setStroke(new BasicStroke(getFator() * 3));
		// Pé da Esquerda
		g2d.drawLine(getFator() + 2, p1 * 5, p1 * 2 - (getFator() * 2), p1 * 5);
		g2d.drawLine(getFator() + 2, p1 * 5, getFator() + 2, p1 * 6);
		// Pé de Baixo
		g2d.drawLine(p1 * 4, p1 * 7 + (getFator() * 2), p1 * 4, p1 * 8);
		g2d.drawLine(p1 * 4, p1 * 8, p1 * 5, p1 * 8);
	}

	@Override
	public void setFator(byte fator) {
		super.setFator(fator);
		this.setSize(fator * TAM, fator * TAM);
	}

	public void setFaceKarel(byte faceKarel) {
		this.faceKarel = faceKarel;
	}

	public byte getFaceKarel() {
		return faceKarel;
	}

	public Color getCorKarel() {
		return corKarel;
	}

	public void setCorKarel(Color corKarel) {
		this.corKarel = corKarel;
		this.repaint();
	}

	public void mover(byte totAvenida) {
		switch (faceKarel) {
			case 1: // Frente
				movEsq(true);
				break;
			case 2: // Cima
				movTop(true, totAvenida);
				break;
			case 3: // Trás
				movEsq(false);
				break;
			case 4: // Baixo
				movTop(false, totAvenida);
				break;
		}		
	}

	private void movEsq(boolean direcao) {
		this.setRua((byte) (this.getRua() + (direcao ? 1 : -1)));
	}

	private void movTop(boolean direcao, byte totAvenida) {
		this.setAvenida((byte) (this.getAvenida() + (direcao ? 1 : -1)), totAvenida);
	}

	public void addFaceKarel() {
		faceKarel++;
		if (faceKarel == 5) {
			faceKarel = 1;
		}
		this.repaint();
	}
	
	public boolean isFace(char pontoCardeal) {
		switch (pontoCardeal) {
			case 'N':
				return faceKarel == 2;
			case 'S':
				return faceKarel == 4;
			case 'L':
				return faceKarel == 1;
			case 'O':
				return faceKarel == 3;
		}
		return false;
	}

	private String posFace() {
		switch (faceKarel) {
			case 1:
				return "east";
			case 2:
				return "north";
			case 3:
				return "east";
			default:
				return "south";
		}
	}
	
	public String colorToStr() {
		return "ColorKarel: " + corKarel.getRGB();		
	}

	@Override
	public String toString() {
		return "Karel: (" + getRua() + ", " + getAvenida() + ") " + posFace();
	}
}