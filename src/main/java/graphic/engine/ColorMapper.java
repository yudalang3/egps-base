package graphic.engine;

import java.awt.Color;
import java.util.List;

/**
 * Interface for mapping numeric values to colors for data visualization.
 */
public interface ColorMapper {

	/**
	 * map value to the color
	 */
	Color mapColor(double value);

	List<Color> mapColors(List<Double> values);
}