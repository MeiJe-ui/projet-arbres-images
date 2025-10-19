package fr.istic.pra.tp_arbres;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

import fr.istic.pra.util.BinaryTree;
import fr.istic.pra.util.BinaryTree.NodeType;
import fr.istic.pra.util.BinaryTreeImpl;
import fr.istic.pra.util.BinaryTreeImplProf;


/**
 * Unit tests for BinaryTreeImpl class.
 * @author Vincent Drevelle
 * @version 12 octobre 2025
 */
public class BinaryTreeImplTest {
    static BinaryTree<Integer> createEmptyTree() {
        return new BinaryTreeImpl<>();
    }

    static BinaryTree<Integer> constructExampleTree() {
        BinaryTree<Integer> tree = createEmptyTree();
        // Construct a tree with double nodes and simple left and simple right nodes
        //            1
        //          /   \
        //        2       3
        //       /       / \
        //     4        6   7
        //    / \        \  
        //   8   9       13
        tree.createRootWithValue(1);
        tree.getLeft().createRootWithValue(2);
        tree.getRight().createRootWithValue(3);
        tree.getLeft().getLeft().createRootWithValue(4);
        tree.getRight().getLeft().createRootWithValue(6);
        tree.getRight().getRight().createRootWithValue(7);
        tree.getLeft().getLeft().getLeft().createRootWithValue(8);
        tree.getLeft().getLeft().getRight().createRootWithValue(9);
        tree.getRight().getLeft().getRight().createRootWithValue(13);
        return tree;
    }
  
    /**
     * Tests empty tree operations.
     */
    @Test
    public void testEmptyTree() {
        BinaryTree<Integer> tree = createEmptyTree();
        assertTrue(tree.isEmpty());
        assertEquals(NodeType.SENTINEL, tree.getType());
        // assertThrows(IllegalStateException.class, () -> tree.getRootValue());
        // assertThrows(IllegalStateException.class, () -> tree.getLeft());
        // assertThrows(IllegalStateException.class, () -> tree.getRight());
        // assertThrows(IllegalStateException.class, () -> tree.setRootValue(10));
        // assertThrows(IllegalStateException.class, () -> tree.removeRoot());
        assertEquals(0, tree.size());
        assertFalse(tree.contains(10));
        tree.clear(); // Should be no-op
        assertTrue(tree.isEmpty());
    }


    /**
     * Tests single node tree operations.
     */
    @Test
    public void testSingleNodeTree() {
        BinaryTree<Integer> tree = createEmptyTree();
        tree.createRootWithValue(5);
        assertFalse(tree.isEmpty());
        assertEquals(NodeType.LEAF, tree.getType());
        assertEquals(5, tree.getRootValue());
        assertTrue(tree.getLeft().isEmpty());
        assertTrue(tree.getRight().isEmpty());
        assertEquals(1, tree.size());
        assertTrue(tree.contains(5));
        assertFalse(tree.contains(10));

        tree.setRootValue(10);
        assertEquals(10, tree.getRootValue());

        tree.removeRoot();
        assertTrue(tree.isEmpty());
    }

    // /**
    //  * Tests that creating root on non-empty tree throws exception.
    //  */
    // @Test
    // public void testCreateRootOnNonEmptyTree() {
    //     BinaryTree<Integer> tree = createEmptyTree();
    //     tree.createRootWithValue(5);
    //     assertThrows(IllegalStateException.class, () -> tree.createRootWithValue(10));
    // }

