package code.frequency_tools;

/**
 * An immutable frequency table where every symbol has the same frequency of 1.
 * Useful as a fallback model when no statistics are available.
 */
public final class FlatFrequencyTable implements FrequencyTable{

    // Total number of symbols, which is at least 1.
    private final int numSymbols;


    /**
     * Constructs a flat frequency table with the specified number of symbols.
     */
    public FlatFrequencyTable(int numSyms) {
        if (numSyms < 1)
            throw new IllegalArgumentException("Number of symbols must be positive");
        numSymbols = numSyms;
    }


    /**
     * Returns the number of symbols in this table, which is at least 1.
     */
    public int getSymbolLimit() {
        return numSymbols;
    }

    /**
     * Returns the frequency of the specified symbol, which is always 1.
     */
    public int get(int symbol) {
        checkSymbol(symbol);
        return 1;
    }

    /**
     * Returns the total of all symbol frequencies, which is
     * always equal to the number of symbols in this table
     */
    public int getTotal() {
        return numSymbols;
    }

    /**
     * Returns the sum of the frequencies of all the symbols strictly below
     */
    public int getLow(int symbol) {
        checkSymbol(symbol);
        return symbol;
    }

    /**
     * Returns the sum of the frequencies of the specified symbol and all
     * the symbols below. The returned value is equal to symbol + 1
     */
    public int getHigh(int symbol) {
        checkSymbol(symbol);
        return symbol + 1;
    }

    /**
     *  Returns silently if 0 <= symbol < numSymbols, otherwise throws an exception
     */
    private void checkSymbol(int symbol) {
        if (symbol < 0 || symbol >= numSymbols)
            throw new IllegalArgumentException("Symbol out of range");
    }

    /**
     * Returns a string representation of this frequency table. The format is subject to change.
     */
    public String toString() {
        return "FlatFrequencyTable=" + numSymbols;
    }

    /**
     * Unsupported operation, because this frequency table is immutable.
     */
    public void set(int symbol, int freq) {
        throw new UnsupportedOperationException();
    }

    /**
     * Unsupported operation, because this frequency table is immutable
     */
    public void increment(int symbol) {
        throw new UnsupportedOperationException();
    }

}
