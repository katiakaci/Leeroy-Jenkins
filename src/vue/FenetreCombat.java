package vue;

import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import creature.AbstractCreature;
import creature.Araigne;
import creature.Dragon;
import creature.Minotaure;
import joueur.Joueur;
import modele.GestionnaireCombat;
import modele.PlanDeJeu;
import observer.MonObserver;

//classe héritant de JFrame et implémentant l’interface MonObserver.
public class FenetreCombat extends JFrame implements MonObserver{

	private Joueur hero;	
	private AbstractCreature creatureCombattu; 
	private GestionnaireCombat gestionnaireCombat;
	private JTextArea texte;
	private JScrollPane scroll;
	private JPanel panneauPrincipal;

	/**constructeur
	 * 
	 * @param hero
	 * @param creatureCombattu
	 * @param gestionnaireCombat
	 */
	public FenetreCombat(Joueur hero, AbstractCreature creatureCombattu, GestionnaireCombat gestionnaireCombat ) {
		this.hero=hero;
		this.creatureCombattu=creatureCombattu;
		this.gestionnaireCombat=gestionnaireCombat;

		//appels aux sous-programmes :
		configurationFrame();	
		configurationImageHero();	
		configurationBoiteMessage();	
		configurationImageCreature();	

		//le JFrame fait un appel à requestFocus() et setVisible(true).
		requestFocus();
		setVisible(true);
	}

	//ajout de la fonctionnalité MonObserver:
	//La méthode avertir doit aller chercher les messages généré par le
	//gestionnaire de combat et les afficher dans la région de texte.
	public void avertir() {
		texte.setText(gestionnaireCombat.getMsg());;
	}

	/** 
	 * configuration du frame 
	 * */
	public void configurationFrame() {
		//initialiser la référence au JPanel, à l’aide d’un appel à getContentPane().
		panneauPrincipal=(JPanel)getContentPane();

		//définir la position de la fenêtre à 600,300 & taille de la fenêtre à 800,400
		setBounds(600, 300, 800, 400);

		//définir un GridLayout(0,3)
		GridLayout gridLayout = new GridLayout(0,3);
		panneauPrincipal.setLayout(gridLayout);

		//ajouter un WindowListener, qui lors d’un événement de type windowClosing, 
		//fait un appel à gestionCombat.combatTermine()
		addWindowListener(new WindowAdapter() { 
			public void windowClosing(WindowEvent e) {
				gestionnaireCombat.combatTermine();
			}
		});
	}

	public void configurationImageHero()  {
		BufferedImage image;
		try {
			image = ImageIO.read(new File("images/hero.png"));
			add(new JLabel(new ImageIcon(image)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/** 
	 * configuration de la boîte de message
	 * */
	public void configurationBoiteMessage() {
		texte= new JTextArea(16,20); //crée région de texte de 16 lignes & 20 colonnes
		texte.setEditable(false); //rendre non-éditable
		scroll = new JScrollPane(texte);//crée un JScroll et lui attacher la région de texte
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		panneauPrincipal.add(scroll); //ajouter le JScroll au JPanel			
	}

	/** 
	 * configuration image créature
	 */
	public void configurationImageCreature() {
		//Choisir l’image en fonction du type de créature combattu.
		BufferedImage imageCreature=null;
		if(creatureCombattu instanceof Dragon)
			try {
				imageCreature = ImageIO.read(new File("images/dragon.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		if(creatureCombattu instanceof Araigne)
			try {
				imageCreature = ImageIO.read(new File("images/spider.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		if(creatureCombattu instanceof Minotaure)
			try {
				imageCreature = ImageIO.read(new File("images/minotaur.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		//ajouter l’image au JPanel
		panneauPrincipal.add(new JLabel(new ImageIcon(imageCreature)));
	}	
}