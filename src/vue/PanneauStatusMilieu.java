package vue;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ListIterator;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import equipements.AbstractEquipement;
import equipements.Arme;
import equipements.Armure;
import equipements.Casque;
import equipements.Potion;
import modele.PlanDeJeu;

public class PanneauStatusMilieu extends JPanel {

	//deux sous-panneaux ajoutés par composition:
	private JPanel panHero; //a gauche
	private JPanel panEquipement; //a droite

	//composants
	private JLabel defense, attaqueTot, nbPotion;
	private JComboBox<Casque> jComboBoxCasque;
	private JComboBox<Armure> jComboBoxArmure;
	private JComboBox<Arme> jComboBoxArme;
	private JButton usePotion;

	public PanneauStatusMilieu() {
		// définit le layout
		setLayout(new GridLayout(1,0));
		//BORDURE
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		//Ajouter panneau de droite et de gauche :
		add(panHeroGauche());
		add(panEquipementDroit());
	}

	/**
	 * Méthode qui crée le panneau de droite comprenant différents composants
	 * @return JPanel, le panneau droit créé
	 */
	public JPanel panEquipementDroit() {
		panEquipement=new JPanel();

		// définit le layout
		panEquipement.setLayout(new GridLayout(0,1));

		//i) Éléments de défense :
		//(1) JLabel montrant la défense totale
		defense=new JLabel(); 
		defense.setHorizontalAlignment(SwingConstants.LEFT); //aligné le texte à gauche
		panEquipement.add(defense);//ajouter le composant

		//(2) JLabel indiquant Casque
		JLabel casque=new JLabel("Casque: "); 
		casque.setHorizontalAlignment(SwingConstants.LEFT);//aligné le texte à gauche
		panEquipement.add(casque);//ajouter le composant

		//(3) JComboBox
		jComboBoxCasque= new JComboBox<>();
		jComboBoxCasque.addItemListener(new ItemChangeListener());
		panEquipement.add(jComboBoxCasque); //ajouter le composant

		//(4) JLabel indiquant Armure
		JLabel armure=new JLabel("Armure: ");
		armure.setHorizontalAlignment(SwingConstants.LEFT);//aligné le texte à gauche
		panEquipement.add(armure);//ajouter le composant

		//(5) JComboBox
		jComboBoxArmure= new JComboBox<>();
		jComboBoxArmure.addItemListener(new ItemChangeListener());
		panEquipement.add(jComboBoxArmure);//ajouter le composant

		//ii) Éléments d’attaque
		//(1) JLabel montrant l’attaque totale 
		attaqueTot=new JLabel();
		attaqueTot.setHorizontalAlignment(SwingConstants.LEFT);//aligné le texte à gauche
		panEquipement.add(attaqueTot);//ajouter le composant

		//(2) JLabel indiquant Arme
		JLabel arme=new JLabel("Arme: ");
		arme.setHorizontalAlignment(SwingConstants.LEFT);//aligné le texte à gauche
		panEquipement.add(arme);//ajouter le composant

		//(3) JComboBox 
		jComboBoxArme= new JComboBox<>();
		jComboBoxArme.addItemListener(new ItemChangeListener());
		panEquipement.add(jComboBoxArme);//ajouter le composant

		//iii) Éléments d’attaque
		//(1) JLabel indiquant le nombre de potions
		nbPotion=new JLabel();
		nbPotion.setHorizontalAlignment(SwingConstants.LEFT);//aligné le texte à gauche
		panEquipement.add(nbPotion);//ajouter le composant

		//(2) JButton indiquant “Utiliser Potion”. 
		usePotion = new JButton("Utiliser Potion");
		usePotion.setEnabled(false); //Le JButton est initialement inactif
		//creation ecouteur : un clic de souris sur le JButton appelle utiliserPotion()
		MouseListener ecouteurPotion = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				PlanDeJeu.getInstance().getJoueur().utiliserPotion();
			}
		};
		usePotion.addMouseListener(ecouteurPotion); //enregistrer ecouteur dans le JButton
		panEquipement.add(usePotion);//ajouter le composant

		return panEquipement;
	}

	/**
	 * Méthode qui crée le panneau de gauche comprenant différents composants
	 * @return JPanel, le panneau gauche créé
	 */
	public JPanel panHeroGauche() {
		panHero=new JPanel();
		//contient une image du héro		
		Image image=null;
		try {
			image=ImageIO.read(new File("images/hero.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		//ajouter l’image au JPanel
		panHero.add(new JLabel(new ImageIcon(new ImageIcon(image).getImage().getScaledInstance(200, 240, Image.SCALE_AREA_AVERAGING))));	
		
// Image trop zoomé sur certain ordi de cette façon :
//		BufferedImage image=null;
//		try {
//			image = ImageIO.read(new File("images/hero.png"));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		//ajouter l’image au JPanel
//		panHero.add(new JLabel(new ImageIcon(image)));	
		
		return panHero;
	}

	/**
	 * Méthode pour mettre à jour le panneau pendant le jeu
	 */
	public void mettreAJoursInfo() {
		this.defense.setText("Defense Totale = "+PlanDeJeu.getInstance().getJoueur().getArmure());
		this.attaqueTot.setText("Attaque Totale = "+PlanDeJeu.getInstance().getJoueur().getForce());

		//METTRE À JOUR LES JCOMBOBOX :
		//Vide les ComboBox
		this.jComboBoxCasque.removeAllItems();
		this.jComboBoxArmure.removeAllItems();
		this.jComboBoxArme.removeAllItems();

		//Crée un compteur de potions égal à zéro.
		int compteurPotion=0;

		//Pour chaque ComboBox, ajoute référence à l’Équipement équipé, s’il y en a un
		//ARME
		if(PlanDeJeu.getInstance().getJoueur().getArmeEquipe()!=null) 
			this.jComboBoxArme.addItem(PlanDeJeu.getInstance().getJoueur().getArmeEquipe());
		//ARMURE
		if(PlanDeJeu.getInstance().getJoueur().getArmureEquipe()!=null)
			this.jComboBoxArmure.addItem(PlanDeJeu.getInstance().getJoueur().getArmureEquipe());
		//CASQUE
		if(PlanDeJeu.getInstance().getJoueur().getCasqueEquipe()!=null)
			this.jComboBoxCasque.addItem(PlanDeJeu.getInstance().getJoueur().getCasqueEquipe());

		//Obtient une référence à la liste d’équipement: joueur.getEquipements()
		ListIterator<AbstractEquipement> iterateur = PlanDeJeu.getInstance().getJoueur().getEquipements().listIterator();
		AbstractEquipement equip;

		//on parcourt la liste des equipements ramassés
		while (iterateur.hasNext()) { 
			equip=iterateur.next();
			//ajouter l'équipement au ComboBox auquel il appartient
			if(equip instanceof Armure)
				this.jComboBoxArmure.addItem((Armure)equip);
			if(equip instanceof Arme)
				this.jComboBoxArme.addItem((Arme)equip);
			if(equip instanceof Casque)
				jComboBoxCasque.addItem((Casque)equip);;
				if(equip instanceof Potion)
					compteurPotion++; //incremente compteur s’il s’agit d’une potion
		}

		//Affiche le nombre de potions
		this.nbPotion.setText("Nombre potions: "+compteurPotion);

		//Activation JButton s'il y a des potions
		if(compteurPotion>0)//Si le nombre de potions est plus grand que 0
			usePotion.setEnabled(true); //activer le JButton
		else
			usePotion.setEnabled(false); //désactiver le bouton			
	}

	//listeners pour les JComboBox
	private class ItemChangeListener implements ItemListener{
		public void itemStateChanged(ItemEvent event) {
			if (event.getStateChange() == ItemEvent.SELECTED) {
				Object item = event.getItem();
				PlanDeJeu.getInstance().getJoueur().equiper((AbstractEquipement) item);
			}
		}
	}

}