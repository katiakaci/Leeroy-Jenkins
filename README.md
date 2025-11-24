# Leeroy Jenkins

**Leeroy Jenkins** est un jeu d’exploration de donjon en Java, combinant génération procédurale, déplacements en labyrinthe, combat au tour par tour et gestion d’équipement.  
Le joueur incarne un héros déterminé à s’enfoncer toujours plus profondément dans un donjon sans fin, affrontant créatures, pièges et boss improvisés, tout en récoltant armes, armures et potions pour améliorer ses chances de survie.

<div align="center">
  <img width="1300" height="731" alt="Leeroy Jenkins" src="https://github.com/user-attachments/assets/012a31a8-e84d-4921-9149-fc327f1465a7" />
</div>


## Fonctionnement général

Chaque partie se déroule dans un donjon généré procéduralement à partir d’un algorithme inspiré des labyrinthes classiques.  
Le héros progresse case par case, découvre progressivement les corridors, combat les créatures qui rôdent, ramasse des équipements, et tente d’atteindre la sortie de chaque niveau.

Le jeu combine :

- **Exploration** : un labyrinthe généré dynamiquement  
- **Déplacement case par case**  
- **IA simple pour les créatures** (déplacements aléatoires)  
- **Combat intégré dans une fenêtre dédiée**  
- **Système d’équipement complet** (armures, armes, casques, potions)  
- **Interface utilisateur en Java Swing**  
- **Console interne affichant l’historique des événements**  
- **Patron de conception Observer** pour synchroniser modèle et interface  

## 🗺️ Donjon généré procéduralement

Le cœur du jeu est un générateur de labyrinthe construit sur un tableau 2D.  
Chaque élément du donjon est représenté par une *Case*, contenant :

- sa position  
- des références vers ses voisins  
- un état « découvert » ou non  
- un état « développé » lors de la génération  
- un marqueur indiquant si elle est la sortie du niveau  

Le labyrinthe est construit grâce à :

- sélection d’une case de départ  
- exploration aléatoire avec pile (approche “backtracking”)  
- création progressive des corridors  
- désignation finale d’une case comme **sortie**  

Le résultat : un donjon unique à chaque partie.

## 🧟‍♂️ Créatures et héros

Plusieurs types de créatures peuvent apparaître dans le donjon :

- **Araignées** : déplacement bondissant  
- **Minotaures** : puissants mais simples  
- **Dragons** : créatures dangereuses de haut niveau  

Elles héritent toutes d’une même classe de base assurant :

- position dans le donjon  
- gestion des déplacements  
- interaction avec la vue  
- interaction avec le joueur  

Le héros, quant à lui :

- se déplace via les flèches du clavier  
- peut être équipé  
- possède des points de vie, de défense et d’attaque  
- notifie la vue à chaque changement  


## 🛡️ Système d’équipement

Le jeu propose quatre types d’équipements :

- **Casques**  
- **Armures**  
- **Armes**  
- **Potions de soin**

### Le héros peut :

- ramasser automatiquement un équipement lorsqu’il marche dessus  
- porter un seul équipement de chaque type  
- utiliser une potion pour restaurer ses points de vie  
- voir ses statistiques recalculées automatiquement après chaque changement

L’interface montre :

- défense totale  
- attaque totale  
- objets équipés  
- objets ramassés (déroulants via JComboBox)  
- nombre de potions disponibles  


## ⚔️ Fenêtre de combat

Lorsqu’une créature touche le héros :

- une fenêtre de combat dédiée apparaît  
- le jeu principal est **bloqué** jusqu’à la fin du combat  
- les actions sont affichées dans une zone de texte déroulante  
- l’issue du combat met automatiquement à jour les points de vie  
- en cas de défaite : la partie repart depuis le début et l’inventaire est réinitialisé  

Cette fenêtre utilise :

- images du héros et de la créature  
- gestionnaire d’événements  
- système d’observation (pattern Observer)  

<img width="789" height="365" alt="image" src="https://github.com/user-attachments/assets/7b230d93-bcae-461f-8f71-752eea6c10c9" />

## 🎮 Contrôles

| Touche / Action | Description |
|----------------|-------------|
| **Flèches du clavier** | Déplacer le héros |
| **Collision avec créature** | Déclenche un combat |
| **Collision avec équipement** | Ramasse l’objet |
| **Bouton “Utiliser Potion”** | Restaure les PV si une potion est disponible |

