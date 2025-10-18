# TP Arbres Binaires et Images 🌳 🖼️

Ce TP est enseigné dans le cadre du cours de **PR**ogrammation **A**vancé en troisième année de licence à l'ISTIC.  
Le sujet de ce TP est disponible sur la page Moodle du cours ou [ici](./pra-tp-arbres-binaires-images.pdf).  

## Récupération du projet

Pour récupérer le projet, il suffit de le cloner sur votre machine avec la commande :
```bash
git clone https://gitlab2.istic.univ-rennes1.fr/pra/tp-arbres-images.git
```
Puis de l'ouvrir avec l'IDE de votre choix :
- **VSCode** (recommandé) : File > Open Folder... > [*votre dossier de travail*]/tp-arbres-images
- **Eclipse** : File > Import... > Maven > Existing Maven Projects > Browse... > [*votre dossier de travail*]/tp-arbres-images > Finish

Vous pouvez aussi faire un « fork » du projet sur votre compte Gitlab avant de le cloner.

## Utilisation du projet

### Interface graphique
Vous disposez d'une interface graphique pour manipuler les structures de données que vous développerez dans ce projet.
Pour la lancer, vous devez :
- **VSCode** : Ouvrir la classe [TpArbre.java](./src/main/java/fr/istic/pra/tp_arbres/TpArbre.java) > cliquer sur « run » au-dessus du `main` ligne 6 ou cliquer sur la flèche en haut à droite
- **Eclipse** : Clic droit sur la classe [TpArbre.java](./src/main/java/fr/istic/pra/tp_arbres/TpArbre.java) dans le Package Explorer > Run As > Java Application, ou bien ouvrir le fichier et cliquer sur l'icône Run.

### Utilitaire de benchmark

La classe [Benchmark.java](./src/main/java/fr/istic/pra/tp_arbres/Benchmark.java) fournit un programme mesurant les performances de l'implémentation des images par arbres binaires, et les compare avec une représentation matricielle.
