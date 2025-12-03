package evoltree.struct;


/**
 * Core interface defining the fundamental structure and operations for evolutionary tree nodes.
 * 
 * <p>
 * This interface represents the top-level abstraction for nodes in phylogenetic trees and other
 * evolutionary graph structures. It provides a comprehensive API for managing parent-child
 * relationships, node properties, and tree traversal operations.
 * </p>
 * 
 * <h2>Node Properties</h2>
 * <p>
 * A {@code Node} in a tree or graph structure has the following fundamental properties:
 * </p>
 * <ol>
 *    <li>A node may have zero to many children nodes (down-links)</li>
 *    <li>A node may have zero to many parent nodes (up-links)</li>
 *    <li>A node can have a branch length associated with it</li>
 *    <li>A node can have a name/label for identification</li>
 *    <li>A node can store auxiliary data (size, double variable, etc.)</li>
 * </ol>
 * 
 * <h2>Implementation Guidelines</h2>
 * 
 * <h3>1. Link Management (Up-links and Down-links)</h3>
 * <p>
 * Methods dealing with children <b>must always</b> maintain consistency between
 * parent-child relationships. Both up-links (child to parent) and down-links
 * (parent to child) should be properly synchronized.
 * </p>
 * 
 * <p><strong>Tree Structure Example:</strong></p>
 * <pre>
 *     |------------------ leaf1
 *     |
 *     |root
 *     |          |---------------leaf2
 *     |----------|internal1
 *                |----------------leaf3
 * </pre>
 * 
 * <p>
 * In this example:
 * <ul>
 *   <li>{@code internal1} has up-link to {@code root} and down-links to {@code leaf2} and {@code leaf3}</li>
 *   <li>{@code leaf2} has only an up-link to {@code internal1}</li>
 * </ul>
 * </p>
 * 
 * <p><strong>Link Removal Behavior:</strong></p>
 * <ul>
 *   <li>{@code internal1.removeChild(leaf2)} - removes both down-link from {@code internal1}
 *       and up-link from {@code leaf2}</li>
 *   <li>{@code leaf2.removeParent(internal1)} - removes only the up-link in {@code leaf2}
 *       (implementation-dependent)</li>
 * </ul>
 * 
 * <h3>2. Multiple Parent Support</h3>
 * <p>
 * Most phylogenetic trees do not support multiple parents (single inheritance).
 * However, this interface is designed to be flexible enough to support both:
 * </p>
 * <ul>
 *   <li><strong>Single parent:</strong> Traditional phylogenetic trees (e.g., {@link ArrayBasedNode})</li>
 *   <li><strong>Multiple parents:</strong> Phylogenetic networks, reticulate evolution</li>
 * </ul>
 * <p>
 * <strong>Important:</strong> Always check the implementation documentation
 * (e.g., {@link #getParentCount()}) to understand whether multiple parents are supported.
 * </p>
 * 
 * <h2>Common Implementations</h2>
 * <ul>
 *   <li>{@link ArrayBasedNode} - Array-backed implementation (single parent only)</li>
 *   <li>{@link LinkedBasedNode} - Linked-list-backed implementation</li>
 * </ul>
 * 
 * @author Li WenXiong, Fu YunXing, Li Haipeng, yudalang
 * @version 1.0
 * @since 1.0
 * @see ArrayBasedNode
 * @see LinkedBasedNode
 * @see TreeCoder
 * @see TreeDecoder
 */
public interface EvolNode {

	/**
	 * Returns the number of parent nodes for this node.
	 * 
	 * <p>
	 * <strong>Implementation Note:</strong> Not all implementations support multiple parents.
	 * Most phylogenetic tree implementations (e.g., {@link ArrayBasedNode}) support only
	 * a single parent, while graph-based implementations may support multiple parents.
	 * </p>
	 * 
	 * <p><strong>Return Values:</strong></p>
	 * <ul>
	 *   <li>0 - Root node (no parents)</li>
	 *   <li>1 - Standard tree node (single parent)</li>
	 *   <li>2+ - Network node (multiple parents, if supported by implementation)</li>
	 * </ul>
	 * 
	 * @return the number of parent nodes
	 * @see #getParentAt(int)
	 * @see #addParent(EvolNode)
	 */
    int getParentCount();

