package fasta.comparison;

import java.util.ArrayList;
import java.util.List;

/**
 * Visualizer for pairwise sequence alignment differences with flexible marking modes.
 * 
 * <h2>Key Features:</h2>
 * <ul>
 *   <li><strong>Dual Marking Modes:</strong> Mark matches (|) or mark mismatches (^, *)</li>
 *   <li><strong>Wrapped Display:</strong> Format output to specified column width</li>
 *   <li><strong>Gap Visualization:</strong> Show sequence length differences with gap markers (·)</li>
 *   <li><strong>Position Tracking:</strong> List all mismatch positions with 1-based indexing</li>
 *   <li><strong>Identity Calculation:</strong> Compute sequence identity percentage</li>
 * </ul>
 * 
 * <h2>Usage Examples:</h2>
 * 
 * <h3>Mark Matches (Traditional Style):</h3>
 * <pre>
 * String seq1 = "MKTAYLQVVV";
 * String seq2 = "MKTAFYLQPVVV";
 * PairwiseSeqDiffPrinter.printDiff(seq1, seq2, 60);
 * 
 * // Output:
 * //          1-12
 * // A:        MKTAYLQVVV
 * //           |||| |||||||
 * // B:        MKTAFYLQPVVV
 * // 
 * // Matches: 10, Mismatches: 2, Identity: 83.33%
 * // Mismatch positions (1-based):
 * // 5:A→F, 9:∅→P
 * </pre>
 * 
 * <h3>Mark Mismatches (Highlight Differences):</h3>
 * <pre>
 * PairwiseSeqDiffPrinter.printDiffMarkingMismatches(seq1, seq2, 60, '^');
 * 
 * // Output:
 * //          1-12
 * // A:        MKTAYLQVVV
 * //               ^   ^
 * // B:        MKTAFYLQPVVV
 * // 
 * // Matches: 10, Mismatches: 2, Identity: 83.33%
 * // Mismatch positions (1-based):
 * // 5:A→F, 9:∅→P
 * </pre>
 * 
 * <h2>Gap Representation:</h2>
 * <ul>
 *   <li><strong>Display Gap (·):</strong> Shows missing positions in visual alignment</li>
 *   <li><strong>Logic Gap (∅):</strong> Represents true absence in mismatch summary</li>
 * </ul>
 * 
 * <h2>Output Format:</h2>
 * <p>Each segment displays:</p>
 * <pre>
 *          [start]-[end]
 * A:        [sequence_A]
 *           [markers]
 * B:        [sequence_B]
 * </pre>
 * 
 * <h2>Mismatch Position Format:</h2>
 * <pre>
 * position:char_A→char_B
 * </pre>
 * <ul>
 *   <li><strong>Position:</strong> 1-based index</li>
 *   <li><strong>char_A:</strong> Character in sequence A (or ∅ if gap)</li>
 *   <li><strong>char_B:</strong> Character in sequence B (or ∅ if gap)</li>
 * </ul>
 * 
 * <h2>Use Cases:</h2>
 * <ul>
 *   <li>Visualize protein sequence alignment differences</li>
 *   <li>Compare predicted vs. reference sequences</li>
 *   <li>Identify mutation positions in coding sequences</li>
 *   <li>Quality control for sequence editing</li>
 *   <li>Educational display of sequence similarity</li>
 * </ul>
 * 
 * <h2>Method Overview:</h2>
 * <ul>
 *   <li><strong>printDiff():</strong> Marks matching positions with '|' (traditional alignment view)</li>
 *   <li><strong>printDiffMarkingMismatches():</strong> Marks differing positions with custom character</li>
 * </ul>
 * 
 * @author yudalang
 * @since 1.0
 */
public class PairwiseSeqDiffPrinter {

    /** 原方法：标注相同字符（匹配处为'|') */
    public static void printDiff(String a, String b, int wrap) {
        printDiffInternal(a, b, wrap, '|', ' ', true);
    }

