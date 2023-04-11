package code.compression_algorithms;

import code.IO_bit_operators.BitInputStream;
import code.IO_bit_operators.BitOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class CompressorLZW {
    // The underlying bit output stream (not null)
    private final BitOutputStream output;

    // The underlying bit input stream (not null)
    private final InputStream input;


    public CompressorLZW(InputStream input, BitOutputStream output) {
        this.output = Objects.requireNonNull(output);
        this.input = Objects.requireNonNull(input);
    }


    public void compressData(String encodingType) throws IOException {
        String text = new String(input.readAllBytes());
        LinkedHashMap<String, Integer> DICTIONARY = new LinkedHashMap<>();
        ArrayList<Integer> encodedTextArray = new ArrayList<>();
        int DICTIONARY_SIZE = 256;

        for (int i = 0; i < DICTIONARY_SIZE; i++) {
            DICTIONARY.put(String.valueOf((char) i), i);
        }

        String knownPhrase = "";
        for (char symbol : text.toCharArray()) {
            String newPhrase = knownPhrase + symbol;
            if (DICTIONARY.containsKey(newPhrase)) {
                knownPhrase = newPhrase;
            }
            else {
                if (!DICTIONARY.containsKey(knownPhrase)) { System.out.println(knownPhrase); }
                encodedTextArray.add(DICTIONARY.get(knownPhrase));
                DICTIONARY.put(newPhrase, DICTIONARY_SIZE);
                knownPhrase = String.valueOf(symbol);

                DICTIONARY_SIZE++;
            }
        }

        if (!knownPhrase.isEmpty()) {
            if (!DICTIONARY.containsKey(knownPhrase)) { System.out.println(knownPhrase); }
            encodedTextArray.add(DICTIONARY.get(knownPhrase));
        }
        System.out.println(encodedTextArray);

        switch (encodingType) {
            case "gamma" -> {
                for (int value : encodedTextArray) {
                    eliasGammaEncode(value);
                }
            }
            case "delta" -> {
                for (int value : encodedTextArray) {
                    eliasDeltaEncode(value);
                }
            }
            case "fibonacci" -> {
                for (int value : encodedTextArray) {
                    fibbonaciEncode(value);
                }
            }
            default -> {
                for (int value : encodedTextArray) {
                    eliasOmegaEncode(value);
                }
            }
        }
        output.close();
        System.out.println("Dictionary size = " + DICTIONARY_SIZE);
    }



    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    void eliasGammaEncode(int symbol) throws IOException {
        int highestPow = (int) ((Math.log(symbol) / Math.log(2)));
        for (int i = 0; i < highestPow; i++) {
            output.writeBit(0);
        }
        output.writeSymbol(symbol);
    }

    void eliasOmegaEncode(int symbol) throws IOException {
        int length = (int) ((Math.log(symbol) / Math.log(2)) + 1);

        ArrayList<Integer> bitQueue = new ArrayList<>();
        bitQueue.add(0);

        while (symbol > 1) {
            for (int i = 0; i < length; i++) {
                bitQueue.add(symbol % 2);
                symbol /= 2;
            }
            symbol = length - 1;
            length = (int) ((Math.log(symbol) / Math.log(2)) + 1);
        }

        for (int i = bitQueue.size() - 1; i >= 0; i--) {
            output.writeBit(bitQueue.get(i));
        }
    }

    void eliasDeltaEncode(int symbol) throws IOException {
        int length = (int) ((Math.log(symbol) / Math.log(2)) + 1);
        int lengthOfLen = (int) (Math.log(length) / Math.log(2));

        for (int i = lengthOfLen; i > 0; i--) {
            output.writeBit(0);
        }
        for (int i = lengthOfLen; i >= 0; i--) {
            output.writeBit((length >> i) & 1);
        }
        for (int i = length - 2; i >= 0; i--) {
            output.writeBit((symbol >> i) & 1);
        }
    }

    public void fibbonaciEncode(int symbol) throws IOException {
        ArrayList<Integer> fibArray = new ArrayList<>();
        fibArray.add(1);
        fibArray.add(2);

        int i;
        for(i = 2; fibArray.get(i - 1) <= symbol; i++) {
            fibArray.add(fibArray.get(i - 1) + fibArray.get(i - 2));
        }

        int index = i - 2;
        ArrayList<Integer> bitArr = new ArrayList<>();
        bitArr.add(1);

        while (symbol > 0) {
            bitArr.add(1);
            symbol = symbol - fibArray.get(index);

            index--;
            while (index >= 0 && fibArray.get(index) > symbol) {
                bitArr.add(0);
                index--;
            }
        }

        for (int bitValue : bitArr) {
            output.writeBit(bitValue);
        }
    }
}
