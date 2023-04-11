package code.z_failed_tries;

import code.IO_bit_operators.BitInputStream;
import code.IO_bit_operators.BitOutputStream;
import code.compression_algorithms.CompressorLZW;
import code.compression_algorithms.DecompressorLZW;
import code.statistics.CompressionRatio;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Tst2 {
    public static void main(String[] args) throws IOException {
        checkCompress("myTest.txt");
        //checkDecompress();

    }



    public static void checkDecompress() throws IOException {
        File infile = new File("src\\main\\java\\files\\comp-resulttt");
        File outfile = new File("src\\main\\java\\files\\decomp-resulttt");

        try (BitInputStream in = new BitInputStream(new BufferedInputStream(new FileInputStream(infile)));
             OutputStream out = new BufferedOutputStream(new FileOutputStream(outfile))) {

            DecompressorLZW decompressor = new DecompressorLZW(in, out);
            decompressor.decompress("", (int) infile.length());
        }
    }

    public static void checkCompress(String input) throws IOException {
        File infile = new File("src\\main\\java\\files\\" + input);
        File outfile = new File("src\\main\\java\\files\\comp-resulttt");

        try (InputStream in = new BufferedInputStream(new FileInputStream(infile));
             BitOutputStream out = new BitOutputStream(new BufferedOutputStream(new FileOutputStream(outfile)))) {

            CompressorLZW compressorLZW = new CompressorLZW(in, out);
            compressorLZW.compressData("fibonacci");
        }
        System.out.println(infile.length());
        System.out.println(outfile.length());
    }
}
