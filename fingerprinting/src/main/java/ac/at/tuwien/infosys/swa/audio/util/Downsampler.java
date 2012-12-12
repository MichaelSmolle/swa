package ac.at.tuwien.infosys.swa.audio.util;

/**
 * Utility class which can be used to convert an audio with {@link #FROM} sample rate into an audio with {@link #TO}
 * sample rate.
 */
public class Downsampler {

    private static final int MULTIPLIER = 8;
    /**
     * From sample rate in Hz.
     */
    public static final float FROM = 44100;
    /**
     * To sample rate in Hz.
     */
    public static final float TO = FROM / MULTIPLIER;

    /**
     * Low pass filter for downsampling 44100Hz to 5512Hz.
     */
    private static final double[] FILTER = {
            -6.4796e-04, -1.4440e-03, -2.7023e-03, -4.4407e-03, -6.1915e-03, -6.9592e-03,
            -5.3707e-03, 2.3907e-18, 1.0207e-02, 2.5522e-02, 4.5170e-02, 6.7289e-02,
            8.9180e-02, 1.0778e-01, 1.2027e-01, 1.2467e-01, 1.2027e-01, 1.0778e-01,
            8.9180e-02, 6.7289e-02, 4.5170e-02, 2.5522e-02, 1.0207e-02, 2.3907e-18,
            -5.3707e-03, -6.9592e-03, -6.1915e-03, -4.4407e-03, -2.7023e-03, -1.4440e-03,
            -6.4796e-04
    };

    /**
     * Downsample specified audio with {@link #FROM} sample rate into an audio with {@link #TO} sample rate.
     *
     * @param buffer the audio with {@link #FROM} sample rate.
     * @return downsampled audio with {@link #TO} sample rate.
     */
    public double[] downsample(final byte[] buffer) {
        final double[] result = new double[buffer.length / MULTIPLIER - 3];

        for (int i = 0; i < result.length; i++) {
            result[i] = convolvePointAt(MULTIPLIER * i, buffer);
        }

        return result;
    }

    private static double convolvePointAt(final int index, final byte[] buffer) {
        double result = 0;

        for (int i = 0; i < FILTER.length; i++) {
            result += buffer[index + i] * FILTER[i];
        }

        return result;
    }
}
