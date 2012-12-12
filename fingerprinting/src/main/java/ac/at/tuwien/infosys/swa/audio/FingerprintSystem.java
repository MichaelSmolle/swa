package ac.at.tuwien.infosys.swa.audio;

import ac.at.tuwien.infosys.swa.audio.util.*;

import javax.sound.sampled.AudioInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.round;

/**
 * Implementation of the fingerprinting algorithm defined in the paper "A Highly Robust Audio Fingerprinting System" by
 * Jaap Haitsma and Ton Kalker.
 *
 * @see <a href="http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.103.2175&rep=rep1&type=pdf ">A Highly Robust
 *      Audio Fingerprinting System</a>
 */
public class FingerprintSystem {

    private static final int READ_BUFFER_LENGTH = 1024;
    private static final float DEFAULT_FRAME_TIME = (float) 2048 / 5512;
    private static final float DEFAULT_SHIFT = (float) 1 / 32;
    private static final int MIN_FINGERPRINT_SIZE = 256;

    private final int frameSize;
    private final float sampleRate;
    private final int shift;
    private final Energizer energizer;
    private final FrequencyAnalyzer analyzer;

    /**
     * Create an instance of fingerprinting system that will fingerprint audio of specified sample rate in Hz, will use
     * specified frame size in bytes for each {@link SubFingerprint}, will make a shift in audio specified number of
     * bytes for each consecutive frame, will use specified {@link IWindowFunction} with the {@link FrequencyAnalyzer},
     * and will use specified {@link ISplitter} with the {@link Energizer}.
     *
     * @param sampleRate the sample rate in Hz of audio that will be fingerprinted.
     * @param frameSize  the number of bytes in audio used for each {@link SubFingerprint}.
     * @param shift      the number of bytes shifted in audio for each consecutive frame.
     * @param function   the {@link IWindowFunction} to be used with the {@link FrequencyAnalyzer}
     * @param splitter   the {@link ISplitter} to be used with the {@link Energizer}.
     */
    public FingerprintSystem(final float sampleRate,
                             final int frameSize,
                             final int shift,
                             final IWindowFunction function,
                             final ISplitter splitter) {
        super();

        this.sampleRate = sampleRate;
        this.frameSize = frameSize;
        this.shift = shift;
        this.energizer = new Energizer(splitter);
        this.analyzer = new FrequencyAnalyzer(frameSize, function);
    }

    /**
     * Create an instance of fingerprinting system that will fingerprint audio of specified sample rate in Hz, will use
     * specified frame size in bytes for each {@link SubFingerprint}, and will make a shift in audio specified number of
     * bytes for each consecutive frame.
     *
     * @param sampleRate the sample rate in Hz of audio that will be fingerprinted.
     * @param frameSize  the number of bytes in audio used for each {@link SubFingerprint}.
     * @param shift      the number of bytes shifted in audio for each consecutive frame.
     */
    public FingerprintSystem(final float sampleRate, final int frameSize, final int shift) {
        this(sampleRate, frameSize, shift, new HannWindowFunction(frameSize), new LogarithmicSplitter());
    }

    /**
     * Create an instance of fingerprinting system that will fingerprint audio of specified sample rate in Hz, and will
     * use specified frame size in bytes for each {@link SubFingerprint}.
     *
     * @param sampleRate the sample rate in Hz of audio that will be fingerprinted.
     * @param frameSize  the number of bytes in audio used for each {@link SubFingerprint}.
     */
    public FingerprintSystem(final float sampleRate, final int frameSize) {
        this(sampleRate, frameSize, round(frameSize * DEFAULT_SHIFT));
    }

    /**
     * Create an instance of fingerprinting system that will fingerprint audio of specified sample rate in Hz.
     *
     * @param sampleRate the sample rate in Hz of audio that will be fingerprinted.
     */
    public FingerprintSystem(final float sampleRate) {
        this(sampleRate, round(sampleRate * DEFAULT_FRAME_TIME));
    }

    /**
     * Return {@link Fingerprint} of specified audio.
     *
     * @param audio the audio to be fingerprinted.
     * @return the {@link Fingerprint}.
     */
    public Fingerprint fingerprint(final byte[] audio) {
        final double[] buffer = new double[audio.length];

        for (int i = 0; i < audio.length; i++) {
            buffer[i] = audio[i];
        }

        return fingerprint(buffer);
    }

    /**
     * Return {@link Fingerprint} of specified audio.
     *
     * @param audio the audio to be fingerprinted.
     * @return the {@link Fingerprint}.
     */
    public Fingerprint fingerprint(final double[] audio) {
        if (audio.length < (this.frameSize + this.shift * MIN_FINGERPRINT_SIZE)) {
            throw new IllegalArgumentException("Input audio is too small to be fingerprinted!");
        }

        double[] previousEnergies = null;

        final List<SubFingerprint> subFingerprints = new LinkedList<SubFingerprint>();
        final double[] frame = new double[this.frameSize];
        for (int i = 0; i < ((audio.length - this.frameSize) / this.shift); i++) {
            System.arraycopy(audio, i * this.shift, frame, 0, this.frameSize);
            final double[] currentEnergies = this.energizer.getEnergies(this.sampleRate,
                    this.analyzer.getMagnitudes(frame));
            if (null == previousEnergies) {
                previousEnergies = new double[currentEnergies.length];
                Arrays.fill(previousEnergies, 0);
                subFingerprints.add(getSubFingerprint(previousEnergies, currentEnergies));
            } else {
                subFingerprints.add(getSubFingerprint(previousEnergies, currentEnergies));
            }
            previousEnergies = currentEnergies;
            System.arraycopy(frame, this.shift, frame, 0, this.frameSize - this.shift);
        }

        return new Fingerprint(0, this.shift / this.sampleRate, subFingerprints);
    }

    private static SubFingerprint getSubFingerprint(final double[] previous, final double[] current) {
        int result = 0;

        for (int i = 0; i < (current.length - 1); i++) {
            result <<= 1;
            if (0 < (current[i] - current[i + 1] - previous[i] + previous[i + 1])) {
                result++;
            }
        }

        return new SubFingerprint(result);
    }

    /**
     * Return {@link Fingerprint} of specified {@link AudioInputStream}.
     *
     * @param audio the {@link AudioInputStream} to be fingerprinted.
     * @return the {@link Fingerprint}.
     * @throws IOException if reading of {@link AudioInputStream} fails.
     */
    public static Fingerprint fingerprint(final AudioInputStream audio) throws IOException {
        final AudioInputStream input = new Converter(audio).toPCM().toMono().to8Bit().getAudioInputStream();

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final byte[] buffer = new byte[READ_BUFFER_LENGTH];
        int read = input.read(buffer);
        while (-1 < read) {
            out.write(buffer, 0, read);
            read = input.read(buffer);
        }

        return Downsampler.FROM > input.getFormat().getSampleRate()
                ? new FingerprintSystem(input.getFormat().getSampleRate()).fingerprint(out.toByteArray())
                : new FingerprintSystem(Downsampler.TO).fingerprint(new Downsampler().downsample(out.toByteArray()));
    }
}
