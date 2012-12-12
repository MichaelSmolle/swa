package ac.at.tuwien.infosys.swa.audio.util;

/**
 * Interface for classes that define a window function.
 *
 * @see <a href="http://en.wikipedia.org/wiki/Window_function">Window Function (Wikipedia)</a>
 * @see <a href="http://www.ee.iitm.ac.in/~nitin/_media/ee462/fftwindows.pdf">Understanding FFT Windows</a>
 */
public interface IWindowFunction {

    /**
     * Multiply specified audio frame with the window function.
     *
     * @param frame the audio frame to be multiplied.
     * @return the result of multiplying specified audio frame with this window function.
     */
    double[] multiplyWith(double[] frame);
}
