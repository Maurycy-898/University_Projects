package code.app_runnable;

import code.compression_algorithms.CompressorLZW;

import java.util.ArrayList;
import java.util.Collections;

public class Tst {
    public static void main(String[] args) {
//        testEncode(72);
//        testEncode(101);
//        System.out.println();

        int[] Arr = {1 ,0 ,1 ,1 ,0 ,1 ,0 ,0 ,1 ,0 ,0 ,0 ,0 ,1 ,0 ,1 ,1 ,0 ,1 ,1 ,0 ,0 ,1 ,0 ,1 ,0};
        int arrPtr = 0;

        int decodedNumber = 1;
        while (Arr[arrPtr] == 1) {
            arrPtr++;
            int length = decodedNumber;
            decodedNumber = 1;
            for (int i = 0; i < length; i++) {
                decodedNumber = decodedNumber << 1;
                if (Arr[arrPtr] == 1) {

                    decodedNumber = decodedNumber | 1;
                }
                arrPtr++;
            }
        }
        System.out.println(decodedNumber);
    }

    public static void testEncode(int symbol) {
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
            System.out.print(bitQueue.get(i) + " ,");
        }
    }
}
