package ac.at.tuwien.infosys.swa.audio.util;

/**
 * Interface for classes that define non-overlapping frequency intervals.
 */
public interface ISplitter {

    /**
     * Return start indexes of each frequency interval for an audio with specified sample rate and frame size. The
     * returned array is defined like this where <code>n</code> is the length of returned array: <ul> <li>0th element
     * defines start of 1st frequency interval.</li> <li>1th element defines start of 2nd frequency interval.</li>
     * <li>...</li> <li><code>(n-1)</code>-th element defines start of <code>n</code>-th frequency interval.</li>
     * <li><code>n</code>-th element defines start of frequency region that should be ignored.</li> </ul> Thus, if
     * returned array has length <code>n</code>, than it defines <code>(n-1)</code> frequency intervals.
     *
     * @param sampleRate the sample rate of audio.
     * @param frameSize  the frame size of audio that was passed to the {@link FrequencyAnalyzer#getMagnitudes(double[])}
     *                   method.
     * @return start indexes of each frequency interval.
     */
    int[] getStartIndexes(double sampleRate, int frameSize);
}
