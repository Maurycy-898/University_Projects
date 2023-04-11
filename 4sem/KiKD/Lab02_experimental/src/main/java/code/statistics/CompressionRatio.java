package code.statistics;

import java.io.File;
import java.io.IOException;

public class CompressionRatio {

    public static void calculateCompressionRation(String baseFileName, String compressedFileName) throws IOException {
        // read given files content to byte array
        File baseFile = new File("src\\main\\java\\files\\" + baseFileName);
        File compressedFile = new File("src\\main\\java\\files\\" + compressedFileName);

        // calculate compression ratio
        double compressionRatio = (double) baseFile.length() / (double) compressedFile.length();

        System.out.println("Compression Ratio = " + compressionRatio);
    }
}
