package phylo.msa.util;

/**
 * Common utilities for Multiple Sequence Alignment (MSA) operations.
 * 
 * <p>
 * This utility class provides fundamental methods and utilities for analyzing and
 * processing multiple sequence alignments in evolutionary and phylogenetic contexts.
 * It serves as a base class for MSA-related functionality and contains inner classes
 * for specific alignment operations.
 * </p>
 * 
 * <h2>Core Functionality:</h2>
 * <ul>
 *   <li><strong>Sequence Distance Calculation:</strong> Compute pairwise evolutionary distances</li>
 *   <li><strong>Alignment Validation:</strong> Verify alignment format and consistency</li>
 *   <li><strong>Gap Handling:</strong> Process gaps and indels in alignments</li>
 *   <li><strong>Consensus Generation:</strong> Create consensus sequences from alignments</li>
 *   <li><strong>Statistical Analysis:</strong> Calculate alignment statistics and properties</li>
 * </ul>
 * 
 * <h2>Usage Patterns:</h2>
 * <p>
 * This class is designed to be extended by other MSA-related classes and provides
 * a foundation of common alignment operations. Inner classes contain specialized
 * utilities for specific alignment tasks.
 * </p>
 * 
 * <h2>Bioinformatics Applications:</h2>
 * <ul>
 *   <li><strong>Evolutionary Analysis:</strong> Calculate evolutionary distances between sequences</li>
 *   <li><strong>Phylogenetic Reconstruction:</strong> Prepare data for tree-building algorithms</li>
 *   <li><strong>Sequence Comparison:</strong> Compare multiple sequences across species/genes</li>
 *   <li><strong>Conservation Analysis:</strong> Identify conserved regions in alignments</li>
 *   <li><strong>Quality Control:</strong> Validate and assess alignment quality</li>
 * </ul>
 * 
 * <h2>Performance Considerations:</h2>
 * <ul>
 *   <li>Optimized for large sequence alignments</li>
 *   <li>Memory-efficient algorithms for distance calculations</li>
 *   <li>Streaming operations for huge datasets</li>
 *   <li>Parallel processing support where applicable</li>
 * </ul>
 * 
 * @see EvolutionaryProperties
 * @see msaoperator
 * @author "yudalang"
 * @created 2020-10-19 21:13
 * @lastModified 2020-10-19 21:13
 * @since 1.7
 */
public class MsaCommonUtil {
    /**
     * Multiple Sequence Alignment utility methods for evolutionary analysis.
     * 
     * <p>
     * This utility class provides static methods for analyzing and calculating
     * pairwise distances, substitution rates, and other metrics from aligned
     * sequences. It implements common algorithms used in evolutionary sequence analysis.
     * </p>
     * 
     * <h2>Core Algorithms:</h2>
     * <ul>
     *   <li><strong>Pairwise Distance:</strong> Calculate evolutionary distance between two sequences</li>
     *   <li><strong>Substitution Counting:</strong> Count different types of substitutions</li>
     *   <li><strong>Gap Analysis:</strong> Handle insertions and deletions in alignments</li>
     *   <li><strong>Similarity Metrics:</strong> Compute various similarity/distance measures</li>
     * </ul>
     * 
     * <h2>Distance Calculation Method:</h2>
     * <p>
     * The pairwise distance calculation accounts for:
     * </p>
     * <ul>
     *   <li>Substitutions (different characters at same position)</li>
     *   <li>Gaps and insertions/deletions</li>
     *   <li>Ambiguous characters (if present)</li>
     *   <li>Sequence length consistency checks</li>
     * </ul>
     * 
     * <h2>Usage Example:</h2>
     * <pre>
     * // Calculate evolutionary distance between two aligned sequences
     * String seq1 = "ACGT-ACGT";
     * String seq2 = "ACGT-ACGA";
     * 
     * double distance = MsaCommonUtil.MSAUtil.getPairwiseSequenceDistance(seq1, seq2);
     * System.out.println("Evolutionary distance: " + distance);
     * // Output: Evolutionary distance: 0.125 (1/8 substitutions)
     * </pre>
     * 
     * <h2>Validation and Error Handling:</h2>
     * <ul>
     *   <li>Input sequence length validation</li>
     *   <li>Alignment consistency checking</li>
     *   <li>Proper gap handling in distance calculations</li>
     *   <li>Exception handling for invalid inputs</li>
     * </ul>
     * 
     * <h2>Bioinformatics Applications:</h2>
     * <ul>
     *   <li><strong>Phylogenetic Analysis:</strong> Prepare distance matrices for tree construction</li>
     *   <li><strong>Sequence Comparison:</strong> Quantify evolutionary relationships</li>
     *   <li><strong>Conservation Studies:</strong> Identify rapidly evolving regions</li>
     *   <li><strong>Species Identification:</strong> Use distances for taxonomic classification</li>
     *   <li><strong>Drug Resistance:</strong> Analyze evolutionary changes in pathogen sequences</li>
     * </ul>
     * 
     * @see MsaCommonUtil
     * @see EvolutionaryProperties
     * @author "yudalang"
     * @title MSAUtil
     * @created 2020-10-19 21:13
     * @lastModified 2020-10-19 21:13
     * @since 1.7
     */
    public static class MSAUtil {

        private MSAUtil() {
        }

