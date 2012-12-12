package ac.at.tuwien.infosys.swa.audio.util;

import static java.lang.StrictMath.PI;
import static java.lang.StrictMath.cos;

/**
 * Class which defines the Hann window function.
 *
 * @see <a href="http://en.wikipedia.org/wiki/Hann_window">Hann window (Wikipedia)</a>
 */
public class HannWindowFunction implements IWindowFunction {

    private final int length;
    private final double[] multipliers;

    /**
     * Create an instance of the Hann window function with specified length.
     *
     * @param length the length of the Hann window function.
     */
    public HannWindowFunction(final int length) {
        super();

        this.length = length;
        this.multipliers = new double[length];
        for (int i = 0; i < length; i++) {
            this.multipliers[i] = 0.5 * (1 - cos(2 * PI * i / (length - 1)));
        }
    }

    /**
     * Multiply specified audio frame with the Hann window function.
     *
     * @param frame the audio frame to be multiplied.
     * @return the result of multiplying specified audio frame with the Hann window function.
     * @throws IllegalArgumentException if specified audio frame has different length than it was defined in the {@link
     *                                  #HannWindowFunction(int)} constructor.
     */
    public double[] multiplyWith(final double[] frame) {
        if (frame.length != this.length) {
            throw new IllegalArgumentException("Window and frame have different lengths!");
        }

        final double[] result = new double[this.length];

        for (int i = 0; i < this.length; i++) {
            result[i] = this.multipliers[i] * frame[i];
        }

        return result;
    }
}
