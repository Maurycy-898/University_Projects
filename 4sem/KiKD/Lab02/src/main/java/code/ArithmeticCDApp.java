package code;

import code.IO_operators.BitInputStream;
import code.IO_operators.BitOutputStream;
import code.algorithms.Compressor;
import code.algorithms.Decompressor;
import code.frequency_tools.FlatFrequencyTable;
import code.frequency_tools.FrequencyTable;
import code.frequency_tools.SimpleFrequencyTable;

import java.io.*;

public class ArithmeticCDApp {
    public static void main(String[] args) throws Exception {
        if (args.length < 3) {
            System.err.println("Invalid usage, please run with arguments\n");
            System.err.println("Usage :\n args[0] -> input file name \nargs[1] -> output file name \nargs[2] -> operation type (\"compress\" / \"decompress\")");
            System.exit(1);
        }
        File inputFile  = new File("src\\main\\java\\files\\" + args[0]);
        File outputFile = new File("src\\main\\java\\files\\" + args[1]);

        if (args[2].equals("compress")) {
            try (InputStream in = new BufferedInputStream(new FileInputStream(inputFile));
                 BitOutputStream out = new BitOutputStream(new BufferedOutputStream(new FileOutputStream(outputFile)))) {
                compress(in, out);
            }
        }
        else if (args[2].equals("decompress")) {
            try (BitInputStream in = new BitInputStream(new BufferedInputStream(new FileInputStream(inputFile)));
            OutputStream out = new BufferedOutputStream(new FileOutputStream(outputFile))) {
                decompress(in, out);
            }
        }
        else {
            System.out.println("Invalid option, give \"compress\" or \"decompress\" as third argument (args[2])");
        }
    }

    static void compress(InputStream in, BitOutputStream out) throws Exception {
        FlatFrequencyTable initFrequencies = new FlatFrequencyTable(257);
        FrequencyTable frequencies = new SimpleFrequencyTable(initFrequencies);
        Compressor compressor = new Compressor(32, out);
        while (true) {
            int symbol = in.read();
            if (symbol == -1)
                break;
            compressor.write(frequencies, symbol);
            frequencies.increment(symbol);
        }
        compressor.write(frequencies, 256);  // EOF
        compressor.finish();  // Flush remaining code bits

    }

    static void decompress(BitInputStream in, OutputStream out) throws Exception{
        FlatFrequencyTable initFrequencies = new FlatFrequencyTable(257);
        FrequencyTable frequencies = new SimpleFrequencyTable(initFrequencies);
        Decompressor decompressor = new Decompressor(32, in);
        while (true) {
            // Decode and write one byte
            int symbol = decompressor.read(frequencies);
            if (symbol == 256)  // EOF symbol
                break;
            out.write(symbol);
            frequencies.increment(symbol);
        }
    }
}
