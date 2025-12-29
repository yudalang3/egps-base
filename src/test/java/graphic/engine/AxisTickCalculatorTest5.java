package graphic.engine;

import org.apache.commons.lang3.tuple.Pair;
import java.util.List;

/**
 * Test5: 生物信息学场景测试
 * 测试常见的生物信息学数据范围
 */
public class AxisTickCalculatorTest5 {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("Test5: 生物信息学场景测试");
        System.out.println("========================================\n");

        testLogPvalue();
        testNegLogPvalue();
        testBranchLength();
        testBootstrapValues();
        testGCContent();
        testFoldChange();
        testSequenceIdentity();
        testPhyloDistance();
    }

    static void testLogPvalue() {
        System.out.println("--- log10(pvalue): [-10, 0] ---");
        System.out.println("    常见于统计显著性，如 log10(0.05)=-1.3");
        runBothVersions(-10.0, 0.0, new int[]{200, 400});
    }

    static void testNegLogPvalue() {
        System.out.println("--- -log10(pvalue): [0, 50] Manhattan plot ---");
        System.out.println("    GWAS Manhattan plot 常用，值越大越显著");
        runBothVersions(0.0, 50.0, new int[]{200, 400, 600});
    }

    static void testBranchLength() {
        System.out.println("--- 进化树枝长: [0, 0.15] 替换/位点 ---");
        runBothVersions(0.0, 0.15, new int[]{150, 300, 500});
    }

    static void testBootstrapValues() {
        System.out.println("--- Bootstrap值: [0, 100] ---");
        runBothVersions(0.0, 100.0, new int[]{150, 300});
    }

    static void testGCContent() {
        System.out.println("--- GC含量: [0.3, 0.7] ---");
        runBothVersions(0.3, 0.7, new int[]{150, 300});
    }

    static void testFoldChange() {
        System.out.println("--- log2(FoldChange): [-5, 5] 差异表达 ---");
        runBothVersions(-5.0, 5.0, new int[]{200, 400});
    }

    static void testSequenceIdentity() {
        System.out.println("--- 序列相似度: [60, 100] % ---");
        runBothVersions(60.0, 100.0, new int[]{150, 300});
    }

    static void testPhyloDistance() {
        System.out.println("--- 系统发育距离(root2leaf): [0, 85.5] ---");
        System.out.println("    这是你遇到问题的场景");
        runBothVersions(0.0, 85.5, new int[]{16, 50, 100, 122, 200, 300, 500});
    }

    static void runBothVersions(double min, double max, int[] spaces) {
        AxisTickCalculator orig = new AxisTickCalculator();
        AxisTickCalculatorHeavy v2 = new AxisTickCalculatorHeavy();
        Pair<Double, Double> range = Pair.of(min, max);

        for (int ws : spaces) {
            orig.setMinAndMaxPair(range);
            orig.setWorkingSpace(ws);
            orig.determineAxisTick();
            List<String> labels1 = orig.getTickLabels();
            orig.clear();

            v2.setMinAndMaxPair(range);
            v2.setWorkingSpace(ws);
            v2.determineAxisTick();
            List<String> labels2 = v2.getTickLabels();
            v2.clear();

            System.out.printf("  ws=%3d | orig: %d %s | v2: %d %s%n",
                    ws, labels1.size(), labels1, labels2.size(), labels2);
        }
        System.out.println();
    }
}
