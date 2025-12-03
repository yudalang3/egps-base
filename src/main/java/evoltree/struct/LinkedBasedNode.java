package evoltree.struct;

import java.util.function.Consumer;

/**
 * Linked-list-based implementation of a tree node structure for phylogenetic trees.
 * 
 * <p>
 * This class represents a node in a multi-way tree using a linked-list approach,
 * where each node maintains references to:
 * <ul>
 *   <li>Its parent node</li>
 *   <li>Its first child node</li>
 *   <li>Its next sibling node</li>
 * </ul>
 * This is also known as a "child-sibling" or "first-child/next-sibling" representation.
 * </p>
 * 
 * <h2>Architecture Comparison:</h2>
 * <table border="1">
 *   <tr>
 *     <th>Aspect</th>
 *     <th>LinkedBasedNode</th>
 *     <th>ArrayBasedNode</th>
 *   </tr>
 *   <tr>
 *     <td>Storage</td>
 *     <td>Linked references</td>
 *     <td>Dynamic arrays</td>
 *   </tr>
 *   <tr>
 *     <td>Memory</td>
 *     <td>Lower overhead per node</td>
 *     <td>Higher due to array allocation</td>
 *   </tr>
 *   <tr>
 *     <td>Child Access</td>
 *     <td>O(n) - must traverse</td>
 *     <td>O(1) - direct indexing</td>
 *   </tr>
 *   <tr>
 *     <td>Add Child</td>
 *     <td>O(n) - traverse to end</td>
 *     <td>O(1) amortized</td>
 *   </tr>
 * </table>
 * 
 * <h2>Structure Diagram:</h2>
 * <pre>
 *         root
 *          |
 *       firstChild (B)
 *          |        \
 *       nextSibling (C) --- nextSibling (D)
 *          |
 *       child (E)
 * </pre>
 * 
 * <h2>Usage Note:</h2>
 * <p>
 * <strong>Important:</strong> This implementation is provided for <em>educational purposes</em>
 * and structural completeness. For production use in phylogenetic analysis, prefer
 * {@link ArrayBasedNode} which offers better performance for most tree operations.
 * </p>
 * 
 * <h2>When to Use:</h2>
 * <ul>
 *   <li>Educational/learning scenarios about tree data structures</li>
 *   <li>Trees with highly variable numbers of children</li>
 *   <li>Memory-constrained environments</li>
 *   <li>Situations where random access to children is not needed</li>
 * </ul>
 * 
 * @author eGPS Development Team
 * @version 1.0
 * @since 1.0
 * @see ArrayBasedNode
 * @see EvolNode
 */
public class LinkedBasedNode {
    public static int nextID;
    protected int ID;
    private LinkedBasedNode parent;
    private LinkedBasedNode firstChild;
    private LinkedBasedNode nextSibling;
    protected String name;

	public LinkedBasedNode() {
        ID = nextID++;
    }

    @Override
    public String toString() {
        return "ID:" + String.valueOf(ID) + ";Name:" + name;
    }

    public void addChild(LinkedBasedNode child) {
        child.parent = this;
        if (firstChild == null) {
            firstChild = child;
        } else {
            LinkedBasedNode sibling = firstChild;
            while (sibling.nextSibling != null) {
                sibling = sibling.nextSibling;
            }
            sibling.nextSibling = child;
        }
    }

    public LinkedBasedNode getParent() {
        return parent;
    }

    public int getChildCount() {
        int count = 0;
        LinkedBasedNode child = firstChild;
        while (child != null) {
            count++;
            child = child.nextSibling;
        }
        return count;
    }

    public void insertChildAt(int index, LinkedBasedNode child) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }
        child.parent = this;
        if (index == 0) {
            child.nextSibling = firstChild;
            firstChild = child;
        } else {
            LinkedBasedNode sibling = firstChild;
            for (int i = 0; i < index - 1 && sibling != null; i++) {
                sibling = sibling.nextSibling;
            }
            if (sibling == null) {
                throw new IndexOutOfBoundsException("Index: " + index);
            }
            child.nextSibling = sibling.nextSibling;
            sibling.nextSibling = child;
        }
    }

    public void removeChild(int index) {
        if (index < 0 || firstChild == null) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }
        if (index == 0) {
            firstChild = firstChild.nextSibling;
        } else {
            LinkedBasedNode sibling = firstChild;
            for (int i = 0; i < index - 1 && sibling != null; i++) {
                sibling = sibling.nextSibling;
            }
            if (sibling == null || sibling.nextSibling == null) {
                throw new IndexOutOfBoundsException("Index: " + index);
            }
            sibling.nextSibling = sibling.nextSibling.nextSibling;
        }
    }

    public void removeChild(LinkedBasedNode child) {
        if (firstChild == child) {
            firstChild = firstChild.nextSibling;
        } else {
            LinkedBasedNode sibling = firstChild;
            while (sibling.nextSibling != null && sibling.nextSibling != child) {
                sibling = sibling.nextSibling;
            }
            if (sibling.nextSibling != null) {
                sibling.nextSibling = sibling.nextSibling.nextSibling;
            }
        }
        child.parent = null;
    }

    public void removeAllChildren() {
        firstChild = null;
    }

	public LinkedBasedNode getFirstChild() {
		return firstChild;
	}

	public LinkedBasedNode getNextSibling() {
		return nextSibling;
	}

	public LinkedBasedNode getChild(int k) {
		if (k < 0) {
			throw new IllegalArgumentException("Index must be non-negative");
		}

		LinkedBasedNode child = this.firstChild;
		int currentIndex = 0;

		while (child != null && currentIndex < k) {
			child = child.nextSibling;
			currentIndex++;
		}

		if (child == null) {
			throw new IndexOutOfBoundsException("Child index out of bounds");
		}

		return child;
	}

    public class LinkedTreeTraversal {

        public static void preOrderTraversal(LinkedBasedNode root, Consumer<LinkedBasedNode> visit) {
            if (root == null) {
                return;
            }

            visit.accept(root); // 访问根节点

            LinkedBasedNode child = root.getFirstChild();
            while (child != null) {
                preOrderTraversal(child, visit);  // 递归访问子节点
                child = child.getNextSibling();
            }
        }

        public static void postOrderTraversal(LinkedBasedNode root, Consumer<LinkedBasedNode> visit) {
            if (root == null) {
                return;
            }

            LinkedBasedNode child = root.getFirstChild();
            while (child != null) {
                postOrderTraversal(child, visit);  // 递归访问子节点
                child = child.getNextSibling();
            }

            visit.accept(root); // 访问根节点
        }

        // Simple test
        public static void main(String[] args) {
            LinkedBasedNode root = new LinkedBasedNode();
            root.name = "root";
            LinkedBasedNode b = new LinkedBasedNode();
            b.name = "I am B";
            LinkedBasedNode c = new LinkedBasedNode();
            c.name = "I am C";
            LinkedBasedNode d = new LinkedBasedNode();
            d.name = "I am D";
            LinkedBasedNode e = new LinkedBasedNode();
            e.name = "I am E";
            LinkedBasedNode f = new LinkedBasedNode();
            f.name = "I am F";

            root.addChild(b);
            root.addChild(c);
            root.addChild(d);
            b.addChild(e);
            b.addChild(f);

            System.out.println("Preorder iteration:");
            preOrderTraversal(root, data -> System.out.print(data + " "));

            System.out.println("\nPost order iteration:");
            postOrderTraversal(root, data -> System.out.print(data + " "));
        }
    }

}
