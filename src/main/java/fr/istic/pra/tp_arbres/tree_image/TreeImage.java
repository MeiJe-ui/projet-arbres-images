package fr.istic.pra.tp_arbres.tree_image;

import java.util.function.Supplier;

import fr.istic.pra.tp_arbres.interfaces.Image;
import fr.istic.pra.tp_arbres.interfaces.ImageOperationsByPixel;
import fr.istic.pra.tp_arbres.interfaces.DrawableByPixel;

import fr.istic.pra.util.BinaryTree.NodeType;
import fr.istic.pra.util.*;

/**
 * Classe décrivant les images en noir et blanc de 256 sur 256 pixels sous forme d'arbres binaires.
 * <p>
 * Chaque image est représentée par un arbre binaire dont les nœuds contiennent des instances de la classe {@link Node}.
 * <p>
 * La racine de l'arbre représente l'image entière (256x256 pixels).
 * Chaque nœud double a la valeur 2, et divise la région qu'il représente en deux sous-régions :
 * <ul>
 *   <li>Si le nœud est à un niveau pair (0, 2, 4, ...), la région représentée est carrée, et la division se fait verticalement : fils gauche correspondant à la moitié haute et fils droit à la moitié basse.</li>
 *   <li>Si le nœud est à un niveau impair (1, 3, 5, ...), la région représentée est rectangulaire, et la division se fait horizontalement : fils gauche correspondant à la moitié gauche et fils droit à la moitié droite.</li>
 * </ul>
 * Chaque feuille de l'arbre représente une région uniforme de l'image (tout noir ou tout blanc).
 * @hidden
 * @author Vincent Drevelle
 * @version 1.02, 2025-10-17
 */

public class TreeImage extends TreeImageBase implements Image, DrawableByPixel, ImageOperationsByPixel
{
	/** Arbre binaire représentant l'image */
	private BinaryTree<Node> tree;

	/** Usine pour créer de nouveaux arbres binaires */
	private final Supplier<BinaryTree<Node>> treeFactory;


	/**
	 * Constructeur par défaut, crée une image toute noire.
	 * 
	 * Par défaut, utilise BinaryTreeImplProf comme implémentation de l'arbre binaire.
	 * Quand vous aurez terminé l'implémentation de BinaryTreeImpl, vous pourrez l'utiliser à la place,
	 * en commentant la ligne correspondante et en décommentant l'autre.
	 */
	public TreeImage() {
		/* TODO: Selectionner votre implémentation d'arbre binaire quand vous l'aurez terminée */
		this(BinaryTreeImplProf::new);
		// this(BinaryTreeImpl::new);
	}

	/**
	 * Crée une nouvelle instance de BinaryTree<Node> en utilisant l'usine d'arbres binaires.
	 * 
	 * Utilisez toujours cette méthode pour créer une nouvelle instance de BinaryTree<Node>,
	 * afin de garantir que la même implémentation de l'arbre binaire est utilisée.
	 * @return une nouvelle instance de BinaryTree<Node>
	 */
	private BinaryTree<Node> createNewTree() {
		return treeFactory.get(); // On utilise l'usine pour créer une nouvelle instance d'arbre binaire
	}

	/**
	 * Constructeur avec choix de l'implémentation de l'arbre binaire.
	 * 
	 * Usage : new TreeImage(BinaryTreeImpl::new) ou new TreeImage(BinaryTreeImplProf::new)
	 * @param treeFactory  usine pour créer des instances de {@link BinaryTree<Node>}
	 */
	public TreeImage(Supplier<BinaryTree<Node>> treeFactory) {
		super();
		this.treeFactory = treeFactory; // On mémorise l'usine pour créer des arbres
		this.tree = createNewTree(); // On utilise l'usine pour créer l'arbre
		this.tree.createRootWithValue(Node.valueOf(0)); // Initialisation avec un nœud racine à 0 (tout noir)
	}

	/**
	 * Crée une nouvelle instance de TreeImage avec la même usine d'arbres binaires.
	 * 
	 * Utilisez toujours cette méthode pour créer une nouvelle instance de TreeImage,
	 * afin de garantir que la même implémentation de l'arbre binaire est utilisée.
	 * @return une nouvelle instance de TreeImage
	 */
	protected TreeImage createTreeImage() {
		return new TreeImage(treeFactory);
	}