	/**
	 * Returns the parent node at the specified index.
	 * 
	 * <p>
	 * <strong>Important:</strong> The caller must ensure that {@code k} is a valid index
	 * (i.e., {@code 0 <= k < getParentCount()}). Behavior for invalid indices is
	 * implementation-dependent.
	 * </p>
	 * 
	 * @param k the index of the parent node (zero-based)
	 * @return the parent node at index {@code k}, or {@code null} if index is invalid
	 * @see #getParentCount()
	 * @see #getParent()
	 */
    EvolNode getParentAt(int k);

	/**
	 * Adds a parent node to this node.
	 * 
	 * <p><strong>Behavior depends on implementation:</strong></p>
	 * <ul>
	 *   <li><strong>Multiple parent support:</strong> Adds the parent to the parent list</li>
	 *   <li><strong>Single parent only:</strong> Replaces the existing parent</li>
	 * </ul>
	 * 
	 * <p>
	 * This method typically updates both the parent's child list and this node's parent list
	 * to maintain bidirectional linking.
	 * </p>
	 * 
	 * @param a the parent node to be added (must not be {@code null})
	 * @see #setParent(EvolNode)
	 * @see #removeParent(EvolNode)
     */
    void addParent(EvolNode a);

	/**
	 * Removes the specified parent node from this node's parent list.
	 * 
	 * <p>
	 * This method breaks the up-link from this node to the specified parent.
	 * Whether it also removes the down-link from the parent to this child is
	 * implementation-dependent.
	 * </p>
	 * 
	 * @param a the parent node to be removed
	 * @return {@code true} if the parent was found and removed, {@code false} if the
	 *         specified node was not in the parent list
	 * @see #removeParent(int)
	 * @see #removeAllParent()
	 */
    boolean removeParent(EvolNode a);

	/**
	 * Removes and returns the parent node at the specified index.
	 * 
	 * @param k the index of the parent to remove (zero-based)
	 * @return the removed parent node, or {@code null} if {@code k} is out of bounds
	 *         (i.e., {@code k < 0} or {@code k >= getParentCount()})
	 * @see #removeParent(EvolNode)
	 * @see #getParentAt(int)
	 */
    EvolNode removeParent(int k);

	/**
	 * Sets the parent node at the specified index position.
	 * 
	 * <p><strong>Behavior:</strong></p>
	 * <ul>
	 *   <li>For implementations supporting multiple parents: Sets the parent at index {@code k}</li>
	 *   <li>For single-parent implementations: May return {@code false} if attempting to add
	 *       a second parent</li>
	 * </ul>
	 * 
	 * @param k the index position to set the parent
	 * @param a the parent node to set
	 * @return {@code false} if the node already exists in the parent list (for multi-parent
	 *         implementations) or if adding multiple parents is not supported; behavior may
	 *         vary by implementation
	 * @see #addParent(EvolNode)
	 * @see #getParentAt(int)
	 */
    boolean setParentAt(int k, EvolNode a);

	/**
	 * Returns the index of the specified parent node in the parent list.
	 * 
	 * @param a the parent node to search for
	 * @return the zero-based index of the parent node, or -1 if not found in the parent list
	 * @see #getParentAt(int)
	 * @see #getParentCount()
	 */
    int indexOfParent(EvolNode a);

	/**
	 * Removes all parent nodes from this node.
	 * 
	 * <p>
	 * After calling this method, {@link #getParentCount()} will return 0.
	 * This effectively makes the node a root node (at least temporarily).
	 * </p>
	 * 
	 * @see #removeParent(EvolNode)
	 * @see #removeParent(int)
	 */
    void removeAllParent();

	/**
	 * Sets the parent of this node (convenience method).
	 * 
	 * <p>
	 * This is equivalent to calling {@code setParentAt(0, a)}.
	 * It sets the first (or only) parent of this node.
	 * </p>
	 * 
	 * @param a the parent node to set
	 * @see #setParentAt(int, EvolNode)
	 * @see #getParent()
	 */
    void setParent(EvolNode a);

	/**
	 * Returns the first parent of this node (convenience method).
	 * 
	 * <p>
	 * This is equivalent to calling {@code getParentAt(0)}.
	 * For most phylogenetic tree implementations, this returns the only parent.
	 * </p>
	 * 
	 * @return the first parent node, or {@code null} if this node has no parents (root node)
	 * @see #getParentAt(int)
	 * @see #getParentCount()
	 */
    EvolNode getParent();

	// ========================================================================================
	// Methods for managing child nodes
	// ========================================================================================
	
	/**
	 * Returns the number of child nodes.
	 * 
	 * <p>
	 * A return value of 0 indicates this is a leaf node (terminal node).
	 * Non-zero values indicate an internal node with descendants.
	 * </p>
	 * 
	 * @return the number of children for this node (>= 0)
	 * @see #getChildAt(int)
	 * @see #addChild(EvolNode)
	 */
    int getChildCount();

