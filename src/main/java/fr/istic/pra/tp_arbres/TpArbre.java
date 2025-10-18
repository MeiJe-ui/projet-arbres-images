package fr.istic.pra.tp_arbres;

import fr.istic.pra.tp_arbres.gui.ImageViewer;

/**
 * Classe principale pour lancer l'application TpArbre.
 * @author Vincent Drevelle <vincent.drevelle@univ-rennes.fr>
 * @version 17 octobre 2025
 * 
 * Affiche une interface graphique permettant de visualiser et manipuler des images
 * représentées sous forme d'arbres binaires (TreeImage). Permet de charger des images,
 * d'appliquer diverses transformations et de visualiser les résultats.
 * Les images bitmap (BitmapImage) sont également prises en charge.
 */
public class TpArbre {
    public static void main(String[] args) {
        ImageViewer viewer = new ImageViewer();
        viewer.display();
    }
}
