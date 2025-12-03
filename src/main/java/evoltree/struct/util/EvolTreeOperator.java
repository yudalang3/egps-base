package evoltree.struct.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import evoltree.struct.ArrayBasedNode;
import evoltree.struct.EvolNode;


/**
 * Utility class for advanced phylogenetic tree operations including rerooting and ancestral analysis.
 * 
 * <p>
 * This class provides sophisticated tree manipulation operations that are essential for
 * phylogenetic analysis, particularly focusing on tree rerooting and finding common ancestors.
 * These operations are fundamental for comparative phylogenetics and evolutionary studies.
 * </p>
 * 
 * <h2>Core Functionalities</h2>
 * <ul>
 *   <li><strong>Tree Rerooting:</strong> Change the root position of phylogenetic trees</li>
 *   <li><strong>Midpoint Rooting:</strong> Root tree at evolutionary midpoint</li>
 *   <li><strong>MRCA Calculation:</strong> Find most recent common ancestor of nodes</li>
 *   <li><strong>Distance Calculations:</strong> Compute evolutionary distances between taxa</li>
 * </ul>
 * 
 * <h2>Rerooting Methods</h2>
 * <p>
 * Rerooting is a fundamental operation in phylogenetics that allows placing the root
 * of an unrooted or differently rooted tree at a new position without changing the
 * tree topology.
 * </p>
 * 
 * <h3>Why Reroot Trees?</h3>
 * <ul>
 *   <li><strong>Outgroup Rooting:</strong> Place root using known outgroup taxa</li>
 *   <li><strong>Midpoint Rooting:</strong> Root at the midpoint of longest path</li>
 *   <li><strong>Molecular Clock:</strong> Assume equal evolutionary rates</li>
 *   <li><strong>Visualization:</strong> Orient tree for better display</li>
 *   <li><strong>Comparison:</strong> Align trees for comparison analyses</li>
 * </ul>
 * 
 * <h2>Midpoint Rooting Algorithm</h2>
 * <p>
 * The midpoint rooting method ({@code rootAtMidPoint}) places the root at the midpoint
 * of the longest path between any two leaf nodes. This assumes a molecular clock
 * (equal evolutionary rates across lineages).
 * </p>
 * 
 * <h3>Algorithm Steps:</h3>
 * <ol>
 *   <li>Find all leaf nodes in the tree</li>
 *   <li>Calculate pairwise distances for all leaf pairs</li>
 *   <li>Identify the two most distant leaves</li>
 *   <li>Find their most recent common ancestor (MRCA)</li>
 *   <li>Calculate midpoint along path between the two leaves</li>
 *   <li>Reroot tree at the midpoint position</li>
 * </ol>
 * 
 * <h3>Complexity:</h3>
 * <ul>
 *   <li><strong>Time Complexity:</strong> O(n²) where n = number of leaves</li>
 *   <li><strong>Space Complexity:</strong> O(n) for storing leaf nodes</li>
 *   <li><strong>Note:</strong> Can be optimized using advanced algorithms (see ETE toolkit)</li>
 * </ul>
 * 
 * <h2>Most Recent Common Ancestor (MRCA)</h2>
 * <p>
 * The {@code getMostRecentCommonAncestor} method finds the lowest common ancestor of
 * two nodes in the tree. This is essential for evolutionary distance calculations and
 * phylogenetic analysis.
 * </p>
 * 
 * <h3>MRCA Algorithm:</h3>
 * <ol>
 *   <li>Traverse from first node to root, storing all ancestors</li>
 *   <li>Traverse from second node toward root</li>
 *   <li>Return first ancestor found in the stored set</li>
 * </ol>
 * 
 * <h3>Use Cases:</h3>
 * <ul>
 *   <li>Calculate evolutionary distance between taxa</li>
 *   <li>Identify clade membership</li>
 *   <li>Determine divergence times</li>
 *   <li>Analyze phylogenetic relationships</li>
 * </ul>
 * 
 * <h2>Usage Examples</h2>
 * <pre>
 * // Example 1: Find MRCA of two taxa
 * EvolNode speciesA = findNodeByName(root, "Homo_sapiens");
 * EvolNode speciesB = findNodeByName(root, "Pan_troglodytes");
 * EvolNode mrca = EvolTreeOperator.getMostRecentCommonAncestor(speciesA, speciesB);
 * System.out.println("Common ancestor: " + mrca.getName());
 * 
 * // Example 2: Midpoint rooting
 * EvolNode unrootedTree = parseNewickTree("((A,B),(C,D));");
 * EvolNode rerootedTree = EvolTreeOperator.rootAtMidPoint(unrootedTree);
 * 
 * // Example 3: Calculate distance between two taxa
 * double distance = EvolTreeOperator.getTwoOTUsDistanceFromMRCA(speciesA, speciesB);
 * System.out.println("Evolutionary distance: " + distance);
 * </pre>
 * 
 * <h2>Key Methods</h2>
 * <ul>
 *   <li><strong>getMostRecentCommonAncestor:</strong> Find MRCA of two nodes</li>
 *   <li><strong>rootAtMidPoint:</strong> Root tree at evolutionary midpoint</li>
 *   <li><strong>setRootAt:</strong> Reroot tree at specified node/branch</li>
 *   <li><strong>getTwoOTUsDistanceFromMRCA:</strong> Calculate distance between taxa</li>
 * </ul>
 * 
 * <h2>Important Considerations</h2>
 * <ul>
 *   <li><strong>Binary Trees:</strong> Primarily designed for bifurcating trees</li>
 *   <li><strong>Multifurcating Support:</strong> Also works with multifurcating trees</li>
 *   <li><strong>Performance:</strong> Midpoint rooting can be slow for very large trees</li>
 *   <li><strong>Molecular Clock:</strong> Midpoint rooting assumes clock-like evolution</li>
 * </ul>
 * 
 * <h2>Integration with eGPS</h2>
 * <ul>
 *   <li>Complements {@link EvolNodeUtil} for comprehensive tree operations</li>
 *   <li>Used in tree comparison and visualization workflows</li>
 *   <li>Essential for phylogenetic tree standardization</li>
 * </ul>
 * 
 * <h2>References</h2>
 * <ul>
 *   <li><a href="http://cabbagesofdoom.blogspot.com/2012/06/how-to-root-phylogenetic-tree.html">
 *       How to Root a Phylogenetic Tree</a></li>
 *   <li>Farris, J.S. (1972). "Estimating phylogenetic trees from distance matrices"</li>
 * </ul>
 *
 * @author yudalang
 * @version 1.0
 * @since 1.0
 * @see EvolNodeUtil For general tree utility operations
 * @see EvolNode For the node interface
 */
