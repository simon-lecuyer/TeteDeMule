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

## Structure du projet
- Deamon (interface) : sur chaque client permmettant le téléchargement de fragment, accessible par socket
    - > public interface Daemon extends Remote {
      >
      >     public byte[] upload(String fileName, int fragmentBegin, int fragmentSize) throws Remote Exception;
      > }
    
- Downloader (interface) : sur les clients souhaitant télécharger en parallèle des fichiers
    - > public interace Downloader {
      > 
      >     public void download(String fileName, int fragmentSize);
      > }

- Diary (interface) : l'annuaire, accessible par RMI, permet d'enregister les fichiers des clients connectés
    - > public interface Diary extends Remote {
      > 
      >     public void createFile(String fileName, String username) throws RemoteException; 
      >     public void updateFile(String fileName, String[] usernames) throws RemoteException;
      >     public String[] getFileUsers(String fileName) throws Remote Exception;
      >     public void deleteFileUsers(String fileName, String[] usernames) throws RemoteException;
      > }