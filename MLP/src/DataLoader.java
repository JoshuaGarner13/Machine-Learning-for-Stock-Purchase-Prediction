import java.io.*;
import java.util.*;

public class DataLoader {

    // Load only the first 5 columns as features
    public static List<double[]> loadFeatures(String filePath) throws IOException {
        List<double[]> features = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line;

        // Skip header
        br.readLine();

        while ((line = br.readLine()) != null) {
            String[] tokens = line.trim().split(",");
            // Skip invalid rows
            if (tokens.length < 6) continue;

            double[] row = new double[5];
            for (int i = 0; i < 5; i++) {
                row[i] = Double.parseDouble(tokens[i]);
            }
            features.add(row);
        }

        br.close();
        return features;
    }

    // Load only the last column as the label
    public static List<Double> loadLabels(String filePath) throws IOException {
        List<Double> labels = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line;

        // Skip header
        br.readLine();

        while ((line = br.readLine()) != null) {
            String[] tokens = line.trim().split(",");
            // Skip invalid rows
            if (tokens.length < 6) continue;

            double label = Double.parseDouble(tokens[5]);
            labels.add(label);
        }

        br.close();
        return labels;
    }
}
