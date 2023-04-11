package code.algorithms;

import code.IO_operators.BitInputStream;
import code.frequency_tools.CheckedFrequencyTable;
import code.frequency_tools.FrequencyTable;
import java.io.IOException;
import java.util.Objects;

public final class Decompressor extends CommonPart{

    // The underlying bit input stream (not null).
    private final BitInputStream input;

    // The current raw code bits being buffered, which is always in the range [low, high].
    private long code;


    /**
     * Constructs an arithmetic coding decoder based on the
     * specified bit input stream, and fills the code bits
     */
    public Decompressor(int numBits, BitInputStream in) throws IOException {
        super(numBits);
        input = Objects.requireNonNull(in);
        code = 0;
        for (int i = 0; i < numStateBits; i++)
            code = code << 1 | readCodeBit();
    }


    /**
     * Decodes the next symbol based on the specified frequency table and returns it.
     * Also updates this arithmetic coder's state and may read in some bits
     */
    public int read(FrequencyTable frequencyTable) throws IOException {
        return read(new CheckedFrequencyTable(frequencyTable));
    }

    /**
     * Decodes the next symbol based on the specified frequency table and returns it.
     * Also updates this arithmetic coder's state and may read in some bits
     */
    public int read(CheckedFrequencyTable frequencies) throws IOException {
        // Translate from coding range scale to frequency table scale
        long total = frequencies.getTotal();
        if (total > maximumTotal)
            throw new IllegalArgumentException("Cannot decode symbol because total is too large");
        long range = high - low + 1;
        long offset = code - low;
        long value = ((offset + 1) * total - 1) / range;
        if (value * range / total > offset)
            throw new AssertionError();
        if (value < 0 || value >= total)
            throw new AssertionError();

        // A kind of binary search. Find the highest symbol such that frequencies.getLow(symbol) <= value.
        int start = 0;
        int end = frequencies.getSymbolLimit();
        while (end - start > 1) {
            int middle = (start + end) >>> 1;
            if (frequencies.getLow(middle) > value)
                end = middle;
            else
                start = middle;
        }
        if (start + 1 != end)
            throw new AssertionError();

        int symbol = start;
        if (offset < frequencies.getLow(symbol) * range / total || frequencies.getHigh(symbol) * range / total <= offset)
            throw new AssertionError();
        update(frequencies, symbol);
        if (code < low || code > high)
            throw new AssertionError("Code out of range");
        return symbol;
    }

    protected void shift() throws IOException {
        code = ((code << 1) & stateMask) | readCodeBit();
    }

    protected void underflow() throws IOException {
        code = (code & halfRange) | ((code << 1) & (stateMask >>> 1)) | readCodeBit();
    }

    /**
     * Returns the next bit (0 or 1) from the input stream. The end
     * of stream is treated as an infinite number of trailing zeros
     */
    private int readCodeBit() throws IOException {
        int temp = input.read();
        if (temp == -1)
            temp = 0;
        return temp;
    }
}
