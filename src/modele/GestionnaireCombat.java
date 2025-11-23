package modele;

/**
 * Gestionnaire des combats prenant place entre le hero et les
 * creatures du donjon.
 * 
 * Le gestionnaire lance un pop-up window qui affiche les dÃ©tails du combat.
 * Le combat s'exÃ©cute dans le gestionnaire qui gÃ©nÃ¨re une liste de messages.
 * La fenÃªtre agit comme observer et affiche les messages gÃ©nÃ©rÃ©s.
 * 
 * @author Fred Simard | ETS
 * @version ETE 2018 - TP3
 */

import java.util.ArrayList;

import creature.AbstractCreature;
import joueur.Joueur;
import vue.FenetreCombat;
import observer.MonObservable;
import personnage.AbstractPersonnage;

public class GestionnaireCombat extends MonObservable implements Runnable{

	private final int TEMP_DELAI = 500;

	private Thread combatFrameThread;
	private AbstractPersonnage hero, creature;
	private boolean combatEnCours = false;
	private ArrayList<String> messages = new ArrayList<String>();
	//compteur du nombre de créatures vaincues
	private int nbCreaturesTuees;

	/**
	 * MÃ©thode qui lance la fenetre pop-up, et la tÃ¢che qui exÃ©cute le combat
	 * @param hero, le hero du donjon
	 * @param creature, la creature avec qui il y a un combat 
	 */
	public void executerCombat(Joueur hero, AbstractCreature creature) {

		// efface les messages
		messages.clear();

		// indique qu'un combat est en cours (suspend l'exÃ©cution du donjon)
		combatEnCours = true;

		// copie les rÃ©fÃ©rences
		this.hero = hero;
		this.creature = creature;

		// lance le pop-up window
		//ajouter l’appel au constructeur du JFrame
		FenetreCombat fenetre = new FenetreCombat(hero, creature, this);
		//attacher le JFrame comme un observer du gestionnaireDeCombat
		this.attacherObserver(fenetre);

		// lance la tÃ¢che qui gÃ¨re le combat
		Thread t = new Thread(this);
		t.start();
	}

	/**
	 * TÃ¢che qui exÃ©cute le combat
	 * Boucle tant que les deux protagoniste sont vivant. Alterne les coups
	 * donnÃ©es entre le hero et la creature.
	 */
	public void run() {

		do{
			// hero donne le premier coup
			int forceCoupDonne = hero.getForce();
			creature.recoitCoup(forceCoupDonne);

			// ajoute les messages et mets Ã  jours la fenetre
			messages.add("Joueur donne un coup: " + forceCoupDonne);
			messages.add("Point de vie creature: " + creature.getPointDeVie());
			this.avertirLesObservers();

			// attend un peu
			try {
				Thread.sleep(TEMP_DELAI);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// si la creature est toujours vivante
			if(creature.estVivant()){

				// creature donne un coup
				forceCoupDonne = creature.getForce();
				hero.recoitCoup(forceCoupDonne);

				// ajoute les messages
				messages.add("Creature donne un coup: " + forceCoupDonne);
				messages.add("Point de vie joueur: " + hero.getPointDeVie());
				this.avertirLesObservers();
			}else{

				// si la creature est morte, c'est la fin du combat
				this.nbCreaturesTuees++; //incrémente le nombre de créatures tuées
				messages.add("Creature vaincu");
				//ajoute message à la console
				PlanDeJeu.getInstance().addMessageJoueurConsole("Vous avez gagné ce combat.");
				this.avertirLesObservers();
			}

			// attend un peu
			try {
				Thread.sleep(TEMP_DELAI);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// si le hero est mort
			if(!hero.estVivant()){
				// ajoute un message
				messages.add("Joueur vaincu");
				//ajoute message à la console
				PlanDeJeu.getInstance().addMessageJoueurConsole("Partie terminée");
				this.avertirLesObservers();
			}	

			// boucle tant que le hero et la creature sont vivants
		}while(hero.estVivant() && creature.estVivant());
	}

	/**
	 * méthode pour obtenir une référence sur le nombre de créature tuées
	 * @return nbCreaturesTuees, nombre de créatures tuées dans la partie
	 */
	public int getNbCreaturesTuees() {
		return this.nbCreaturesTuees;
	}

	/**
	 * méthode pour assigné la valeur du paramètre recu au nombre de créature tuées
	 */
	public void setNbCreaturesTuees(int nbCreaturesTuees) {
		this.nbCreaturesTuees=nbCreaturesTuees;
	}

	/**
	 * informatrice pour savoir s'il y a un combat en cours.
	 * @return true/false, indiquant si un combat est en cours
	 */
	public boolean combatEstEnCours(){
		return combatEnCours;
	}

	/**
	 * mutatrice pour indiquer que le combat est termine
	 */
	public void combatTermine(){
		combatEnCours = false;
	}

	/**
	 * methode pour obtenir la liste des messages du combat
	 * @return chaine de caracteres contenant tous les messages avec des sauts de lignes
	 */
	public String getMsg(){

		String str = "";
		for (int i = 0; i < messages.size(); ++i){
			str += messages.get(i) + "\n";
		}
		return str;
	}
}