	/**
	 * Returns the child node at the specified index.
	 * 
	 * <p>
	 * <strong>Important:</strong> The caller should verify that
	 * {@code k >= 0 && k < getChildCount()} before calling this method.
	 * </p>
	 * 
	 * @param k the index of the child node (zero-based)
	 * @return the child node at index {@code k}
	 * @throws IndexOutOfBoundsException if {@code k} is out of range (implementation-dependent)
	 * @see #getChildCount()
	 * @see #getFirstChild()
	 * @see #getLastChild()
	 */
    EvolNode getChildAt(int k);

	/**
	 * Returns the first child of this node (convenience method).
	 * 
	 * <p>Equivalent to {@code getChildAt(0)}.</p>
	 * 
	 * @return the first child node, or {@code null} if this is a leaf node
	 * @see #getChildAt(int)
	 * @see #getLastChild()
	 */
	EvolNode getFirstChild();

	/**
	 * Returns the last child of this node (convenience method).
	 * 
	 * <p>Equivalent to {@code getChildAt(getChildCount() - 1)}.</p>
	 * 
	 * @return the last child node, or {@code null} if this is a leaf node
	 * @see #getChildAt(int)
	 * @see #getFirstChild()
	 */
	EvolNode getLastChild();

	/**
	 * Inserts a child node at the specified position without replacing existing children.
	 * 
	 * <p>
	 * This method shifts existing children at and after the insertion point to make room
	 * for the new child. It maintains bidirectional links between parent and child.
	 * </p>
	 * 
	 * <p><strong>Example:</strong></p>
	 * <pre>
	 * Before: children = [A, B, C]
	 * insertChildAt(1, X)
	 * After:  children = [A, X, B, C]
	 * </pre>
	 * 
	 * @param index the position to insert the child (must be >= 0 and <= getChildCount())
	 * @param a the child node to insert
	 * @return {@code true} if insertion was successful (implementation-dependent)
	 * @see #setChildAt(int, EvolNode)
	 * @see #addChild(EvolNode)
	 */
    boolean insertChildAt(int index, EvolNode a);

	/**
	 * Replaces the child at the specified position with a new child node.
	 * 
	 * <p>
	 * Unlike {@link #insertChildAt(int, EvolNode)}, this method replaces the existing
	 * child at position {@code k} rather than inserting. This operation maintains
	 * bidirectional links, establishing both:
	 * <ul>
	 *   <li>Down-link from this parent to the new child</li>
	 *   <li>Up-link from the new child to this parent</li>
	 * </ul>
	 * </p>
	 * 
	 * <p><strong>Example:</strong></p>
	 * <pre>
	 * Before: children = [A, B, C]
	 * setChildAt(1, X)
	 * After:  children = [A, X, C]  // B is replaced by X
	 * </pre>
	 * 
	 * @param k the index position of the child to replace
	 * @param a the new child node
	 * @return {@code false} if the node already exists in the child list (implementation-dependent)
	 * @see #insertChildAt(int, EvolNode)
	 * @see #getChildAt(int)
	 */
    boolean setChildAt(int k, EvolNode a);

	/**
	 * Removes and returns the child node at the specified index.
	 * 
	 * <p>
	 * This method breaks both the down-link (from this parent to the child) and
	 * the up-link (from the child to this parent), effectively severing the
	 * parent-child relationship.
	 * </p>
	 * 
	 * @param k the index of the child to remove (should satisfy {@code 0 <= k < getChildCount()})
	 * @return the removed child node, or {@code null} if {@code k} is out of bounds
	 * @see #removeChild(EvolNode)
	 * @see #removeAllChild()
	 */
    EvolNode removeChild(int k);

	/**
	 * Removes the specified child node from this node's child list.
	 * 
	 * <p>
	 * Searches for the given node in the child list and removes it if found.
	 * Both parent-to-child and child-to-parent links are broken.
	 * </p>
	 * 
	 * @param a the child node to remove
	 * @return {@code true} if the child was found and successfully removed,
	 *         {@code false} if the node was not in the child list
	 * @see #removeChild(int)
	 * @see #indexOfChild(EvolNode)
	 */
    boolean removeChild(EvolNode a);
	
	/**
	 * Returns the index of the specified child node in the child list.
	 * 
	 * @param a the child node to search for
	 * @return the zero-based index of the child, or -1 if not found
	 * @see #getChildAt(int)
	 * @see #removeChild(EvolNode)
	 */
    int indexOfChild(EvolNode a);

