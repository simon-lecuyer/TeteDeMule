## Principe du projet
- Un client peut télécharger en parallèle plusieurs fichiers depuis différetns sources
- Annuaire : 
    - Enregistrer les clients et les fichiers qu'ils possèdent
    - Fichier : nom
    - Donner la liste des clients possédant un fichier
- Client :
    - Enregistrer les fichiers qu'il possède et télécharge
    - Permettre le téléchargement fragmenté des fichiers
    - Télécharger un fichier depuis les clients

## Lancement d'un projet
- A la racine *TeteDeMule/* : 
    - Créer un dossiser *Available/* et mettre les fichiers à disposition de téléchargement (est créé vide automatiquement au lancement de TeteDeMule)
    - Optionnel : créer un dossier *Download/* pour récupérer les fichiers à télécharger (est créé automatiquement s'il n'existe pas)
- Se place dans dans le dossier bin : *TeteDeMule/bin/* :
    - Exécuter la commande : `java DiaryImpl <nom du Diary>`, initialise le Diary sur le port 4000 --> **/name:4000/diary**
    - Dans un nouveau terminal : `java TeteDeMule`, lance l'application pour le client. l'utilisateur doit rentrer ensuite : le *nom du diary* et le *port de communication*. L'utilisateur est représenté par son *hostname*. Dans le Diary l'utilisateur est stocké :  **ip:port**