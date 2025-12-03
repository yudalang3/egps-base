package rest.interpro.entrytaxon;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Fragment data model for protein sequence regions.
 * 
 * <p>
 * This class represents a fragment or region within a protein sequence,
 * providing location information and metadata about specific sequence segments.
 * It is used in the context of InterPro analysis to describe protein domains,
 * conserved regions, or other functional units within sequences.
 * </p>
 * 
 * <h2>Key Properties:</h2>
 * <ul>
 *   <li><strong>start:</strong> Start position (1-based) of the fragment in the sequence</li>
 *   <li><strong>end:</strong> End position of the fragment in the sequence</li>
 *   <li><strong>dc_status:</strong> Domain consensus status indicator</li>
 *   <li><strong>representative:</strong> Whether this fragment is representative of the family</li>
 * </ul>
 * 
 * <h2>JSON Field Mapping:</h2>
 * <p>
 * This class handles the JSON field naming convention where Java variable names
 * cannot contain hyphens. The field {@code dc_status} maps to the JSON field
 * {@code "dc-status"} using the {@code @JSONField} annotation.
 * </p>
 * 
 * <h2>Bioinformatics Applications:</h2>
 * <ul>
 *   <li><strong>Domain Annotation:</strong> Define protein domain boundaries</li>
 *   <li><strong>Fragment Assembly:</strong> Reconstruct complete domain architectures</li>
 *   <li><strong>Coverage Analysis:</strong> Determine sequence coverage by domains</li>
 *   <li><strong>Quality Control:</strong> Validate domain predictions</li>
 *   <li><strong>Comparative Analysis:</strong> Compare domain architectures across proteins</li>
 * </ul>
 * 
 * <h2>InterPro Integration:</h2>
 * <p>
 * Fragments are used throughout InterPro to represent protein domains, sites,
 * and regions identified by various member databases. They provide precise
 * location information for functional annotations.
 * </p>
 * 
 * <h2>Usage Example:</h2>
 * <pre>
 * Fragments domain = new Fragments();
 * domain.setStart(25);
 * domain.setEnd(150);
 * domain.setDc_status("T");
 * domain.setRepresentative(true);
 * 
 * // Use for domain mapping and analysis
 * int domainLength = domain.getEnd() - domain.getStart() + 1;
 * </pre>
 * 
 * @author yudal
 * @see com.alibaba.fastjson.annotation.JSONField
 * @see IPREntryTaxonInfoRecordBean
 * @since 1.0
 */
public class Fragments {

	int start;
	int end;
	
	@JSONField(name = "dc-status")
	String dc_status;
	boolean representative;
	
	
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}
	public String getDc_status() {
		return dc_status;
	}
	public void setDc_status(String dc_status) {
		this.dc_status = dc_status;
	}
	public boolean isRepresentative() {
		return representative;
	}
	public void setRepresentative(boolean representative) {
		this.representative = representative;
	}
	
	
	
}
