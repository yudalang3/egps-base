package evoltree.txtdisplay;

import evoltree.struct.EvolNode;
import evoltree.struct.util.EvolNodeUtil;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.tuple.Pair;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * To display the tree with text
 * @author yudalang
 * @lastModifiedDate 2025-04-07
 */
public class TextTreeDescriber {
	private double tempVariable;
	private EvolNode tempCalculateNode;
	
	/**
	 * Like the phylip software, directly input a Node to visualize it! Command-line friendly and quick!<br>
	 * Directly display the evolutionary tree in file format, allowing direct viewing without a GUI. Currently, not fully polished, sometimes we don't need to open a separate application.
	 *
	 * @title displayNodesWithoutGuiByTextLines
	 * @createdDate 2021-03-16 14:40
	 * @lastModifiedDate 2021-03-16 14:40
	 * @lastModifiedDate 2025-04-07
	 * @author yudalang
	 *
	 * @param root the tree to display
	 */
	public void displayNodesWithoutGuiByTextLines(EvolNode root) {
		throw  new NotImplementedException("Not implemented.");
	}
	/**
	 * Provide a more convenient way to view a tree with default parameters.
	 *
	 * @title displayNodesWithoutGuiByTextLines
	 * @createdDate 2021-03-16 14:41
	 * @lastModifiedDate 2021-03-16 14:41
	 * @author yudalang
	 * @since 1.7
	 *
	 */
	public void displayNodesWithoutGui(EvolNode root, int width, int height, boolean onlyTopology) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date time = Calendar.getInstance().getTime();
		System.out.println("Now we start to display the node:" + simpleDateFormat.format(time));

		ReflectGraphicNode<EvolNode> reflectGraphicNode = new ReflectGraphicNode<>(root);

		if (onlyTopology) {
			EvolNodeUtil.iterateByLevelTraverse(root, node -> {
				node.setLength(1);
			});
		}

		Pair<Double, EvolNode> getMaxLengthOfRoot2Leaf = getMaxLengthOfRoot2Leaf(reflectGraphicNode);

		double ratio = width / getMaxLengthOfRoot2Leaf.getLeft();

		List<EvolNode> leaves = EvolNodeUtil.getLeaves(reflectGraphicNode);
		int numOfLeaves = leaves.size();
		int eachYLength = height / numOfLeaves;

		char[][] printChars = new char[height][width];
		for (char[] cs : printChars) {
			Arrays.fill(cs, ' ');
		}
		// This as loop index!
		tempVariable = 0;
		recursiveCalculate4Visualize(reflectGraphicNode, 0, ratio, eachYLength);

		Map<Integer, String> lineIndex2leafNameMap = new HashMap<>();
		assignChars2visualTree(reflectGraphicNode, printChars, lineIndex2leafNameMap);

		char[] blinkChars = new char[width];
		Arrays.fill(blinkChars, ' ');
		System.out.print(new String(blinkChars));
		System.out.println("Reference");

		for (int i = 0; i < height; i++) {
			char[] cs = printChars[i];
			System.out.print(new String(cs));

			String string = lineIndex2leafNameMap.get(i);

			if (string == null) {
				System.out.println();
			} else {
				System.out.println(string);
			}
		}
	}
	
	/**
	 * Calculate the maximum distance from root to leaf (excluding outgroup)<br>
	 * Excludes root length
	 * 
	 * @return first element is the value, second is the specific leaf node
	 */
	public Pair<Double, EvolNode> getMaxLengthOfRoot2Leaf(EvolNode node) {
		tempCalculateNode = null;
		tempVariable = -1;
		recursive2getMaxValueNoOutgroup(node, 0);
		Objects.requireNonNull(tempCalculateNode);
		EvolNode retNode = tempCalculateNode;
		tempCalculateNode = null;
		return Pair.of(tempVariable, retNode);
	}
	
	/**
	 * Calculate the maximum distance from root to leaf (excluding outgroups) by calling this recursive method<br>
	 * Excludes root length
	 */
	private void recursive2getMaxValueNoOutgroup(EvolNode node, double len) {
		double newLength = len + node.getLength();
		int childCount = node.getChildCount();
		if (childCount == 0) {
			if (newLength > tempVariable) {
				tempVariable = newLength;
				tempCalculateNode = node;
			}
		} else {
			for (int j = 0; j < childCount; j++) {
				recursive2getMaxValueNoOutgroup(node.getChildAt(j), newLength);
			}
		}
	}
	
	

	private static void assignChars2visualTree(ReflectGraphicNode node, char[][] printChars,
											   Map<Integer, String> lineIndex2leafNameMap) {
		int childCount = node.getChildCount();
		if (childCount != 0) {

			int firstChildY = 0, lastChildY = 0;
			for (int i = 0; i < childCount; i++) {
				ReflectGraphicNode childAt = (ReflectGraphicNode) node.getChildAt(i);


				assignChars2visualTree(childAt, printChars, lineIndex2leafNameMap);

				if (i == 0) {
					firstChildY = (int) childAt.getYParent();
				} else if (i == (childCount - 1)) {
					lastChildY = (int) childAt.getYParent();
				}
			}

			int xSelf = (int) node.getXSelf();

			for (int i = firstChildY; i < lastChildY; i++) {
				printChars[i][xSelf] = '|';
			}

		}

		int xSelf = (int) node.getXSelf();
		int ySelf = (int) node.getYSelf();
		int xParent = (int) node.getXParent();

		char[] cs = printChars[ySelf];
		// System.out.println("xSelf\tySelf\txParent: "+xSelf+" "+ySelf+" "+xParent);
		for (int i = xParent; i < xSelf; i++) {
			cs[i] = '-';
		}

		// Display inner node ID
		if (xSelf < printChars[0].length) {
			int id = node.getID();
			String valueOf = String.valueOf(id);
			for (int i = 0; i < valueOf.length(); i++) {
				cs[xSelf + i] = valueOf.charAt(i);
			}
		}
		// Display branch length
		if (node.getParentCount() != 0) {
			if (ySelf - 1 > -1) {
				char[] cs2 = printChars[ySelf - 1];
				int xx = (xSelf + xParent) / 2 - 1;
				
				int len = (int) node.getLength();
				String valueOf = String.valueOf(len);
				for (int i = 0; i < valueOf.length(); i++) {
					cs2[xx + i] = valueOf.charAt(i);
				}
			}
		}

		if (childCount == 0) {
			lineIndex2leafNameMap.put(ySelf, node.getName());
		}

	}

	private void recursiveCalculate4Visualize(ReflectGraphicNode root, double cumulateBranchLengthExceptCurrent,
											  double r, int eachY) {
		int childCount = root.getChildCount();
		if (childCount == 0) {
			// start from 0
			double basey = tempVariable * eachY;
			tempVariable++;

			double xx = cumulateBranchLengthExceptCurrent * r;
			root.setXParent(xx);
			root.setXSelf(root.getLength() * r + xx);
			root.setYParent(basey);
			root.setYSelf(basey);
		} else {

			// start from 0
			double xx = cumulateBranchLengthExceptCurrent * r;
			root.setXParent(xx);
			root.setXSelf(root.getLength() * r + xx);

			double basey = 0;
			for (int i = 0; i < childCount; i++) {
				ReflectGraphicNode childAt = (ReflectGraphicNode) root.getChildAt(i);
				recursiveCalculate4Visualize(childAt, cumulateBranchLengthExceptCurrent + root.getLength(), r, eachY);
				basey += 0.5 * childAt.getYSelf();
			}

			root.setYParent(basey);
			root.setYSelf(basey);
		}

	}

}
