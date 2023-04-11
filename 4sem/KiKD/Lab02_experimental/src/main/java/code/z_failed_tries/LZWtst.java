package code.z_failed_tries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class LZWtst {
    public String lzw_compress(String text){
        int LZW_DICT_SIZE = 256;
        HashMap<String,Integer> dictionary = new LinkedHashMap<>();
        for (int i = 0; i < LZW_DICT_SIZE; i++) {
            dictionary.put(String.valueOf((char) i), i);
        }
        StringBuilder compressedText;
        char[] textArr = text.toCharArray();

        String phraseToAdd;
        String knownPhrase = "";
        for (char symbol : textArr) {
            if (dictionary.containsKey(knownPhrase + symbol)) {
                knownPhrase += symbol;
            }
            else {

            }
        }

        String[] data = (text + "").split("");
        StringBuilder out = new StringBuilder();
        ArrayList<String> temp_out = new ArrayList<>();
        String currentChar;
        String phrase = data[0];


        for(int i = 1; i < data.length; i++){
            currentChar = data[i];
            if (dictionary.get(phrase + currentChar) != null){
                phrase += currentChar;
            }
            else{
                if(phrase.length() > 1){
                    temp_out.add(Character.toString((char)dictionary.get(phrase).intValue()));
                }
                else{
                    temp_out.add(Character.toString((char)Character.codePointAt(phrase,0)));
                }

                dictionary.put(phrase+currentChar,LZW_DICT_SIZE);
                LZW_DICT_SIZE++;
                phrase = currentChar;
            }
        }

        if(phrase.length() > 1){
            temp_out.add(Character.toString((char)dictionary.get(phrase).intValue()));
        }
        else{
            temp_out.add(Character.toString((char)Character.codePointAt(phrase,0)));
        }

        for(String outchar:temp_out){
            out.append(outchar);
        }
        System.out.println(LZW_DICT_SIZE);
        return out.toString();
    }

    public String lzw_extract(String input){
        HashMap<Integer,String> dictionary = new LinkedHashMap<>();
        String[] data = (input + "").split("");
        String currentChar = data[0];
        String oldPhrase = currentChar;
        String out = currentChar;
        int code = 256;
        String phrase="";
        for(int i = 1; i<data.length; i++){
            int currCode = Character.codePointAt(data[i],0);
            if(currCode < 256){
                phrase = data[i];
            }
            else{
                if(dictionary.get(currCode) != null){
                    phrase = dictionary.get(currCode);
                }
                else{
                    phrase = oldPhrase + currentChar;
                }
            }
            out+=phrase;
            currentChar = phrase.substring(0,1);
            dictionary.put(code,oldPhrase+currentChar);
            code++;
            oldPhrase = phrase;
        }
        return out;
    }
}