    /**
     * Tests tree with left and right children.
     */
    @Test
    public void testTreeWithChildren() {
        BinaryTree<Integer> tree = createEmptyTree();
        tree.createRootWithValue(1);
        tree.getLeft().createRootWithValue(2);
        tree.getRight().createRootWithValue(3);

        assertEquals(NodeType.DOUBLE, tree.getType());
        assertEquals(3, tree.size());

        assertThrows(ClassCastException.class, () -> { BinaryTreeImplProf<Integer> t = (BinaryTreeImplProf<Integer>) tree; }, "BinaryTreeImpl should not be derived from BinaryTreeImplProf");
        assertThrows(ClassCastException.class, () -> { BinaryTreeImplProf<Integer> t = (BinaryTreeImplProf<Integer>) tree.getLeft(); }, "BinaryTreeImpl should not be derived from BinaryTreeImplProf");
        assertThrows(ClassCastException.class, () -> { BinaryTreeImplProf<Integer> t = (BinaryTreeImplProf<Integer>) tree.getRight(); }, "BinaryTreeImpl should not be derived from BinaryTreeImplProf");
        
        assertTrue(tree.contains(1));
        assertTrue(tree.contains(2));
        assertTrue(tree.contains(3));
        assertFalse(tree.contains(4));
        assertEquals(1, tree.getRootValue());
        assertEquals(2, tree.getLeft().getRootValue());
        assertEquals(3, tree.getRight().getRootValue());
        assertEquals(NodeType.LEAF, tree.getLeft().getType());
        assertEquals(NodeType.LEAF, tree.getRight().getType());
        assertTrue(tree.getLeft().getLeft().isEmpty());
        assertTrue(tree.getLeft().getRight().isEmpty());
        assertTrue(tree.getRight().getLeft().isEmpty());
        assertTrue(tree.getRight().getRight().isEmpty());
        assertEquals(1, tree.getLeft().size());
        assertEquals(1, tree.getRight().size());
        assertEquals(0, tree.getLeft().getLeft().size());
        assertEquals(0, tree.getLeft().getRight().size());
        assertEquals(0, tree.getRight().getLeft().size());
        assertEquals(0, tree.getRight().getRight().size());
    }

    /**
     * Tests complex tree operations.
     */
    @Test
    public void testNodeTypes() {
        BinaryTree<Integer> tree = constructExampleTree();
        assertFalse(tree.isEmpty());
        assertEquals(9, tree.size());
        // Check node types
        assertEquals(NodeType.DOUBLE, tree.getType());
        assertEquals(NodeType.SIMPLE_LEFT, tree.getLeft().getType());
        assertEquals(NodeType.DOUBLE, tree.getLeft().getLeft().getType());
        assertEquals(NodeType.LEAF, tree.getLeft().getLeft().getLeft().getType());
        assertEquals(NodeType.DOUBLE, tree.getRight().getType());
        assertEquals(NodeType.SIMPLE_RIGHT, tree.getRight().getLeft().getType());
        assertEquals(NodeType.LEAF, tree.getRight().getRight().getType());
    }

    /**
     * Test removal of root in single node tree.
     */
    @Test
    public void testRemoveRootInSingleNodeTree() {
        BinaryTree<Integer> tree = createEmptyTree();
        tree.createRootWithValue(1);
        tree.removeRoot();
        assertTrue(tree.isEmpty());
    }

    /**
     * Test removal of root with one child.
     */
    @Test
    public void testRemoveRootWithOneChild() {
        BinaryTree<Integer> tree = createEmptyTree();
        tree.createRootWithValue(1);
        tree.getLeft().createRootWithValue(2);
        tree.removeRoot();
        assertEquals(2, tree.getRootValue());
        assertTrue(tree.getLeft().isEmpty());
        assertTrue(tree.getRight().isEmpty());
    }

    /**
     * Test removal of a leaf node.
     */
    @Test
    public void testRemoveLeafNode() {
        BinaryTree<Integer> tree = createEmptyTree();
        tree.createRootWithValue(1);
        tree.getLeft().createRootWithValue(2);
        tree.getRight().createRootWithValue(3);
        tree.getLeft().removeRoot();
        assertEquals(1, tree.getRootValue());
        assertEquals(3, tree.getRight().getRootValue());
        assertTrue(tree.getLeft().isEmpty());
        assertFalse(tree.getRight().isEmpty());
    }

    // /**
    //  * Test that removing root of empty tree throws exception.
    //  */
    // @Test
    // public void testRemoveRootOfEmptyTree() {
    //     BinaryTree<Integer> tree = createEmptyTree();
    //     assertThrows(IllegalStateException.class, () -> tree.removeRoot(), "Cannot remove root from empty tree");
    // }

