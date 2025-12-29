package graphic.engine;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

/**
 * 轴刻度计算器，用于科学图表的轴刻度自动生成。
 * 
 * <p>
 * 该类实现了基于"Nice Numbers"算法的轴刻度计算，能够自动生成美观、易读的轴刻度。
 * 它根据数据范围和可用空间，智能地选择刻度间隔和标签格式。
 * </p>
 * 
 * <h2>核心功能</h2>
 * <ul>
 *   <li><strong>自动刻度生成</strong>：根据数据范围自动计算合适的刻度位置</li>
 *   <li><strong>智能格式化</strong>：自动选择普通格式或科学计数法</li>
 *   <li><strong>空间适配</strong>：根据可用空间调整刻度密度</li>
 *   <li><strong>Nice Numbers算法</strong>：使用标准的Nice Numbers序列生成美观的刻度间隔</li>
 * </ul>
 * 
 * <h2>算法原理</h2>
 * <p>
 * 基于标准的Nice Numbers算法：
 * <ol>
 *   <li>将数据范围分解为尾数和指数部分</li>
 *   <li>从预定义的Nice Numbers序列中选择合适的尾数</li>
 *   <li>计算最终的刻度步长</li>
 *   <li>生成刻度位置和标签</li>
 * </ol>
 * </p>
 * 
 * <h2>使用示例</h2>
 * <pre>
 * // 创建计算器
 * AxisTickCalculator calculator = new AxisTickCalculator();
 * 
 * // 设置数据范围
 * Pair&lt;Double, Double&gt; minMax = Pair.of(52.5, 57.5);
 * calculator.setMinAndMaxPair(minMax);
 * 
 * // 设置工作空间参数
 * calculator.setWorkingSpace(250);  // 可用像素空间
 * calculator.setWorkSpaceRatio(1.0f); // 空间利用率
 * 
 * // 计算刻度
 * calculator.determineAxisTick();
 * 
 * // 获取结果
 * System.out.println("刻度标签: " + calculator.getTickLabels());
 * System.out.println("刻度位置: " + calculator.getTickLocations());
 * </pre>
 * 
 * <h2>格式化规则</h2>
 * <p>
 * 自动选择标签格式：
 * </p>
 * <ul>
 *   <li>普通格式：当数值在 0.0001 到 9999 范围内时</li>
 *   <li>科学计数法：当数值超出上述范围时</li>
 *   <li>小数精度：最多保留12位小数</li>
 * </ul>
 * 
 * <h2>性能特点</h2>
 * <ul>
 *   <li>计算复杂度：O(n)，其中n为生成的刻度数量</li>
 *   <li>内存使用：轻量级，仅存储必要的刻度信息</li>
 *   <li>适用场景：实时图表生成、科学可视化</li>
 * </ul>
 * 
 * <h2>参考实现</h2>
 * <p>
 * 该算法参考了xChart库的实现，并进行了适配和优化。
 * </p>
 * 
 * @author eGPS Development Team
 * @version 1.0
 * @since 1.0
 * @see AxisTickCalculatorHeavy 改进版本的高级轴刻度计算器
 */
public class AxisTickCalculator {

	private Pair<Double, Double> minAndMaxPair;

	/** the arraylist of tick label position in pixels */
	private List<Integer> tickLocations = new LinkedList<>();
	private List<String> tickLabels = new LinkedList<>();

	private int workingSpace;
	
	private float workSpaceRatio = 0.95f;

	/** the default tick mark step hint */
	private static final int DEFAULT_TICK_MARK_STEP_HINT = 64;

	protected  final static int AXIS_TICK_PADDING = 4;

	/** the normal format for tick labels */
	private Format normalFormat = new DecimalFormat("#.###########");

	/** the scientific format for tick labels */
	private Format scientificFormat = new DecimalFormat("0.###E0");

	public AxisTickCalculator() {
	}

	public List<Integer> getTickLocations() {
		return tickLocations;
	}
	
	public List<String> getTickLabels() {
		return tickLabels;
	}
	
	/**
	 * Do not forget to call this
	 * 千万不要忘记，否则计算会出问题
	 */
	public void clear() {
		tickLocations = new LinkedList<>();
		tickLabels = new LinkedList<>();
	}

