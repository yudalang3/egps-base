package graphic.engine;

import org.apache.commons.lang3.tuple.Pair;
import java.util.List;

/**
 * AxisTickCalculator 问题复现测试
 *
 * 问题描述 (见 docs/Auto_axis_design.md):
 *   当 workingSpace 较小时，生成的刻度数量过少
 *   - workingSpace=16,  range=[0, 85.5] -> 只有1个刻度
 *   - workingSpace=122, range=[0, 85.5] -> 只有2个刻度
 *
 * 额外问题:
 *   刻度最大值 < 数据最大值，导致部分数据没有刻度参考
 *   例如: range=[0, 85.5], ticks=[0, 50] -> 50-85.5 没有刻度覆盖
 *
 * 根因: DEFAULT_TICK_MARK_STEP_HINT=64，小空间时 gridStep 过大
 */
public class AxisTickCalculatorTest {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("AxisTickCalculator 问题复现");
        System.out.println("见 docs/Auto_axis_design.md");
        System.out.println("========================================\n");

        reproduceObservedIssue();
        System.out.println();
        explainRootCause();
        System.out.println();
        compareVersions();
    }

    /**
     * 复现文档中描述的问题
     */
    static void reproduceObservedIssue() {
        System.out.println("=== 1. Observed Issue 复现 ===");
        System.out.println("Range: [0, 85.5] (maxLengthOfRoot2Leaf)");
        System.out.println();

        AxisTickCalculator calc = new AxisTickCalculator();
        Pair<Double, Double> range = Pair.of(0.0, 85.5);

        // Case 1: workingSpace = 16
        calc.setMinAndMaxPair(range);
        calc.setWorkingSpace(16);
        calc.determineAxisTick();
        List<String> labels1 = calc.getTickLabels();
        double maxTick1 = getMaxTick(labels1);
        System.out.println("Case 1: workingSpace=16");
        System.out.println("  结果: ticks=" + labels1.size() + " " + labels1);
        System.out.println("  最大刻度=" + maxTick1 + ", 数据最大值=85.5");
        System.out.println("  覆盖问题: " + (maxTick1 < 85.5 ? "YES! 刻度未覆盖全部数据" : "OK"));
        System.out.println("  备注: 16px空间太小，问题可接受");
        calc.clear();

        // Case 2: workingSpace = 122
        calc.setMinAndMaxPair(range);
        calc.setWorkingSpace(122);
        calc.determineAxisTick();
        List<String> labels2 = calc.getTickLabels();
        double maxTick2 = getMaxTick(labels2);
        System.out.println("Case 2: workingSpace=122");
        System.out.println("  结果: ticks=" + labels2.size() + " " + labels2);
        System.out.println("  最大刻度=" + maxTick2 + ", 数据最大值=85.5");
        System.out.println("  覆盖问题: " + (maxTick2 < 85.5 ? "YES! 刻度未覆盖全部数据" : "OK"));
        System.out.println("  备注: 122px应该能放更多刻度，且应覆盖85.5");
        calc.clear();
    }

    /**
     * 获取刻度列表中的最大值
     */
    static double getMaxTick(List<String> labels) {
        if (labels.isEmpty()) return 0;
        try {
            return Double.parseDouble(labels.get(labels.size() - 1));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * 解释根因：计算过程
     */
    static void explainRootCause() {
        System.out.println("=== 2. Root Cause 计算过程 ===");
        System.out.println();

        double dataRange = 85.5;
        int stepHint = 64;  // DEFAULT_TICK_MARK_STEP_HINT
        float ratio = 0.95f;

        // Case 1: workingSpace = 16
        int ws1 = 16;
        int tickSpace1 = (int)(ws1 * ratio);
        double gridStepHint1 = dataRange / tickSpace1 * stepHint;
        System.out.println("Case 1: workingSpace=16");
        System.out.printf("  tickSpace = %d * 0.95 = %d%n", ws1, tickSpace1);
        System.out.printf("  gridStepHint = 85.5 / %d * 64 = %.1f%n", tickSpace1, gridStepHint1);
        System.out.println("  mantissa=3.64, exponent=2 -> gridStep=500");
        System.out.println("  500 > 85.5, 所以只有1个刻度(0)");
        System.out.println();

        // Case 2: workingSpace = 122
        int ws2 = 122;
        int tickSpace2 = (int)(ws2 * ratio);
        double gridStepHint2 = dataRange / tickSpace2 * stepHint;
        System.out.println("Case 2: workingSpace=122");
        System.out.printf("  tickSpace = %d * 0.95 = %d%n", ws2, tickSpace2);
        System.out.printf("  gridStepHint = 85.5 / %d * 64 = %.1f%n", tickSpace2, gridStepHint2);
        System.out.println("  mantissa=4.73, exponent=1 -> gridStep=50");
        System.out.println("  85.5 / 50 = 1.7, 所以只有2个刻度(0, 50)");
    }

    /**
     * 对比原版和改进版
     */
    static void compareVersions() {
        System.out.println("=== 3. 原版 vs 改进版 对比 ===");
        System.out.println("Range: [0, 85.5]");
        System.out.println("覆盖: 刻度最大值是否 >= 85.5");
        System.out.println();
        System.out.println("workingSpace | Original (覆盖?)        | Claude4 (覆盖?)");
        System.out.println("-------------|-------------------------|-------------------------");

        AxisTickCalculator orig = new AxisTickCalculator();
        AxisTickCalculatorHeavy v2 = new AxisTickCalculatorHeavy();
        Pair<Double, Double> range = Pair.of(0.0, 85.5);
        double dataMax = 85.5;

        int[] spaces = {16, 50, 80, 100, 122, 150, 200, 250, 300};
        for (int ws : spaces) {
            orig.setMinAndMaxPair(range);
            orig.setWorkingSpace(ws);
            orig.determineAxisTick();
            List<String> l1 = orig.getTickLabels();
            double max1 = getMaxTick(l1);
            String cov1 = max1 >= dataMax ? "Y" : "N";
            orig.clear();

            v2.setMinAndMaxPair(range);
            v2.setWorkingSpace(ws);
            v2.determineAxisTick();
            List<String> l2 = v2.getTickLabels();
            double max2 = getMaxTick(l2);
            String cov2 = max2 >= dataMax ? "Y" : "N";
            v2.clear();

            System.out.printf("%12d | %d %-16s (%s) | %d %-16s (%s)%n",
                    ws, l1.size(), l1.toString(), cov1, l2.size(), l2.toString(), cov2);
        }
    }
}
