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
        /* TODO: À vous de compléter ! (en attendant, on fait planter) */
        throw new UnsupportedOperationException("À vous de l'implémenter");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BinaryTreeImpl)) return false;
        BinaryTreeImpl<?> that = (BinaryTreeImpl<?>) o;
        /* TODO: À vous de compléter ! (en attendant, on fait planter) */
        throw new UnsupportedOperationException("À vous de l'implémenter");
    }

    @Override
    public int hashCode() {
        /* TODO: À vous de compléter ! (en attendant, on fait planter) */
        throw new UnsupportedOperationException("À vous de l'implémenter");
    }

    @Override
    public T getRootValue() {
        /* TODO: À vous de compléter ! (en attendant, on fait planter) */
        throw new UnsupportedOperationException("À vous de l'implémenter");
    }

    @Override
    public BinaryTreeImpl<T> getLeft() {
        /* TODO: À vous de compléter ! (en attendant, on fait planter) */
        throw new UnsupportedOperationException("À vous de l'implémenter");
    }

    @Override
    public BinaryTreeImpl<T> getRight() {
        /* TODO: À vous de compléter ! (en attendant, on fait planter) */
        throw new UnsupportedOperationException("À vous de l'implémenter");
    }

    @Override
    public void setRootValue(T value) {
        /* TODO: À vous de compléter ! (en attendant, on fait planter) */
        throw new UnsupportedOperationException("À vous de l'implémenter");
    }

    @Override
    public void createRootWithValue(T value) {
        /* TODO: À vous de compléter ! (en attendant, on fait planter) */
        throw new UnsupportedOperationException("À vous de l'implémenter");
    }

    @Override
    public void removeRoot() {
        /* TODO: À vous de compléter ! (en attendant, on fait planter) */
        throw new UnsupportedOperationException("À vous de l'implémenter");
    }

    @Override
    public void clear() {
        /* TODO: À vous de compléter ! (en attendant, on fait planter) */
        throw new UnsupportedOperationException("À vous de l'implémenter");
    }

    @Override
    public boolean isEmpty() {
        /* TODO: À vous de compléter ! (en attendant, on fait planter) */
        throw new UnsupportedOperationException("À vous de l'implémenter");
    }

    @Override
    public NodeType getType() {
        /* TODO: À vous de compléter ! (en attendant, on fait planter) */
        throw new UnsupportedOperationException("À vous de l'implémenter");
    }
}
