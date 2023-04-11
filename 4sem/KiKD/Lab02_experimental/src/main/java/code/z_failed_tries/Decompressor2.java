package code.z_failed_tries;

import code.IO_bit_operators.BitInputStream;
import code.tools.FrequencyTable;

import java.io.IOException;
import java.util.Objects;

public final class Decompressor2 {

    // The top bit at width numStateBits, which is 0100...000
    private final long halfRange;

    // The second-highest bit at width numStateBits, which is 0010...000. This is zero when numStateBits = 1
    private final long quarterRange;

    // Bit mask of numStateBits ones, which is 0111...111
    private final long topRange;

    // Low end of this arithmetic coder's current range. Conceptually has an infinite number of trailing 0s
    private long low;

    // High end of this arithmetic coder's current range. Conceptually has an infinite number of trailing 1s
    private long high;

    // The underlying bit input stream (not null).
    private final BitInputStream input;

    // The current raw code bits being buffered, which is always in the range [low, high].
    private long value;


    /**
     * Constructs an arithmetic coding decoder based on the
     * specified bit input stream, and fills the code bits
     */
    public Decompressor2(int numberOfBits, BitInputStream in) throws IOException {
        if (numberOfBits < 1 || numberOfBits > 62) { throw new IllegalArgumentException("State size out of range"); }

        topRange = (1L << numberOfBits) - 1;
        halfRange = (topRange / 2) + 1;
        quarterRange = halfRange / 2;

        low = 0;
        high = topRange;

        value = 0;
        input = Objects.requireNonNull(in);
        for (int i = 0; i < numberOfBits; i++) {
            value = 2 * value + readCodeBit();
        }
    }


    /**
     * Decodes the next symbol based on the specified frequency table and returns it.
     * Also updates this arithmetic coder's state and may read in some bits
     */
    public int read(FrequencyTable frequencyTable) throws IOException {
        long sumOfAllFrequencies = frequencyTable.getSumOfAllFrequencies();
        long range = high - low + 1;
        int cumulative = (int) ((((long) (value - low) + 1) * (long)frequencyTable.getCumulativeFrequency(0)) / range);
        int symbol;
        for (symbol = 1; frequencyTable.getCumulativeFrequency(symbol) > cumulative; symbol++);

        high = low + (range * frequencyTable.getCumulativeFrequency(symbol - 1)) / frequencyTable.getCumulativeFrequency(0) - 1;
        low = low + (range * frequencyTable.getCumulativeFrequency(symbol)) / frequencyTable.getCumulativeFrequency(0);

        while(true) {

            if (low >= halfRange) {
                value -= halfRange;
                low -= halfRange;
                high -= halfRange;
            }
            else if (low >= quarterRange && high < 3 * quarterRange) {
                value -= quarterRange;
                low -= quarterRange;
                high -= quarterRange;
            }
            else {
                break;
            }
            low = 2 * low;
            high = 2 * high + 1;
            value = 2 * value + readCodeBit();
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
}
