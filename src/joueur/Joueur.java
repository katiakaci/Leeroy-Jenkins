package joueur;

import java.util.ListIterator;

/**
 * DÃ©finition du Joueur.
 * 
 * @author Fred Simard | ETS
 * @version ETE 2018 - TP2
 */

import java.util.Vector;

/**
 * Cette classe reprÃ©sente le joueur humain. Elle surcharge le
 * personnage abstrait pour le dÃ©placement et propose une mÃ©thode
 * pour mettre Ã  jours la visibilitÃ© des cases en fonction de la vision.
 * 
 * @author Fred Simard | ETS
 * @version ETE 2018 - TP2
 */

import dongon.Case;
import equipements.AbstractEquipement;
import equipements.Arme;
import equipements.Armure;
import equipements.Casque;
import equipements.Potion;
import modele.PlanDeJeu;
import personnage.AbstractPersonnage;
import physique.Direction;
import physique.Position;

public class Joueur extends AbstractPersonnage {

	private final int PROFONDEUR_VISION = 2;
	private boolean mouvement = true;
	
	//collection qui contiendra tous les équipements ramassés par le joueur.
	private Vector <AbstractEquipement> equipementRamasses =new Vector<>();

	//ajouter 3 variables membres, références au casque, à l’armure et à l’arme
	private Casque casqueEquipe;
	private Arme armeEquipe;
	private Armure armureEquipe;

	/**
	 * Constructeur par paramètre
	 * @param pos, position du joueur
	 */
	public Joueur() {
		pointDeVie=100;
		pointDeVieMax=100;
	}

	/**
	 * surcharge de la mÃ©thode pour dÃ©placer le joueur dans la direction donnÃ©e
	 * @param direction(int), direction du mouvement
	 */
	public void seDeplacer(int direction){

		if(mouvement) {
			// se dÃ©placer
			super.seDeplacer(direction);

			// mise Ã  jour de la vision
			mettreAJourVision();
		}
	}

	/**
	 * surcharge de la mÃ©thode pour placer le joueur Ã  sa case de dÃ©part
	 * @param caseCourante(Case), case courante
	 */
	public void setCase(Case caseCourante){

		// assigne la case
		super.setCase(caseCourante);

		// mise Ã  jour de la vision
		mettreAJourVision();
	}

	/**
	 * mÃ©thode qui mets Ã  jour la vision
	 */
	private void mettreAJourVision(){

		// rend visible la case courante
		super.caseCourante.setDecouverte(true);

		// dans toutes les directions
		for(int i=0;i<Direction.NB_DIRECTIONS;i++){

			// dÃ©voile les voisins jusqu'Ã  la profondeur de la vision
			Case voisin = super.caseCourante.getVoisin(i);
			for(int j=0;j<PROFONDEUR_VISION;j++){
				if(voisin!=null){
					voisin.setDecouverte(true);
					voisin = voisin.getVoisin(i);
				}
			}
		}
	}

	public void setMouvement(boolean etat){
		this.mouvement = etat;
	}


	/**
	 * Remise Ã  zÃ©ro du joueur
	 * - remet les points de vie Ã  max
	 * - vide Ã©quipement
	 */
	public void remiseAZero(){
		this.pointDeVie = this.pointDeVieMax;

		//vider la liste d’équipement ramassés
		equipementRamasses.clear();

		//enlever l’équipement équipé
		casqueEquipe=null;
		armeEquipe=null;
		armureEquipe=null;

		//appel à equiper(null) pour régénérer les calculs
		equiper(null);
	}

	/**
	 * Ramasser une pièce d'équipement
	 * - indique que la piece à ramasser n'est plus au sol
	 * - pièce ramassé ajouté à la collection d'équipement du joueur
	 * @param equipementARamasser(AbstractEquipement), pièce à ramasser
	 */
	public void ramasserEquipement(AbstractEquipement equipementARamasser) {
		//La mutatrice de l’équipement est appelée pour indiquer qu’il n’est plus au sol.
		equipementARamasser.setAuSol(false);
		//La référence à l’équipement est ajoutée à la collection
		equipementRamasses.add(equipementARamasser);
		//ajoute message à la console
		PlanDeJeu.getInstance().addMessageJoueurConsole("Vous avez trouvé un équipement."); 
	}

	// méthode pour obtenir une référence sur la collection d’équipements
	public Vector<AbstractEquipement> getEquipements(){
		return this.equipementRamasses;
	}
	//méthode retournant une référence au casque équipé
	public Casque getCasqueEquipe() {
		return this.casqueEquipe;
	}
	//cette méthode retourne une référence à l’armure équipé
	public Armure getArmureEquipe() {
		return this.armureEquipe;
	}
	//cette méthode retourne une référence à l'arme équipé
	public Arme getArmeEquipe() {
		return this.armeEquipe;
	}

	/**
	 * Equiper une pièce d’équipement reçu en paramètre
	 * @param equipement(AbstractEquipement), pièce à équiper
	 */
	public void equiper(AbstractEquipement equipement) {
		//identification du type de la pièce d’équipement (instanceof) 
		//les seuls types d’équipements à identifier sont: Casque, Armure, Arme.
		//change l’équipement équipé en question
		if(equipement instanceof Arme ){
			armeEquipe = (Arme)equipement;
		}
		if (equipement instanceof Casque) {
			casqueEquipe= (Casque)equipement; 
		}
		if (equipement instanceof Armure) {
			armureEquipe = (Armure)equipement;
		}

		//DEFENSE (ARMURE ET CASQUE)
		//remet la variable membre armure à 0
		this.armure=0;

		//assigne à armure la somme des valeurs obtenues des équipements de défense
		//{armure et casque}. Un équipement non équipé ne compte pas.		
		int bonusArmure=0;
		int bonusCasque=0;
		
		if(this.armureEquipe!=null)
			bonusArmure = this.armureEquipe.getValeur();
		if(this.casqueEquipe!=null) 
			bonusCasque = this.casqueEquipe.getValeur();
		
		this.armure= bonusArmure+bonusCasque;
		
		//remet la variable membre bonusAttaque à 0
		this.bonusAttaque=0;

		//assigne à bonusAttaque la valeur de l’arme équipé, s’il y en a une
		if(armeEquipe!=null)
			this.bonusAttaque=armeEquipe.getValeur();
	}

	/**
	 * - Trouve première potion dans la collection d’équipement,
	 * - l’enlève 
	 * - remet les points de vie à point de vie max.
	 */
	public void utiliserPotion() {		
		ListIterator<AbstractEquipement> iterateur = equipementRamasses.listIterator();
		AbstractEquipement equip;
		boolean trouvee=false; //boolean si 1ere potion trouvée (true) ou non (false)

		//on parcourt la liste des equipements ramassés tant qu'on a pas trouvé de potion
		while (iterateur.hasNext() && !trouvee){ 
			equip=iterateur.next();
			if(equip instanceof Potion) { //si on trouve une potion
				equipementRamasses.remove(equip); //on enleve la potion de la collection
				//remet les points de vie à point de vie max
				pointDeVie=pointDeVieMax;
				trouvee=true;
			}		
		}
		//ajoute message à la console
		PlanDeJeu.getInstance().addMessageJoueurConsole("Vous avez utilisé une potion."); 
	}	
}