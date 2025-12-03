package graphic.engine;

import java.awt.Color;
import java.util.List;

import org.apache.commons.compress.utils.Lists;

/**
 * Default linear color mapper implementation using gradient color scheme from blue to white to red.
 */
public class DefaultLinerColorMapper implements ColorMapper {

	private final Color color4NA;
	private final GradientColorHolder gradientColorHolder;
	private final double min;
	private final double range;


	public DefaultLinerColorMapper(double min, double max) {
		this(min, max, Color.gray);
	}
	public DefaultLinerColorMapper(double min, double max, Color color4NA) {
		GradientColorHolder gradientColorHolder = new GradientColorHolder();

		gradientColorHolder.setColorScheme(new float[] { (float) 0, (float) 0.5, (float) 1 },
				new Color[] { Color.blue, Color.white, Color.red });
		this.gradientColorHolder = gradientColorHolder;
		this.color4NA = color4NA;
		this.range = max - min;
		this.min = min;
	}

	@Override
	public Color mapColor(double value) {
		if (Double.isNaN(value)) {
			return color4NA;
		}
		double midValue = (value - min) / range;
		return gradientColorHolder.getColorFromPallet(midValue);
	}

	@Override
	public List<Color> mapColors(List<Double> values) {

		List<Color> ret = Lists.newArrayList();
		for (Double dvl : values) {
			Color mapColor = mapColor(dvl);
			ret.add(mapColor);
		}

		return ret;
	}

	public GradientColorHolder getGradientColorHolder() {
		return gradientColorHolder;
	}

}