package code.tools;

public final class FrequencyTable {

    // The frequency for each symbol. Its length is at least 1, and each element is non-negative
    private final int[] frequencies;

    // The sum of frequencies (array above) from zero (inclusive) to index (exclusive)
    private int[] cumulativeFrequencies;

    // Always equal to the sum of 'frequencies'
    private int sumOfFrequencies;

    /**
     * Constructs a frequency table based on number of symbols
     */
    public FrequencyTable(int numberOfSymbols) {
        if (numberOfSymbols < 1) {
            throw new IllegalArgumentException("Number of symbols must be positive (at least 1)");
        }
        sumOfFrequencies = 0;
        frequencies = new int[numberOfSymbols];

        for (int i = 0; i < frequencies.length; i++) {
            // At the beginning all symbols have frequency = 1
            frequencies[i] = 1;
            sumOfFrequencies = safeAdd(sumOfFrequencies, 1);
        }
        cumulativeFrequencies = null;
    }


    /**
     * Returns the total number of symbols in this frequency table
     */
    public int getSymbolLimit() {
        return frequencies.length;
    }

    /**
     * Increments the frequency of the specified symbol
     */
    public void incrementSymbolFrequency(int symbol) {
        if (frequencies[symbol] == Integer.MAX_VALUE) {
            throw new ArithmeticException("Arithmetic overflow");
        }
        sumOfFrequencies = safeAdd(sumOfFrequencies, 1);
        frequencies[symbol] ++;

        cumulativeFrequencies = null;
    }

    /**
     * Returns the total of all symbol frequencies. The returned value is at
     */
    public int getSumOfAllFrequencies() {
        return sumOfFrequencies;
    }

    /**
     * Returns the sum of the frequencies of all symbols below the specified symbol value
     */
    public int getCumulativeFrequency(int symbol) {
        if (cumulativeFrequencies == null) {
            computeCumulativeFrequency();
        }
        return cumulativeFrequencies[symbol];
    }

    // Recomputes the array of cumulative symbol frequencies
    private void computeCumulativeFrequency() {
        cumulativeFrequencies = new int[frequencies.length + 1];
        int sum = 0;
        for (int i = 0; i < frequencies.length; i++) {
            sum = safeAdd(frequencies[i], sum);
            cumulativeFrequencies[i + 1] = sum;
        }
    }

    // Adds the given integers, throws an exception if the result cannot be represented as an int
    private static int safeAdd(int a, int b) {
        int sum = a + b;
        if (b > 0 && sum < a || b < 0 && sum > a) {
            throw new ArithmeticException("Arithmetic overflow");
        }
        return sum;
    }
}