	/**
	 * Appends a child node to the end of the child list.
	 * 
	 * <p>
	 * This is a convenience method equivalent to {@code insertChildAt(getChildCount(), a)}.
	 * The child is added as the last child of this node.
	 * </p>
	 * 
	 * @param a the child node to add
	 * @see #insertChildAt(int, EvolNode)
	 * @see #removeChild(EvolNode)
	 */
	void addChild(EvolNode a);

	/**
	 * Removes all children from this node.
	 * 
	 * <p>
	 * After calling this method, {@link #getChildCount()} will return 0,
	 * making this node a leaf node. All parent-child links are broken.
	 * </p>
	 * 
	 * @see #removeChild(EvolNode)
	 * @see #removeChild(int)
	 */
	void removeAllChild();

	// ========================================================================================
	// Node identification and auxiliary data
	// ========================================================================================

	/**
	 * Returns the unique identifier for this node.
	 * 
	 * @return the node's ID
	 * @see #setSize(int)
	 */
	int getID();

	/**
	 * Returns the size value associated with this node.
	 * 
	 * <p>
	 * This is a multi-purpose variable used for various computations. Common uses include:
	 * <ul>
	 *   <li>Number of leaves in the subtree rooted at this node</li>
	 *   <li>Cluster size in UPGMA algorithm</li>
	 *   <li>Any other algorithm-specific metric</li>
	 * </ul>
	 * </p>
	 * 
	 * <p>
	 * <strong>Note:</strong> This provides quick access without needing to traverse
	 * the tree (unlike {@code NodeUtil.getNumOfLeaves()}).
	 * </p>
	 * 
	 * @return the size value
	 * @see #setSize(int)
	 */
	int getSize();

	/**
	 * Sets the size value for this node.
	 * 
	 * <p>
	 * This is a flexible variable that can represent different metrics depending on
	 * the algorithm or use case. For example:
	 * <ul>
	 *   <li><strong>UPGMA:</strong> Number of original sequences in the cluster</li>
	 *   <li><strong>Tree traversal:</strong> Number of descendants (leaves)</li>
	 *   <li><strong>Custom algorithms:</strong> Any integer metric needed</li>
	 * </ul>
	 * </p>
	 * 
	 * @param s the size value to set
	 * @see #getSize()
	 */
	void setSize(int s);

	/**
	 * Returns the double-precision value associated with this node.
	 * 
	 * <p>
	 * This is a general-purpose storage variable for algorithms that need to
	 * associate floating-point data with nodes. Common uses include:
	 * <ul>
	 *   <li>Bootstrap values</li>
	 *   <li>Support values</li>
	 *   <li>Posterior probabilities</li>
	 *   <li>Likelihood scores</li>
	 *   <li>Temporary computation results</li>
	 * </ul>
	 * </p>
	 * 
	 * @return the stored double value
	 * @see #setDoubleVariable(double)
	 */
	double getDoubleVariable();

	/**
	 * Sets a double-precision value for this node.
	 * 
	 * <p>
	 * This provides a fast way to store algorithm-specific floating-point data
	 * without extending the node class.
	 * </p>
	 * 
	 * @param value the double value to store
	 * @see #getDoubleVariable()
	 */
	void setDoubleVariable(double value);

	// ========================================================================================
	// Node name and branch length
	// ========================================================================================

	/**
	 * Sets the name/label for this node.
	 * 
	 * <p>
	 * For leaf nodes, this typically represents the taxon or sequence name.
	 * For internal nodes, this may represent clade names or be left empty.
	 * </p>
	 * 
	 * @param name the name to assign to this node
	 * @see #getName()
	 */
	void setName(String name);

	/**
	 * Returns the name/label of this node.
	 * 
	 * @return the node's name, or {@code null} if no name is set
	 * @see #setName(String)
	 */
	String getName();

	/**
	 * Sets the branch length leading to this node.
	 * 
	 * <p>
	 * In phylogenetic trees, this represents the evolutionary distance or time
	 * from this node to its parent. For the root node, this value is typically 0.0.
	 * </p>
	 * 
	 * @param len the branch length (typically >= 0.0)
	 * @see #getLength()
	 */
	void setLength(double len);

	/**
	 * Returns the branch length leading to this node.
	 * 
	 * <p>
	 * This represents the edge length between this node and its parent in the tree.
	 * </p>
	 * 
	 * @return the branch length
	 * @see #setLength(double)
	 */
	double getLength();

}