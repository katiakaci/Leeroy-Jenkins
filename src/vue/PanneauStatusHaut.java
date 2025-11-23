package vue;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import modele.PlanDeJeu;

public class PanneauStatusHaut extends JPanel {

	//composants du panneau
	private JProgressBar niveauVie;
	private JLabel niveau;
	private JLabel ennemisTues;
	private JLabel tempsJeu;

	public PanneauStatusHaut() {
		// initialise les composantes
		initComposantes();
	}

	public void initComposantes() {
		// définit le layout
		setLayout(new GridLayout(0,1));

		//BORDURE
		setBorder(BorderFactory.createLineBorder(Color.BLACK));

		//NOM
		JLabel nom=new JLabel("Leeroy Jenkins");
		//créer un font : en italique (italic), caractère gras(bold) et taille 24
		Font font=new Font("", Font.BOLD+Font.ITALIC, 24);
		//appliquer ce font au nom
		nom.setFont(font);
		//centré le texte
		nom.setHorizontalAlignment(JLabel.CENTER);
		//ajouter le composant
		add(nom);

		//NIVEAU VIE HERO
		niveauVie = new JProgressBar(0,PlanDeJeu.getInstance().getJoueur().getPointDeVieMax());
		niveauVie.setValue(PlanDeJeu.getInstance().getJoueur().getPointDeVie()*100/PlanDeJeu.getInstance().getJoueur().getPointDeVieMax());
		//couleur d'avant-plan (foreground) vert
		niveauVie.setForeground(Color.GREEN);
		//couleur d'arrière plan (background) rouge
		niveauVie.setBackground(Color.RED);
		//afficher le % de vie
		niveauVie.setStringPainted(true);
		//ajouter le composant
		add(niveauVie);

		//NUMERO DU NIVEAU
		niveau=new JLabel();
		//centré le texte
		niveau.setHorizontalAlignment(JLabel.CENTER);
		//ajouter le composant
		add(niveau);

		//NOMBRE D'ENNEMIE TUÉ
		ennemisTues=new JLabel();
		//centré le texte
		ennemisTues.setHorizontalAlignment(JLabel.CENTER);
		//ajouter le composant
		add(ennemisTues);

		//TEMPS DE JEU
		tempsJeu=new JLabel();
		//centré le texte
		tempsJeu.setHorizontalAlignment(JLabel.CENTER);
		//ajouter le composant
		add(tempsJeu);
	}

	/**
	 * Méthode pour mettre à jour le panneau pendant le jeu
	 */
	public void mettreAJoursInfo() {
		this.niveau.setText("Niveau : "+PlanDeJeu.getInstance().getNiveau());
		this.niveauVie.setValue(PlanDeJeu.getInstance().getJoueur().getPointDeVie());
		this.ennemisTues.setText("Nombre d'ennemis tués : "+PlanDeJeu.getInstance().getGestionnaireCombat().getNbCreaturesTuees());
		this.tempsJeu.setText("Temps de jeu du niveau : "+PlanDeJeu.getInstance().getChronometreNiveau()+" secondes");
	}
}
