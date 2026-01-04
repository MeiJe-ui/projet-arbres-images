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
		//this(BinaryTreeImplProf::new);
		this(BinaryTreeImpl::new);
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
		Node nouvelleRacine = Node.valueOf(color);

		if(!this.tree.isEmpty()){
			this.tree.clear();
		}
		this.tree.createRootWithValue(nouvelleRacine);
	}

	/** 
	 * Teste si un pixel est allumé 
	 * @param x  abscisse du pixel (0 à 255)
	 * @param y  ordonnée du pixel (0 à 255)
	 * @return true si le pixel est allumé (blanc), false sinon (noir)
	 */
	@Override
	public boolean isPixelOn(int x, int y) {

		BinaryTree<Node> current = this.tree;
		int xMin = 0;
		int xMax = 255;
		int yMin = 0;
		int yMax = 255;

		boolean parite = false;

		while(current.getRootValue().state == Node.INDETERMINATE_STATE){
			
			if(parite){
				if(x >= xMin && x <= xMin+(xMax-xMin)/2){
					current = current.getLeft();
					xMax = xMin+(xMax-xMin)/2;
				}
				else{
					current = current.getRight();
					xMin += (xMax-xMin)/2 + 1;
				}
		
			}
			else{
				if( y >= yMin && y <= yMin+(yMax-yMin)/2){
					current = current.getLeft();
					yMax = yMin+(yMax-yMin)/2;
				}
				else{
					current = current.getRight();
					yMin += (yMax-yMin)/2 + 1;
				}
			
			}
			
			parite = !parite;			
		}
		
		if(current.getRootValue().state == 1){
			return true;
		} else {
			return false;
		}

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

		this.tree.clear();

		BinaryTree<Node> treeImg2 = image2.getTree();
		
		affectRec(this.tree, treeImg2);
		
	}
	
	private void affectRec(BinaryTree<Node> tree1, BinaryTree<Node> tree2){

		if(tree2.isEmpty()){
			return;
		}
		
		if(tree2.getType() == NodeType.SENTINEL){
			return;
		}
		if(tree1.isEmpty()){
			tree1.createRootWithValue(tree2.getRootValue());
			
		} else { 
			tree1.setRootValue(tree2.getRootValue());
		}

		affectRec(tree1.getLeft(), tree2.getLeft());
		affectRec(tree1.getRight(), tree2.getRight());

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
		BinaryTree<Node> treeImg = image.getTree();
		
		intersectionRec(this.tree, treeImg);
	}



	private void intersectionRec(BinaryTree<Node> tree1, BinaryTree<Node> tree2) {

    if (tree2.getType() == NodeType.SENTINEL) {
        tree1.clear();
        tree1.createRootWithValue(Node.valueOf(0));
        return;
    }
    
    if (tree1.getType() == NodeType.SENTINEL) {
        return;
    }

    if (tree1.isEmpty()) {
        tree1.createRootWithValue(Node.valueOf(0));
        return;
    }

    int s1 = tree1.getRootValue().state;
    int s2 = tree2.getRootValue().state;

    if (s2 == 0) {
        tree1.clear();
        tree1.createRootWithValue(Node.valueOf(0));
        return;
	}
    
    if (s2 == 1) {
        return;
    }

    if (s1 != 2) {

        if (s1 == 1) {
            affectRec(tree1, tree2); // 1 ∩ structure = structure
            return;
        }

        if (s1 == 0) {
            //tree1.clear();
            //tree1.createRootWithValue(Node.valueOf(0));
            return;
        }
    }

    intersectionRec(tree1.getLeft(),  tree2.getLeft());
    intersectionRec(tree1.getRight(), tree2.getRight());

    
    if (tree1.getLeft().getType()  == NodeType.LEAF &&
        tree1.getRight().getType() == NodeType.LEAF) {

        int leftColor  = tree1.getLeft().getRootValue().state;
        int rightColor = tree1.getRight().getRootValue().state;

        if (leftColor == rightColor) {
            tree1.clear();
            tree1.createRootWithValue(Node.valueOf(leftColor));
        }
    }
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
		TreeImage imageRotate = this.copy();

		rotated180Rec(imageRotate.getTree());

		return imageRotate;
	}

	private void rotated180Rec(BinaryTree<Node> tree1){

		if(tree1.getType() == NodeType.SENTINEL || tree1.getType() != NodeType.DOUBLE){
			return;
		}
	
		TreeImage leftCopy = createTreeImage();
    	TreeImage rightCopy = createTreeImage();
    	affectRec(leftCopy.getTree(), tree1.getLeft());
    	affectRec(rightCopy.getTree(), tree1.getRight());

		tree1.getLeft().clear();
    	tree1.getRight().clear();

		affectRec(tree1.getLeft(), rightCopy.getTree());
		affectRec(tree1.getRight(), leftCopy.getTree());

		rotated180Rec(tree1.getLeft());
		rotated180Rec(tree1.getRight());

	}



	/**
	 * Inversion des couleurs de l'image
	 * @return une nouvelle image correspondant à l'inversion des couleurs de l'image courante
	 * 
	 * Un pixel noir devient blanc, un pixel blanc devient noir.
	 */
	@Override
	public TreeImage inverted() {
		TreeImage copieInverted = this.copy();
		
		invertedRec(copieInverted.tree);

		return copieInverted;

		
	}
	
	private void invertedRec(BinaryTree<Node> tree){
		if(tree.getType() == NodeType.SENTINEL){
			return;
		}
		else{
			if(tree.getType() == NodeType.LEAF){
				if(tree.getRootValue().state == 1){
					tree.setRootValue(Node.valueOf(0));
				}
				else if(tree.getRootValue().state == 0){
					tree.setRootValue(Node.valueOf(1));
				}

				return;
			}
			invertedRec(tree.getLeft());
			invertedRec(tree.getRight());
		}
	}


	/** 
	 * Symétrie horizontale de l'image 
	 * La gauche devient la droite et la droite devient la gauche.
	 * @return une nouvelle image correspondant au retournement horizontal de l'image courante
	 */
	@Override
	public TreeImage flippedHorizontal() {
		TreeImage copieFlipped = this.copy();
		flippedHorizontalRec(copieFlipped.getTree(), true);
		return copieFlipped;
	}

	private void flippedHorizontalRec (BinaryTree<Node> tree1, boolean pair){
		if(tree1.getType() == NodeType.SENTINEL || tree1.getType() != NodeType.DOUBLE){
			return;
		}

		if(!pair){
			TreeImage leftCopy = createTreeImage();
			TreeImage rightCopy = createTreeImage();
			affectRec(leftCopy.getTree(), tree1.getLeft());
			affectRec(rightCopy.getTree(), tree1.getRight());
	
			tree1.getLeft().clear();
			tree1.getRight().clear();
	
			affectRec(tree1.getLeft(), rightCopy.getTree());
			affectRec(tree1.getRight(), leftCopy.getTree());
		}

		flippedHorizontalRec(tree1.getLeft(), !pair);
		flippedHorizontalRec(tree1.getRight(), !pair);
	}



	/**
	 * Rotation de l'image de 90 degrés dans le sens des aiguilles d'une montre (vers la droite)
	 * @return une nouvelle image correspondant à l'image courante tournée de 90 degrés dans le sens des aiguilles d'une montre
	 */
	@Override
	public TreeImage rotatedClockwise90() {
		// Nous avons essayé sur papier mais nous ne trouvions pas de lien entre l'arbre original et celui de l'image à 90 
		// nous aurions dû gérer une multidude de cas et nous avons pas vraiment réussi à faire une implémentation
		
		TreeImage flip180 = this.rotated180();
		TreeImage symetrie90 = flip180.flippedHorizontal();
		return symetrie90;
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
		return testDiagonalRec(tree, 0, 255, 0, 255, true);
	}

	private boolean testDiagonalRec(BinaryTree<Node> node, int xMin, int xMax, int yMin, int yMax, boolean pair) {
    	if (xMax < yMin || yMax < xMin){
    	    return true;
		}

    	NodeType type = node.getType();

    
    	if (type == NodeType.LEAF) {
        	return node.getRootValue().state == 1; 
    	}	

    	int xMid = (xMin + xMax) / 2;
    	int yMid = (yMin + yMax) / 2;

    	if (pair) {
        	return testDiagonalRec(node.getLeft(),  xMin,    xMid,  yMin, yMax, !pair) && testDiagonalRec(node.getRight(), xMid+1,  xMax,  yMin, yMax, !pair);
		} else 
		{
        return testDiagonalRec(node.getLeft(),  xMin, xMax, yMin,   yMid,  !pair) && testDiagonalRec(node.getRight(), xMin, xMax, yMid+1, yMax,  !pair);
    	}
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