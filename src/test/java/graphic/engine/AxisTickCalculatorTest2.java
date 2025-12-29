package graphic.engine;

import org.apache.commons.lang3.tuple.Pair;
import java.util.List;

/**
 * Test2: 小数/浮点数测试
 * 测试各种小数精度场景
 */
public class AxisTickCalculatorTest2 {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("Test2: 小数/浮点数测试");
        System.out.println("========================================\n");

        testOneDecimal();
        testTwoDecimals();
        testThreeDecimals();
        testVerySmallDecimals();
        testFractionalRange();
        testPercentageDecimals();
    }

    static void testOneDecimal() {
        System.out.println("--- [0, 8.5] 一位小数 ---");
        runBothVersions(0.0, 8.5, new int[]{150, 300, 500});
    }

    static void testTwoDecimals() {
        System.out.println("--- [0.25, 0.95] 两位小数 (比例值) ---");
        runBothVersions(0.25, 0.95, new int[]{150, 300, 500});
    }

    static void testThreeDecimals() {
        System.out.println("--- [0.001, 0.009] 三位小数 ---");
        runBothVersions(0.001, 0.009, new int[]{150, 300, 500});
    }

    static void testVerySmallDecimals() {
        System.out.println("--- [0.00001, 0.00005] 极小值 ---");
        runBothVersions(0.00001, 0.00005, new int[]{200, 400});
    }

    static void testFractionalRange() {
        System.out.println("--- [0, 1] 归一化范围 ---");
        runBothVersions(0.0, 1.0, new int[]{150, 300, 500});
    }

    static void testPercentageDecimals() {
        System.out.println("--- [52.5, 57.5] 小范围高精度 ---");
        runBothVersions(52.5, 57.5, new int[]{150, 250, 400});
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
