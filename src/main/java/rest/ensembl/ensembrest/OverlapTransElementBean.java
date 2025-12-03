package rest.ensembl.ensembrest;

import com.google.common.base.Joiner;

/**
 * Data model for genomic feature overlap elements in Ensembl REST API.
 * 
 * <p>
 * This class represents individual protein features and domain annotations
 * retrieved from the Ensembl REST API. It provides comprehensive information
 * about protein domains, features, and their genomic locations, enabling
 * detailed analysis of protein structure and function annotations.
 * </p>
 * 
 * <h2>JSON Response Structure:</h2>
 * <pre>
 * {
 *   "seq_region_name": "ENSP00000363827",
 *   "interpro": "IPR000742",
 *   "translation_id": 1169849,
 *   "type": "Pfam",
 *   "cigar_string": "",
 *   "hseqname": "PF00008",
 *   "end": 4138,
 *   "start": 4108,
 *   "id": "PF00008",
 *   "hit_end": 31,
 *   "feature_type": "protein_feature",
 *   "align_type": null,
 *   "description": "EGF-like domain",
 *   "hit_start": 1,
 *   "Parent": "ENST00000374695"
 * }
 * </pre>
 * 
 * <h2>Key Properties:</h2>
 * <ul>
 *   <li><strong>seq_region_name:</strong> Ensembl protein sequence identifier</li>
 *   <li><strong>interpro:</strong> InterPro entry accession number</li>
 *   <li><strong>translation_id:</strong> Internal Ensembl translation identifier</li>
 *   <li><strong>type:</strong> Database source (Pfam, Prosite, SMART, etc.)</li>
 *   <li><strong>hseqname:</strong> Domain/family identifier from source database</li>
 *   <li><strong>start/end:</strong> Protein sequence coordinates</li>
 *   <li><strong>description:</strong> Functional description of the domain</li>
 *   <li><strong>feature_type:</strong> Type of protein feature annotation</li>
 * </ul>
 * 
 * <h2>Protein Domain Analysis:</h2>
 * <p>This class is essential for analyzing protein domain architecture
 * and functional annotations. It provides detailed information about
 * domain boundaries, functional descriptions, and database cross-references.</p>
 * 
 * <h2>Bioinformatics Applications:</h2>
 * <ul>
 *   <li><strong>Domain Architecture:</strong> Map protein domain organization</li>
 *   <li><strong>Functional Annotation:</strong> Assign functions based on domain predictions</li>
 *   <li><strong>Cross-Database Mapping:</strong> Link domains across multiple databases</li>
 *   <li><strong>Structural Analysis:</strong> Analyze 3D structure predictions</li>
 *   <li><strong>Comparative Analysis:</strong> Compare domains across protein families</li>
 * </ul>
 * 
 * <h2>Database Integration:</h2>
 * <p>Features can originate from multiple member databases of InterPro:
 * Pfam (protein families), Prosite (functional sites), SMART (signaling domains),
 * CDD (conserved domains), TIGRFAMs (protein families), and others.</p>
 * 
 * <h2>Coordinate Systems:</h2>
 * <ul>
 *   <li><strong>Protein Coordinates:</strong> start/end positions on protein sequence</li>
 *   <li><strong>HMM Coordinates:</strong> hit_start/hit_end positions on profile HMM</li>
 *   <li><strong>CIGAR String:</strong> Alignment information for complex domains</li>
 * </ul>
 * 
 * <h2>Usage Example:</h2>
 * <pre>
 * OverlapTransElementBean feature = new OverlapTransElementBean();
 * feature.setSeqRegionName("ENSP00000363827");
 * feature.setInterpro("IPR000742");
 * feature.setType("Pfam");
 * feature.setStart("4108");
 * feature.setEnd("4138");
 * feature.setDescription("EGF-like domain");
 * 
 * // Extract key information
 * String proteinId = feature.getSeqRegionName();
 * String domainType = feature.getType();
 * int domainStart = Integer.parseInt(feature.getStart());
 * int domainEnd = Integer.parseInt(feature.getEnd());
 * 
 * // Calculate domain properties
 * int domainLength = domainEnd - domainStart + 1;
 * String description = feature.getDescription();
 * </pre>
 * 
 * <h2>String Representation:</h2>
 * <p>The toString() method provides a tab-separated output format:
 * {@code id\tstart\tend\ttype\tfeature_type\tdescription}
 * This format is suitable for importing into spreadsheet applications
 * or further processing with command-line tools.</p>
 * 
 * @see OverlapTransBeanParser
 * @see RestGetProteinDomains
 * @author yudal
 * @since 1.0
 */
public class OverlapTransElementBean {

	private String seq_region_name;
	private String id;
	private String hseqname;
	private String description;
	private String type;
	private String interpro;
	private String hit_start;
	private String hit_end;
	private String feature_type;
	private String start;
	private String end;
	private String align_type;
	private String cigar_string;
	private String translation_id;
	private String Parent;

	
	@Override
	public String toString() {
		//输出你自己想要的信息
		String join = Joiner.on('\t').join(id, start, end, type, feature_type, description );
		
		return join;

	}
	
	public String getHit_start() {
		return hit_start;
	}
	public void setHit_start(String hit_start) {
		this.hit_start = hit_start;
	}
	public String getFeature_type() {
		return feature_type;
	}
	public void setFeature_type(String feature_type) {
		this.feature_type = feature_type;
	}
	public String getHit_end() {
		return hit_end;
	}
	public void setHit_end(String hit_end) {
		this.hit_end = hit_end;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAlign_type() {
		return align_type;
	}
	public void setAlign_type(String align_type) {
		this.align_type = align_type;
	}
	public String getCigar_string() {
		return cigar_string;
	}
	public void setCigar_string(String cigar_string) {
		this.cigar_string = cigar_string;
	}
	public String getTranslation_id() {
		return translation_id;
	}
	public void setTranslation_id(String translation_id) {
		this.translation_id = translation_id;
	}
	public String getInterpro() {
		return interpro;
	}
	public void setInterpro(String interpro) {
		this.interpro = interpro;
	}
	public String getParent() {
		return Parent;
	}
	public void setParent(String parent) {
		Parent = parent;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSeq_region_name() {
		return seq_region_name;
	}
	public void setSeq_region_name(String seq_region_name) {
		this.seq_region_name = seq_region_name;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
	public String getHseqname() {
		return hseqname;
	}
	public void setHseqname(String hseqname) {
		this.hseqname = hseqname;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
}
