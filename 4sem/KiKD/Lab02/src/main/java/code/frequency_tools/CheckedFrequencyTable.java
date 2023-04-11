package code.frequency_tools;

import java.util.Objects;

/**
 * A wrapper that checks the preconditions (arguments) and post-conditions (return value)
 * of all the frequency table methods. Useful for finding faults in a frequency table
 * implementation
 */
public final class CheckedFrequencyTable implements FrequencyTable{

    // The underlying frequency table that holds the data (not null).
    private final FrequencyTable frequencyTable;


    public CheckedFrequencyTable(FrequencyTable frequencyTable) {
        this.frequencyTable = Objects.requireNonNull(frequencyTable);
    }

    public int getSymbolLimit() {
        int result = frequencyTable.getSymbolLimit();
        if (result <= 0)
            throw new AssertionError("Non-positive symbol limit");
        return result;
    }

    public int get(int symbol) {
        int result = frequencyTable.get(symbol);
        if (!isSymbolInRange(symbol))
            throw new AssertionError("IllegalArgumentException expected");
        if (result < 0)
            throw new AssertionError("Negative symbol frequency");
        return result;
    }

    public int getTotal() {
        int result = frequencyTable.getTotal();
        if (result < 0)
            throw new AssertionError("Negative total frequency");
        return result;
    }

    public int getLow(int symbol) {
        if (isSymbolInRange(symbol)) {
            int low   = frequencyTable.getLow (symbol);
            int high  = frequencyTable.getHigh(symbol);
            if (!(0 <= low && low <= high && high <= frequencyTable.getTotal()))
                throw new AssertionError("Symbol low cumulative frequency out of range");
            return low;
        } else {
            frequencyTable.getLow(symbol);
            throw new AssertionError("IllegalArgumentException expected");
        }
    }

    public int getHigh(int symbol) {
        if (isSymbolInRange(symbol)) {
            int low   = frequencyTable.getLow (symbol);
            int high  = frequencyTable.getHigh(symbol);
            if (!(0 <= low && low <= high && high <= frequencyTable.getTotal()))
                throw new AssertionError("Symbol high cumulative frequency out of range");
            return high;
        } else {
            frequencyTable.getHigh(symbol);
            throw new AssertionError("IllegalArgumentException expected");
        }
    }

    public String toString() {
        return "CheckedFrequencyTable (" + frequencyTable.toString() + ")";
    }

    public void set(int symbol, int freq) {
        frequencyTable.set(symbol, freq);
        if (!isSymbolInRange(symbol) || freq < 0)
            throw new AssertionError("IllegalArgumentException expected");
    }

    public void increment(int symbol) {
        frequencyTable.increment(symbol);
        if (!isSymbolInRange(symbol))
            throw new AssertionError("IllegalArgumentException expected");
    }

    private boolean isSymbolInRange(int symbol) {
        return 0 <= symbol && symbol < getSymbolLimit();
    }
}
