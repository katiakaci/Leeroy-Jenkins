package vue;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import modele.PlanDeJeu;
import observer.MonObserver;

public class PanneauStatusBas extends JPanel {

	private JTextArea textArea;

	public PanneauStatusBas() {

		textArea=new JTextArea(12,42);//crée région de texte de 12 lignes & 42 colonnes
		textArea.setEditable(false); //rendre non-éditable				
		
		//crée un JScroll et lui attacher la région de texte	
		JScrollPane scroll = new JScrollPane(textArea);	
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);		
		add(scroll);//ajouter le JScroll au JPanel	
	}
	
	/**
	 * Méthode pour mettre à jour le panneau pendant le jeu
	 */
	public void mettreAJoursInfo() {
		this.textArea.setText(PlanDeJeu.getInstance().getMessagesJoueur());
	}
}