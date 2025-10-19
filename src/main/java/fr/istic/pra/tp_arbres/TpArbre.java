package fr.istic.pra.tp_arbres;

import fr.istic.pra.tp_arbres.gui.ImageViewer;

/**
 * Classe principale pour lancer l'application TpArbre.
 * 
 * Affiche une interface graphique permettant de visualiser et manipuler des images
 * représentées sous forme d'arbres binaires (TreeImage). Permet de charger des images,
 * d'appliquer diverses transformations et de visualiser les résultats.
 * Les images bitmap (BitmapImage) sont également prises en charge.
 * 
 * @author Vincent Drevelle
 * @version 1.02, 2025-10-17
 */
public class TpArbre {
    /**
     * Point d'entrée principal de l'application.
     * @param args arguments de la ligne de commande (non utilisés)
     */
    public static void main(String[] args) {
        ImageViewer viewer = new ImageViewer();
        viewer.display();
    }
}
