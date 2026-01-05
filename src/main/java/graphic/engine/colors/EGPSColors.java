package graphic.engine.colors;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import utils.EGPSFileUtil;

/**
 * The quick utils for the colors.
 * 
 * @author yudalang
 */
public class EGPSColors {

	Map<String, Color> colorMap;

	public EGPSColors() {
		colorMap = Maps.newHashMap();
	}

	public Color allocateColor(String key) {
		if (colorMap.containsKey(key)) {
			return colorMap.get(key);
		} else {
			Color[] predinedCellChatColors = getPredinedRColorBrowserSet3Colors();
			int size = colorMap.size();
			Color color = predinedCellChatColors[size % predinedCellChatColors.length];
			colorMap.put(key, color);
			return color;
		}
	}

	/**
	 * Input a Color instance and convert it to hexadecimal representation.
	 * 
	 * @param color the color instance
	 * @return the hex string
	 */
	public static String toHexFormColor(Color color) {
		String hexString = Integer.toHexString(color.getRGB());
		return "#" + hexString.substring(2);
	}

	/**
     * Input a Color instance and convert it to hexadecimal representation with alpha.
     * This method may not be highly efficient, but it is sufficient for general use.
     *
     * <a href="https://stackoverflow.com/questions/3607858/convert-a-rgb-color-value-to-a-hexadecimal-string">...</a>
     *
     * @param color the color instance
     * @return hex string with alpha
     */
	public static String toHexFormColorWithAlpha(Color color) {
		int rgba = (color.getRGB() << 8) | color.getAlpha();
		return String.format("#%08X", rgba);
	}

	public static Color randomColor() {
		Random random = new Random();
		int r = random.nextInt(256);
		int g = random.nextInt(256);
		int b = random.nextInt(256);
		return new Color(r, g, b);
	}

	/**
	 * Predefined Office 2013 standard colors.
	 * 
	 * @return an array of predefined colors
	 */
	public static Color[] getPredinedOffice2013StandardColors() {
		Color[] colors = { new Color(192, 0, 0), new Color(255, 0, 0), new Color(255, 192, 0), new Color(255, 255, 0),
				new Color(146, 208, 80), new Color(0, 176, 80), new Color(0, 176, 240), new Color(0, 112, 192),
				new Color(0, 32, 96), new Color(112, 48, 160)

		};
		return colors;
	}

	/**
	 * Predefined R Color Brewer Set3 colors.
	 * 
	 * @return an array of predefined colors
	 */
	public static Color[] getPredinedRColorBrowserSet3Colors() {
		Color[] ret = new Color[] { EGPSColors.parseColor("#8DD3C7"), EGPSColors.parseColor("#FFFFB3"),
				EGPSColors.parseColor("#BEBADA"), EGPSColors.parseColor("#FB8072"),
				EGPSColors.parseColor("#80B1D3"), EGPSColors.parseColor("#FDB462"),
				EGPSColors.parseColor("#B3DE69"), EGPSColors.parseColor("#FCCDE5"),
				EGPSColors.parseColor("#D9D9D9"), EGPSColors.parseColor("#BC80BD"),
				EGPSColors.parseColor("#CCEBC5") };
		return ret;
	}

	/**
	 * These colors are from an R package.
	 * 
	 * @return an array of predefined colors
	 */
	public static Color[] getPredinedCellChatColors() {
		String colorsFromCellChat = "#E41A1C,#377EB8,#4DAF4A,#984EA3,#F29403,#F781BF,#BC9DCC,#A65628,#54B0E4,#222F75,#1B9E77,#B2DF8A,#E3BE00,#FB9A99,#E7298A,#910241,#00CDD1,#A6CEE3,#CE1261,#5E4FA2,#8CA77B,#00441B,#DEDC00,#B3DE69,#8DD3C7,#999999";

		Iterable<String> split = Splitter.on(',').split(colorsFromCellChat);
		List<Color> colors = Lists.newArrayList();
		split.forEach(e -> {
			Color decode = Color.decode(e);
			colors.add(decode);
		});

		Color[] colorOfCellChat = colors.toArray(new Color[0]);
		return colorOfCellChat;
	}

	/**
	 * Colors that 95% of people can distinguish.
	 * 
	 * @return an array of predefined colors
	 */
	public static Color[] getPredined95PercPeopleReconColors() {

		InputStream input = EGPSColors.class.getResourceAsStream("Percentage95.hex.color.txt");
		return getColors(input);
	}

