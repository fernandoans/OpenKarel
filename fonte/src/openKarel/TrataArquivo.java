package openKarel;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JFileChooser;

public class TrataArquivo {

	private XKarel frame;
	private MundoKarel mundo;
	private String lin;

	protected TrataArquivo(XKarel frame, MundoKarel mundo) {
		this.frame = frame;
		this.mundo = mundo;
	}

	/**
	 * Abrir o arquivo do Projeto
	 * 
	 * @return Lógico informando o sucesso ou fracasso da operação
	 * @throws KarelException
	 */
	protected boolean abrirArquivo() throws KarelException {
		JFileChooser dig = new JFileChooser();
		dig.setCurrentDirectory(new File(System.getProperty("user.home")));
		int result = dig.showOpenDialog(frame);
		if (result == JFileChooser.APPROVE_OPTION) {
			lerArquivo(dig.getSelectedFile());
			return true;
		}
		return false;
	}

	/**
	 * Salvar o arquivo do Projeto
	 * 
	 * @return Lógico informando o sucesso ou fracasso da operação
	 * @throws KarelException
	 */
	protected boolean salvarArquivo() throws KarelException {
		JFileChooser dig = new JFileChooser();
		dig.setCurrentDirectory(new File(System.getProperty("user.home")));
		int result = dig.showSaveDialog(frame);
		if (result == JFileChooser.APPROVE_OPTION) {
			gravarArquivo(dig.getSelectedFile());
			return true;
		}
		return false;
	}

	private void lerArquivo(File nomArq) throws KarelException {
		try {
			BufferedReader arqEntrada = new BufferedReader(new FileReader(nomArq));
			StringBuilder mapaLido = new StringBuilder("\n\nInformações do Mapa Lido\n\n");
			mundo.limparAcessorios();
			while ((lin = arqEntrada.readLine()) != null) {
				processarLinha();
				mapaLido.append(lin + '\n');
			}
			arqEntrada.close();
			mundo.recarregarMundo();
			frame.carregarPrograma(mapaLido.toString());
		} catch (IOException e) {
			throw new KarelException(e.getMessage());
		}
	}

	private void gravarArquivo(File nomArq) throws KarelException {
		try {
			PrintWriter arqSaida = new PrintWriter(new FileWriter(nomArq));
			arqSaida.print(mundo.obterMundo(frame.getVelocidade()));
			arqSaida.close();
		} catch (IOException e) {
			throw new KarelException(e.getMessage());
		}
	}

	private void processarLinha() throws KarelException {
		if (lin.startsWith("Dimension:")) {
			processarTamanhoMundo();
		} else if (lin.startsWith("Wall:")) {
			processarParede();
		} else if (lin.startsWith("Beeper:")) {
			processarBeeper();
		} else if (lin.startsWith("Karel:")) {
			processarKarel();
		} else if (lin.startsWith("ColorKarel:")) {
			processarCorKarel();
		} else if (lin.startsWith("BeeperBag:")) {
			processarBolsa();
		} else if (lin.startsWith("Speed:")) {
			processarVelocidade();
		}
	}

	// Dimension: (6, 4)
	private void processarTamanhoMundo() throws KarelException {
		byte nRua = acharRua();
		byte nAvenida = acharAvenida();
		mundo.mudarTamanho(nRua, nAvenida);
		frame.arrumarRuaAvenida(nRua, nAvenida);
	}

	// Wall: (4, 1) west
	private void processarParede() throws KarelException {
		byte rua = acharRua();
		byte avenida = acharAvenida();
		boolean emPe = true;
		if ((lin.indexOf("north") > -1) || (lin.indexOf("south") > -1)) {
			emPe = false;
		}
		mundo.addParede(rua, avenida, emPe);
	}

	// Beeper: (2, 1) 1
	private void processarBeeper() throws KarelException {
		byte rua = acharRua();
		byte avenida = acharAvenida();
		mundo.addSinalizador(rua, avenida);
	}

	// Karel: (1, 1) east
	private void processarKarel() {
		byte rua = acharRua();
		byte avenida = acharAvenida();
		byte faceKarel = 0;
		if (lin.indexOf("east") > -1) {
			faceKarel = 1;
		} else if (lin.indexOf("north") > -1) {
			faceKarel = 2;
		} else if (lin.indexOf("west") > -1) {
			faceKarel = 3;
		} else if (lin.indexOf("south") > -1) {
			faceKarel = 4;
		}
		mundo.carregarKarel(rua, avenida, faceKarel);
	}

	// ColorKarel: RGB
	private void processarCorKarel() {
		mundo.setCorKarel(new Color(pegarValor()));
	}
	
	// BeeperBag: 10 (manter a compatibilidade INFINITE)
	private void processarBolsa() {
		int bolsaKarel = 0;
		if (lin.indexOf("INFINITE") > 0) {
			bolsaKarel = Integer.MAX_VALUE;
		} else {
			bolsaKarel = pegarValor();
		}
		mundo.setBolsaKarel(bolsaKarel);
	}
	
	// Speed: 10.0 (manter a compatibilidade . e ,)
	private void processarVelocidade() {
		int velocidade = 0;
		if (lin.indexOf("0,00") > 0 || lin.indexOf("0.00") > 0) {
			velocidade = 5;
		} else {
			velocidade = pegarValor();
		}
		frame.setVelocidade(velocidade);
	}
	
	private byte acharRua() {
		return Byte.parseByte(lin.substring(lin.indexOf("(") + 1, lin.indexOf(",")).trim());
	}

	private byte acharAvenida() {
		return Byte.parseByte(lin.substring(lin.indexOf(",") + 1, lin.indexOf(")")).trim());
	}
	
	private int pegarValor() {
		String vl = "";
		for (int i = lin.indexOf(": ")+2; i < lin.length(); i++) {
			if ("-0123456789".indexOf(lin.charAt(i)) < 0) {
				break;
			}
			vl += lin.charAt(i);
		}
		return frame.strToInt(vl);
	}
}