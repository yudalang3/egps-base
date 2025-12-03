package graphic.engine;

import org.apache.commons.lang3.tuple.Pair;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.LinkedList;
import java.util.List;

/**
 * 高级轴刻度计算器，是AxisTickCalculator的改进版本。
 * 
 * <p>
 * 该类提供了更智能和灵活的轴刻度计算算法，具有以下改进：
 * <ul>
 *   <li><strong>自适应步长选择</strong>：通过评估多种步长提示，自动选择最优的刻度密度</li>
 *   <li><strong>扩展的Nice Numbers序列</strong>：使用更丰富的尾数值集合，提供更美观的刻度间隔</li>
 *   <li><strong>质量评估系统</strong>：通过评分机制选择最佳的刻度布局</li>
 *   <li><strong>智能边界处理</strong>：确保刻度能够覆盖数据范围</li>
 * </ul>
 * </p>
 * 
 * <h2>算法特点</h2>
 * <p>
 * 相比原始版本，该计算器具有以下优势：
 * </p>
 * <ul>
 *   <li>支持多种密度级别（32, 48, 64, 80, 96）</li>
 *   <li>使用扩展的Nice Numbers序列：{1.0, 1.2, 1.5, 2.0, 2.5, 3.0, 4.0, 5.0, 6.0, 8.0, 10.0}</li>
 *   <li>智能评分系统考虑刻度数量、密度和边界覆盖</li>
 *   <li>自动选择最优的刻度布局</li>
 * </ul>
 * 
 * <h2>使用示例</h2>
 * <pre>
 * // 创建计算器
 * AxisTickCalculatorByClaude4 calculator = new AxisTickCalculatorByClaude4();
 * 
 * // 设置数据范围
 * Pair&lt;Double, Double&gt; minMax = Pair.of(52.5, 57.5);
 * calculator.setMinAndMaxPair(minMax);
 * 
 * // 设置工作空间
 * calculator.setWorkingSpace(250);
 * calculator.setWorkSpaceRatio(1.0f);
 * 
 * // 计算刻度
 * calculator.determineAxisTick();
 * 
 * // 获取结果
 * List&lt;String&gt; labels = calculator.getTickLabels();
 * List&lt;Integer&gt; locations = calculator.getTickLocations();
 * </pre>
 * 
 * <h2>性能考虑</h2>
 * <p>
 * 该算法会尝试多种步长提示并评估每种结果的质量，因此计算复杂度比原始版本稍高，
 * 但能够提供更美观和实用的刻度布局。对于大多数应用场景，性能影响可以忽略不计。
 * </p>
 * 
 * @author Claude4 (AI改进版本)
 * @version 1.0
 * @since 1.0
 * @see AxisTickCalculator 原始版本的轴刻度计算器
 */
public class AxisTickCalculatorByClaude4 {

    private Pair<Double, Double> minAndMaxPair;
    private List<Integer> tickLocations = new LinkedList<>();
    private List<String> tickLabels = new LinkedList<>();
    private int workingSpace;
    private float workSpaceRatio = 0.95f;

    // 改进1：更灵活的步长提示系统
    private static final int[] STEP_HINTS = {32, 48, 64, 80, 96}; // 不同密度选项
    private int currentStepHint = 64;

    // 改进2：扩展Nice Numbers序列
    private static final double[] NICE_MANTISSAS = {1.0, 1.2, 1.5, 2.0, 2.5, 3.0, 4.0, 5.0, 6.0, 8.0, 10.0};
    private static final double[] NICE_THRESHOLDS = {1.1, 1.35, 1.75, 2.25, 2.75, 3.5, 4.5, 5.5, 7.0, 9.0};

    protected final static int AXIS_TICK_PADDING = 4;
    private Format normalFormat = new DecimalFormat("#.###########");
    private Format scientificFormat = new DecimalFormat("0.###E0");

    public void clear() {
        tickLocations = new LinkedList<>();
        tickLabels = new LinkedList<>();
    }

    /**
     * 改进的主函数：增加自适应步长选择
     */
    public void determineAxisTick() {
        // 尝试不同的步长提示，选择最优结果
        GridStepResult bestResult = null;
        double bestScore = Double.MAX_VALUE;

        for (int stepHint : STEP_HINTS) {
            GridStepResult result = calculateWithStepHint(stepHint);
            double score = evaluateResult(result);

            if (score < bestScore) {
                bestScore = score;
                bestResult = result;
                currentStepHint = stepHint;
            }
        }

        // 应用最优结果
        applyResult(bestResult);
    }

    /**
     * 使用指定步长提示计算结果
     */
    private GridStepResult calculateWithStepHint(int stepHint) {
        int tickSpace = getTickSpace();
        int margin = getMargin(tickSpace);

        GridStepResult result = new GridStepResult();
        result.tickSpace = tickSpace;
        result.margin = margin;

        if (getMax() == getMin()) {
            result.tickLabels.add(format(getMax()));
            result.tickLocations.add((int) (margin + tickSpace / 2.0));
            result.tickCount = 1;
            return result;
        }

        final BigDecimal MIN = new BigDecimal(Double.toString(getMin()));
        BigDecimal gridStep = getImprovedGridStep(tickSpace, stepHint);
        result.gridStep = gridStep;

        // 计算起始位置
        double remainder = MIN.remainder(gridStep).doubleValue();
        BigDecimal firstPosition;
        if (remainder <= 0.0) {
            firstPosition = MIN.subtract(MIN.remainder(gridStep));
        } else {
            firstPosition = MIN.subtract(MIN.remainder(gridStep)).add(gridStep);
        }

        // 生成刻度
        for (BigDecimal b = firstPosition; b.doubleValue() <= getMax(); b = b.add(gridStep)) {
            result.tickLabels.add(format(b.doubleValue()));
            int tickLabelPosition = (int) (margin +
                    ((b.doubleValue() - getMin()) / (getMax() - getMin()) * tickSpace));
            result.tickLocations.add(tickLabelPosition);
            result.tickCount++;
        }

        return result;
    }

