package openKarel;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import openKarel.clonagem.BeeperClone;
import openKarel.clonagem.KarelClone;
import openKarel.objetos.BeeperObject;
import openKarel.objetos.KarelObject;
import openKarel.objetos.WallObject;

/**
 * Compor o mundo que vive Karel
 * 
 * @author Fernando Anselmo
 * @version 1.0
 */
public class MundoKarel extends JPanel {

	private static final long serialVersionUID = 1113326040272605783L;
	private byte fator = 2;
	private byte faceKarel = 3;
	private byte totLin = 10;
	private byte totCol = 10;
	private JTextArea programa;

	private KarelObject karel;
	private List<BeeperObject> beepers;
	private List<WallObject> walls;
	private int bolsaKarel;

	// Cria uma cópia do mapa para reiniciar
	private KarelClone karelClone;
	private List<BeeperClone> beepersClone;
	private int bolsaKarelClone;

	protected MundoKarel(JTextArea programa) {
		this.programa = programa;
		this.setLayout(null);
		this.setOpaque(false);
		karel = new KarelObject(fator, faceKarel);
		this.add(karel);
		this.novoMapa();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		int posEsq = 28 * fator;
		int posTop = 12 * fator;

		int totEsq = (30 * fator * totCol) + totCol + 30 + (4 * fator);
		int totTop = (30 * fator * totLin) + totLin + 30;

		g.setColor(Color.GRAY);
		// Paredes Laterais
		g.drawLine(totEsq, 0, totEsq, totTop - 25);
		g.drawLine(totEsq - 1, 0, totEsq - 1, totTop - 25);
		g.drawLine(0, totTop - 24, totEsq, totTop - 24);
		g.drawLine(0, totTop - 25, totEsq, totTop - 25);
		// Pontos do mapa
		for (byte lin = 0; lin < totLin; lin++) {
			for (byte col = 0; col < totCol; col++) {
				g.fillRect(posEsq, posTop, 5, 5);
				posEsq += 30 * fator;
			}
			posTop += 30 * fator;
			posEsq = 28 * fator;
		}

		g.setColor(Color.BLUE);
		// Números Esquerda
		posTop = 18 * fator;
		for (byte lin = 0; lin < totLin; lin++) {
			g.drawString("" + (totLin - lin), 5, posTop);
			posTop += 30 * fator;
		}
		// Números de Baixo
		posEsq = 26 * fator;
		posTop = (28 * fator * totLin) + totLin + 10;
		for (byte col = 0; col < totCol; col++) {
			g.drawString("" + (col + 1), posEsq, totTop - 10);
			posEsq += 30 * fator;
		}
	}

	// -------------------------------------------------------------------
	// Obter informações do Mapa
	// -------------------------------------------------------------------

	protected byte getTotLin() {
		return totLin;
	}

	protected byte getTotCol() {
		return totCol;
	}

	// -------------------------------------------------------------------
	// Responder aos comandos de Karel
	// -------------------------------------------------------------------

	/**
	 * Resposta ao comando move()
	 */
	protected void andarKarel() throws KarelException {
		if (!frenteLimpa()) {
			throw new KarelException("Existe uma parede na frente de Karel.");
		}
		karel.mover(totLin);
		karel.setLocation(karel.getLocEsq(), karel.getLocTop());
		programa.append("\nKarel mudou sua localização para Rua " + karel.getRua() + " e Avenida " + karel.getAvenida());
	}

	/**
	 * Resposta ao comando turnLeft()
	 */
	protected void mudarDirecaoKarel() {
		karel.addFaceKarel();
		programa.append("\nKarel mudou de direção:");
		programa.append("\n  Sua frente está " + (frenteLimpa() ? "Livre" : "Ocupada"));
		programa.append("\n  Sua esquerda está " + (esquerdaLimpa() ? "Livre" : "Ocupada"));
		programa.append("\n  Sua direita está " + (direitaLimpa() ? "Livre" : "Ocupada"));
	}

	/**
	 * Resposta ao comando pickBeeper()
	 */
	protected void pegarSinalizador() throws KarelException {
		if (!existeSinalizadorAqui()) {
			throw new KarelException("Não existe um sinalizador na posição de Karel.");
		}
		BeeperObject beeper = this.getSinalizador(karel.getRua(), karel.getAvenida());
		if (beeper != null) {
			bolsaKarel++;
			beepers.remove(beeper);
			this.remove(beeper);
		}
		programa.append("\nKarel obteve um sinalizador, possui " + bolsaKarel + " em sua bolsa.");
	}

