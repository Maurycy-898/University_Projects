package code.IO_bit_operators;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

public final class BitOutputStream implements AutoCloseable {

    // The underlying byte stream to write to (not null).
    private final OutputStream output;

    // The accumulated bits for the current byte, always in the range [0x00, 0xFF].
    private int currentByte;

    // Number of accumulated bits in the current byte, always between 0 and 7 (inclusive).
    private int numBitsFilled;


    /**
     * Constructs an output stream of bits based on the specified byte output stream
     */
    public BitOutputStream(OutputStream out) {
        output = Objects.requireNonNull(out);
        currentByte = 0;
        numBitsFilled = 0;
    }

    /**
     * Writes symbol bits to the stream.
     */
    public void writeSymbol(int symbol) throws IOException {
        int symbolLength = (int) (Math.log(symbol) / Math.log(2) + 1);
        int[] bitArray = new int[symbolLength];

        for (int i = 0; i < symbolLength; i++) {
            bitArray[i] = symbol % 2;
            symbol /= 2;
        }

        for (int i = symbolLength - 1; i >= 0; i--) {
            writeBit(bitArray[i]);
        }
    }

    /**
     * Writes a bit to the stream. The specified bit must be 0 or 1
     */
    public void writeBit(int bit) throws IOException {
        if (bit != 0 && bit != 1) {
            throw new IllegalArgumentException("Argument must be 0 or 1");
        }

        currentByte = (currentByte << 1) | bit;
        numBitsFilled ++;

        if (numBitsFilled == 8) {
            output.write(currentByte);
            currentByte = 0;
            numBitsFilled = 0;
        }
    }

    /**
     * Closes this stream and the underlying output stream. If called when this
     * bit stream is not at a byte boundary, then the minimum number of "0" bits
     * (between 0 and 7 of them) are written as padding to reach the next byte boundary
     */
    public void close() throws IOException {
        while (numBitsFilled != 0)
            writeBit(0);
        output.close();
    }
}
