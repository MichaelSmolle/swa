package ac.at.tuwien.infosys.swa.audio.util;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;
import static javax.sound.sampled.AudioFormat.Encoding.PCM_UNSIGNED;

/**
 * Utility class which can be used to convert an {@link AudioInputStream} into different formats.
 */
public class Converter {

    private static final int DEFAULT_SAMPLE_SIZE_IN_BITS = 16;
    private final AudioInputStream input;
    private final AudioFormat format;

    /**
     * Create an instance  that will convert specified {@link AudioInputStream}.
     *
     * @param input the {@link AudioInputStream} to be converted.
     */
    public Converter(final AudioInputStream input) {
        super();

        this.input = input;
        this.format = input.getFormat();
    }

    /**
     * Return the {@link AudioInputStream}.
     *
     * @return the {@link AudioInputStream}.
     */
    public AudioInputStream getAudioInputStream() {
        return this.input;
    }

    /**
     * Return a converter that will return a <code>PCM</code> {@link AudioInputStream} when its {@link
     * #getAudioInputStream()} method is invoked.
     *
     * @return a converter with a <code>PCM</code> {@link AudioInputStream}.
     */
    public Converter toPCM() {
        return isPCM()
                ? this
                : convertTo(new AudioFormat(this.format.getSampleRate(),
                DEFAULT_SAMPLE_SIZE_IN_BITS,
                this.format.getChannels(),
                true,
                true), this.input);
    }

    /**
     * Return a converter that will return a mono {@link AudioInputStream} when its {@link #getAudioInputStream()}
     * method is invoked.
     *
     * @return a converter with a mono {@link AudioInputStream}.
     */
    public Converter toMono() {
        return isMono()
                ? this
                : is8Bit() ? convertTo(new AudioFormat(this.format.getSampleRate(),
                8,
                1,
                false,
                true), this.input)
                : convertTo(new AudioFormat(this.format.getSampleRate(),
                this.format.getSampleSizeInBits(),
                1,
                true,
                true), this.input);
    }

    /**
     * Return a converter that will return an 8-bit {@link AudioInputStream} when its {@link #getAudioInputStream()}
     * method is invoked.
     *
     * @return a converter with an 8-bit {@link AudioInputStream}.
     */
    public Converter to8Bit() {
        return is8Bit()
                ? this
                : convertTo(new AudioFormat(this.format.getSampleRate(),
                8,
                this.format.getChannels(),
                false,
                true), this.input);
    }

    private boolean isPCM() {
        return PCM_UNSIGNED.equals(this.format.getEncoding()) || PCM_SIGNED.equals(this.format.getEncoding());
    }

    private boolean isMono() {
        return 1 == this.format.getChannels();
    }

    private boolean is8Bit() {
        return 8 == this.format.getSampleSizeInBits();
    }

    private static Converter convertTo(final AudioFormat format, final AudioInputStream input) {
        return new Converter(AudioSystem.getAudioInputStream(format, input));
    }
}