	/**
	 * Resposta ao comando putBeeper()
	 */
	protected void deixarSinalizador() throws KarelException {
		if (!existeSinalizadorBolsa()) {
			throw new KarelException("Não existe um sinalizador na bolsa de Karel.");
		}
		addSinalizador(karel.getRua(), karel.getAvenida());
		bolsaKarel--;
		programa.append("\nKarel deixou um sinalizador, possui " + bolsaKarel + " em sua bolsa.");
	}

	/**
	 * Resposta aos comandos frontIsClear() e frontIsBlocked()
	 * 
	 * @return Se existe um bloqueio a frente de Karel
	 */
	protected boolean frenteLimpa() {
		byte ruaK = karel.getRua();
		byte avnK = karel.getAvenida();
		if (karel.isFace('L'))
			return (this.getParede((byte) (ruaK + 1), avnK, true) == null) && (ruaK < totCol);
		if (karel.isFace('O'))
			return (this.getParede(ruaK, avnK, true) == null) && (ruaK - 1 > 0);
		if (karel.isFace('N'))
			return (this.getParede(ruaK, (byte) (avnK + 1), false) == null) && (avnK < totLin);
		return (this.getParede(ruaK, avnK, false) == null) && (avnK - 1 > 0);
	}

	/**
	 * Resposta aos comandos leftIsClear() e leftIsBlocked()
	 * 
	 * @return Se existe um bloqueio a esquerda de Karel
	 */
	protected boolean esquerdaLimpa() {
		byte ruaK = karel.getRua();
		byte avnK = karel.getAvenida();
		if (karel.isFace('L'))
			return (this.getParede(ruaK, (byte) (avnK + 1), false) == null) && (avnK < totLin);
		if (karel.isFace('O'))
			return (this.getParede(ruaK, avnK, false) == null) && (avnK - 1 > 0);
		if (karel.isFace('N'))
			return (this.getParede(ruaK, avnK, true) == null) && (ruaK - 1 > 0);
		return (this.getParede((byte) (ruaK + 1), avnK, true) == null) && (ruaK < totCol);
	}

	/**
	 * Resposta aos comandos rightIsClear() e rightIsBlocked()
	 * 
	 * @return Se existe um bloqueio a direita de Karel
	 */
	protected boolean direitaLimpa() {
		byte ruaK = karel.getRua();
		byte avnK = karel.getAvenida();
		if (karel.isFace('L'))
			return (this.getParede(ruaK, avnK, false) == null) && (avnK - 1 > 0);
		if (karel.isFace('O'))
			return (this.getParede(ruaK, (byte) (avnK + 1), false) == null) && (avnK < totLin);
		if (karel.isFace('N'))
			return (this.getParede((byte) (ruaK + 1), avnK, true) == null) && (ruaK < totCol);
		return (this.getParede(ruaK, avnK, true) == null) && (ruaK - 1 > 0);
	}

	/**
	 * Resposta aos comandos beepersPresent() e noBeepersPresent()
	 * 
	 * @return Se aonde karel está existe um sinalizador ou não
	 */
	protected boolean existeSinalizadorAqui() {
		return (this.getSinalizador(karel.getRua(), karel.getAvenida()) != null);
	}

	/**
	 * Resposta aos comandos beepersInBag() e noBeepersInBag()
	 * 
	 * @return Se existem sinalizadores na bolsa de Karel
	 */
	protected boolean existeSinalizadorBolsa() {
		return bolsaKarel > 0;
	}

	/**
	 * Resposta aos comandos: facing...() e notFacing...()
	 * 
	 * @param pontoCardeal ponto cardeal N, S, L ou O para pesquisar
	 * @return se a face de Karel está ou não apontada para o ponto cardeal
	 */
	protected boolean isFace(char pontoCardeal) {
		return karel.isFace(pontoCardeal);
	}

	// -------------------------------------------------------------------
	// Para modificações ocorridas no Mundo de Karel
	// -------------------------------------------------------------------

	/**
	 * Criar um novo mundo para Karel.
	 */
	protected void novoMapa() {
		fator = 2;
		faceKarel = 3;
		totLin = 10;
		totCol = 10;
		karel.setCorKarel(Color.BLACK);
		this.carregarKarel((byte) 1, (byte) 1, (byte) 1);
		this.limparAcessorios();
		this.recarregarMundo();
	}

	/**
	 * Guardar dados do Mundo de Karel.
	 */
	protected void guardarCopia() {
		karelClone = new KarelClone(karel.getRua(), karel.getAvenida(), karel.getFaceKarel(), karel.getCorKarel());
		beepersClone = new ArrayList<BeeperClone>();
		if (beepers != null && beepers.size() > 0) {
			for (BeeperObject beeper : beepers) {
				beepersClone.add(new BeeperClone(beeper.getRua(), beeper.getAvenida()));
			}
		}
		bolsaKarelClone = bolsaKarel;
	}

