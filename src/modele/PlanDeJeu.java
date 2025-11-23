package modele;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.ListIterator;

/**
 * Le plan de jeu est la classe qui supporte le modèle du programme.
 * Il contient:
 * 	- le donjon
 *  - le joueur
 *  - les créatures
 *  
 * et actionne les mécaniques du jeu.
 * 
 * Le plan de jeu est implémenté en Lazy Singleton
 * 
 * @author Fred Simard | ETS
 * @version ETE 2018 - TP2
 */


import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import creature.AbstractCreature;
import creature.Araigne;
import creature.Dragon;
import creature.Minotaure;
import dongon.Case;
import dongon.Configuration;
import dongon.Donjon;
import equipements.AbstractEquipement;
import equipements.Arme;
import equipements.Armure;
import equipements.Casque;
import equipements.Potion;
import joueur.Joueur;
import observer.MonObservable;
import observer.MonObserver;
import physique.Direction;
import physique.Position;

public class PlanDeJeu extends MonObservable implements MonObserver, Runnable {

	private Donjon donjon;
	private Joueur joueur = new Joueur();
	private boolean partieEnCours = false;
	GestionnaireCombat gestCombat = new GestionnaireCombat();
	private int niveauCourant = 0;
	private Vector<AbstractCreature> creatures = new Vector<AbstractCreature>(10);
	private Vector<AbstractEquipement> equipements = new Vector<AbstractEquipement>(10);
	private Random rand = new Random(System.currentTimeMillis());

	//pour le chronom�tre d'un niveau (dans le panneau de haut)
	private Instant debutNiveau;
	private long chronometreNiveau;

	//collection contenant les messages de la console
	private ArrayList<String> messageJoueurConsole = new ArrayList<String>();

	private static final PlanDeJeu instance = new PlanDeJeu();
	private static Thread t;

	/**
	 * constructeur du plan de jeu
	 */
	public PlanDeJeu(){
		partieEnCours = true;
		nouveauNiveau();
	}

	/**
	 * m�thode pour obtenir une r�f�rence sur le chronom�tre du temps jou� pour le niveau courant
	 * @return chronometreNiveau, le chronom�tre en secondes
	 */
	public long getChronometreNiveau() {
		return this.chronometreNiveau;
	}

	/**
	 * méthode pour obtenir une référence au plan de jeu
	 * @return l'instance
	 */
	public static PlanDeJeu getInstance(){
		return instance;
	}

	/**
	 * méthode pour obtenir une référence au donjon
	 * @return référence au donjon
	 */
	public Donjon getDonjon(){
		return this.donjon;
	}

	/**
	 * méthode qui permet de déterminer si un combat est en cours.
	 * @return true si combat en cours
	 */
	public boolean estEnCombat() {
		return gestCombat.combatEstEnCours();
	}

	/**
	 * méthode pour obtenir une référence au vecteur de créature
	 * @return référence aux créatures
	 */
	public Vector<AbstractCreature> getCreatures(){
		return this.creatures;
	}

	/**
	 * méthode pour obtenir une référence sur le joueur
	 * @return référence au joueur
	 */
	public Joueur getJoueur(){
		return this.joueur;
	}

	/**
	 * méthode pour obtenir une référence sur le joueur
	 * @return référence au joueur
	 */
	public Vector<AbstractEquipement> getEquipements(){
		return this.equipements;
	}

