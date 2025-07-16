import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter seed: ");
        int seed = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter train file path: ");
        String trainPath = scanner.nextLine();

        System.out.print("Enter test file path: ");
        String testPath = scanner.nextLine();

        // Load features
        List<double[]> testFeatureList = DataLoader.loadFeatures(testPath);
        List<Double> testLabelsList = DataLoader.loadLabels(testPath);
        
        // Load labels
        List<double[]> trainFeatureList = DataLoader.loadFeatures(trainPath);
        
        List<Double> trainLabelsList = DataLoader.loadLabels(trainPath);
        // Convert to double[][]
        double[][] trainFeatures = listTo2DArray(trainFeatureList);
        double[][] testFeatures = listTo2DArray(testFeatureList);

        int[] trainLabels = trainLabelsList.stream().mapToInt(Double::intValue).toArray();
        int[] testLabels = testLabelsList.stream().mapToInt(Double::intValue).toArray();

        // Initialize and train MLP
        int inputSize = trainFeatures[0].length;
        MLPNetwork mlp = new MLPNetwork(inputSize, 10, 2, seed);
        mlp.train(trainFeatures, trainLabels, 1000, 0.01);

        // Predict on training set
        int[] trainPredictions = mlp.predictAll(trainFeatures);
        double trainAcc = Utils.accuracy(trainPredictions, trainLabels);
        double trainF1 = Utils.f1Score(trainPredictions, trainLabels);

        // Predict on testing set
        int[] testPredictions = mlp.predictAll(testFeatures);
        double testAcc = Utils.accuracy(testPredictions, testLabels);
        double testF1 = Utils.f1Score(testPredictions, testLabels);

        // Output results
        System.out.println("\n===== MLP Classification Results =====");
        System.out.println("Seed Used: " + seed);
        System.out.println();
        System.out.printf("Training Accuracy: %.2f%%\n", trainAcc);
        System.out.printf("Training F1 Score: %.4f\n", trainF1);
        System.out.println();
        System.out.printf("Testing Accuracy: %.2f%%\n", testAcc);
        System.out.printf("Testing F1 Score: %.4f\n", testF1);
        System.out.println("=======================================");
    }

    private static double[][] listTo2DArray(List<double[]> list) {
        double[][] array = new double[list.size()][];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return array;
    }
}