	/**
	 * Colors that 99% of people can distinguish.
	 * 
	 * @return an array of predefined colors
	 */
	public static Color[] getPredined99PercPeopleReconColors() {

		InputStream input = EGPSColors.class.getResourceAsStream("Percentage99.hex.color.txt");
		return getColors(input);
	}

	/**
	 * Colors that 99.99% of people can distinguish.
	 * 
	 * @return an array of predefined colors
	 */
	public static Color[] getPredined9999PercPeopleReconColors() {
		InputStream input = EGPSColors.class.getResourceAsStream("Percentage9999.hex.color.txt");
		return getColors(input);
	}

	private static Color[] getColors(InputStream input) {
		List<Color> colors = Lists.newArrayList();

		try {
			List<String> contentFromInputStreamAsLines = EGPSFileUtil.getContentFromInputStreamAsLines(input);
			for (String string : contentFromInputStreamAsLines) {
				Color color = Color.decode(string);
				colors.add(color);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		Color[] colorOfCellChat = colors.toArray(new Color[0]);
		return colorOfCellChat;
	}

	/**
	 * Quickly input a List<String> or List<Object> to get a mapping of items to colors.
	 * Note that this implementation uses toString, so be cautious.
	 * 
	 * @param <T> the type of items
	 * @param items the list of items
	 * @return a map of item strings to colors
	 */
	public static <T> HashMap<String, Color> getMapItemsColor(List<T> items) {
		HashMap<String, Color> newHashMap = Maps.newLinkedHashMap();

		Color[] colors = getPredinedRColorBrowserSet3Colors();

		for (T t : items) {
			if (newHashMap.containsKey(t.toString())) {

			} else {
				newHashMap.put(t.toString(), colors[newHashMap.size()]);
			}

		}
		return newHashMap;
	}

	/**
	 * Parse color from the source.
	 * 
	 * @param str like 0,0,0,255 or #FFFFFFFF hex=form
	 * @return a color instance
	 */
	public static Color parseColor(String str) {
		if (Strings.isNullOrEmpty(str)) {
			return Color.gray;
		}

		if (str.charAt(0) == '#') {
			int length = str.length();

			if (length == 7) {
				return Color.decode(str);
			} else if (length == 9) {
				// 解析 ARGB 值（包含透明度）
				str = str.substring(1);
				int r = Integer.parseInt(str.substring(0, 2), 16);
				int g = Integer.parseInt(str.substring(2, 4), 16);
				int b = Integer.parseInt(str.substring(4, 6), 16);
				int a = Integer.parseInt(str.substring(6, 8), 16);
				return new Color(r, g, b, a);
			} else {
				throw new IllegalArgumentException("Invalid color string: " + str);
			}

		}

		String[] split = str.split(",");
		if (split.length < 3) {
			throw new IllegalArgumentException("Please input string like 0,0,0,255.");
		}

		int r = Integer.parseInt(split[0]);
		int g = Integer.parseInt(split[1]);
		int b = Integer.parseInt(split[2]);

		int a = 255;

		if (split.length >= 4) {
			a = Integer.parseInt(split[3]);
		}

		return new Color(r, g, b, a);
	}

	/**
     * Modify the alpha of an ARGB hex value using integer values in Java.
     * Reference: <a href="https://stackoverflow.com/questions/23316983/how-to-modify-the-alpha-of-an-argb-hex-value-using-integer-values-in-java">...</a>
     *
     * @param color the color instance
     * @param alpha 0-255
     * @return color with alpha
     */
	public static Color newColorWithAlpha(Color color, int alpha) {
		// more advanced than this implementation
		return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
//		int origColor = color.getRGB();
//		origColor = origColor & 0x00ffffff; // drop the previous alpha value
//		int ret = (alpha << 24) | origColor; // add the one the user inputted
//		return new Color(ret);
	}

	/**
	 * https://www.qulaba.com/1053.html
	 * Recommand 11 kinds of the colors
	 * @return
	 */
	public static Color getEyeFriendlyColor() {
		Color color = parseColor("#C7EDCC");
		return color;
	}
	public static Color getEyeFriendlyColor(int alpha) {
		Color color = parseColor("#C7EDCC");
		return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
	}
}