	/**
	 * méthode pour initialiser le vecteur de créatures
	 */
	private void initCreatures(){

		// obtient une référence au cases du donjon et au configuration
		Case[][] casesDonjon = this.donjon.getCases();
		Configuration config = Configuration.getInstance();

		// vide le vecteur, s'il contient des créatures
		this.creatures.removeAllElements();

		// initialise le nombre de créatures demandées
		for(int i=0;i<config.getConfig(Configuration.NB_CREATURES);i++){

			// tire un type de créature aléatoirement
			int type = rand.nextInt((int)config.getConfig(Configuration.NB_TYPES_CREATURES));
			AbstractCreature cetteCreature = null;

			// Tire une position aléatoire
			Position posAlea = this.donjon.getPositionAlea();

			// crée une créature du type demandé (factory style)
			switch(type){

			case 0:
				cetteCreature= new Araigne(posAlea);
				break;
			case 1:
				cetteCreature = new Dragon(posAlea);
				break;
			case 2:
				cetteCreature = new Minotaure(posAlea);
				break;

			default:
				cetteCreature = new Araigne(posAlea);
				break;
			}

			// attache la créature au plan de jeu
			cetteCreature.attacherObserver(this);
			cetteCreature.setCase(casesDonjon[posAlea.getI()][posAlea.getJ()]);
			this.creatures.add(cetteCreature);
		}
	}

	/**
	 * méthode pour initialiser le vecteur de créatures
	 */
	private void initEquipements(){
		// obtient une référence au cases du donjon et au configuration
		Case[][] casesDonjon = this.donjon.getCases();
		Configuration config = Configuration.getInstance();

		// vide le vecteur, s'il contient des créatures
		this.equipements.removeAllElements();

		// initialise le nombre de créatures demandées
		for(int i=0;i<config.getConfig(Configuration.NB_EQUIPEMENTS);i++){

			// tire un type de créature aléatoirement
			int type = rand.nextInt((int)config.getConfig(Configuration.NB_TYPES_CREATURES));
			AbstractEquipement cetEquipement = null;

			// Tire une position aléatoire
			Position posAlea = this.donjon.getPositionAlea();

			switch(i){
			case 0:
				cetEquipement = new Arme(posAlea);
				break;
			case 1:
				cetEquipement = new Armure(posAlea);
				break;
			case 2:
				cetEquipement = new Casque(posAlea);
				break;
			default:
				cetEquipement = new Potion(posAlea);
				break;
			}
			cetEquipement.setCase(casesDonjon[posAlea.getI()][posAlea.getJ()]);
			this.equipements.add(cetEquipement);
		}
	}
	/**
	 * méthode pour initialiser le joueur
	 */
	private void initJoueur(){

		// obtient la case de départ
		Case depart = this.donjon.getDepart();
		// l'assigne au joueur et attache le joueur au plan de jeu
		this.joueur.setPos(new Position(depart.getPos().getI(),depart.getPos().getJ()));
		this.joueur.attacherObserver(this);
		this.joueur.setCase(depart);
	}

	@Override
	/**
	 * callback implémenté par l'observer
	 */
	public void avertir() {

		// vérifie les règles du jeu
		validerEtatJeu();

		// avertie les observers du plan de jeu
		this.avertirLesObservers();
	}

	@Override
	/**
	 * implémente la méthode run de Runnable
	 */
	public void run() {

		// tant qu'une partie est en cours
		while(partieEnCours){ 

			//le chronom�tre fonctionne en calculant � chaque seconde la diff�rence entre le d�but du niveau et l'instant actuel
			chronometreNiveau = Duration.between(debutNiveau, Instant.now()).toSeconds();

			if(!gestCombat.combatEstEnCours()){
				// déplace toutes les créatures
				for(int i=0;i<this.creatures.size();i++){
					this.creatures.get(i).seDeplacer(Direction.obtenirDirAlea());
				}
			}

			// attend X nombre de secondes
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			this.avertirLesObservers();
		}
	}

