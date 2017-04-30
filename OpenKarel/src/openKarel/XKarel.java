package openKarel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import openKarel.dialogs.DiagBeeper;
import openKarel.dialogs.DiagWall;

/**
 * Classe Principal do Sistema que será herdada pelo CLIENTE
 * 
 * @author Fernando Anselmo
 * @version 1.0
 */
public abstract class XKarel extends JFrame implements Runnable {

	private static final long serialVersionUID = -5321773012961358821L;
	/* Versao atual */
	private static final String VERSAO = "1.03";
	public static final String SISTEMA = "Open Karel";

	private JTextArea programa = new JTextArea("Bem vindo ao " + SISTEMA + " desenvolvido em Linguagem Java\n");
	private MundoKarel mundo = new MundoKarel(programa);
	private JSlider slRua = new JSlider(JSlider.HORIZONTAL, 1, 20, 10);
	private JSlider slAvenida = new JSlider(JSlider.HORIZONTAL, 1, 20, 10);
	private JSlider slEspera = new JSlider(JSlider.HORIZONTAL, 1, 50, 5);
	private Thread execKarel;

	public XKarel() {
		super(SISTEMA + " (v" + VERSAO + ")");
		this.setSize(1100, 800);
		this.mntMenu();

		// Montar Painel de Botões
		JButton btExecutar = new JButton("");
		btExecutar.setIcon(getImage("executar.png"));
		btExecutar.setToolTipText("Executar");
		btExecutar.addActionListener(e -> {
			executar();
		});

		JButton btReiniciar = new JButton("");
		btReiniciar.setIcon(getImage("retornar.png"));
		btReiniciar.setToolTipText("Reiniciar");
		btReiniciar.addActionListener(e -> {
			reiniciar();
		});

		JPanel pnBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pnBotoes.setPreferredSize(new Dimension(130, 45));
		pnBotoes.add(btExecutar);
		pnBotoes.add(btReiniciar);

		// Montar Painel de Sliders
		JPanel pnSliders = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		//pnSliders.setPreferredSize(new Dimension(930, 45));

		pnSliders.add(new JLabel("Ruas: "));
		slRua.setMinorTickSpacing(2);
		slRua.setPaintTicks(true);
		slRua.addChangeListener(e -> {
			mudarTamMundo();
		});
		pnSliders.add(slRua);

		pnSliders.add(new JLabel("Avenidas: "));
		slAvenida.setMinorTickSpacing(2);
		slAvenida.setPaintTicks(true);
		slAvenida.addChangeListener(e -> {
			mudarTamMundo();
		});
		pnSliders.add(slAvenida);

		pnSliders.add(new JLabel("Espera: "));
		slEspera.setMinorTickSpacing(2);
		slEspera.setPaintTicks(true);
		pnSliders.add(slEspera);

		JPanel pnBarra = new JPanel();
		pnBarra.add(pnBotoes);
		pnBarra.add(pnSliders);

		this.add(pnBarra, BorderLayout.SOUTH);

		// Montar Conteudo da Janela
		JSplitPane splitH = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mundo, new JScrollPane(programa));
		splitH.setOneTouchExpandable(true);
		splitH.setDividerLocation(660);
		this.add(splitH);

		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}

	/**
	 * Montar o menu da Janela
	 */
	private void mntMenu() {
		/* Objetos do Menu do Sistema */
		JMenuBar barra = new JMenuBar();
		JMenu menuArq = new JMenu("Arquivo");

		JMenuItem itNovo = new JMenuItem("Novo Mapa");
		itNovo.setIcon(getImage("novo.png"));
		itNovo.addActionListener(e -> {
			acionarNovoMapa();
		});

		JMenuItem itLer = new JMenuItem("Ler Mapa");
		itLer.setIcon(getImage("ler.png"));
		itLer.addActionListener(e -> {
			acionarLerMapa();
		});

		JMenuItem itGravar = new JMenuItem("Gravar Mapa");
		itGravar.setIcon(getImage("gravar.png"));
		itGravar.addActionListener(e -> {
			acionarGravarMapa();
		});

		JMenuItem itSair = new JMenuItem("Sair");
		itSair.addActionListener(e -> {
			acionarFechar();
		});

		JMenu menuObj = new JMenu("Objetos");

		JMenuItem itAddSinal = new JMenuItem("Adicionar Sinalizador");
		itAddSinal.setIcon(getImage("addBeeper.png"));
		itAddSinal.addActionListener(e -> {
			addSinalizador();
		});

		JMenuItem itRemSinal = new JMenuItem("Remover Sinalizador");
		itRemSinal.setIcon(getImage("delBeeper.png"));
		itRemSinal.addActionListener(e -> {
			remSinalizador();
		});

		JMenuItem itAddParede = new JMenuItem("Adicionar Parede");
		itAddParede.setIcon(getImage("addWall.png"));
		itAddParede.addActionListener(e -> {
			addParede();
		});

		JMenuItem itRemParede = new JMenuItem("Remover Parede");
		itRemParede.setIcon(getImage("delWall.png"));
		itRemParede.addActionListener(e -> {
			remParede();
		});

		JMenuItem itBolsaKarel = new JMenuItem("Bolsa de Karel");
		itBolsaKarel.addActionListener(e -> {
			bolsaKarel();
		});

		JMenuItem itCorKarel = new JMenuItem("Cor de Karel");
		itCorKarel.addActionListener(e -> {
			corKarel();
		});

		menuArq.add(itNovo);
		menuArq.add(itLer);
		menuArq.add(itGravar);
		menuArq.addSeparator();
		menuArq.add(itSair);

		menuObj.add(itAddSinal);
		menuObj.add(itRemSinal);
		menuObj.addSeparator();
		menuObj.add(itAddParede);
		menuObj.add(itRemParede);
		menuObj.addSeparator();
		menuObj.add(itBolsaKarel);
		menuObj.add(itCorKarel);

		barra.add(menuArq);
		barra.add(menuObj);
		this.setJMenuBar(barra);
	}

	// -------------------------------------------------------------------
	// Ações do Menu
	// -------------------------------------------------------------------

	/** Novo mapa */
	private void acionarNovoMapa() {
		arrumarRuaAvenida(10, 10);
		mundo.novoMapa();
	}

	/** Abrir um mapa */
	private void acionarLerMapa() {
		TrataArquivo arq = new TrataArquivo(this, mundo);
		try {
			if (arq.abrirArquivo())
				JOptionPane.showMessageDialog(null, "Mapa carregado sem problemas.", SISTEMA, JOptionPane.INFORMATION_MESSAGE);
		} catch (KarelException e) {
			showErro(e.getMessage());
		}
	}

	/** Salvar um mapa */
	private void acionarGravarMapa() {
		TrataArquivo arq = new TrataArquivo(this, mundo);
		try {
			if (arq.salvarArquivo())
				JOptionPane.showMessageDialog(null, "Mapa salvo sem problemas.", SISTEMA, JOptionPane.INFORMATION_MESSAGE);
			this.getContentPane().repaint();
		} catch (KarelException e) {
			showErro(e.getMessage());
		}
	}

	/** Adicionar Parede */
	private void addParede() {
		new DiagWall(mundo, true);
	}

	/** Remover Parede */
	private void remParede() {
		new DiagWall(mundo, false);
	}

	/** Adicionar Sinalizador */
	private void addSinalizador() {
		new DiagBeeper(mundo, true);
	}

	/** Remover Sinalizador */
	private void remSinalizador() {
		new DiagBeeper(mundo, false);
	}

	/** Ajustar a bolsa de Karel */
	private void bolsaKarel() {
		String valor = JOptionPane.showInputDialog("Sinalizadores na Bolsa de Karel", mundo.getBolsaKarel());
    if (valor != null) {
    	mundo.setBolsaKarel(strToInt(valor));
    }
	}

	/** Ajustar a cor de Karel */
	private void corKarel() {
    Color color = mundo.getCorKarel();
    color = JColorChooser.showDialog(null, "Escolha a Cor de Karel", color);
    if (color != null)
    	mundo.setCorKarel(color);
	}

	/** Encerrar a aplicacao */
	private void acionarFechar() {
		System.exit(0);
	}

	// -------------------------------------------------------------------
	// Comandos dos Botões
	// -------------------------------------------------------------------

	private void executar() {
		mundo.guardarCopia();
		execKarel = new Thread(this);
		execKarel.start();
	}

	private void reiniciar() {
		mundo.executarCopia();
	}

	// -------------------------------------------------------------------
	// Comandos de KAREL
	// -------------------------------------------------------------------

	/**
	 * Avançar um bloco. Karel não pode responder ao comando, se existir obstáculo
	 * a sua frente
	 */
	public void move() {
		try {
			mundo.andarKarel();
			pararMovimento();
		} catch (KarelException e) {
			showErro(e.getMessage());
			throw new RuntimeException();
		}
	}

	/**
	 * Girar 90 graus para a esquerda (sentido anti-horário)
	 */
	public void turnLeft() {
		mundo.mudarDirecaoKarel();
	}

	/**
	 * Pegar um sinalizador e armazená-lo na sua bolsa, que pode conter um número
	 * infinito.
	 */
	public void pickBeeper() {
		try {
			mundo.pegarSinalizador();
		} catch (KarelException e) {
			showErro(e.getMessage());
			throw new RuntimeException();
		}
	}

	/**
	 * Pegar um sinalizador da bolsa e colocá-lo no local atual.
	 */
	public void putBeeper() {
		try {
			mundo.deixarSinalizador();
		} catch (KarelException e) {
			showErro(e.getMessage());
			throw new RuntimeException();
		}
	}

	/**
	 * Frente está livre?
	 */
	public boolean frontIsClear() {
		return mundo.frenteLimpa();
	}

	/**
	 * Frente está bloqueada?
	 */
	public boolean frontIsBlocked() {
		return !mundo.frenteLimpa();
	}

	/**
	 * Esquerda está livre?
	 */
	public boolean leftIsClear() {
		return mundo.esquerdaLimpa();
	}

	/**
	 * Esquerda está bloqueada?
	 */
	public boolean leftIsBlocked() {
		return !mundo.esquerdaLimpa();
	}

	/**
	 * Direita está livre?
	 */
	public boolean rightIsClear() {
		return mundo.direitaLimpa();
	}

	/**
	 * Direita está bloqueada?
	 */
	public boolean rightIsBlocked() {
		return !mundo.direitaLimpa();
	}

	/**
	 * Existe um sinalizador na posição de Karel.
	 */
	public boolean beepersPresent() {
		return mundo.existeSinalizadorAqui();
	}

	/**
	 * Não existe um sinalizador na posição de Karel.
	 */
	public boolean noBeepersPresent() {
		return !mundo.existeSinalizadorAqui();
	}

	/**
	 * Existe um sinalizador na bolsa de Karel.
	 */
	public boolean beepersInBag() {
		return mundo.existeSinalizadorBolsa();
	}

	/**
	 * Não existe um sinalizador na bolsa de Karel.
	 */
	public boolean noBeepersInBag() {
		return !mundo.existeSinalizadorBolsa();
	}

	/**
	 * Karel está virada para o Norte.
	 */
	public boolean facingNorth() {
		return mundo.isFace('N');
	}

	/**
	 * Karel não está virada para o Norte.
	 */
	public boolean notFacingNorth() {
		return !mundo.isFace('N');
	}

	/**
	 * Karel está virada para o Leste.
	 */
	public boolean facingEast() {
		return mundo.isFace('L');
	}

	/**
	 * Karel não está virada para o Leste.
	 */
	public boolean notFacingEast() {
		return !mundo.isFace('L');
	}

	/**
	 * Karel está virada para o Sul.
	 */
	public boolean facingSouth() {
		return mundo.isFace('S');
	}

	/**
	 * Karel não está virada para o Sul.
	 */
	public boolean notFacingSouth() {
		return !mundo.isFace('S');
	}

	/**
	 * Karel está virada para o Oeste.
	 */
	public boolean facingWest() {
		return mundo.isFace('O');
	}

	/**
	 * Karel não está virada para o Oeste.
	 */
	public boolean notFacingWest() {
		return !mundo.isFace('O');
	}

	// -------------------------------------------------------------------
	// Comandos da tela
	// -------------------------------------------------------------------

	private void mudarTamMundo() {
		try {
			mundo.mudarTamanho((byte) slRua.getValue(), (byte) slAvenida.getValue());
		} catch (KarelException e) {
			slRua.setValue(mundo.getTotCol());
			slAvenida.setValue(mundo.getTotLin());
			showErro(e.getMessage());
		}
	}

	/**
	 * Devolve um objeto ImageIcon com determinada imagem
	 * 
	 * @param s String contendo o nome da imagem
	 * @return Objeto ImageIcon
	 */
	private ImageIcon getImage(String s) {
		URL url = getResource("openKarel/images/" + s);
		if (url != null)
			return new ImageIcon(url);
		else
			return new ImageIcon("openKarel/images/" + s);
	}

	private URL getResource(String s) {
		return ClassLoader.getSystemResource(s);
	}

	protected void carregarPrograma(String conteudo) {
		programa.append(conteudo);
	}

	protected void arrumarRuaAvenida(int rua, int avenida) {
		slRua.setValue(rua);
		slAvenida.setValue(avenida);
	}

	// -------------------------------------------------------------------
	// Métodos de auxílio
	// -------------------------------------------------------------------
	
	protected int getVelocidade() {
		return slEspera.getValue();
	}

	protected void setVelocidade(int velocidade) {
		slEspera.setValue(velocidade);
	}

	private void showErro(String mens) {
		JOptionPane.showMessageDialog(this, mens, SISTEMA, JOptionPane.ERROR_MESSAGE);
	}

	@SuppressWarnings("static-access")
	private void pararMovimento() {
		this.repaint();
		try {
			execKarel.sleep(slEspera.getValue()*10);
		} catch (InterruptedException e) {
		}
	}
	
	protected int strToInt(String valor) {
		if (valor != null && valor.length() > 0) {
			try {
				return Integer.parseInt(valor);
			} catch (Exception e) {
			}
		}
		return 0;
	}
}