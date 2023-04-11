package code.compression_algorithms;

import code.IO_bit_operators.BitInputStream;
import code.tools.FrequencyTable;
import java.io.IOException;
import java.util.Objects;

public final class ArithmeticDecompressor {

    // The top bit at width numStateBits, which is 0100...000
    private final long halfRange;

    // The second-highest bit at width numStateBits, which is 0010...000. This is zero when numStateBits = 1
    private final long quarterRange;

    // Maximum allowed total from a frequency table at all times during coding
    private final long maximumTotal;

    // Bit mask of numStateBits ones, which is 0111...111
    private final long stateMask;

    // Low end of this arithmetic coder's current range. Conceptually has an infinite number of trailing 0s
    private long low;

    // High end of this arithmetic coder's current range. Conceptually has an infinite number of trailing 1s
    private long high;

    // The underlying bit input stream (not null).
    private final BitInputStream input;

    // The current raw code bits being buffered, which is always in the range [low, high].
    private long code;


    /**
     * Constructs an arithmetic coding decoder based on the
     * specified bit input stream, and fills the code bits
     */
    public ArithmeticDecompressor(int numberOfBits, BitInputStream in) throws IOException {
        if (numberOfBits < 1 || numberOfBits > 62) {
            throw new IllegalArgumentException("State size out of range");
        }
        long fullRange = 1L << numberOfBits;
        halfRange = fullRange >>> 1;
        quarterRange = halfRange >>> 1;
        long minimumRange = quarterRange + 2;
        maximumTotal = Math.min(Long.MAX_VALUE / fullRange, minimumRange);
        stateMask = fullRange - 1;
        low = 0;
        high = stateMask;

        code = 0;
        input = Objects.requireNonNull(in);
        for (int i = 0; i < numberOfBits; i++) {
            code = code << 1 | readCodeBit();
        }
    }


    /**
     * Decodes the next symbol based on the specified frequency table and returns it.
     * Also updates this arithmetic coder's state and may read in some bits
     */
    public int read(FrequencyTable frequencyTable) throws IOException {
        // Translate from coding range scale to frequency table scale
        long total = frequencyTable.getSumOfAllFrequencies();
        long range = high - low + 1;
        long offset = code - low;
        long value = ((offset + 1) * total - 1) / range;
        if (value * range / total > offset)
            throw new AssertionError();
        if (value < 0 || value >= total)
            throw new AssertionError();

        // A kind of binary search. Find the highest symbol such that frequencies.getLow(symbol) <= value.
        int start = 0;
        int end = frequencyTable.getSymbolLimit();
        while (end - start > 1) {
            int middle = (start + end) >>> 1;
            if (frequencyTable.getCumulativeFrequency(middle) > value) {
                end = middle;
            }
            else {
                start = middle;
            }
        }
        if (start + 1 != end) {
            throw new AssertionError();
        }
        int symbol = start;
        if (offset < frequencyTable.getCumulativeFrequency(symbol) * range / total || frequencyTable.getCumulativeFrequency(symbol + 1) * range / total <= offset) {
            throw new AssertionError();
        }
        update(frequencyTable, symbol);
        if (code < low || code > high) {
            throw new AssertionError("Code out of range");
        }
        return symbol;
    }

    /**
     * Returns the next bit (0 or 1) from the input stream. The end
     * of stream is treated as an infinite number of trailing zeros
     */
    private int readCodeBit() throws IOException {
        int temp = input.readBit();
        if (temp == -1)
            temp = 0;
        return temp;
    }



    /**
     * Updates the code range (low and high) of this arithmetic coder as a result
     * of processing the specified symbol with the specified frequency table.
     *
     */
    private void update(FrequencyTable frequencyTable, int symbol) throws IOException {
        if (low >= high || (low & stateMask) != low || (high & stateMask) != high) {
            throw new AssertionError("Low or high out of range");
        }

        // Frequency table values
        long sumOfAllFrequencies = frequencyTable.getSumOfAllFrequencies();
        long symbolCumulativeLow = frequencyTable.getCumulativeFrequency(symbol);
        long symbolCumulativeHigh = frequencyTable.getCumulativeFrequency(symbol + 1);

        // Update range
        long range = high - low + 1;
        long newLow  = low + (symbolCumulativeLow  * range / sumOfAllFrequencies);
        long newHigh = low + (symbolCumulativeHigh * range / sumOfAllFrequencies) - 1;
        low = newLow;
        high = newHigh;

        // While low and high have the same top bit value, shift them out
        while (((low ^ high) & halfRange) == 0) {
            shift();
            low  = ((low  << 1) & stateMask);
            high = ((high << 1) & stateMask) | 1;
        }

        // While low's top two bits are 01 and highs are 10, delete the second-highest bit of both
        while ((low & ~high & quarterRange) != 0) {
            underflow();
            low = (low << 1) ^ halfRange;
            high = ((high ^ halfRange) << 1) | halfRange | 1;
        }
    }

    /**
     * Called to handle the situation when the top bit of low and high are equal.
     */
    private void shift() throws IOException {
        code = ((code << 1) & stateMask) | readCodeBit();
    }

    /**
     * Called to handle the situation when low=01... and high=10...
     */
    private void underflow() throws IOException {
        code = (code & halfRange) | ((code << 1) & (stateMask >>> 1)) | readCodeBit();
    }
}
