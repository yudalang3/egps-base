package graphic.engine;

import org.apache.commons.lang3.tuple.Pair;
import java.util.List;

/**
 * Test1: 基础整数范围测试
 * 测试常见的整数范围场景
 */
public class AxisTickCalculatorTest1 {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("Test1: 基础整数范围测试");
        System.out.println("========================================\n");

        testZeroToHundred();
        testZeroToTen();
        testZeroToThousand();
        testTenToNinety();
        testLargeRange();
        testSameMinMax();
    }

    static void testZeroToHundred() {
        System.out.println("--- [0, 100] 标准百分比范围 ---");
        runBothVersions(0.0, 100.0, new int[]{100, 200, 300, 500});
    }

    static void testZeroToTen() {
        System.out.println("--- [0, 10] 小整数范围 ---");
        runBothVersions(0.0, 10.0, new int[]{100, 200, 300});
    }

    static void testZeroToThousand() {
        System.out.println("--- [0, 1000] 千级范围 ---");
        runBothVersions(0.0, 1000.0, new int[]{200, 400, 600});
    }

    static void testTenToNinety() {
        System.out.println("--- [10, 90] 非零起点范围 ---");
        runBothVersions(10.0, 90.0, new int[]{150, 300, 500});
    }

    static void testLargeRange() {
        System.out.println("--- [0, 10000] 万级范围 ---");
        runBothVersions(0.0, 10000.0, new int[]{200, 400, 600});
    }

    static void testSameMinMax() {
        System.out.println("--- [50, 50] min==max 边界情况 ---");
        runBothVersions(50.0, 50.0, new int[]{100, 200, 300});
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
