package code.IO_operators;

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
     * Constructs a bit output stream based on the specified byte output stream
     */
    public BitOutputStream(OutputStream out) {
        output = Objects.requireNonNull(out);
        currentByte = 0;
        numBitsFilled = 0;
    }


    /**
     * Writes a bit to the stream. The specified bit must be 0 or 1
     */
    public void write(int b) throws IOException {
        if (b != 0 && b != 1)
            throw new IllegalArgumentException("Argument must be 0 or 1");
        currentByte = (currentByte << 1) | b;
        numBitsFilled++;
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
            write(0);
        output.close();
    }
}