    /**
     * 改进的网格步长计算：使用扩展的Nice Numbers
     */
    private BigDecimal getImprovedGridStep(int tickSpace, int stepHint) {
        double length = Math.abs(getMax() - getMin());
        double gridStepHint = length / tickSpace * stepHint;

        // 尾数-指数分解
        double mantissa = gridStepHint;
        int exponent = 0;

        if (mantissa == 0) {
            exponent = 1;
        } else if (mantissa < 1) {
            while (mantissa < 1) {
                mantissa *= 10.0;
                exponent--;
            }
        } else {
            while (mantissa >= 10) {
                mantissa /= 10.0;
                exponent++;
            }
        }

        // 使用扩展的Nice Numbers选择
        double selectedMantissa = selectBestMantissa(mantissa);

        return new BigDecimal(Double.toString(selectedMantissa))
                .multiply(pow(10, exponent));
    }

    /**
     * 选择最佳尾数值
     */
    private double selectBestMantissa(double mantissa) {
        for (int i = 0; i < NICE_THRESHOLDS.length; i++) {
            if (mantissa <= NICE_THRESHOLDS[i]) {
                return NICE_MANTISSAS[i];
            }
        }
        return NICE_MANTISSAS[NICE_MANTISSAS.length - 1]; // 默认返回10.0
    }

    /**
     * 评估结果质量
     */
    private double evaluateResult(GridStepResult result) {
        if (result.tickCount == 0) return Double.MAX_VALUE;

        double score = 0;

        // 1. 刻度数量评分（偏好4-7个刻度）
        int idealTickCount = 5;
        score += Math.abs(result.tickCount - idealTickCount) * 2.0;

        // 2. 刻度密度评分（避免过密或过疏）
        if (result.tickCount > 1) {
            int avgSpacing = result.tickSpace / (result.tickCount - 1);
            if (avgSpacing < 30) score += 10; // 过密惩罚
            if (avgSpacing > 120) score += 5; // 过疏惩罚
        }

        // 3. 边界覆盖评分
        if (!result.tickLabels.isEmpty()) {
            try {
                double firstTick = Double.parseDouble(result.tickLabels.get(0));
                double lastTick = Double.parseDouble(result.tickLabels.get(result.tickLabels.size() - 1));

                if (firstTick > getMin()) score += 1; // 起始点未覆盖
                if (lastTick < getMax()) score += 1; // 结束点未覆盖
            } catch (NumberFormatException e) {
                // 忽略格式化问题
            }
        }

        return score;
    }

    /**
     * 应用计算结果
     */
    private void applyResult(GridStepResult result) {
        this.tickLocations = result.tickLocations;
        this.tickLabels = result.tickLabels;
    }

    /**
     * 结果封装类
     */
    private static class GridStepResult {
        List<Integer> tickLocations = new LinkedList<>();
        List<String> tickLabels = new LinkedList<>();
        int tickCount = 0;
        int tickSpace;
        int margin;
        BigDecimal gridStep;
    }

    // 保持原有的辅助方法
    private int getMargin(int tickSpace) {
        return (int) (0.5 * (workingSpace - tickSpace));
    }

    private int getTickSpace() {
        return (int) (workingSpace * workSpaceRatio);
    }

    private BigDecimal pow(double base, int exponent) {
        BigDecimal value;
        if (exponent > 0) {
            value = new BigDecimal(Double.toString(base)).pow(exponent);
        } else {
            value = BigDecimal.ONE.divide(new BigDecimal(Double.toString(base)).pow(-exponent));
        }
        return value;
    }

    private String format(double value) {
        if (Math.abs(value) < 9999 && Math.abs(value) > .0001 || value == 0) {
            return this.normalFormat.format(value);
        } else {
            return this.scientificFormat.format(value);
        }
    }

    // Getters and Setters
    public List<Integer> getTickLocations() { return tickLocations; }
    public List<String> getTickLabels() { return tickLabels; }
    public void setMinAndMaxPair(Pair<Double, Double> minAndMaxPair) { this.minAndMaxPair = minAndMaxPair; }
    public void setWorkSpaceRatio(float workSpaceRatio) { this.workSpaceRatio = workSpaceRatio; }
    public void setWorkingSpace(int workingSpace) { this.workingSpace = workingSpace; }
    private double getMin() { return this.minAndMaxPair.getLeft(); }
    private double getMax() { return this.minAndMaxPair.getRight(); }

    /**
     * 获取当前使用的步长提示
     */
    public int getCurrentStepHint() {
        return currentStepHint;
    }
}