	/**
	 * Ler os dados guardados do Mundo de Karel.
	 */
	protected void executarCopia() {
		if (karelClone != null) {
			retirarSinalizadoresTela();
			karel.setRua(karelClone.getRua());
			karel.setAvenida(karelClone.getAvenida(), totLin);
			karel.setFaceKarel(karelClone.getFaceKarel());
			karel.setCorKarel(karelClone.getCorKarel());
			beepers = new ArrayList<BeeperObject>();
			if (beepersClone != null && beepersClone.size() > 0) {
				for (BeeperClone clone : beepersClone) {
					BeeperObject beeper = new BeeperObject(fator);
					beeper.setRua(clone.getRua());
					beeper.setAvenida(clone.getAvenida(), totLin);
					beepers.add(beeper);
				}
			}
			bolsaKarel = bolsaKarelClone;
			adicionarSinalizadoresTela();
			this.reposicionarObjetos();
		}
	}

	/**
	 * Tirar todos os sinalizadores e paredes da tela.
	 */
	protected void limparAcessorios() {
		retirarSinalizadoresTela();
		retirarParedesTela();
		beepers = new ArrayList<BeeperObject>();
		walls = new ArrayList<WallObject>();
		bolsaKarel = 0;
		karelClone = null;
		beepersClone = null;
		bolsaKarelClone = 0;
	}

	/**
	 * Mudar o tamanho de ruas e avenidas.
	 * 
	 * @param totRua número total de ruas
	 * @param totAvenida número total de avenidas
	 */
	protected void mudarTamanho(byte totRua, byte totAvenida) throws KarelException {
		if (existeElementos(totRua, totAvenida)) {
			throw new KarelException("Existem elementos que ficarão escondidos");
		}
		this.totCol = totRua;
		this.totLin = totAvenida;
		this.recarregarMundo();
	}

