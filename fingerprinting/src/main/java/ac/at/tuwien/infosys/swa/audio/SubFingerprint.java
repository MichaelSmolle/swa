package ac.at.tuwien.infosys.swa.audio;

import java.io.Serializable;

import static java.lang.Math.abs;

/**
 * Class to store a subfingerprint so it can be easily compared with other subfingerprints using the {@link
 * #difference(SubFingerprint)} method.
 */
public class SubFingerprint implements Serializable {

    private static final long serialVersionUID = -6294164835173846660L;

    /**
     * The number of bits used by each subfingerprint.
     */
    public static final int SIZE = Integer.SIZE;

    private final int value;

    /**
     * A subfingerprint with specified <code>int</code> value.
     *
     * @param value the <code>int</code> value.
     */
    public SubFingerprint(final int value) {
        super();

        this.value = value;
    }

    /**
     * Return the <code>int</code> value of this subfingerprint.
     *
     * @return the <code>int</code> value.
     */
    public int getValue() {
        return this.value;
    }

    /**
     * Returns a number of bits that are different from specified other subfingerprint. The difference is computed as a
     * Hamming distance (i.e. the number of bit errors).
     *
     * @param other the other subfingerprint that is compared to this subfingerprint.
     * @return the number of bits that are different.
     * @see <a href="http://en.wikipedia.org/wiki/Hamming_distance">Hamming Distance (Wikipedia)</a>
     */
    public int difference(final SubFingerprint other) {
        int result = 0;

        int value1 = this.value;
        int value2 = other.value;
        for (int i = 0; i < SIZE; i++) {
            if ((value1 % 2) != (value2 % 2)) {
                result++;
            }
            value1 >>= 1;
            value2 >>= 1;
        }

        return result;
    }

    @Override
    public int hashCode() {
        return this.value;
    }

    @Override
    public boolean equals(final Object other) {
        final boolean result;

        if ((null == other) || !(other instanceof SubFingerprint)) {
            result = false;
        } else {
            final SubFingerprint that = (SubFingerprint) other;
            result = that.canEqual(this) && (this.value == that.value);
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
        return other instanceof SubFingerprint;
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder(SIZE);

        int v = this.value;
        for (int i = 0; i < SIZE; i++) {
            result.insert(0, abs(v % 2));
            v >>= 1;
        }

        return result.toString();
    }
}