	// Getter pour l'arbre
	@Override
	public BinaryTree<Node> getTree() {
		return tree;
	}

	/** 
	 * Crée une copie de l'image courante (clone).
	 * 
	 * La méthode est implémentée ici en utilisant affect (que vous devez implémenter plus loin dans le code)
	 * et createTreeImage (pour créer une nouvelle instance de TreeImage avec la même usine d'arbres binaires).
	 * 
	 * @return une copie de l'image courante
	 **/
	@Override
	public TreeImage copy() {
		TreeImage result = createTreeImage();
		result.affect(this);
		return result;
	}

	/**
	 * Remplit toute l'image avec la couleur spécifiée
	 * @param color couleur de remplissage (0 ou 1)
	 */
	@Override
	public void fill(int color) {
		/* TODO: À vous de compléter ! (en attendant, on fait planter) */
		throw new UnsupportedOperationException("À vous de l'implémenter");
	}

	/** 
	 * Teste si un pixel est allumé 
	 * @param x  abscisse du pixel (0 à 255)
	 * @param y  ordonnée du pixel (0 à 255)
	 * @return true si le pixel est allumé (blanc), false sinon (noir)
	 */
	@Override
	public boolean isPixelOn(int x, int y) {
		/* TODO: À vous de compléter ! (en attendant, on fait planter) */
		throw new UnsupportedOperationException("À vous de l'implémenter");
	}


	//
	// Méthodes modifiant l'image courante en place, avec une autre image
	//

	/**
	 * Affecter le contenu d'une autre image à cette image
	 * La méthode appelle une implémentation optimisée (que vous devez implémenter) si l'autre image est aussi une TreeImage.
	 * @param image  l'image source
	 */
	@Override
	public void affect(Image image) {
		if (image instanceof TreeImage image2) {
			affect(image2); // On utilise l'implémentation optimisée pour les arbres si l'autre image est une TreeImage
		} else {
			ImageOperationsByPixel.super.affect(image); // Sinon, on utilise l'implémentation par défaut (pixel par pixel)
		}
	}

	/** 
	 * Affecter le contenu d'une autre TreeImage à cette image
	 * @param image2  l'image source (sous forme d'arbre binaire)
	 */
	public void affect(TreeImage image2) {
		/* TODO: À vous de compléter ! (en attendant, on fait planter) */
		throw new UnsupportedOperationException("À vous de l'implémenter");
	}


	/** 
	 * Intersection de deux images (priorité aux pixels noirs) 
	 * La méthode appelle une implémentation optimisée (que vous devez implémenter) si l'autre image est aussi une TreeImage.
	 * @param image  l'image à intersecter avec l'image courante
	 */
	@Override
	public void intersection(Image image) {
		if (image instanceof TreeImage treeImage) {
			intersection(treeImage); // On utilise l'implémentation optimisée pour les arbres si l'autre image est une TreeImage
		} else {
			ImageOperationsByPixel.super.intersection(image); // Sinon, on utilise l'implémentation pixel par pixel par défaut
		}
	}

	/** 
	 * Intersection de deux TreeImage (priorité aux pixels noirs)
	 * Un pixel resultat est blanc si le pixel correspondant est blanc dans les deux images.
	 * @param image  l'image à intersecter avec l'image courante
	 */
	public void intersection(TreeImage image) {
		/* TODO: À vous de compléter ! (en attendant, on fait planter) */
		throw new UnsupportedOperationException("À vous de l'implémenter");
	}




	//
	// Méthodes de transformation d'images retournant une nouvelle image
	//

	/**
	 * Rotation de l'image de 180 degrés
	 * @return une nouvelle image correspondant à l'image courante tournée de 180 degrés
	 */
	@Override
	public TreeImage rotated180() {
		/* TODO: À vous de compléter ! (en attendant, on fait planter) */
		throw new UnsupportedOperationException("À vous de l'implémenter");
	}


	/**
	 * Inversion des couleurs de l'image
	 * @return une nouvelle image correspondant à l'inversion des couleurs de l'image courante
	 * 
	 * Un pixel noir devient blanc, un pixel blanc devient noir.
	 */
	@Override
	public TreeImage inverted() {
		/* TODO: À vous de compléter ! (en attendant, on fait planter) */
		throw new UnsupportedOperationException("À vous de l'implémenter");
	}


