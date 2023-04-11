package LZW;

import statistics.CompressionRatio;
import statistics.EntropyCalculator;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class LZW_Test_App {
    public static void main(String[] args) throws IOException {
        if (args[0].equals("compress")) {
            testCompress(args[1]);
        }
        else if (args[0].equals("decompress")) {
            testDecompress();
        }
    }

    public static void testCompress(String input) throws IOException {
        File infile = new File("src\\main\\java\\test_files\\" + input);
        File outfile = new File("src\\main\\java\\test_files\\comp_result.lzw");

        try (InputStream in = new BufferedInputStream(new FileInputStream(infile));
             OutputStream out = new BufferedOutputStream(new FileOutputStream(outfile))) {

            String fileText = new String(in.readAllBytes());
            in.close();
            String decompFile = LZW_Comp_Decomp.compress(fileText);
            out.write(decompFile.getBytes(StandardCharsets.UTF_8));
        }

        System.out.println("Base file size (bytes) = " + infile.length());
        System.out.println("Compressed file size (bytes) = " + outfile.length());
        CompressionRatio.calculateCompressionRation(input, "comp_result.lzw");
        System.out.print("Base File ");
        EntropyCalculator.calculateEntropy(input);
        System.out.print("Compressed File ");
        EntropyCalculator.calculateEntropy("comp_result.lzw");
    }

    public static void testDecompress() throws IOException {
        File infile = new File("src\\main\\java\\test_files\\comp_result.lzw");
        File outfile = new File("src\\main\\java\\test_files\\decomp_result.txt");

        try (InputStream in = new BufferedInputStream(new FileInputStream(infile));
             OutputStream out = new BufferedOutputStream(new FileOutputStream(outfile))) {

            String fileText = new String(in.readAllBytes());
            String decompFile = LZW_Comp_Decomp.decompress(fileText);
            out.write(decompFile.getBytes(StandardCharsets.UTF_8));
        }
    }

    public static void reformatFile(String outFilename) throws IOException {
        File outFile = new File("src\\main\\java\\test_files\\" + outFilename);
        File inFile = new File("src\\main\\java\\test_files\\Test_tmp.txt");

        try (InputStream in = new BufferedInputStream(new FileInputStream(inFile));
             OutputStream out = new BufferedOutputStream(new FileOutputStream(outFile))) {

            String fileText = new String(in.readAllBytes());
            in.close();

            for (char c : fileText.toCharArray()) {
                if (c > 255) {
                    fileText = fileText.replace(Character.toString(c), "");
                }
            }
            out.write(fileText.getBytes(StandardCharsets.UTF_8));
        }
    }
}