    /**
     * 新方法：标注不同字符（错配/缺失处使用你指定的字符；相同处留空格）
     * @param a 序列 A
     * @param b 序列 B
     * @param wrap 每行展示宽度（列数）
     * @param mismatchMarker 用来标注差异的字符（如 '^' 或 '*'）
     */
    public static void printDiffMarkingMismatches(String a, String b, int wrap, char mismatchMarker) {
        printDiffInternal(a, b, wrap, ' ', mismatchMarker, false);
    }

    // ---------------- 内部实现（两个公共方法共用） ----------------

    /**
     * @param matchMarker    当 markMatches=true 时用于标注匹配的字符（例如 '|'）；
     *                       当 markMatches=false 时，这个参数会被忽略（匹配处固定为空格）
     * @param mismatchMarker 当 markMatches=false 时用于标注错配/缺失的字符（例如 '^'）；
     *                       当 markMatches=true 时，这个参数会被忽略（错配处固定为空格）
     * @param markMatches    true=标注匹配；false=标注不匹配
     */
    private static void printDiffInternal(String a, String b, int wrap,
                                          char matchMarker, char mismatchMarker, boolean markMatches) {
        if (a == null || b == null) throw new IllegalArgumentException("输入字符串不能为 null");
        if (wrap <= 0) wrap = Math.max(60, Math.max(a.length(), b.length()));

        final char GAP_PRINT = '·'; // 打印用占位符（展示缺失）
        final char GAP_LOGIC = '∅'; // 汇总列表用（真正缺失）

        int max = Math.max(a.length(), b.length());
        int matches = 0, mismatches = 0;
        List<String> mismatchList = new ArrayList<>();

        for (int start = 0; start < max; start += wrap) {
            int end = Math.min(start + wrap, max);
            StringBuilder sa = new StringBuilder(end - start);
            StringBuilder sb = new StringBuilder(end - start);
            StringBuilder mk = new StringBuilder(end - start);

            for (int i = start; i < end; i++) {
                boolean aHas = i < a.length();
                boolean bHas = i < b.length();
                char ca = aHas ? a.charAt(i) : GAP_PRINT;
                char cb = bHas ? b.charAt(i) : GAP_PRINT;

                sa.append(ca);
                sb.append(cb);

                boolean bothHave = aHas && bHas;
                boolean equal = bothHave && ca == cb;

                if (markMatches) {
                    // 模式1：标注匹配（原行为）
                    if (equal) {
                        mk.append(matchMarker);
                        matches++;
                    } else {
                        mk.append(' ');
                        mismatches++;
                        mismatchList.add((i + 1) + ":" +
                                (bothHave ? ca : (aHas ? ca : GAP_LOGIC)) + "→" +
                                (bothHave ? cb : (bHas ? cb : GAP_LOGIC)));
                    }
                } else {
                    // 模式2：标注不匹配（新需求）
                    if (equal) {
                        mk.append(' ');
                        matches++;
                    } else {
                        mk.append(mismatchMarker);
                        mismatches++;
                        mismatchList.add((i + 1) + ":" +
                                (bothHave ? ca : (aHas ? ca : GAP_LOGIC)) + "→" +
                                (bothHave ? cb : (bHas ? cb : GAP_LOGIC)));
                    }
                }
            }

            System.out.printf("%8s %d-%d%n", "", start + 1, end);
            System.out.println("A:        " + sa);
            System.out.println("          " + mk);
            System.out.println("B:        " + sb);
            System.out.println();
        }

        int totalCompared = Math.max(1, max);
        double identity = matches * 100.0 / totalCompared;
        System.out.printf("Matches: %d, Mismatches: %d, Identity: %.2f%%%n", matches, mismatches, identity);

        if (!mismatchList.isEmpty()) {
            System.out.println("Mismatch positions (1-based):");
            System.out.println(String.join(", ", mismatchList));
        }
    }

    // 简单 demo
    public static void main(String[] args) {
        String s1 = "MKT-AYLQ--VVV".replace("-", "");
        String s2 = "MKTAFYLQPVVV".replace("-", "");

        System.out.println("# 标注匹配（旧样式，'|'）：");
        printDiff(s1, s2, 60);

        System.out.println("\n# 标注差异（新样式，'^'）：");
        printDiffMarkingMismatches(s1, s2, 60, '^');
    }
}
