package code.IO_operators;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public final class BitInputStream implements AutoCloseable{

    // The underlying byte stream to read from (not null).
    private InputStream input;

    // Either in the range [0x00, 0xFF] if bits are available, or -1 if end of stream is reached.
    private int currentByte;

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
     * Reads a bit from this stream. Returns 0 or 1 if a bit is available, or -1 if
     * the end of stream is reached. The end of stream always occurs on a byte boundary
     */
    public int read() throws IOException {
        if (currentByte == -1)
            return -1;
        if (numBitsRemaining == 0) {
            currentByte = input.read();
            if (currentByte == -1)
                return -1;
            numBitsRemaining = 8;
        }
        if (numBitsRemaining <= 0)
            throw new AssertionError();
        numBitsRemaining--;
        return (currentByte >>> numBitsRemaining) & 1;
    }

    /**
     * Reads a bit from this stream. Returns 0 or 1 if a bit is available, or throws an EOFException
     * if the end of stream is reached. The end of stream always occurs on a byte boundary
     */
    public int readNoEof() throws IOException {
        int result = read();
        if (result != -1)
            return result;
        else
            throw new EOFException();
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
