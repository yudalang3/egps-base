package graphic.engine;

import org.apache.commons.lang3.tuple.Pair;
import java.util.List;

/**
 * Test3: 负数和跨零范围测试
 * 测试包含负数的各种场景
 */
public class AxisTickCalculatorTest3 {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("Test3: 负数和跨零范围测试");
        System.out.println("========================================\n");

        testNegativeToPositive();
        testSymmetricRange();
        testAllNegative();
        testNegativeDecimals();
        testAsymmetricCrossZero();
        testSmallNegativeRange();
    }

    static void testNegativeToPositive() {
        System.out.println("--- [-50, 50] 对称跨零 ---");
        runBothVersions(-50.0, 50.0, new int[]{200, 400, 600});
    }

    static void testSymmetricRange() {
        System.out.println("--- [-100, 100] 大对称范围 ---");
        runBothVersions(-100.0, 100.0, new int[]{200, 400, 600});
    }

    static void testAllNegative() {
        System.out.println("--- [-100, -20] 全负数范围 ---");
        runBothVersions(-100.0, -20.0, new int[]{200, 400, 600});
    }

    static void testNegativeDecimals() {
        System.out.println("--- [-0.5, 0.5] 小数跨零 ---");
        runBothVersions(-0.5, 0.5, new int[]{200, 400});
    }

    static void testAsymmetricCrossZero() {
        System.out.println("--- [-20, 80] 非对称跨零 ---");
        runBothVersions(-20.0, 80.0, new int[]{200, 400, 600});
    }

    static void testSmallNegativeRange() {
        System.out.println("--- [-5, -1] 小负数范围 ---");
        runBothVersions(-5.0, -1.0, new int[]{150, 300});
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
