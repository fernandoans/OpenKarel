package openKarel.dialogs;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import openKarel.KarelException;
import openKarel.MundoKarel;
import openKarel.XKarel;

/**
 * Transações sobre construção e destruição de Sinalizadores
 * 
 * @author Fernando Anselmo
 * @version 1.0
 */
public class DiagBeeper extends JDialog {

	private static final long serialVersionUID = 5504938812776771081L;
	private MundoKarel mundo;
	private boolean tipo;

	private SpinnerModel modelAvenida = new SpinnerNumberModel(1, 1, 20, 1);
	private JSpinner nAvenida = new JSpinner(modelAvenida);
	private SpinnerModel modelRua = new SpinnerNumberModel(1, 1, 20, 1);
	private JSpinner nRua = new JSpinner(modelRua);

	public DiagBeeper(MundoKarel mundo, boolean tipo) {
		this.mundo = mundo;
		this.tipo = tipo;
		this.setTitle(((tipo) ? "Adicionar" : "Remover") + " Sinalizador");
		this.setSize(300, 180);
		this.setModal(true);

		// Qual Rua
		JPanel pnRua = new JPanel();
		pnRua.add(new JLabel("Nº Rua"));
		pnRua.add(nRua);

		// Qual Avenida
		JPanel pnAvenida = new JPanel();
		pnAvenida.add(new JLabel("Nº Avenida"));
		pnAvenida.add(nAvenida);

		// Unir os 3 combos
		JPanel pnCentral = new JPanel();
		pnCentral.setLayout(new GridLayout(2, 1));
		pnCentral.add(pnRua);
		pnCentral.add(pnAvenida);

		// Botões da tela
		JButton btConfirmar = new JButton("Confirmar");
		btConfirmar.addActionListener(e -> {
			confirmar();
		});

		JButton btCancelar = new JButton("Cancelar");
		btCancelar.addActionListener(e -> {
			cancelar();
		});

		JPanel pnBotoes = new JPanel();
		pnBotoes.add(btConfirmar);
		pnBotoes.add(btCancelar);

		// Montar Tela
		this.add(pnCentral);
		this.add(pnBotoes, BorderLayout.SOUTH);

		this.setVisible(true);
	}

	private void confirmar() {
		byte rua = ((Integer) nRua.getValue()).byteValue();
		byte avenida = ((Integer) nAvenida.getValue()).byteValue();
		try {
			if (tipo) {
				mundo.addSinalizador(rua, avenida);
				mundo.recarregarMundo();
			} else {
				if (mundo.remSinalizador(rua, avenida)) {
					mundo.recarregarMundo();
				} else {
					throw new KarelException("Sinalizador não localizada!");
				}
			}
		} catch (KarelException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), XKarel.SISTEMA, JOptionPane.ERROR_MESSAGE);
		}
	}

	private void cancelar() {
		this.dispose();
	}
}