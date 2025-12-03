package msaoperator.alignment.sequence;

/**
 * Data holder for sequence component statistics including base composition and ratios.
 */
public class SequenceComponentRatio {

	private Character base;

	private Integer maxValue;

	private int percentage;

	public Character getBase() {
		return base;
	}

	public void setBase(Character base) {
		this.base = base;
	}

	public Integer getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(Integer maxValue) {
		this.maxValue = maxValue;
	}

	public int getPercentage() {
		return percentage;
	}

	public void setPercentage(int percentage) {
		this.percentage = percentage;
	}
	
	

}