package ac.at.tuwien.infosys.swa.audio.util;

import java.util.Arrays;

import static java.lang.StrictMath.log10;
import static java.lang.StrictMath.pow;

/**
 * Class which defines the the splitter where frequency intervals have logarithmically spacing.
 *
 * @see <a href="http://en.wikipedia.org/wiki/Bark_scale">Bark Scale (Wikipedia)</a>
 */
public class LogarithmicSplitter implements ISplitter {

    /**
     * The start frequency that is used by the {@link #LogarithmicSplitter()} constructor.
     */
    public static final int DEFAULT_START_FREQUENCY = 300;
    /**
     * The end frequency that is used by the {@link #LogarithmicSplitter()} constructor.
     */
    public static final int DEFAULT_END_FREQUENCY = 2000;
    /**
     * The number of frequency intervals that is used by the {@link #LogarithmicSplitter()} constructor.
     */
    public static final int DEFAULT_INTERVALS_NUMBER = 33;

    private final double startFrequency;
    private final double endFrequency;
    private final int intervalsNumber;
    private final double[] intervalEndFrequencies;

    /**
     * Create an instance of logarithmic splitter that will split interval between start frequency and end frequency
     * into specified number of frequency intervals that will have logarithmic spacing.
     *
     * @param startFrequency  the start frequency.
     * @param endFrequency    the end frequency.
     * @param intervalsNumber the number of frequency intervals.
     */
    public LogarithmicSplitter(final double startFrequency, final double endFrequency, final int intervalsNumber) {
        super();

        this.startFrequency = startFrequency;
        this.endFrequency = endFrequency;
        this.intervalsNumber = intervalsNumber;

        this.intervalEndFrequencies = new double[intervalsNumber];
        final double start = log10(startFrequency);
        final double end = log10(endFrequency);
        final double step = (end - start) / intervalsNumber;

        for (int i = 1; i < intervalsNumber; i++) {
            this.intervalEndFrequencies[i - 1] = pow(10, start + (step * i));
        }
        this.intervalEndFrequencies[intervalsNumber - 1] = endFrequency;
    }

    /**
     * Create an instance of logarithmic splitter that will split interval between {@link #DEFAULT_START_FREQUENCY} and
     * {@link #DEFAULT_END_FREQUENCY} into {@link #DEFAULT_INTERVALS_NUMBER} number of frequency intervals that will
     * have logarithmic spacing.
     */
    public LogarithmicSplitter() {
        this(DEFAULT_START_FREQUENCY, DEFAULT_END_FREQUENCY, DEFAULT_INTERVALS_NUMBER);
    }

    /**
     * Return start indexes of each logarithmic frequency interval for an audio with specified sample rate and frame
     * size. The returned array is defined like this where <code>n</code> is the length of returned array: <ul> <li>0th
     * element defines start of 1st frequency interval.</li> <li>1th element defines start of 2nd frequency
     * interval.</li> <li>...</li> <li><code>(n-1)</code>-th element defines start of <code>n</code>-th frequency
     * interval.</li> <li><code>n</code>-th element defines start of frequency region that should be ignored.</li> </ul>
     * Thus, if returned array has length <code>n</code>, than it defines <code>(n-1)</code> frequency intervals.
     *
     * @param sampleRate the sample rate of audio.
     * @param frameSize  the frame size of audio that was passed to the {@link FrequencyAnalyzer#getMagnitudes(double[])}
     *                   method.
     * @return start indexes of each logarithmic frequency interval.
     */
    public int[] getStartIndexes(final double sampleRate, final int frameSize) {
        final int[] result = new int[this.intervalsNumber + 1];
        Arrays.fill(result, -1);

        for (int i = 0; i < (frameSize / 2); i++) {
            final double frequency = (sampleRate / frameSize) * i;
            int interval = -1;

            if ((frequency >= this.startFrequency) && (frequency <= this.endFrequency)) {
                interval = 0;
                while ((frequency > this.intervalEndFrequencies[interval]) && (interval < this.intervalsNumber)) {
                    interval++;
                }
                if (interval >= this.intervalsNumber) {
                    interval = -1;
                }
            }

            if (0 <= interval) {
                if (-1 == result[interval]) {
                    result[interval] = i;
                }
            } else if ((-1 != result[this.intervalsNumber - 1]) && (-1 == result[this.intervalsNumber])) {
                result[this.intervalsNumber] = i;
                break;
            }
        }

        return result;
    }
}