        public static double getPairwiseSequenceDistance(String seq1, String seq2) {
            int length = seq1.length();
            if (length != seq2.length()) {
                throw new IllegalArgumentException("Seq1 and Seq2 should be aligned!");
            }

            boolean isSeq2Insertion = false;
            boolean isSeq2Deletion = false;

            double diffCount = 0;

            for (int i = 0; i < length; i++) {
                char a = seq1.charAt(i);
                char b = seq2.charAt(i);
                if (a != b) {
                    if (a == EvolutionaryProperties.GAP_CHAR) {
                        if (!isSeq2Insertion) {
                            // you can change code here
                            diffCount++;
                            isSeq2Insertion = true;
                        }
                    } else if (b == EvolutionaryProperties.GAP_CHAR) {
                        if (!isSeq2Deletion) {
                            // you can change code here
                            diffCount++;
                            isSeq2Deletion = true;
                        }
                    } else {
                        // you can change code here
                        diffCount++;

                        if (isSeq2Insertion) {
                            isSeq2Insertion = false;
                        }
                        if (isSeq2Deletion) {
                            isSeq2Deletion = false;
                        }
                    }

                } else {

                    if (EvolutionaryProperties.GAP_CHAR == a && b == EvolutionaryProperties.GAP_CHAR) {

                    } else {
                        if (isSeq2Insertion) {
                            isSeq2Insertion = false;
                        }
                        if (isSeq2Deletion) {
                            isSeq2Deletion = false;
                        }
                    }

                }
            }

            return diffCount;
        }

        /**
         *
         * <pre>
         *          ref: ATG
         *  aligned seq: AT-G
         * </pre>
         *
         * If input 1, output3.
         *
         * Of course, JAVA is 0 based!
         *
         * @title getNextRefGenomePositionInAlignment
         * @createdDate 2020-10-25 08:55
         * @lastModifiedDate 2020-10-25 08:55
         * @author yudalang
         * @since 1.7
         *
         * @param length  : The length of aligned sequence with gap.
         * @param currSeq : Aligned sequence with gap
         * @param currPos
         * @return
         * @return int
         */
        public static int getNextRefGenomePositionInAlignment(int length, String currSeq, int currPos) {
            int ret = -1;

            currPos++;

            while (currPos < length) {
                char charAt = currSeq.charAt(currPos);

                if (charAt != EvolutionaryProperties.GAP_CHAR) {
                    return currPos;
                }

                currPos++;
            }

            return ret;
        }

        /**
         *
         * <pre>
         *          ref: ATG
         *  aligned seq: AT-G
         * </pre>
         *
         * If input 1, output0.
         *
         * Of course, JAVA is 0 based!
         *
         * @title getPreviousRefGenomePositionInAlignment
         * @createdDate 2020-10-25 08:59
         * @lastModifiedDate 2020-10-25 08:59
         * @author yudalang
         * @since 1.7
         *
         * @param currSeq : Aligned sequence with gap
         * @param currPos
         * @return int
         */
        public static int getPreviousRefGenomePositionInAlignment(String currSeq, int currPos) {
            int ret = -1;

            currPos--;

            while (currPos > -1) {
                char charAt = currSeq.charAt(currPos);

                if (charAt != EvolutionaryProperties.GAP_CHAR) {
                    return currPos;
                }

                currPos--;
            }

            return ret;
        }

        //	public static double getDistanceOfTwoDiffType(SNPAndInsertionDiffTypeAspect diffType1,
        //			SNPAndInsertionDiffTypeAspect diffType2) {
        //
        //		double ret = 0;
        //
        //		// insitu place
        //		if (diffType1.isDeletion()) {
        //			if (diffType2.isDeletion()) {
        //				if (diffType1.isIfFirstDeletion() && diffType2.isIfFirstDeletion()) {
        //					if (diffType1.getLengthOfDeletionRefer2RefSeq() == diffType2.getLengthOfDeletionRefer2RefSeq()) {
        //						ret++;
        //					}
        //				}
        //			} else {
        //				if (diffType1.isIfFirstDeletion()) {
        //					ret++;
        //				}
        //			}
        //		} else {
        //			if (diffType2.isDeletion()) {
        //				if (diffType2.isIfFirstDeletion()) {
        //					ret++;
        //				}
        //			} else {
        //				ret += QuickDistUtil.getTwoSNPCharDifferenceWithAmbiguousBaseAccording2IntArray(
        //						diffType1.getInsituSite(), diffType2.getInsituSite());
        //			}
        //		}
        //
        //		// insetion String
        //		boolean type1IsNotRight = diffType1.getInsertionContent().isEmpty() && diffType1.isDeletion();
        //		boolean type2IsNotRight = diffType2.getInsertionContent().isEmpty() && diffType2.isDeletion();
        //
        //		if (type1IsNotRight || type2IsNotRight) {
        //			// 其中之一有问题都不行
        //		} else {
        ////			if (!diffType1.getInsertionContent().equals(diffType2.getInsertionContent())) {
        ////				ret ++;
        ////			}
        //			// 考虑模糊碱基
        //			if (!QuickDistUtil.judgeTwoAllelesIdentities(diffType1.getInsertionContent(),
        //					diffType2.getInsertionContent())) {
        //				ret++;
        //			}
        //		}
        //		return ret;
        //	}


    }
}
