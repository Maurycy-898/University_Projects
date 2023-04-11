package code.algorithms;

import code.IO_operators.BitOutputStream;
import code.frequency_tools.CheckedFrequencyTable;
import code.frequency_tools.FrequencyTable;
import java.io.IOException;
import java.util.Objects;

public final class Compressor extends CommonPart {

    // The underlying bit output stream (not null)
    private final BitOutputStream output;

    // Number of saved underflow bits
    private int numUnderflow;


    /**
     * Constructs an arithmetic coding encoder based on the specified bit output stream
     */
    public Compressor(int numBits, BitOutputStream out) {
        super(numBits);
        output = Objects.requireNonNull(out);
        numUnderflow = 0;
    }

    /**
     * Encodes the specified symbol based on the specified frequency table.
     * This updates this arithmetic coder's state and may write out some bits.
     */
    public void write(FrequencyTable frequencyTable, int symbol) throws IOException {
        write(new CheckedFrequencyTable(frequencyTable), symbol);
    }


    /**
     * Encodes the specified symbol based on the specified frequency table.
     * Also updates this arithmetic coder's state and may write out some bits.
     */
    public void write(CheckedFrequencyTable frequencies, int symbol) throws IOException {
        update(frequencies, symbol);
    }


    /**
     * Terminates the arithmetic coding by flushing any buffered bits, so that the output can be decoded properly.
     * It is important that this method must be called at the end of encoding process.
     */
    public void finish() throws IOException {
        output.write(1);
    }


    protected void shift() throws IOException {
        int bit = (int)(low >>> (numStateBits - 1));
        output.write(bit);

        // Write out the saved underflow bits
        for (; numUnderflow > 0; numUnderflow--)
            output.write(bit ^ 1);
    }


    protected void underflow() {
        if (numUnderflow == Integer.MAX_VALUE)
            throw new ArithmeticException("Maximum underflow reached");
        numUnderflow++;
    }
}
