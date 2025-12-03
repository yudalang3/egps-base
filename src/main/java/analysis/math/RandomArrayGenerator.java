package analysis.math;


import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.MersenneTwister;

import java.util.Random;

/**
 * Utility class for generating random arrays with various distributions.
 * 
 * <p>
 * This class provides multiple methods for generating random double arrays
 * using different random number generators and techniques:
 * <ul>
 *   <li>Apache Commons Math3 MersenneTwister (reproducible with seed)</li>
 *   <li>Java standard Random (simple generation)</li>
 *   <li>Uniform real distribution over [min, max] range</li>
 * </ul>
 * </p>
 * 
 * <h2>Key Features:</h2>
 * <ul>
 *   <li><strong>Seeded Generation:</strong> Reproducible random sequences using seed values</li>
 *   <li><strong>MersenneTwister:</strong> High-quality pseudo-random number generator</li>
 *   <li><strong>Uniform Distribution:</strong> Even distribution over specified range</li>
 *   <li><strong>Performance:</strong> Optimized for generating large arrays</li>
 * </ul>
 * 
 * <h2>Usage Examples:</h2>
 * <pre>
 * RandomArrayGenerator generator = new RandomArrayGenerator();
 * 
 * // Generate reproducible random array (with seed)
 * double[] array1 = generator.generateRandomDoublesByColt(1000, 0.0, 1.0, 12345L);
 * 
 * // Generate simple random array (no seed)
 * double[] array2 = generator.generateRandomDoubles(1000, 0.0, 1.0);
 * 
 * // Generate using Math3 implementation
 * double[] array3 = generator.generateRandomDoublesByMath3(1000, 0.0, 1.0, 12345L);
 * </pre>
 * 
 * <h2>Use Cases:</h2>
 * <ul>
 *   <li>Monte Carlo simulations</li>
 *   <li>Bootstrap resampling</li>
 *   <li>Permutation tests</li>
 *   <li>Random sampling for bioinformatics</li>
 * </ul>
 * 
 * @author eGPS Development Team
 * @version 1.0
 * @since 1.0
 * @see UniformRealDistribution
 * @see MersenneTwister
 */
public class RandomArrayGenerator {

	/**
	 * Generates an array of random doubles using MersenneTwister and uniform distribution.
	 * 
	 * <p>
	 * This method uses Apache Commons Math3's MersenneTwister (32-bit) for high-quality
	 * pseudo-random number generation. The seed parameter ensures reproducibility.
	 * </p>
	 * 
	 * <p><strong>Note:</strong> MersenneTwister provides better statistical properties
	 * than Java's standard Random class.</p>
	 * 
	 * @param length the number of random values to generate
	 * @param min the minimum value (inclusive)
	 * @param max the maximum value (exclusive)
	 * @param seed the seed for reproducible random generation
	 * @return an array of random double values uniformly distributed in [min, max)
	 */
	public double[] generateRandomDoublesByColt(int length, double min, double max, long seed) {
		// Create random number generator
		// MersenneTwister64 and MersenneTwister are available
		MersenneTwister rng = new MersenneTwister(seed);           // 32-bit MT
		UniformRealDistribution dist = new UniformRealDistribution(rng, min, max);

		return dist.sample(length);
	}
	/**
	 * Generates an array of random doubles using Java's standard Random class.
	 * 
	 * <p>
	 * This is a simple, lightweight method using Java's built-in Random class.
	 * Each call uses a different seed (based on system time), so results are
	 * not reproducible.
	 * </p>
	 * 
	 * <p><strong>Performance:</strong> Suitable for general-purpose random generation
	 * where statistical quality is not critical.</p>
	 * 
	 * @param length the number of random values to generate
	 * @param min the minimum value (inclusive)
	 * @param max the maximum value (inclusive)
	 * @return an array of random double values uniformly distributed in [min, max]
	 */
	public double[] generateRandomDoubles(int length, double min, double max) {
		Random random = new Random();
		double[] randomArray = new double[length];
		for (int i = 0; i < length; i++) {
			randomArray[i] = min + (max - min) * random.nextDouble();
		}
		return randomArray;
	}



	/**
	 * Generates an array of random doubles using Apache Commons Math3.
	 * 
	 * <p>
	 * This method explicitly uses Apache Commons Math3's RandomGenerator interface
	 * with MersenneTwister implementation. It provides fine-grained control over
	 * the random generation process.
	 * </p>
	 * 
	 * <p><strong>Difference from generateRandomDoublesByColt:</strong><br>
	 * This method samples one value at a time in a loop, while generateRandomDoublesByColt
	 * uses the bulk sample() method. Both produce identical results given the same seed.</p>
	 * 
	 * @param arrayLength the number of random values to generate
	 * @param min the minimum value (inclusive)
	 * @param max the maximum value (exclusive)
	 * @param seed the seed for reproducible random generation
	 * @return an array of random double values uniformly distributed in [min, max)
	 */
	public double[] generateRandomDoublesByMath3(int arrayLength, double min, double max, long seed) {
		// Create random number generator
		org.apache.commons.math3.random.RandomGenerator rng = new MersenneTwister(seed);

		// Create uniform distribution object over [min, max] range
		UniformRealDistribution uniformDist = new UniformRealDistribution(
				rng, min, max);

		// Create and populate array with uniformly distributed values
		double[] uniformArray = new double[arrayLength];
		for (int i = 0; i < arrayLength; i++) {
			uniformArray[i] = uniformDist.sample();
		}

		return uniformArray;
	}
}