	/** 
	 * Symétrie horizontale de l'image 
	 * La gauche devient la droite et la droite devient la gauche.
	 * @return une nouvelle image correspondant au retournement horizontal de l'image courante
	 */
	@Override
	public TreeImage flippedHorizontal() {
		/* TODO: À vous de compléter ! (en attendant, on fait planter) */
		throw new UnsupportedOperationException("À vous de l'implémenter");
	}



	/**
	 * Rotation de l'image de 90 degrés dans le sens des aiguilles d'une montre (vers la droite)
	 * @return une nouvelle image correspondant à l'image courante tournée de 90 degrés dans le sens des aiguilles d'une montre
	 */
	@Override
	public TreeImage rotatedClockwise90() {
		/* TODO: À vous de compléter ! (en attendant, on fait planter) */
		throw new UnsupportedOperationException("À vous de l'implémenter");
	}




	//
	// Prédicats sur l'image
	//

	/** 
	 * Test de la diagonale principale
	 * La diagonale principale correspond aux pixels (i,i) pour i allant de 0 à 255.
	 * 
	 * L'utilisation de isPixelOn dans cette méthode est interdite pour des raisons de performance.
	 * 
	 * @return true si tous les pixels de la diagonale sont allumés (blancs), false sinon
	 */
	@Override
	public boolean testDiagonal() {
		/* TODO: À vous de compléter ! (en attendant, on fait planter) */
		throw new UnsupportedOperationException("À vous de l'implémenter");
	}




	///////////////////////////////////////////////////////////////////////////////////////////////
	// Méthodes pour le dessin pixel par pixel
	//
	// Comme TreeImage implémente DrawableByPixel, en implémentant drawPixel et fillRect, vous avez
	// automatiquement une implémentation pixel par pixel de drawLine, drawCircle, drawRect, etc.
	//
	// Les méthodes pixel par pixel permettent aussi l'intéropérabilité des opérations avec des images
	// représentées par d'autres structures de données (comme BitmapImage) : affect, intersection, union, etc.

	/**
	 * Dessine un pixel dans l'image avec la couleur spécifiée.
	 * 
	 * La méthode est implémentée ici en utilisant fillRect (que vous devez implémenter plus loin dans le code),
	 * car dessiner un pixel revient à dessiner un rectangle de 1x1 pixel.
	 * 
	 * @param x  abscisse du pixel
	 * @param y  ordonnée du pixel
	 * @param color couleur du pixel (0 ou 1)
	 */
	@Override
	public void drawPixel(int x, int y, int color) {
		fillRect(x, y, 1, 1, color);
	}

	/**
	 * Remplit un rectangle dans l'image avec la couleur spécifiée.
	 * 
	 * La méthode appelle une implémentation récursive (que vous devez implémenter) pour remplir le rectangle.
	 * L'implémentation de cette méthode est en bonus.
	 * 
	 * @param x  abscisse du coin supérieur gauche du rectangle
	 * @param y  ordonnée du coin supérieur gauche du rectangle
	 * @param w  largeur du rectangle
	 * @param h  hauteur du rectangle
	 * @param color couleur de remplissage (0 ou 1)
	 */
	@Override
	public void fillRect(int x, int y, int w, int h, int color) {
		if (color < 0 || color > 1) {
			throw new IllegalArgumentException("Color must be 0 or 1");
		}
		xFillRect(tree, x, y, x+w, y+h, WINDOW_SIZE, WINDOW_SIZE, color);
	}

	/** 
	 * Méthode récursive pour remplir un rectangle dans l'arbre avec la couleur spécifiée 
	 * @param node nœud courant de l'arbre
	 * @param x0 abscisse gauche du rectangle (incluse)
	 * @param y0 ordonnée haute du rectangle (incluse)
	 * @param x1 abscisse droite du rectangle (exclue)
	 * @param y1 ordonnée basse du rectangle (exclue)
	 * @param w largeur de l'image correspondant au rectangle du nœud courant
	 * @param h hauteur de l'image correspondant au rectangle du nœud courant
	 * @param color couleur de remplissage (0 ou 1)
	*/
	private void xFillRect(BinaryTree<Node> node, int x0, int y0, int x1, int y1, int w, int h, int color) {
		/* TODO: À vous de compléter ! (en attendant, on fait planter) */
		throw new UnsupportedOperationException("À vous de l'implémenter");
	}

}