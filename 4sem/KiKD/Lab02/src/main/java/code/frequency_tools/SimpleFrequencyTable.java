package code.frequency_tools;

import java.util.Objects;

public final class SimpleFrequencyTable implements FrequencyTable {

    // The frequency for each symbol. Its length is at least 1, and each element is non-negative.
    private final int[] frequencies;

    // cumulative[i] is the sum of 'frequencies' from 0 (inclusive) to i (exclusive).
    private int[] cumulative;

    // Always equal to the sum of 'frequencies'.
    private int total;


    /**
     * Constructs a frequency table from the specified array of symbol frequencies. There must be at least
     * 1 symbol, no symbol has a negative frequency, and the total must not exceed {@code Integer.MAX_VALUE}.
     */
    public SimpleFrequencyTable(int[] frequencies) {
        Objects.requireNonNull(frequencies);
        if (frequencies.length < 1)
            throw new IllegalArgumentException("At least 1 symbol needed");
        if (frequencies.length > Integer.MAX_VALUE - 1)
            throw new IllegalArgumentException("Too many symbols");

        this.frequencies = frequencies.clone();  // Make copy
        total = 0;
        for (int x : this.frequencies) {
            if (x < 0)
                throw new IllegalArgumentException("Negative frequency");
            total = checkedAdd(x, total);
        }
        cumulative = null;
    }

    /**
     * Constructs a frequency table by copying the specified frequency table.
     */
    public SimpleFrequencyTable(FrequencyTable frequencyTable) {
        Objects.requireNonNull(frequencyTable);
        int numSym = frequencyTable.getSymbolLimit();
        if (numSym < 1)
            throw new IllegalArgumentException("At least 1 symbol needed");

        frequencies = new int[numSym];
        total = 0;
        for (int i = 0; i < frequencies.length; i++) {
            int x = frequencyTable.get(i);
            if (x < 0)
                throw new IllegalArgumentException("Negative frequency");
            frequencies[i] = x;
            total = checkedAdd(x, total);
        }
        cumulative = null;
    }


    /**
     * Returns the number of symbols in this frequency table, which is at least 1.
     */
    public int getSymbolLimit() {
        return frequencies.length;
    }

    /**
     * Returns the frequency of the specified symbol. The returned value is at least 0.
     */
    public int get(int symbol) {
        checkSymbol(symbol);
        return frequencies[symbol];
    }

    /**
     * Sets the frequency of the specified symbol to the specified value. The frequency value
     * must be at least 0. If an exception is thrown, then the state is left unchanged.
     */
    public void set(int symbol, int freq) {
        checkSymbol(symbol);
        if (freq < 0)
            throw new IllegalArgumentException("Negative frequency");

        int temp = total - frequencies[symbol];
        if (temp < 0)
            throw new AssertionError();
        total = checkedAdd(temp, freq);
        frequencies[symbol] = freq;
        cumulative = null;
    }

    /**
     * Increments the frequency of the specified symbol.
     */
    public void increment(int symbol) {
        checkSymbol(symbol);
        if (frequencies[symbol] == Integer.MAX_VALUE)
            throw new ArithmeticException("Arithmetic overflow");
        total = checkedAdd(total, 1);
        frequencies[symbol]++;
        cumulative = null;
    }

    /**
     * Returns the total of all symbol frequencies. The returned value is at
     */
    public int getTotal() {
        return total;
    }


    /**
     * Returns the sum of the frequencies of all the symbols strictly
     * below the specified symbol value. The returned value is at least 0
     */
    public int getLow(int symbol) {
        checkSymbol(symbol);
        if (cumulative == null)
            initCumulative();
        return cumulative[symbol];
    }

    /**
     * Returns the sum of the frequencies of the specified symbol
     * and all the symbols below. The returned value is at least 0.
     */
    public int getHigh(int symbol) {
        checkSymbol(symbol);
        if (cumulative == null)
            initCumulative();
        return cumulative[symbol + 1];
    }

    // Recomputes the array of cumulative symbol frequencies.
    private void initCumulative() {
        cumulative = new int[frequencies.length + 1];
        int sum = 0;
        for (int i = 0; i < frequencies.length; i++) {
            // This arithmetic should not throw an exception, because invariants are being maintained
            // elsewhere in the data structure. This implementation is just a defensive measure.
            sum = checkedAdd(frequencies[i], sum);
            cumulative[i + 1] = sum;
        }
        if (sum != total)
            throw new AssertionError();
    }

    // Returns silently if 0 <= symbol < frequencies.length, otherwise throws an exception.
    private void checkSymbol(int symbol) {
        if (symbol < 0 || symbol >= frequencies.length)
            throw new IllegalArgumentException("Symbol out of range");
    }

    /**
     * Returns a string representation of this frequency table,
     * useful for debugging only, and the format is subject to change.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < frequencies.length; i++)
            sb.append(String.format("%d\t%d%n", i, frequencies[i]));
        return sb.toString();
    }

    // Adds the given integers, or throws an exception if the result cannot be represented as an int (i.e. overflow).
    private static int checkedAdd(int x, int y) {
        int z = x + y;
        if (y > 0 && z < x || y < 0 && z > x)
            throw new ArithmeticException("Arithmetic overflow");
        else
            return z;
    }
}
