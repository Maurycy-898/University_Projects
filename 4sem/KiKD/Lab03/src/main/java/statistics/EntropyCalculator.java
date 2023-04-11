package statistics;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class EntropyCalculator {

    public static void calculateEntropy(String fileName) throws IOException {

        // read given file content to byte array
        File file = new File("src\\main\\java\\test_files\\" + fileName);
        FileInputStream in = new FileInputStream(file);
        byte[] fileContent = new byte[(int) file.length()];
        in.read(fileContent);

        // create array to keep track of frequency of bytes
        int[] frequency_array = new int[256];
        int fileContentLength = fileContent.length - 1;

        // count frequency of bytes
        for(int i = 0; i < fileContentLength; i++) {
            byte byteValue = fileContent[i];
            frequency_array[Byte.toUnsignedInt(byteValue)]++;
        }

        // calculate entropy
        double entropy = 0;
        for (int frequency : frequency_array) {
            if (frequency != 0) {
                // calculate the probability of a byte
                double probabilityOfByte = (double) frequency / (double) fileContentLength;
                entropy += probabilityOfByte * (Math.log(probabilityOfByte) / Math.log(2));
            }
        }
        entropy *= -1;

        System.out.println("Entropy = " + entropy);
    }
}