public class EvolTreeOperator {

	private static boolean debug = false;

	private static HashSet<EvolNode> nodeHashSet = new HashSet<>();

	/**
	 * 获取最近公共祖先节点
	 * 如果两个节点来自不同的根节点，则返回null
	 *
	 * @param node1 第一个节点
	 * @param node2 第二个节点
	 * @return 最近公共祖先节点，如果不存在则返回null
	 */
	public static <T extends EvolNode> T getMostRecentCommonAncestor(T node1, T node2) {
		nodeHashSet.clear();
		T tempNode = node1;

		nodeHashSet.add(tempNode);

		while (tempNode.getParentCount() != 0) {
			tempNode = EvolNodeUtil.getParent(tempNode).get();
			nodeHashSet.add(tempNode);
		}

		tempNode = node2;

		while (tempNode != null) {
			if (nodeHashSet.contains(tempNode)) {
				return tempNode;
			}
			if (tempNode.getParentCount() == 0) {
				tempNode = null;
			} else {
				tempNode = EvolNodeUtil.getParent(tempNode).get();
			}
		}

		return null;
	}


	/**
	 * 也只考虑了二叉树，但是这个多叉树也是可以用的！ This will be rooted by the mid-point method
	 * http://cabbagesofdoom.blogspot.com/2012/06/how-to-root-phylogenetic-tree.html
	 * Reroot the tree at the mid-point between two most distant external nodes.
	 * 
	 * @author yudalang
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static EvolNode rootAtMidPoint(EvolNode root) throws InstantiationException, IllegalAccessException {

		double longest = 0.0e0;

		List<EvolNode> leafList = EvolNodeUtil.getLeavesByRecursive(root);

		EvolNode[] leafs = leafList.toArray(new EvolNode[1]);
		leafList = null;
		/**
		 * 这个方法还是有提升的空间，还是用ete4吧，那个有更加高效的计算方法。
		 * 这里要遍历 n*n的时间复杂度，但是这个方法还是有提升空间，
		 */

