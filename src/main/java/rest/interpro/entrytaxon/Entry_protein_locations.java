package rest.interpro.entrytaxon;

import java.util.List;

/**
 * Protein location information for InterPro entries.
 * 
 * <p>
 * This class represents the spatial and statistical information for
 * proteins associated with a specific InterPro entry. It provides
 * detailed location data including domain boundaries and statistical
 * significance scores for protein-annotation relationships.
 * </p>
 * 
 * <h2>Key Properties:</h2>
 * <ul>
 *   <li><strong>model:</strong> HMM or model identifier for the domain prediction</li>
 *   <li><strong>score:</strong> Statistical significance score for the domain hit</li>
 *   <li><strong>fragments:</strong> List of protein sequence fragments/domains</li>
 * </ul>
 * 
 * <h2>Domain Prediction Context:</h2>
 * <p>The model field typically contains the identifier of the profile HMM
 * (Hidden Markov Model) or other computational model used to predict
 * the domain location. The score indicates the statistical confidence
 * of the prediction.</p>
 * 
 * <h2>Location Analysis Applications:</h2>
 * <ul>
 *   <li><strong>Domain Boundaries:</strong> Define precise start/end positions</li>
 *   <li><strong>Quality Control:</strong> Assess prediction reliability via scores</li>
 *   <li><strong>Fragment Analysis:</strong> Analyze domain coverage and gaps</li>
 *   <li><strong>Model Validation:</strong> Evaluate computational model performance</li>
 *   <li><strong>Protein Architecture:</strong> Build complete domain maps</li>
 * </ul>
 * 
 * <h2>Scoring System:</h2>
 * <p>Scores are typically HMM log-odds scores or similar statistical
 * measures. Higher scores indicate better matches between the protein
 * sequence and the domain model.</p>
 * 
 * <h2>Fragment Processing:</h2>
 * <p>Each protein may have multiple fragments associated with the same
 * entry, representing different domain instances or overlapping regions.
 * The fragments list provides detailed coordinate information.</p>
 * 
 * <h2>Bioinformatics Applications:</h2>
 * <ul>
 *   <li><strong>Domain Mapping:</strong> Visualize protein domain architecture</li>
 *   <li><strong>Evolutionary Analysis:</strong> Track domain evolution across species</li>
 *   <li><strong>Functional Prediction:</strong> Infer protein function from domain location</li>
 *   <li><strong>Structural Modeling:</strong> Guide 3D structure prediction</li>
 *   <li><strong>Comparative Genomics:</strong> Analyze domain conservation patterns</li>
 * </ul>
 * 
 * <h2>Usage Example:</h2>
 * <pre>
 * EntryProteinLocations locations = new EntryProteinLocations();
 * locations.setModel("Pfam:PF00001");
 * locations.setScore(45.2f);
 * locations.setFragments(fragmentList);
 * 
 * // Analyze location quality
 * float qualityScore = locations.getScore();
 * List&lt;Fragments&gt; fragments = locations.getFragments();
 * 
 * for (Fragments fragment : fragments) {
 *     int start = fragment.getStart();
 *     int end = fragment.getEnd();
 *     // Process fragment boundaries
 * }
 * </pre>
 * 
 * @see Fragments
 * @see Entries
 * @author eGPS Development Team
 * @since 1.0
 */
public class Entry_protein_locations {

	String model;
	
	float score;

    List<Fragments> fragments;

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public List<Fragments> getFragments() {
		return fragments;
	}

	public void setFragments(List<Fragments> fragments) {
		this.fragments = fragments;
	}
	
	
	
	
}
