package ac.at.tuwien.infosys.swa.audio.util;

/**
 * Utility class which can be used to return energy of each frequency interval which is defined by an {@link
 * ISplitter}.
 */
public class Energizer {

    private final ISplitter splitter;

    /**
     * Create an instance that uses specified {@link ISplitter} to define frequency intervals.
     *
     * @param splitter the {@link ISplitter} which will define frequency intervals.
     */
    public Energizer(final ISplitter splitter) {
        super();

        this.splitter = splitter;
    }

    /**
     * Return an energy of each frequency interval defined by the {@link ISplitter}. The energy of each frequency
     * interval is sum of magnitudes that are part of this interval. The {@link ISplitter} is used to define
     * non-overlapping frequency intervals.
     *
     * @param sampleRate the sample rate of audio.
     * @param magnitudes the squared frequency magnitudes of audio returned from the {@link
     *                   FrequencyAnalyzer#getMagnitudes(double[])} method.
     * @return an energy of each frequency interval.
     */
    public double[] getEnergies(final double sampleRate, final double[] magnitudes) {
        final int[] startIndexes = this.splitter.getStartIndexes(sampleRate, magnitudes.length * 2);
        final double[] result = new double[startIndexes.length - 1];

        for (int i = 0; i < result.length; i++) {
            for (int j = startIndexes[i]; j < startIndexes[i + 1]; j++) {
                result[i] += magnitudes[j];
            }
            result[i] /= startIndexes[i + 1] - startIndexes[i];
        }

        return result;
    }
}