	/**
	 * méthode qui valide les règles du jeu
	 */
	private void validerEtatJeu() {
		if(!gestCombat.combatEstEnCours()){
			joueur.setMouvement(true);

			// verifie si le joueur est mort...
			if(!this.joueur.estVivant()){
				// oui, partie perdu
				partiePerdu();
			}

			// verifie si le joueur vient de trouver de l'équipement
			// (à compléter)
			ListIterator<AbstractEquipement> iterateur = equipements.listIterator();
			AbstractEquipement equipement;
			while (iterateur.hasNext()){ //on parcourt tous les equipements du jeu
				equipement=iterateur.next();
				//si on trouve un equipement au sol dont la case est la meme que celle du joueur
				if(joueur.getCase()==equipement.getCase() && equipement.estAusol()) 
					joueur.ramasserEquipement(equipement); //on ramasse cet equipement
			}

			// verifie s'il y a un combat
			for(int i=0;i<this.creatures.size();i++){
				if(this.creatures.get(i).estVivant()){
					if(this.joueur.getPos().equals(this.creatures.get(i).getPos())){
						//ajoute message � la console
						messageJoueurConsole.add("D�but de combat."); 
						// oui, fais la résolution du combat
						gestCombat.executerCombat(this.joueur,this.creatures.get(i));
						joueur.setMouvement(false);
					}
				}
			}

			// verifie si le joueur est sur la case finale...
			if(this.joueur.getCase() == donjon.getFin()){
				System.out.println(this.joueur.getCase());
				System.out.println(donjon.getFin());

				//ajoute message � la console
				messageJoueurConsole.add("Vous avez termin� le niveau "+getNiveau()+" en "+chronometreNiveau+" secondes."); 

				// oui, passe au prochain niveau
				partieGagne();
			}
		}
		this.avertirLesObservers();

	}

	/**
	 * méthode qui lance un nouveau niveau
	 */
	private void  nouveauNiveau(){

		System.out.println("nouveau niveau");

		//note le temps du d�but du niveau
		debutNiveau= Instant.now();
		// la partie est toujours en cours
		partieEnCours = true;
		// crée un nouveau donjon
		this.donjon = new Donjon();
		// initialise les créatures
		initCreatures();
		// initialise le joueur
		initJoueur();
		// initialise les equipements
		initEquipements();

		// si la tâche qui gère les créature
		// n'a pas encore été lancé, la lance.
		if(t ==null){
			t = new Thread(this);
			t.start();
		}

		this.avertirLesObservers();
	}

	/**
	 * informatrice pour savoir le niveau courant
	 */
	public int getNiveau(){
		return niveauCourant;
	}

	/**
	 * méthode qui gère une partie gagné
	 */
	private void partieGagne(){
		// incrémente le compteur de niveau
		niveauCourant++;

		// obtient les configs
		Configuration config = Configuration.getInstance();
		int nbCols = (int)config.getConfig(Configuration.NB_COLONNES);
		int nbLignes = (int)config.getConfig(Configuration.NB_LIGNES);
		int nbCreatures = (int)config.getConfig(Configuration.NB_CREATURES);
		// mets à jours les configs
		config.setConfig(Configuration.NB_COLONNES,nbCols+1);
		config.setConfig(Configuration.NB_LIGNES,nbLignes+1);
		config.setConfig(Configuration.NB_CREATURES,nbCreatures+2);

		// lance un nouveau niveau
		System.out.println("partie gagné");
		nouveauNiveau();
	}

	/**
	 * gestion d'une partie perdu
	 */
	private void partiePerdu(){

		// remise à zéro du jeu
		niveauCourant = 0;
		//remet � z�ro le nb de cr�atures tu�es lorsque la partie est termin�e
		gestCombat.setNbCreaturesTuees(0);

		joueur.remiseAZero();
		Configuration.remiseAZero();
		messageJoueurConsole.clear(); //efface tous les messages de la console

		nouveauNiveau();
	}

	public GestionnaireCombat getGestionnaireCombat(){
		return gestCombat;
	}

	/**
	 * Ajoute le message re�u en param�tre � la collection de message de la console
	 * @param msg, message � ajouter � la collection
	 */
	public void addMessageJoueurConsole(String msg) {
		this.messageJoueurConsole.add(msg);
	}

	/**
	 * M�thode pour obtenir les messages de la collection de messages de la console
	 * @return chaine de caracteres contenant les messages de la console avec des sauts de lignes
	 */
	public String getMessagesJoueur() {
		String message="";
		for(int i=0;i<messageJoueurConsole.size();i++)
			message+=messageJoueurConsole.get(i)+"\n";
		return message;
	}
}
