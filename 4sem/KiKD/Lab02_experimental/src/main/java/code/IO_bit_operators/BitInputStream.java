package code.IO_bit_operators;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public final class BitInputStream implements AutoCloseable{

    // The underlying byte stream to read from (not null).
    private final InputStream input;

    // Either in the range [0x00, 0xFF] if bits are available, or -1 if end of stream is reached.
    public int currentByte;

    // Number of remaining bits in the current byte, always between 0 and 7 (inclusive).
    private int numBitsRemaining;


    /**
     * Constructs a bit input stream based on the specified byte input stream
     */
    public BitInputStream(InputStream in) {
        input = Objects.requireNonNull(in);
        currentByte = 0;
        numBitsRemaining = 0;
    }


    /**
     * Reads symbol (byte) from the stream. Symbol value should be >= 0 and < 256
     */
    public int readByteSymbol(int symbolLength) throws IOException {
        if (symbolLength >= 32) {
            System.out.println("Length must be < 32 (int properties)");
            return -1;
        }

        int symbol = 0;
        for (int i = 0; i < symbolLength; i++) {
            symbol = (symbol << 1) | readBit();
        }

        return symbol;
    }

    /**
     * Reads a bit from this stream. Returns 0 or 1 if a bit is available, or -1 if
     * the end of stream is reached. The end of stream always occurs on a byte boundary
     */
    public int readBit() throws IOException {
        if (currentByte == -1) {
            return -1;
        }
        if (numBitsRemaining == 0) {
            currentByte = input.read();
            if (currentByte == -1) {
                return -1;
            }

            numBitsRemaining = 8;
        }
        if (numBitsRemaining <= 0) {
            throw new AssertionError();
        }
        numBitsRemaining--;
        return (currentByte >>> (numBitsRemaining - 1)) & 1;
    }

    /**
     * Reads a bit from this stream. Returns 0 or 1 if a bit is available, or throws an EOFException
     * if the end of stream is reached. The end of stream always occurs on a byte boundary
     */
    public int readNoEof() throws IOException {
        int result = readBit();
        if (result != -1) {
            return result;
        }
        else {
            throw new EOFException();
        }
    }

    /**
     * Closes this stream and the underlying input stream.
     */
    public void close() throws IOException {
        input.close();
        currentByte = -1;
        numBitsRemaining = 0;
    }

}