		EvolNode midA = null, midB = null;
		int size = leafs.length;
		for (int i = 0; i < size - 1; i++) {
			for (int j = i + 1; j < size; j++) {
				double x = getTwoOTUsDistanceFromMRCA(leafs[i], leafs[j]);
				if (x > longest) {

					midA = leafs[i];
					midB = leafs[j];
					longest = x;
					if (debug) {
						System.out.println("x= " + x + " " + midA.getName() + " " + midB.getName());
					}
				}
			}
		}

		EvolNode mrca = getMostRecentCommonAncestor(midA, midB);
		for (int kk = 0; kk < 2; kk++) {
			double x = 0.0e0;
			EvolNode tmp = midA;
			if (kk == 1)
				tmp = midB;

			while (true) {
				double bl = tmp.getLength();
				if (x + bl >= longest / 2) {
					if (debug) {
						System.out.println("The root is at branch between nodes " + tmp.getName() + " "
								+ (tmp.getParent()).getName() + " bl=" + tmp.getLength() + "\n" + "distance to node "
								+ tmp.getName() + " is " + (longest / 2 - x));
					}
					break;
				}

				x += bl;
				tmp = tmp.getParent();
				if (tmp == mrca)
					break;
			}

			if (tmp != mrca) {
				root = setRootAt(root, tmp, longest / 2 - x);

				return root;
			}
		}

