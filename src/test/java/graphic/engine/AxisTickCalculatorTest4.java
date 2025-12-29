package graphic.engine;

import org.apache.commons.lang3.tuple.Pair;
import java.util.List;

/**
 * Test4: 科学计数/极端值测试
 * 测试需要科学计数法表示的极端值场景
 */
public class AxisTickCalculatorTest4 {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("Test4: 科学计数/极端值测试");
        System.out.println("========================================\n");

        testMillionRange();
        testBillionRange();
        testVerySmallScientific();
        testMixedMagnitude();
        testTinyRange();
        testHugeWithOffset();
    }

    static void testMillionRange() {
        System.out.println("--- [0, 1e6] 百万级 ---");
        runBothVersions(0.0, 1000000.0, new int[]{200, 400, 600});
    }

    static void testBillionRange() {
        System.out.println("--- [0, 1e9] 十亿级 ---");
        runBothVersions(0.0, 1e9, new int[]{200, 400, 600});
    }

    static void testVerySmallScientific() {
        System.out.println("--- [0, 1e-6] 微量级 ---");
        runBothVersions(0.0, 1e-6, new int[]{200, 400});
    }

    static void testMixedMagnitude() {
        System.out.println("--- [1e-5, 1e-3] 跨数量级 ---");
        runBothVersions(1e-5, 1e-3, new int[]{200, 400});
    }

    static void testTinyRange() {
        System.out.println("--- [0.00001, 0.000011] 极小范围 ---");
        runBothVersions(0.00001, 0.000011, new int[]{200, 400});
    }

    static void testHugeWithOffset() {
        System.out.println("--- [1e8, 1.1e8] 大数小范围 ---");
        runBothVersions(1e8, 1.1e8, new int[]{200, 400, 600});
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

            System.out.printf("  ws=%3d | orig: %d %s%n", ws, labels1.size(), labels1);
            System.out.printf("         | v2:   %d %s%n", labels2.size(), labels2);
        }
        System.out.println();
    }
}
