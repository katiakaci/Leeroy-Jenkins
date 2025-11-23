package vue;

/**
 * Panneau principal du jeu
 * 
 * contient:
 * - le panneau du donjon
 * 
 * @author Fred Simard | ETS
 * @version ETE 2018 - TP2
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

public class PanneauPrincipal extends JPanel{

	Dimension tailleEcran = Toolkit.getDefaultToolkit().getScreenSize();

	// Panneaux
	public PanneauDonjon panDonjon;
	public PanneauStatus panStatus;

	/**
	 * Constructeur
	 * @param taille La taille de l'ecran
	 */
	public PanneauPrincipal() {
		// assigne la tÃ¢che
		setSize(tailleEcran);
		setPreferredSize(tailleEcran);

		// initialise les composantes
		initComposantes();
	}

	/*
	 * Dimensionne et ajoute les differents panneaux e leur place.
	 */
	private void initComposantes() {

		// définit le layout
		setLayout(new BorderLayout());

		// définit le panneau de donjon
		panDonjon = new PanneauDonjon(tailleEcran);
		add(panDonjon, BorderLayout.CENTER);

		// définit le panneau de status
		panStatus = new PanneauStatus(tailleEcran);
		add(panStatus, BorderLayout.LINE_END); //placer le PanneauStatus à droite/east	
	}
}