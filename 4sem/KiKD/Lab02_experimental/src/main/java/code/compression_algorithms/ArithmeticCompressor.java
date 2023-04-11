package code.compression_algorithms;

import code.IO_bit_operators.BitOutputStream;
import code.tools.FrequencyTable;
import java.io.IOException;
import java.util.Objects;

public final class ArithmeticCompressor {
    // Number of bits for the 'low' and 'high' state variables. Must be in the range [1, 62]
    private final int numStateBits;

    // The top bit at width numStateBits, which is 0100...000
    private final long halfRange;

    // The second-highest bit at width numStateBits, which is 0010...000. This is zero when numStateBits = 1
    private final long quarterRange;

    // Bit mask of numStateBits ones, which is 0111...111
    private final long stateMask;

    // Low end of this arithmetic coder's current range. Conceptually has an infinite number of trailing 0s
    private long low;

    // High end of this arithmetic coder's current range. Conceptually has an infinite number of trailing 1s
    private long high;

    // The underlying bit output stream (not null)
    private final BitOutputStream output;

    // Number of saved underflow bits
    private int numUnderflow;


    /**
     * Constructs an arithmetic compressor based on the specified bit output stream and number of bits
     */
    public ArithmeticCompressor(int numberOfBits, BitOutputStream out) {
        if (numberOfBits < 1 || numberOfBits > 62) {
            throw new IllegalArgumentException("State size out of range");
        }
        numStateBits = numberOfBits;
        long fullRange = 1L << numStateBits;
        halfRange = fullRange >>> 1;  // Non-zero
        quarterRange = halfRange >>> 1;  // Can be zero
        stateMask = fullRange - 1;
        low = 0;
        high = stateMask;

        output = Objects.requireNonNull(out);
        numUnderflow = 0;
    }


    /**
     * Encodes given symbol based on the frequency table
     * Updates the code range (low and high)
     */
    public void write(FrequencyTable frequencyTable, int symbol) throws IOException {
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
            shiftAndEmit();
            low  = ((low  << 1) & stateMask);
            high = ((high << 1) & stateMask) | 1;
        }
        // Now low's top bit must be 0 and high's top bit must be 1

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
    private void shiftAndEmit() throws IOException {
        int bit = (int)(low >>> (numStateBits - 1));
        output.writeBit(bit);

        // Write out the saved underflow bits
        while (numUnderflow > 0) {
            output.writeBit(bit ^ 1);
            numUnderflow --;
        }
    }

    /**
     * Called to handle the situation when low=01... and high=10...
     */
    private void underflow() {
        if (numUnderflow == Integer.MAX_VALUE) {
            throw new ArithmeticException("Maximum underflow reached");
        }
        numUnderflow ++;
    }

    /**
     * Terminates the arithmetic coding by flushing any buffered bits, so that the output can be decoded properly
     * It is important that this method must be called at the end of compression process
     */
    public void finishCompression() throws IOException {
        output.writeBit(1);
    }
}
