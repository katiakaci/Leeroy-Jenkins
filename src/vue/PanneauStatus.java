package vue;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;

import modele.PlanDeJeu;
import observer.MonObserver;

public class PanneauStatus extends JPanel implements MonObserver {

	Dimension taille;

	//Chacun des PanneauxStatus{Haut, Milieu, Bas} est membre du PanneauStatus
	private PanneauStatusHaut panStatusHaut;
	private PanneauStatusMilieu panStatusMilieu; 
	private PanneauStatusBas panStatusBas;

	// rÃ©fÃ©rence au plan de jeu
	private PlanDeJeu planDeJeu = PlanDeJeu.getInstance();

	/**
	 * Constructeur
	 * @param taille, taille de la fenêtre
	 */
	public PanneauStatus(Dimension taille){

		///largeur= 1/3 largeur de la taille de l’écran & hauteur= taille de l’écran
		Dimension size = new Dimension(taille.width/3, taille.height);

		planDeJeu.attacherObserver(this);
		this.taille = size;

		// assigne la tÃ¢che
		setSize(size);
		setPreferredSize(size);

		// initialise les composantes
		initComposantes();
	}


	/*
	 * Dimensionne et ajoute les differents panneaux (haut,milieu,bas) à leur place.
	 */
	public void initComposantes() {

		// définit le layout
		this.setLayout(new GridLayout(0,1));

		// définit le panneau de status haut
		panStatusHaut = new PanneauStatusHaut();
		add(panStatusHaut); //placer un PanneauStatusHaut en haut

		// définit le panneau de status milieu
		panStatusMilieu = new PanneauStatusMilieu();
		add(panStatusMilieu); //placer un PanneauStatusMilieu au milieu

		// définit le panneau de status bas
		panStatusBas = new PanneauStatusBas();
		add(panStatusBas); //placer un PanneauStatusBas en bas
	}

	/**
	 * mÃ©thode callback, pour la gestion de l'observer
	 */
	public void avertir() {
		panStatusHaut.mettreAJoursInfo();
		panStatusMilieu.mettreAJoursInfo();
		panStatusBas.mettreAJoursInfo();
	}
}
