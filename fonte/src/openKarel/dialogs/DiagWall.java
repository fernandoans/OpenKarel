package openKarel.dialogs;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
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
 * Transações sobre construção e destruição de Paredes
 * 
 * @author Fernando Anselmo
 * @version 1.0
 */
public class DiagWall extends JDialog {

	private static final long serialVersionUID = 6178940177570262058L;
	private MundoKarel mundo;
	private boolean tipo;

	private SpinnerModel modelAvenida = new SpinnerNumberModel(1, 1, 20, 1);
	private JSpinner nAvenida = new JSpinner(modelAvenida);
	private SpinnerModel modelRua = new SpinnerNumberModel(1, 1, 20, 1);
	private JSpinner nRua = new JSpinner(modelRua);
	private String[] tipos = { "Em pé", "Deitada" };
	private JComboBox<String> cbTipo = new JComboBox<String>(tipos);

	public DiagWall(MundoKarel mundo, boolean tipo) {
		this.mundo = mundo;
		this.tipo = tipo;
		this.setTitle(((tipo) ? "Adicionar" : "Remover") + " Parede");
		this.setSize(300, 200);
		this.setModal(true);

		// Qual Rua
		JPanel pnRua = new JPanel();
		pnRua.add(new JLabel("Nº Rua"));
		pnRua.add(nRua);

		// Qual Avenida
		JPanel pnAvenida = new JPanel();
		pnAvenida.add(new JLabel("Nº Avenida"));
		pnAvenida.add(nAvenida);

		// Qual Tipo
		JPanel pnTipo = new JPanel();
		pnTipo.add(new JLabel("Tipo"));
		pnTipo.add(cbTipo);

		// Unir os 3 combos
		JPanel pnCentral = new JPanel();
		pnCentral.setLayout(new GridLayout(3, 1));
		pnCentral.add(pnRua);
		pnCentral.add(pnAvenida);
		pnCentral.add(pnTipo);

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
		boolean emPe = (cbTipo.getSelectedIndex() == 0);
		try {
			if (tipo) {
				mundo.addParede(rua, avenida, emPe);
				mundo.recarregarMundo();
			} else {
				if (mundo.remParede(rua, avenida, emPe)) {
					mundo.recarregarMundo();
				} else {
					throw new KarelException("Parede não localizada!");
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