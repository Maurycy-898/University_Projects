package code.z_failed_tries;

    import code.IO_bit_operators.BitOutputStream;
    import code.tools.FrequencyTable;
    import java.io.IOException;
    import java.util.Objects;

    public final class Compressor2 {

        // Number of bits for the 'low' and 'high' state variables. Must be in the range [1, 62]
        private final int numberOfBits;

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

        // The underlying bit output stream (not null)
        private final BitOutputStream output;

        // Number of saved underflow bits
        private int bitsToFollow;


        /**
         * Constructs an arithmetic compressor based on the specified bit output stream and number of bits
         */
        public Compressor2(int numberOfBits, BitOutputStream out) {
            if (numberOfBits < 1 || numberOfBits > 62) {
                throw new IllegalArgumentException("State size out of range");
            }
            this.numberOfBits = numberOfBits;
            topRange = (1L << this.numberOfBits) - 1;
            halfRange = topRange / 2 + 1;  // Non-zero
            quarterRange = halfRange / 2;  // Can be zero

            low = 0;
            high = topRange;

            output = Objects.requireNonNull(out);
            bitsToFollow = 0;
        }


        /**
         * Encodes given symbol based on the frequency table
         * Updates the code range (low and high)
         */
        public void write(FrequencyTable frequencyTable, int symbol) throws IOException {
            // Frequency table values
            long sumOfAllFrequencies = frequencyTable.getSumOfAllFrequencies();
            long symbolCumulativeLow = frequencyTable.getCumulativeFrequency(symbol);
            long symbolComulativeHigh = frequencyTable.getCumulativeFrequency(symbol + 1);

            // Update range
            long range = high - low + 1;
            long newLow  = low + (symbolCumulativeLow  * range / sumOfAllFrequencies);
            long newHigh = low + (symbolComulativeHigh * range / sumOfAllFrequencies) - 1;
            low = newLow;
            high = newHigh;


            while(true) {
                if (high < halfRange) {
                   bitPlusFollow(0);
                }
                else if (low >= halfRange) {
                    bitPlusFollow(1);
                    low -= halfRange;
                    high -= halfRange;
                }
                else if (low >= quarterRange && high < 3 * quarterRange) {
                    bitsToFollow += 1;
                    low -= quarterRange;
                    high -= quarterRange;
                }
                else {
                    break;
                }
                low = 2 * low;
                high = 2 * high + 1;
            }
        }

        /**
         * Called to handle the situation when the top bit of low and high are equal.
         */
        private void bitPlusFollow(int bit) throws IOException {
            output.writeBit(bit);

            // Write out the saved underflow bits
            while (bitsToFollow > 0) {
                output.writeBit(bit ^ 1);
                bitsToFollow--;
            }
        }

        /**
         * Terminates the arithmetic coding by flushing any buffered bits, so that the output can be decoded properly
         * It is important that this method must be called at the end of compression process
         */
        public void finishCompression() throws IOException {
            bitsToFollow++;
            if (low < quarterRange) {
                bitPlusFollow(0);
            }
            else {
                bitPlusFollow(1);
            }
        }
    }
