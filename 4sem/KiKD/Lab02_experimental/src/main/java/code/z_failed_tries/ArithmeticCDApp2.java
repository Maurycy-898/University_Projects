package code.z_failed_tries;

import code.IO_bit_operators.BitInputStream;
import code.IO_bit_operators.BitOutputStream;
import code.tools.FrequencyTable;
import code.statistics.CompressionRatio;
import code.statistics.EntropyCalculator;

import java.io.*;

public class ArithmeticCDApp2 {
    final static int NUMBER_OF_SYMBOLS = 257;
    final static int NUMBER_OF_BITS = 16;

    public static void main(String[] args) throws Exception {
        if (args.length < 3) {
            System.err.println("Invalid usage, please run with arguments\n");
            System.err.println("Usage :\n args[0] -> input file name \nargs[1] -> output file name \nargs[2] -> operation type (\"compress\" / \"decompress\")");
            System.exit(1);
        }
        File inputFile  = new File("src\\main\\java\\files\\" + args[0]);
        File outputFile = new File("src\\main\\java\\files\\" + args[1]);

        if (args[2].equals("compress")) {
            // pan-tadeusz.txt compressed_file compress
            try (InputStream in = new BufferedInputStream(new FileInputStream(inputFile));
                 BitOutputStream out = new BitOutputStream(new BufferedOutputStream(new FileOutputStream(outputFile)))) {
                compress(in, out);
            }

            // Printing Compression Stats
            System.out.println("Base file Entropy:");
            EntropyCalculator.calculateEntropy(args[0]);
            System.out.println("\nCompressed file Entropy:");
            EntropyCalculator.calculateEntropy(args[1]);
            System.out.println("\nCompression Ratio:");
            CompressionRatio.calculateCompressionRation(args[0], args[1]);
        }
        else if (args[2].equals("decompress")) {
            // compressed_file decompressed_file.txt decompress
            try (BitInputStream in = new BitInputStream(new BufferedInputStream(new FileInputStream(inputFile)));
            OutputStream out = new BufferedOutputStream(new FileOutputStream(outputFile))) {
                decompress(in, out);
            }
        }
        else {
            System.err.println("Invalid option, give \"compress\" or \"decompress\" as third argument (args[2])");
            System.exit(1);
        }
    }

    static void compress(InputStream in, BitOutputStream out) throws Exception {
        FrequencyTable frequencies = new FrequencyTable(NUMBER_OF_SYMBOLS);
        Compressor3 compressor = new Compressor3(NUMBER_OF_BITS, out);
        while (true) {
            int symbol = in.read();
            // EOF symbol
            if (symbol == -1) {
                break;
            }
            compressor.write(frequencies, symbol);
            frequencies.incrementSymbolFrequency(symbol);
        }
        compressor.write(frequencies, 256);  // EOF symbol
        compressor.finishCompression();
        in.close();
        out.close();
    }

    static void decompress(BitInputStream in, OutputStream out) throws Exception{
        FrequencyTable frequencies = new FrequencyTable(NUMBER_OF_SYMBOLS);
        Decompressor2 decompressor = new Decompressor2(NUMBER_OF_BITS, in);
        while (true) {
            int symbol = decompressor.read(frequencies);
            // EOF symbol
            if (symbol == 256) {
                break;
            }
            out.write(symbol);
            frequencies.incrementSymbolFrequency(symbol);
        }
        in.close();
        out.close();
    }
}
