package code.frequency_tools;

public interface FrequencyTable {
    /**
     * Returns the number of symbols in this frequency table, which is a positive number.
     */
    public int getSymbolLimit();


    /**
     * Returns the frequency of the specified symbol. The returned value is at least 0.
     */
    public int get(int symbol);


    /**
     * Sets the frequency of the specified symbol to the specified value.
     * The frequency value must be at least 0.
     */
    public void set(int symbol, int freq);


    /**
     * Increments the frequency of the specified symbol.
     */
    public void increment(int symbol);


    /**
     * Returns the total of all symbol frequencies. The returned value is at
     * least 0 and is always equal to getHigh(getSymbolLimit() - 1).
     */
    public int getTotal();


    /**
     * Returns the sum of the frequencies of all the symbols strictly
     * below the specified symbol value. The returned value is at least 0.
     */
    public int getLow(int symbol);


    /**
     * Returns the sum of the frequencies of the specified symbol
     * and all the symbols below. The returned value is at least 0.
     */
    public int getHigh(int symbol);

}