    /**
     * Test remove in complex tree.
     */
    @Test
    public void testRemoveInComplexTree() {
        BinaryTree<Integer> tree = constructExampleTree();
        assertEquals(9, tree.size());
        tree.getLeft().removeRoot(); // Remove node 2
        assertEquals(8, tree.size());
        assertEquals(1, tree.getRootValue());
        assertEquals(4, tree.getLeft().getRootValue());
        assertEquals(3, tree.getRight().getRootValue());
        tree.getRight().getLeft().removeRoot(); // Remove node 6
        assertEquals(7, tree.size());
        assertEquals(13, tree.getRight().getLeft().getRootValue());
        tree.getRight().getRight().removeRoot(); // Remove node 7
        assertEquals(6, tree.size());
        assertTrue(tree.getRight().getRight().isEmpty());
    }

    /**
     * Test that removing root with two children throws exception.
     */
    @Test
    public void testRemoveRootWithTwoChildren() {
        BinaryTree<Integer> tree = constructExampleTree();
        assertThrows(IllegalStateException.class, () -> tree.removeRoot(), "Cannot remove root with two children");
        assertEquals(9, tree.size());
        assertEquals(1, tree.getRootValue());
        assertEquals(2, tree.getLeft().getRootValue());
        assertEquals(3, tree.getRight().getRootValue());
    }

    /**
     * Test clear operation.
     */
    @Test
    public void testClear() {
        BinaryTree<Integer> tree = constructExampleTree();
        assertFalse(tree.isEmpty());
        tree.clear();
        assertTrue(tree.isEmpty());
    }

    /**
     * Test iterators.
     */
    @Test
    public void testIterators() {
        BinaryTree<Integer> tree = constructExampleTree();
        // Test prefix iterator
        Integer[] expectedPrefix = {1, 2, 4, 8, 9, 3, 6, 13, 7};
        int idx = 0;
        Iterator<Integer> it = tree.prefixIterator();
        while (it.hasNext()) {
            assertEquals(expectedPrefix[idx++], it.next(), "Prefix iterator mismatch at index " + idx);
        }
        assertThrows(NoSuchElementException.class, () -> it.next());
        assertEquals(expectedPrefix.length, idx);
        // Test infix iterator
        Integer[] expectedInfix = {8, 4, 9, 2, 1, 6, 13, 3, 7};
        idx = 0;
        Iterator<Integer> it2 = tree.infixIterator();
        while (it2.hasNext()) {
            assertEquals(expectedInfix[idx++], it2.next(), "Infix iterator mismatch at index " + idx);
        }
        assertThrows(NoSuchElementException.class, () -> it2.next());
        assertEquals(expectedInfix.length, idx);
        // Test iterator() defaults to infix
        idx = 0;
        for (Integer val : tree) {
            assertEquals(expectedInfix[idx++], val, "Default iterator mismatch at index " + idx + "(should be infix)");
        }
        assertEquals(expectedInfix.length, idx);
    }

    /**
     * Test equality.
     */
    @Test
    public void testEquality() {
        BinaryTree<Integer> tree1 = constructExampleTree();
        BinaryTree<Integer> tree2 = constructExampleTree();
        assertEquals(tree1, tree1); // Reflexive
        assertEquals(tree1, tree2);
        tree2.getRight().getLeft().setRootValue(99);
        assertNotEquals(tree1, tree2);
        BinaryTree<Integer> empty1 = createEmptyTree();
        BinaryTree<Integer> empty2 = createEmptyTree();
        assertEquals(empty1, empty2);
        assertNotEquals(tree1, empty1);
        assertNotEquals(tree2, empty2);
        assertNotEquals(empty1, tree1);
        assertNotEquals(tree1, null);
        assertNotEquals(tree1, "some string");
    }

    /**
     * Test equality betwwen trees and subtrees.
     */
    @Test
    public void testEqualityWithSubtrees() {
        // First get a view on a subtree of a full tree
        BinaryTree<Integer> fullTree = constructExampleTree();
        BinaryTree<Integer> treeView = fullTree.getLeft().getLeft(); // Should be subtree rooted at 4
        assertEquals(4, treeView.getRootValue());
        // Now construct an independent tree with same structure and values
        BinaryTree<Integer> independentTree = createEmptyTree();
        independentTree.createRootWithValue(4);
        independentTree.getLeft().createRootWithValue(8);
        independentTree.getRight().createRootWithValue(9);
        // Check equality
        assertEquals(treeView, independentTree, "Subtree should equal independent tree with same structure and values");
        assertEquals(independentTree, treeView, "Independent tree should equal subtree with same structure and values");
    }
}
