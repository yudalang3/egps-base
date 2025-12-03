package rest.ensembl.phylo;

import com.google.common.base.Joiner;
import evoltree.phylogeny.DefaultPhyNode;
import org.apache.logging.log4j.util.Strings;

import java.util.ArrayList;
import java.util.List;

/**
 * Customized Ensembl JSON tree parser with enhanced name formatting and taxonomy extraction.
 */
public class EnsJsonTreeParserCustomized extends EnsJsonTreeParser {
	
	
	List<String> output = new ArrayList<>();

	@Override
	protected DefaultPhyNode iterate2transferTree(TreeBean tree) {
		DefaultPhyNode node = new DefaultPhyNode();
		node.setLength(tree.branch_length);
		
		String humanNamedName = tree.getTaxonomy().getCommon_name();
		if (Strings.isEmpty(humanNamedName)) {
			humanNamedName = tree.getTaxonomy().getScientific_name();
		}
		
		List<TreeBean> children = tree.children;
		if (children == null) {
			node.setName(humanNamedName);
			
			output.add(tree.taxonomy.id + "\t" + humanNamedName);
			
			return node;
		}
		
		String name = Joiner.on('|').join(humanNamedName,tree.taxonomy.timetree_mya);
		node.setName(name);
		for (TreeBean treeBean : children) {
			DefaultPhyNode child = iterate2transferTree(treeBean);
			node.addChild(child);
		}
		
		return node;
	}
	
	public List<String> getOutput() {
		return output;
	}
	
}