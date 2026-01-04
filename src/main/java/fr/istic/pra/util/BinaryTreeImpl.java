package fr.istic.pra.util;

/** 
 * Implémentation d'un arbre binaire avec des pointeurs vers les enfants et des nœuds sentinelles.
 * @hidden
 * @author Vincent Drevelle
 * @version 1.02, 2025-10-07
 * @param <T> Le type des valeurs stockées dans les nœuds de l'arbre.
 */
public class BinaryTreeImpl<T> implements BinaryTree<T> {
    private T rootValue;
    private BinaryTreeImpl<T> left;
    private BinaryTreeImpl<T> right;

    @Override
    public String toString() {
        if (isEmpty()) return "∅"; 
        if (getType() == NodeType.LEAF) return rootValue.toString();
        return "(" + left.toString() + " " + rootValue + " " + right.toString() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BinaryTreeImpl)) return false;
        BinaryTreeImpl<?> that = (BinaryTreeImpl<?>) o;

        if (this.isEmpty() && that.isEmpty()) return true;
        if (this.isEmpty() != that.isEmpty()) return false;

        if (!this.rootValue.equals(that.rootValue)) return false;

        return this.left.equals(that.left) && this.right.equals(that.right);
    }

    @Override
    public int hashCode() {
        if (isEmpty()) return 0;
        int h = rootValue.hashCode();
        h +=  left.hashCode();
        h +=  right.hashCode();
        return h;
    }

    @Override
    public T getRootValue() {
        return rootValue;
    }

    @Override
    public BinaryTreeImpl<T> getLeft() {
       return left;
    }

    @Override
    public BinaryTreeImpl<T> getRight() {
        return right;
    }

    @Override
    public void setRootValue(T value) {
       if (isEmpty()) {
            //left  = new BinaryTreeImpl<>();
            //right = new BinaryTreeImpl<>();
            return;
        }
        this.rootValue = value;
    }

    @Override
    public void createRootWithValue(T value) {
        if (!isEmpty()) return;
        this.rootValue = value;
        left  = new BinaryTreeImpl<>();
        right = new BinaryTreeImpl<>();
    }

    @Override
    public void removeRoot() {
        NodeType typeRacine = this.getType();
        if(typeRacine == NodeType.DOUBLE){
            throw new IllegalStateException( "Cannot remove root with two children");
        }
        if(typeRacine == NodeType.LEAF){
            rootValue = null;
            left = null;
            right = null;
            return;
        }
        if(typeRacine == NodeType.SIMPLE_LEFT){
            this.rootValue = left.getRootValue();
            BinaryTreeImpl<T> copieRight = left.getRight();
            this.left = left.getLeft();
            this.right = copieRight;
            return;

        }
        if(typeRacine == NodeType.SIMPLE_RIGHT){
            this.rootValue = right.getRootValue();
            BinaryTreeImpl<T> copieRight = right.getRight();
            this.left = right.getLeft();
            this.right = copieRight;
            return;
        }
    }

    @Override
    public void clear() {
        this.left = null;
        this.right = null;
        this.rootValue = null;
    }

    @Override
    public boolean isEmpty() {
        return rootValue == null && left == null && right == null;
    }

    @Override
    public NodeType getType() {
    
        if (isEmpty()) return NodeType.SENTINEL;
        if ((left == null || left.isEmpty()) && (right == null || right.isEmpty())) return NodeType.LEAF;
        if (left == null || left.isEmpty()) return NodeType.SIMPLE_RIGHT;
        if (right == null || right.isEmpty()) return NodeType.SIMPLE_LEFT;

        return NodeType.DOUBLE;
    }
}
