package ac.at.tuwien.infosys.swa.audio.util;

import edu.emory.mathcs.jtransforms.fft.DoubleFFT_1D;

import java.util.Arrays;

/**
 * Utility class which can be used to analyze frequency spectrum of an audio frame using a Fast Fourier Transform.
 *
 * @see <a href="http://en.wikipedia.org/wiki/Frequency_spectrum">Frequency spectrum (Wikipedia)</a>
 * @see <a href="http://en.wikipedia.org/wiki/Fast_Fourier_transform">Fast Fourier Transform (Wikipedia)</a>
 */
public class FrequencyAnalyzer {

    private final int frameSize;
    private final IWindowFunction function;
    private final DoubleFFT_1D fft;

    /**
     * Create an instance of frequency spectrum analyzer for audio frames of specified size that will be multiplied with
     * specified {@link IWindowFunction} before their frequency spectrum is analyzed.
     *
     * @param frameSize the size of audio frame that will be used for frequency spectrum analysis.
     * @param function  the window function that will be multiplied with each frame before its frequency spectrum is
     *                  analyzed.
     */
    public FrequencyAnalyzer(final int frameSize, final IWindowFunction function) {
        super();

        this.frameSize = frameSize;
        this.function = function;
        this.fft = new DoubleFFT_1D(frameSize);
    }

    /**
     * Return squared magnitudes of specified audio frame using a Fast Fourier Transform for the frequency spectrum
     * analysis. The frame will be multiplied with the {@link IWindowFunction} before it is analyzed.
     *
     * @param frame the audio frame to be frequency spectrum analyzed.
     * @return the squared magnitudes of frequency spectrum.
     * @throws IllegalArgumentException if specified audio frame has different length than it was defined in the {@link
     *                                  #FrequencyAnalyzer(int, IWindowFunction)} constructor.
     */
    public double[] getMagnitudes(final double[] frame) {
        if (frame.length != this.frameSize) {
            throw new IllegalArgumentException("Input frame has wrong length!");
        }

        final double[] result = this.function.multiplyWith(frame);

        this.fft.realForward(result);
        result[0] *= result[0];
        for (int i = 1; i < (this.frameSize / 2); i++) {
            result[i] = result[2 * i] * result[2 * i] + result[2 * i + 1] * result[2 * i + 1];
        }

        return Arrays.copyOf(result, this.frameSize / 2);
    }
}
