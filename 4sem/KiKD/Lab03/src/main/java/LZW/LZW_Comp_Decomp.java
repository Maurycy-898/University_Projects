package LZW;

import java.util.LinkedHashMap;

public class LZW_Comp_Decomp {

    public static String compress(String text) {
        int DICTIONARY_INDEX;
        LinkedHashMap<String, Integer> dictionary = new LinkedHashMap<>();
        StringBuilder compressedText = new StringBuilder();

        for (DICTIONARY_INDEX = 0; DICTIONARY_INDEX < 256; DICTIONARY_INDEX++) {
            dictionary.put(Character.toString((char) DICTIONARY_INDEX), DICTIONARY_INDEX);
        }

        String knownPhrase = "";
        for (char symbol : text.toCharArray()) {
            String newPhrase = knownPhrase + symbol;

            if (dictionary.containsKey(newPhrase)) {
                knownPhrase = newPhrase;
            }
            else {
                if(! dictionary.containsKey(knownPhrase)) {
                    System.out.println(knownPhrase);
                }
                compressedText.append((char) dictionary.get(knownPhrase).intValue());
                dictionary.put(knownPhrase + symbol, DICTIONARY_INDEX);
                knownPhrase = Character.toString(symbol);
                DICTIONARY_INDEX++;
            }
        }
        if (!knownPhrase.isEmpty()) {
            compressedText.append((char) dictionary.get(knownPhrase).intValue());
        }

        System.out.println("Dictionary length = " + DICTIONARY_INDEX);
        return compressedText.toString();
    }

    public static String decompress(String text){
        int DICTIONARY_INDEX;
        String[] compressedTextArray = (text + "").split("");
        LinkedHashMap<Integer,String> DICTIONARY = new LinkedHashMap<>();
        StringBuilder decompressedText = new StringBuilder();

        for (DICTIONARY_INDEX = 0; DICTIONARY_INDEX < 256; DICTIONARY_INDEX++) {
            DICTIONARY.put(DICTIONARY_INDEX, Character.toString((char) DICTIONARY_INDEX));
        }

        String oldPhrase = compressedTextArray[0];
        String currentPhrase = "";
        String lastPhraseSymbol = "";

        decompressedText.append(oldPhrase);
        for(int i = 1; i < compressedTextArray.length; i++){

            int currCode = Character.codePointAt(compressedTextArray[i],0);
            if (DICTIONARY.get(currCode) != null){
                currentPhrase = DICTIONARY.get(currCode);
            }
            else {
                currentPhrase = oldPhrase + lastPhraseSymbol;
            }

            lastPhraseSymbol = currentPhrase.substring(0, 1);
            DICTIONARY.put(DICTIONARY_INDEX, oldPhrase + lastPhraseSymbol);
            decompressedText.append(currentPhrase);
            oldPhrase = currentPhrase;

            DICTIONARY_INDEX ++;
        }

        return decompressedText.toString();
    }
}
