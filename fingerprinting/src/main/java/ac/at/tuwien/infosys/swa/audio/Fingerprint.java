package ac.at.tuwien.infosys.swa.audio;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Class to store a fingerprint so it can be easily compared with other fingerprints using the {@link
 * #match(Fingerprint...)} method.
 */
public class Fingerprint implements Serializable {

    private static final long serialVersionUID = -5584723374871814891L;

    /**
     * The length for new fingerprints that is used by the {@link #split()} method.
     */
    public static final int DEFAULT_SPLIT_LENGTH = 256;
    /**
     * The shift between each two consecutive fingerprints that is used by the {@link #split()} method.
     */
    public static final int DEFAULT_SPLIT_SHIFT = 1;

    /**
     * Maximum ratio of difference in bits for matching to be considered successful.
     */
    private static final double THRESHOLD = 0.35d;

    private final double startTime;
    private final double shiftDuration;
    private final SubFingerprint[] subFingerprints;

    /**
     * Create an instance with specified time in seconds from beginning of the audio from which specified {@link
     * SubFingerprint}s start, the duration in seconds between each two consecutive {@link SubFingerprint}, and having
     * specified {@link SubFingerprint}s.
     *
     * @param startTime       the time in seconds from beginning of the audio from which specified {@link
     *                        SubFingerprint}s start.
     * @param shiftDuration   the duration in seconds between each two consecutive {@link SubFingerprint}
     * @param subFingerprints the {@link SubFingerprint}s.
     */
    private Fingerprint(final double startTime, final double shiftDuration, final SubFingerprint... subFingerprints) {
        super();

        this.subFingerprints = Arrays.copyOf(subFingerprints, subFingerprints.length);
        this.startTime = startTime;
        this.shiftDuration = shiftDuration;
    }

    /**
     * Create an instance with specified time in seconds from beginning of the audio from which specified {@link
     * SubFingerprint}s start, the duration in seconds between each two consecutive {@link SubFingerprint}, and having
     * specified {@link SubFingerprint}s.
     *
     * @param startTime       the time in seconds from beginning of the audio from which specified {@link
     *                        SubFingerprint}s start.
     * @param shiftDuration   the duration in seconds between each two consecutive {@link SubFingerprint}
     * @param subFingerprints the {@link SubFingerprint}s.
     */
    public Fingerprint(final double startTime,
                       final double shiftDuration,
                       final Collection<SubFingerprint> subFingerprints) {
        super();

        this.startTime = startTime;
        this.shiftDuration = shiftDuration;

        final SubFingerprint[] array = new SubFingerprint[subFingerprints.size()];
        subFingerprints.toArray(array);
        this.subFingerprints = array;
    }

    /**
     * Try to match each specified fingerprint with this fingerprint. If matching was successful, then return the time
     * in seconds from beginning of the audio where best match was found. Otherwise, the method will return -1.
     *
     * @param others fingerprints to be matched against this fingerprint.
     * @return the time in seconds from beginning of the audio where best match was found, or -1 if matching was
     *         unsuccessful.
     */
    public double match(final Fingerprint... others) {
        double result = -1;
        int best = Integer.MAX_VALUE;

        for (final Fingerprint other : others) {
            for (int i = 0; i <= (this.subFingerprints.length - other.subFingerprints.length); i++) {
                int difference = 0;
                for (int j = 0; j < other.subFingerprints.length; j++) {
                    difference += this.subFingerprints[i + j].difference(other.subFingerprints[j]);
                }

                if (THRESHOLD > ((double) difference / (other.subFingerprints.length * SubFingerprint.SIZE))) {
                    if (difference < best) {
                        best = difference;
                        result = this.startTime + this.shiftDuration * i - other.startTime;
                    }
                }
            }
        }

        return result;
    }

    /**
     * Return the time in seconds from beginning of the audio from which this fingerprint was created.
     *
     * @return the time in seconds from beginning of the audio.
     */
    public double getStartTime() {
        return this.startTime;
    }

    /**
     * Return the duration in seconds between each {@link SubFingerprint} in this fingerprint.
     *
     * @return the duration in seconds between each {@link SubFingerprint}.
     */
    public double getShiftDuration() {
        return this.shiftDuration;
    }

    /**
     * Return all {@link SubFingerprint}s that defined this fingerprint.
     *
     * @return all {@link SubFingerprint}s
     */
    public SubFingerprint[] getSubFingerprints() {
        return Arrays.copyOf(this.subFingerprints, this.subFingerprints.length);
    }

    /**
     * Split this fingerprint into one or more fingerprints of specified length where each fingerprint is shifted
     * specified number of {@link SubFingerprint}s further away from the previous fingerprint.
     *
     * @param length the length of each new fingerprint.
     * @param shift  the number of {@link SubFingerprint}s between two consecutive fingerprints.
     * @return the one or more fingerprints of specified length.
     * @throws IllegalArgumentException if specified length is greater than the number of {@link SubFingerprint}s in
     *                                  this fingerprint.
     */
    public Fingerprint[] split(final int length, final int shift) {
        if (this.subFingerprints.length < length) {
            throw new IllegalArgumentException("The length is greater than number of subfingerprints.");
        }

        final List<Fingerprint> fingerprints = new LinkedList<Fingerprint>();
        final SubFingerprint[] array = new SubFingerprint[length];

        for (int i = 0; i < ((this.subFingerprints.length - length) / shift); i++) {
            System.arraycopy(this.subFingerprints, i * shift, array, 0, length);
            fingerprints.add(new Fingerprint(this.startTime + i * this.shiftDuration, this.shiftDuration, array));
        }

        final Fingerprint[] result = new Fingerprint[fingerprints.size()];
        return fingerprints.toArray(result);
    }

    /**
     * Split this fingerprint into one or more fingerprints of {@link #DEFAULT_SPLIT_LENGTH} length where each
     * fingerprint is shifted {@link #DEFAULT_SPLIT_SHIFT} number of {@link SubFingerprint}s further away from the
     * previous fingerprint.
     *
     * @return the one or more fingerprints of specified length.
     * @throws IllegalArgumentException if the number of {@link SubFingerprint}s in this fingerprint is less than {@link
     *                                  #DEFAULT_SPLIT_LENGTH}.
     */
    public Fingerprint[] split() {
        return split(DEFAULT_SPLIT_LENGTH, DEFAULT_SPLIT_SHIFT);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.subFingerprints);
    }

    @Override
    public boolean equals(final Object other) {
        final boolean result;

        if ((null == other) || !(other instanceof Fingerprint)) {
            result = false;
        } else {
            final Fingerprint that = (Fingerprint) other;
            result = that.canEqual(this) && Arrays.equals(this.subFingerprints, that.subFingerprints);
        }

        return result;
    }

    /**
     * Return if specified {@link Object} can at all be equal to this instance.
     *
     * @param other the {@link Object}.
     * @return if specified {@link Object} can at all be equal to this instance.
     * @see <a href="http://www.artima.com/lejava/articles/equality.html">How to Write an Equality Method in Java</a>
     */
    protected boolean canEqual(final Object other) {
        return other instanceof Fingerprint;
    }

    @Override
    public String toString() {
        final String lineSeparator = System.getProperty("line.separator");
        final StringBuilder result = new StringBuilder(this.subFingerprints.length * SubFingerprint.SIZE
                + this.subFingerprints.length * lineSeparator.length());

        for (final SubFingerprint subFingerprint : this.subFingerprints) {
            result.append(subFingerprint).append(lineSeparator);
        }

        return result.toString();
    }
}