		return null;
	}

	/**
	 *
	 * 这个调用这个方法比先调用 {@link #getMostRecentCommonAncestor(EvolNode, EvolNode)} 更加高效！
	 */
	public static double getTwoOTUsDistanceFromMRCA(EvolNode node1, EvolNode node2) {
		EvolNode mrca = node1;

		HashSet<EvolNode> hashSet = new HashSet<>();
		hashSet.add(mrca);

		while (mrca.getParentCount() != 0) {
			mrca = mrca.getParent();
			hashSet.add(mrca);
		}

		mrca = node2;
		double ret = 0;
		while (mrca.getParentCount() != 0) {
			ret += mrca.getLength();
			if (hashSet.contains(mrca)) {
				break;
			}

			mrca = mrca.getParent();
		}

		EvolNode temp = node1;
		while (temp != mrca) {
			ret += temp.getLength();
			temp = temp.getParent();
		}

		return ret;
	}

	/**
	 * <pre>
	 * 例如对于一棵树
	 *                        |---------node         a
	 *        |---------------|oldParent
	 *        |               |--------------------- b
	 *        |
	 * oldRoot|        |---------------------------- c
	 *        |        |
	 *        |--------|             |-------------- d
	 *                 |-------------|
	 *                               |-------------- e
	 * 
	 * </pre>
	 * @param node1
	 * @param node2
	 * @return 假设输入的节点是a和b，那么返回2；假如输入的是 a和c，那么返回的是4; 假如输入的是 a,a那么返回0
	 * 假如输入的两个节点到了根都没有MRCA那么就返回 -1 , 这种情况就是说：两个节点来自于不同的树。
	 */
	public static int getTwoOTUsLevelsFromMRCA(EvolNode node1, EvolNode node2) {
		Objects.requireNonNull(node1);
		Objects.requireNonNull(node2);
		
		if (node1 == node2) {
			return 0;
		}
		
		EvolNode mrca = node1;

		HashSet<EvolNode> hashSet = new HashSet<>();
		hashSet.add(mrca);

		while (mrca.getParentCount() != 0) {
			mrca = mrca.getParent();
			hashSet.add(mrca);
		}

		mrca = node2;
		int ret = 0;
		boolean notGetMRCA = true;
		while (mrca.getParentCount() != 0) {
			ret += 1;
			mrca = mrca.getParent();
			if (hashSet.contains(mrca)) {
				notGetMRCA = false;
				break;
			}
		}
		
		if (notGetMRCA) {
			return -1;
		}
		
		

		EvolNode temp = node1;
		while (temp.getParentCount() != 0) {
			ret += 1;
			temp = temp.getParent();
			if ( temp == mrca) {
				break;
			}
		}

		return ret;
	}

	/**
	 * Place the root of the tree on the Node of node such that the length of node
	 * is equal to length!
	 * 
	 * yudalang: 下面讲算法：先来一个图，有图有真相。
	 * 
	 * <pre>
	 * 定根前的树：
	 *                        |---------node         a
	 *        |---------------|oldParent
	 *        |               |--------------------- b
	 *        |
	 * oldRoot|        |---------------------------- c
	 *        |        |
	 *        |--------|             |-------------- d
	 *                 |-------------|
	 *                               |-------------- e
	 *                               
	 *  定根后的树：我们用a重新定根
	 *  
	 *        |-------a
	 *        |               
	 *        |
	 * newRoot|        |----------- b
	 *        |        |                      |----e
	 *        |--------|             |--------|
	 *                 |-------------|        |----d
	 *                               |
	 *                               |-------------- c
	 * 
	 * 
	 * </pre>
	 * 
	 * 算法：首先找到 node到root的一条路径。比如这里的话是node->oldParent->oldRoot。 然后反转他们的继承关系。
	 * 最后看看原来的根节点那是否是一个Child，是的话就去掉原来的根。
	 * 
	 * 注意： 1) 如果原来有三叉树，并且这个三叉分支不再路径上，那么就还是三叉树。 2) 这里要生成新的节点,
	 * 我们运用的是反射来生成新的节点。故要处理这个反射异常。 3) 我们需要这个EvolNode 的实现有空的构造方法。
	 * 
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	private static EvolNode setRootAt(EvolNode root, EvolNode node, double length)
			throws InstantiationException, IllegalAccessException {
		// List a is an order list of parents with the first being the
		// paraent and the last being the root.
		// yudalang：一定要看懂了，因为出问题了！
		// yudalang：第一个是node，最后一个是root

		List<EvolNode> a = getPath(root, node);
		int size = a.size();
		// size 为1的话，第二个参数node就是它自己！所以不用reroot了。
		if (size < 2) {
			return root;
		}

		EvolNode oldParent = node.getParent();
		EvolNode oldRoot = root;

		// redirect the parents
		// yudalang: 这不就是root吗
		EvolNode previous = a.get(size - 1);
		// System.out.println(previous);
		for (int i = size - 2; i >= 0; i--) {
			EvolNode current = a.get(i);
			previous.removeChild(current);
			current.addChild(previous);

			previous.setLength(current.getLength());
			previous = current;
		}
		node.removeChild(oldParent);

		// add the new root
		EvolNode newRoot = node.getClass().newInstance();
		newRoot.addChild(node);

		newRoot.addChild(oldParent);

		node.setLength(length);
		oldParent.setLength(oldParent.getLength() - length);

		// remove the old root if it only has one child
		if (oldRoot.getChildCount() == 1) {
			EvolNode c = oldRoot.removeChild(0);
			c.setLength(c.getLength() + oldRoot.getLength());
			oldRoot.getParent().addChild(c);
			oldRoot.removeAllParent();
		}

		return newRoot;
	}

	/**
	 * Get the path from specific node to node! <b>Note:</b> The parameter root
	 * should be the ancestor of child!
	 * 
	 * @param root  the start node, should be ancestor of child!
	 * @param child the end node
	 * @return an List of nodes from child to root ( include root and child)!
	 * @author yudalang
	 */
	public static ArrayList<EvolNode> getPath(EvolNode root, EvolNode child) {
		return EvolNodeUtil.getPath(root, child);
	}

	/**
	 * 
	 * <pre>
	* 定根前的树：
	*                        |---------             a
	*        |---------------|node
	*        |               |--------------------- b
	*        |
	* oldRoot|        |---------------------------- c
	*        |        |
	*        |--------|             |-------------- d
	*                 |-------------|
	*                               |-------------- e
	*                               
	*  定根后的树：我们用node作为根
	*  
	*        
	*                 |-----------a       
	*                 |
	*                 |----------- b
	*                 |                                |----e
	*             node|                       |--------|
	*                 |------（oldRoot）-------|        |----d
	*                                         |
	*                                         |-------------- c
	* 
	* oldRoot只有一个child的话，最后要将oldRoot去除
	* 
	* 
	* 
	* 和setRootAt方法的区别是：setRootAt是将输入的node作为外群重新定根，是成为根的child
	* 而此方法则是将输入的node作为根进行定根
	* 
	* node需要为一个内节点，如果是个叶子作为根的话，则其只有一个child
	 * </pre>
	 * 
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * 
	 * @title setCertainNodeAsRoot
	 * @createdDate 2021-02-24 09:11
	 * @lastModifiedDate 2021-02-24 09:11
	 * @author yjn
	 * @since 1.7
	 * 
	 * @param root
	 * @param node
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @return EvolNode
	 */
	public static EvolNode setCertainNodeAsRoot(EvolNode root, EvolNode node)
			throws InstantiationException, IllegalAccessException {

//		if(node.getChildCount()==0) {
//			System.out.println("The reroot node is a leaf,can't reroot!");
//			return root;
//		}else {

		// List a is an order list of parents with the first being the
		// parent and the last being the root.

		// yudalang：第一个是node，最后一个是root
		EvolNode oldRoot = root;
		List<EvolNode> a = getPath(root, node);
		int size = a.size();
		// size 为1的话，第二个参数node就是它自己！所以不用reroot了。
		if (size < 2) {
			return root;
		}

		// redirect the parents
		// yudalang: 这不就是root吗
		EvolNode previous = a.get(size - 1);
		// System.out.println(previous);
		for (int i = size - 2; i >= 0; i--) {
			EvolNode current = a.get(i);
			previous.removeChild(current);
			current.addChild(previous);

			previous.setLength(current.getLength());
			previous = current;
		}

		// remove the old root if it only has one child
		if (oldRoot.getChildCount() == 1) {
			EvolNode c = oldRoot.removeChild(0);
			c.setLength(c.getLength() + oldRoot.getLength());
			oldRoot.getParent().addChild(c);
			oldRoot.removeAllParent();
		}
		return node;
//		}

	}

	/**
	 * If root is null, or the outgroup name cannot be found in the tree (with the
	 * root), return null. <b>Note:</b> This method not used, so I have not
	 * implement Junit4 test!
	 *
	 * @param root
	 * @param outgroupName
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static EvolNode setRootAt(EvolNode root, String outgroupName)
			throws InstantiationException, IllegalAccessException {

		// Check if already rooted
		int childCount = root.getChildCount();
		for (int i = 0; i < childCount; i++) {
			EvolNode child = root.getChildAt(i);
			String leafName = child.getName();
			if (leafName != null && leafName.equalsIgnoreCase(outgroupName)) {
				return root;
			}
		}

		EvolNode outgroupNode = null;

		List<EvolNode> leaves = EvolNodeUtil.getLeavesByRecursive(root);
		for (int i = 0; i < leaves.size(); i++) {
			EvolNode leaf = leaves.get(i);

			String leafName = leaf.getName();
			if (leafName != null && leafName.equalsIgnoreCase(outgroupName)) {
				outgroupNode = leaf;

				break;
			}
		}

		Objects.requireNonNull(outgroupNode, "Can't find reroot OTU!");
		EvolNode newRoot = setRootAt(root, outgroupNode, outgroupNode.getLength() * 0.5);

		return newRoot;
	}

	/**
	 * 字面意思，调用了{@link #setRootAt(EvolNode, EvolNode, double)}
	 * 
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static EvolNode rerootByNode(EvolNode root, EvolNode reNode)
			throws InstantiationException, IllegalAccessException {
		return setRootAt(root, reNode, reNode.getLength() * 0.5);
	}

	
	/**
	 * 
	 * Reroot method write by Gao feng!
	 */
	public static EvolNode rerootGF(EvolNode root, EvolNode reNode) {
		if (reNode.getParent() == root) {
			return null;
		}

		EvolNode newRoot = shadowCloneOneNode(reNode);
		double dist = reNode.getLength() / 2.0;
		reNode.setLength(dist);

		EvolNode sib = new ArrayBasedNode();
		sib.setLength(dist);

		EvolNode nodPar = reNode.getParent();

		reNode.removeAllParent();
		// add sibling of node
		while (nodPar.getChildCount() > 0) {
			EvolNode tmpChild = nodPar.getChildAt(0);
			tmpChild.removeAllParent();
			sib.addChild(tmpChild);
		}

		EvolNode nextNode = creNode(root, nodPar);
		if (nextNode == null) {
			if (sib.getChildCount() == 1) {
				sib = sib.getChildAt(0);
				sib.setLength(dist);
			}
		} else {
			sib.addChild(nextNode);
		}

		newRoot.addChild(sib);
		newRoot.addChild(reNode);

		return newRoot;
	}

	private static EvolNode creNode(EvolNode root, EvolNode node) {

		if (root == node || node == null) {
			return null;
		}

		EvolNode nodPar = node.getParent();
		node.removeAllParent();
		// add sibling of node
		while (nodPar.getChildCount() > 0) {
			EvolNode tmpChild = nodPar.getChildAt(0);
			tmpChild.removeAllParent();
			node.addChild(tmpChild);
		}

		EvolNode nextNode = creNode(root, nodPar);
		if (nextNode == null) {
			if (node.getChildCount() == 1) {
				double nextBraLen = node.getLength();
				node = node.getChildAt(0);
				node.setLength(node.getLength() + nextBraLen);
			}
		} else {
			node.addChild(nextNode);
		}

		return node;
	}

	/**
	 * shadow copy the input node!
	 * 
	 * @author yudalang
	 */
	public static EvolNode copyTheTree(EvolNode root) {
		EvolNode newRoot = shadowCloneOneNode(root);
		copyTheNode(newRoot, root);
		return newRoot;
	}

	public static EvolNode shadowCloneOneNode(EvolNode node) {
		ArrayBasedNode ret = new ArrayBasedNode();
		ret.setLength(node.getLength());
		ret.setID(node.getID());
		ret.setName(node.getName());
		return ret;
	}

	private static void copyTheNode(EvolNode newNode, EvolNode oldNode) {
		for (int i = 0; i < oldNode.getChildCount(); i++) {
			EvolNode oldChild = oldNode.getChildAt(i);
			EvolNode newChild = shadowCloneOneNode(oldChild);
			newNode.addChild(newChild);

			copyTheNode(newChild, oldChild);
			// System.out.println(newNode + " -> " + newChild);
		}
	}

	/**
	 * Compute the deeps of given tree!<br>
	 * <b>Note:</b> Not the longest deep nor does the shortest deep!
	 * 
	 * <pre>
	 * e.g. in this tree the deeps is 1.
	 *   |----leaf1
	 * --|
	 *   |------leaf2
	 * </pre>
	 *
	 * @author yudalang
	 */
	public static int getTreeDepth(EvolNode root) {
		int ret = 0;
		EvolNode tmp = root;

		while (tmp.getChildCount() != 0) {
			ret++;
			tmp = tmp.getFirstChild();
		}
		return ret;
	}

	/**
	 * 得到所有树的枝长，<strong>不</strong>包括根的长度！
	 */
	public static double getTreeLength(EvolNode root) {
		final double rootLength = 0;
		return getTreeLength(root, rootLength);
	}

	private static double getTreeLength(EvolNode root, double treeLength) {
		treeLength += root.getLength();
		for (int i = 0; i < root.getChildCount(); i++) {
			treeLength = getTreeLength(root.getChildAt(i), treeLength);
		}
		return treeLength;
	}

	public static void main(String[] args) {
		
		/*
		 * <pre>
		 *            
		 *         |-----leaf1
		 * root ---|  
		 *         |
		 *         |            |---- leaf3
		 *         |------leaf2-|
		 *                      |---- leaf4
		 *                      
		 * </pre>
		 */
		ArrayBasedNode root = new ArrayBasedNode();
		ArrayBasedNode leaf1 = new ArrayBasedNode();
		ArrayBasedNode leaf2 = new ArrayBasedNode();
		ArrayBasedNode leaf3 = new ArrayBasedNode();
		ArrayBasedNode leaf4 = new ArrayBasedNode();
		
		root.addChild(leaf1);
		root.addChild(leaf2);
		
		leaf2.addChild(leaf3);
		leaf2.addChild(leaf4);
		
		double twoOTUsLevelsFromMRCA = getTwoOTUsLevelsFromMRCA(leaf1,leaf2);
		System.out.println( twoOTUsLevelsFromMRCA == 2);
		
		twoOTUsLevelsFromMRCA = getTwoOTUsLevelsFromMRCA(leaf1,leaf3);
		System.out.println( twoOTUsLevelsFromMRCA == 3);
		twoOTUsLevelsFromMRCA = getTwoOTUsLevelsFromMRCA(leaf3,leaf1);
		System.out.println( twoOTUsLevelsFromMRCA == 3);
		
		twoOTUsLevelsFromMRCA = getTwoOTUsLevelsFromMRCA(leaf1,leaf4);
		System.out.println( twoOTUsLevelsFromMRCA == 3);
		
		twoOTUsLevelsFromMRCA = getTwoOTUsLevelsFromMRCA(leaf1,leaf1);
		System.out.println( twoOTUsLevelsFromMRCA == 0);
		twoOTUsLevelsFromMRCA = getTwoOTUsLevelsFromMRCA(root,leaf1);
		System.out.println( twoOTUsLevelsFromMRCA == 1);
		twoOTUsLevelsFromMRCA = getTwoOTUsLevelsFromMRCA(root,root);
		System.out.println( twoOTUsLevelsFromMRCA == 0);
		twoOTUsLevelsFromMRCA = getTwoOTUsLevelsFromMRCA(root,leaf4);
		System.out.println( twoOTUsLevelsFromMRCA == 2);
		
		ArrayBasedNode anotherRoot = new ArrayBasedNode();
		twoOTUsLevelsFromMRCA = getTwoOTUsLevelsFromMRCA(root,anotherRoot);
		System.out.println( twoOTUsLevelsFromMRCA == -1);
	}
}