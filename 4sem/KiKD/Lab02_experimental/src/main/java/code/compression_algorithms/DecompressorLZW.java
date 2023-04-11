package code.compression_algorithms;

import code.IO_bit_operators.BitInputStream;

import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Objects;

public class DecompressorLZW {
    private int fileSize = 0;
    // The underlying bit input stream (not null)
    private final BitInputStream input;

    // Stream to write decompressed data to file (not null)
    private final OutputStream output;


    public DecompressorLZW(BitInputStream input, OutputStream output) {
        this.input = Objects.requireNonNull(input);
        this.output = Objects.requireNonNull(output);
    }


    public void decompress(String decodingType, int fileSize) throws IOException {
        this.fileSize = fileSize;
        ArrayList<Integer> encodedTextArray = new ArrayList<>();

        switch (decodingType) {
            case "gamma" -> {
                while (input.currentByte != -1) {
                    Integer value = eliasGammaDecode();
                    encodedTextArray.add(value);
                }
            }
            case "delta" -> {
                while (input.currentByte != -1) {
                    Integer value = eliasDeltaDecode();
                    encodedTextArray.add(value);
                }
            }
            case "fibonacci" -> {
                while (input.currentByte != -1) {
                    Integer value = fibonacciDecode();
                    encodedTextArray.add(value);
                }
            }
            default -> {
                while (true) {
                    try {
                        int value = eliasOmegaDecode();
                        encodedTextArray.add(value);
                    }
                    catch (EOFException ex) {
                        System.out.println(encodedTextArray);
                        break;
                    }
                }
            }
        }
        System.out.println(encodedTextArray);

        StringBuilder decodedText = new StringBuilder();
        LinkedHashMap<Integer, String> DICTIONARY = new LinkedHashMap<>();
        int DICTIONARY_SIZE = 256;

        for (int i = 0; i < DICTIONARY_SIZE; i++) {
            DICTIONARY.put(i, String.valueOf((char) i));
        }

        String oldPhrase = String.valueOf(encodedTextArray.get(0));
        String currentPhrase = "";
        String lastPhraseSymbol = "";

        decodedText.append(oldPhrase);
        for(int i = 1; i < encodedTextArray.size(); i++){

            int currCode = encodedTextArray.get(i);
            if (DICTIONARY.get(currCode) != null){
                currentPhrase = DICTIONARY.get(currCode);
            }
            else {
                currentPhrase = oldPhrase + lastPhraseSymbol;
            }

            lastPhraseSymbol = currentPhrase.substring(0, 1);
            DICTIONARY.put(DICTIONARY_SIZE, oldPhrase + lastPhraseSymbol);
            decodedText.append(currentPhrase);
            oldPhrase = currentPhrase;

            DICTIONARY_SIZE ++;
        }

        output.write(decodedText.toString().getBytes(StandardCharsets.UTF_8));
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    int eliasGammaDecode() throws IOException {
        int countZeros = 0;
        while (input.readBit() == 0) {

            countZeros ++;
        }
        int decodedNumber = 1;
        for (int i = 0; i < countZeros; i++) {
            decodedNumber = (decodedNumber << 1) + input.readBit();
        }

        return decodedNumber;
    }

    Integer eliasOmegaDecode() throws IOException {
        int decodedNumber = 1;

        while (input.readNoEof() == 1) {
            int length = decodedNumber;
            decodedNumber = 1;
            for (int i = 0; i < length; i++) {
                decodedNumber = decodedNumber << 1;
                if (input.readNoEof() == 1) {
                    decodedNumber = decodedNumber | 1;
                }
            }
        }
        return decodedNumber;
    }

    int eliasDeltaDecode() throws IOException {
        int decodedNumber = 1;
        int length = 1;
        int lengthOfLen = 0;

        while (input.readBit() == 0) {
            lengthOfLen++;
        }

        for (int i = 0; i < lengthOfLen; i++) {
            length = length << 1;
            if (input.readBit() == 1) {
                length = length | 1;
            }
        }

        for (int i = 0; i < length-1; i++) {
            decodedNumber = decodedNumber << 1;
            if (input.readBit() == 1) {
                decodedNumber = decodedNumber | 1;
            }
        }

        return decodedNumber;
    }

    int fibonacciDecode() throws IOException {
        int decodedNumber = 0;
        ArrayList<Integer> bitArr = new ArrayList<>();
        bitArr.add(input.readBit());
        bitArr.add(input.readBit());
        int i = 1;
        while (!(bitArr.get(i - 1) == 1 && bitArr.get(i) == 1)) {
            bitArr.add(input.readBit());
            i++;
        }

        ArrayList<Integer> fibArray = new ArrayList<>();
        fibArray.add(1);
        fibArray.add(2);
        for(int j = 2; fibArray.get(j - 1) <= bitArr.size(); j++) {
            fibArray.add(fibArray.get(j - 1) + fibArray.get(j - 2));
        }

        for (int k = 0; k < bitArr.size() - 1; k++) {
            if (bitArr.get(k) == 1) {
                decodedNumber += fibArray.get(k);
            }
        }

        return decodedNumber;
    }
}