	protected int getWorkingSpace() {
		return this.workingSpace;
	}

	/**
	 * 这是该类的主函数，表示进行tip的计算。
	 */
	public void determineAxisTick() {
//		 System.out.println("workingSpace= " + workingSpace);
		int tickSpace = getTickSpace();
//		 System.out.println("tickSpace= " + tickSpace);

		int margin = getMargin(tickSpace);

		// a check if all axis data are the exact same values
		if (getMax() == getMin()) {
			tickLabels.add(format(getMax()));
			tickLocations.add((int) (margin + tickSpace / 2.0));
		} else {

			final BigDecimal MIN = new BigDecimal(new Double(getMin()).toString());
			BigDecimal gridStep = getGridStep(tickSpace);

			double xyz = MIN.remainder(gridStep).doubleValue();
			BigDecimal firstPosition;
			if (xyz <= 0.0) {
				firstPosition = MIN.subtract(MIN.remainder(gridStep));
			} else {
				firstPosition = MIN.subtract(MIN.remainder(gridStep)).add(gridStep);
			}

			for (BigDecimal b = firstPosition; b.doubleValue() <= getMax(); b = b.add(gridStep)) {

				// System.out.println("b= " + b);
				tickLabels.add(format(b.doubleValue()));
				int tickLabelPosition = (int) (margin
						+ ((b.doubleValue() - getMin()) / (getMax() - getMin()) * tickSpace));
				// System.out.println("tickLabelPosition= " + tickLabelPosition);

				tickLocations.add(tickLabelPosition);
			}
		}
	}

	private int getMargin(int tickSpace) {
		int ret = (int) (0.5 * (workingSpace - tickSpace));
		return ret;
	}

	private int getTickSpace() {
		int ret = (int) (workingSpace * workSpaceRatio);
		return ret;
	}

	private BigDecimal getGridStep(int tickSpace) {

		double length = Math.abs(getMax() - getMin());
		// System.out.println(axis.getMax());
		// System.out.println(axis.getMin());
		// System.out.println(length);
		double gridStepHint = length / tickSpace * DEFAULT_TICK_MARK_STEP_HINT;
        //                  小数部分             指数
		// gridStepHint --> mantissa * 10 ** exponent
		// e.g. 724.1 --> 7.241 * 10 ** 2
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

		// calculate the grid step with hint.
		BigDecimal gridStep;
		if (mantissa > 7.5) {
			// gridStep = 10.0 * 10 ** exponent
			gridStep = BigDecimal.TEN.multiply(pow(10, exponent));
		} else if (mantissa > 3.5) {
			// gridStep = 5.0 * 10 ** exponent
			gridStep = new BigDecimal(new Double(5).toString()).multiply(pow(10, exponent));
		} else if (mantissa > 1.5) {
			// gridStep = 2.0 * 10 ** exponent
			gridStep = new BigDecimal(new Double(2).toString()).multiply(pow(10, exponent));
		} else {
			// gridStep = 1.0 * 10 ** exponent
			gridStep = pow(10, exponent);
		}
		return gridStep;
	}

	/**
	 * Calculates the value of the first argument raised to the power of the second
	 * argument.
	 * 
	 * @param base     the base
	 * @param exponent the exponent
	 * @return the value <tt>a<sup>b</sup></tt> in <tt>BigDecimal</tt>
	 */
	private BigDecimal pow(double base, int exponent) {

		BigDecimal value;
		if (exponent > 0) {
			value = new BigDecimal(new Double(base).toString()).pow(exponent);
		} else {
			value = BigDecimal.ONE.divide(new BigDecimal(new Double(base).toString()).pow(-exponent));
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

	public void setMinAndMaxPair(Pair<Double, Double> minAndMaxPair) {
		this.minAndMaxPair = minAndMaxPair;
	}
	
	/**
	 * 可以设置为1，如果你已经考虑了留白
	 */
	public void setWorkSpaceRatio(float workSpaceRatio) {
		this.workSpaceRatio = workSpaceRatio;
	}

	private double getMin() {
		return this.minAndMaxPair.getLeft();
	}

	private double getMax() {
		return this.minAndMaxPair.getRight();
	}
	
	public void setWorkingSpace(int workingSpace) {
		this.workingSpace = workingSpace;
	}

}