	/**
	 * Se existe qualquer elemento em uma determinada rua ou avenida
	 * @param rua número da rua
	 * @param avenida número da avenida
	 * @return se existe ou não algum elemento
	 */
	public boolean existeElementos(byte rua, byte avenida) {
		if (karel.getRua() > rua || karel.getAvenida() > avenida) {
			return true;
		}
		for (BeeperObject beeper : beepers) {
			if (beeper.getRua() > rua || beeper.getAvenida() > avenida) {
				return true;
			}
		}
		for (WallObject wall : walls) {
			if (wall.getRua() > rua || wall.getAvenida() > avenida) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Recarregar o Mundo de Karel na tela.
	 */
	public void recarregarMundo() {
		byte newFator = 3;
		if (totLin > 10 || totCol > 10) {
			newFator = 1;
		} else if (totLin > 6 || totCol > 6) {
			newFator = 2;
		}
		if (this.fator != newFator) {
			this.fator = newFator;
			karel.setFator(fator);
			for (BeeperObject beeper : beepers) {
				beeper.setFator(fator);
			}
			for (WallObject wall : walls) {
				wall.setFator(fator);
			}
		}
		reposicionarObjetos();
		this.repaint();
	}

	/**
	 * Reposicionar todos os objetos na tela.
	 */
	private void reposicionarObjetos() {
		karel.setAvenida(karel.getAvenida(), totLin);
		karel.setLocation(karel.getLocEsq(), karel.getLocTop());
		karel.repaint();
		for (BeeperObject beeper : beepers) {
			beeper.setAvenida(beeper.getAvenida(), totLin);
			beeper.setLocation(beeper.getLocEsq(), beeper.getLocTop());
			beeper.repaint();
		}
		for (WallObject wall : walls) {
			wall.setAvenida(wall.getAvenida(), totLin);
			wall.setLocation(wall.getLocEsq(), wall.getLocTop());
			wall.repaint();
		}
	}

	/**
	 * Obtém os dados do Mundo de Karel para gravar um Mapa
	 * @return dados do mundo
	 */
	protected String obterMundo(int velocidade) {
		StringBuilder dados = new StringBuilder("");
		dados.append("Dimension: (" + totCol + ", " + totLin + ")\n");
		for (WallObject wall : walls) {
			dados.append(wall.toString() + '\n');
		}
		for (BeeperObject beeper : beepers) {
			dados.append(beeper.toString() + '\n');
		}
		dados.append(karel.toString() + '\n');
		dados.append(karel.colorToStr() + '\n');
		dados.append("\nBeeperBag: " + bolsaKarel + '\n');
		dados.append("Speed: " + velocidade);
		return dados.toString();
	}

	// -------------------------------------------------------------------
	// Em relação a Karel
	// -------------------------------------------------------------------

	/**
	 * Colocar karel em uma determinada posição.
	 * 
	 * @param rua localização da rua
	 * @param avenida localização da avenida
	 * @param face para qual direção está virada
	 */
	protected void carregarKarel(byte rua, byte avenida, byte face) {
		karel.setRua(rua);
		karel.setAvenida(avenida, totLin);
		karel.setLocation(karel.getLocEsq(), karel.getLocTop());
		karel.setFaceKarel(face);
		karel.repaint();
	}

	protected void setBolsaKarel(int bolsaKarel) {
		this.bolsaKarel = bolsaKarel;
	}

	protected int getBolsaKarel() {
		return bolsaKarel;
	}
	
	protected void setCorKarel(Color cor) {
		karel.setCorKarel(cor);
	}

	protected Color getCorKarel() {
		return karel.getCorKarel();
	}
	
	// -------------------------------------------------------------------
	// Em relação aos Sinalizadores
	// -------------------------------------------------------------------

	/**
	 * Adicionar um localizador.
	 * 
	 * @param rua localização da rua
	 * @param avenida localização da avenida
	 */
	public void addSinalizador(byte rua, byte avenida) throws KarelException {
		if (rua > totCol || avenida > totLin) {
			throw new KarelException("É impossível adicionar um Sinalizador nesta posição");
		}
		BeeperObject beeper = new BeeperObject(fator);
		beeper.setRua(rua);
		beeper.setAvenida(avenida, totLin);
		beepers.add(beeper);
		beeper.setLocation(beeper.getLocEsq(), beeper.getLocTop());
		this.add(beeper);
	}

	/**
	 * Remover a sinalizador da tela.
	 * 
	 * @param rua localização da rua
	 * @param avenida localização da avenida
	 * @return objeto sinalizador da coleção de sinalizadores
	 */
	public boolean remSinalizador(byte rua, byte avenida) {
		BeeperObject beeper = this.getSinalizador(rua, avenida);
		if (beeper != null) {
			this.remove(beeper);
			beepers.remove(beeper);
			return true;
		}
		return false;
	}

	/**
	 * Retorna um sinalizador da tela.
	 * 
	 * @param rua localização da rua
	 * @param avenida localização da avenida
	 * @return objeto sinalizador da coleção de sinalizadores
	 */
	private BeeperObject getSinalizador(byte rua, byte avenida) {
		for (BeeperObject beeper : beepers) {
			if (beeper.getRua() == rua && beeper.getAvenida() == avenida) {
				return beeper;
			}
		}
		return null;
	}

	private void retirarSinalizadoresTela() {
		if (beepers != null && beepers.size() > 0) {
			for (BeeperObject beeper : beepers) {
				this.remove(beeper);
			}
		}
	}

	private void adicionarSinalizadoresTela() {
		if (beepers != null && beepers.size() > 0) {
			for (BeeperObject beeper : beepers) {
				this.add(beeper);
			}
		}
	}

	// -------------------------------------------------------------------
	// Em relação as Paredes
	// -------------------------------------------------------------------

	/**
	 * Adicionar uma parede.
	 * 
	 * @param rua localização da rua
	 * @param avenida localização da avenida
	 * @param emPe se está em pé ou deitada
	 */
	public void addParede(byte rua, byte avenida, boolean emPe) throws KarelException {
		if (rua > totCol || avenida > totLin || (rua == 1 && emPe) || (avenida == 1 && !emPe)) {
			throw new KarelException("É impossível adicionar uma Parede nesta posição");
		}
		WallObject wall = new WallObject(fator);
		wall.setEmPe(emPe);
		wall.setRua(rua);
		wall.setAvenida(avenida, totLin);
		walls.add(wall);
		wall.setLocation(wall.getLocEsq(), wall.getLocTop());
		this.add(wall);
	}

	/**
	 * Remover a parede da tela.
	 * 
	 * @param rua localização da rua
	 * @param avenida localização da avenida
	 * @param emPe se está em pé ou deitada
	 * @return se removeu ou não
	 */
	public boolean remParede(byte rua, byte avenida, boolean emPe) {
		WallObject wall = this.getParede(rua, avenida, emPe);
		if (wall != null) {
			this.remove(wall);
			walls.remove(wall);
			return true;
		}
		return false;
	}

	/**
	 * Retorna a parede da tela.
	 * 
	 * @param rua localização da rua
	 * @param avenida localização da avenida
	 * @param emPe se está em pé ou deitada
	 * @return objeto parede da coleção de paredes
	 */
	private WallObject getParede(byte rua, byte avenida, boolean emPe) {
		for (WallObject wall : walls) {
			if (wall.getRua() == rua && wall.getAvenida() == avenida && wall.isEmPe() == emPe) {
				return wall;
			}
		}
		return null;
	}

	private void retirarParedesTela() {
		if (walls != null && walls.size() > 0) {
			for (WallObject wall : walls) {
				this.remove(wall);
			}
		}
	}
}