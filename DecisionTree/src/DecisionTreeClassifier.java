import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericToNominal;
import weka.filters.unsupervised.attribute.ReplaceMissingValues;
import weka.filters.supervised.instance.Resample;
import java.io.File;
import java.util.Random;
import java.util.Scanner;

public class DecisionTreeClassifier {
    public static void main(String[] args) {
        try {
            // Prompt user for seed and file paths
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter the seed value for random number generation: ");
            long seed = scanner.nextLong();
            scanner.nextLine(); // Consume newline
            System.out.print("Enter the path to the training file (CSV): ");
            String trainFilePath = scanner.nextLine();
            System.out.print("Enter the path to the test file (CSV): ");
            String testFilePath = scanner.nextLine();
            scanner.close();

            // Load training data from CSV
            CSVLoader trainLoader = new CSVLoader();
            trainLoader.setSource(new File(trainFilePath));
            Instances trainData = trainLoader.getDataSet();
            if (trainData.classIndex() == -1) {
                trainData.setClassIndex(trainData.numAttributes() - 1); // Set last attribute as class
            }

            // Handle missing values
            ReplaceMissingValues replaceFilter = new ReplaceMissingValues();
            replaceFilter.setInputFormat(trainData);
            trainData = Filter.useFilter(trainData, replaceFilter);

            // Convert numeric class to nominal
            if (trainData.classAttribute().isNumeric()) {
                System.out.println("Converting numeric class to nominal...");
                NumericToNominal filter = new NumericToNominal();
                filter.setAttributeIndices("last");
                filter.setInputFormat(trainData);
                trainData = Filter.useFilter(trainData, filter);
            } else if (!trainData.classAttribute().isNominal()) {
                throw new Exception("Class attribute must be nominal for J48.");
            }

            // Balance class distribution
            Resample resample = new Resample();
            resample.setBiasToUniformClass(1.0);
            resample.setRandomSeed((int) seed);
            resample.setInputFormat(trainData);
            trainData = Filter.useFilter(trainData, resample);

            // Print class distribution
            System.out.println("Training class distribution after balancing: " + trainData.attributeStats(trainData.classIndex()));

            // Load test data from CSV
            CSVLoader testLoader = new CSVLoader();
            testLoader.setSource(new File(testFilePath));
            Instances testData = testLoader.getDataSet();
            if (testData.classIndex() == -1) {
                testData.setClassIndex(testData.numAttributes() - 1);
            }

            // Handle missing values in test data
            testData = Filter.useFilter(testData, replaceFilter);

            // Convert test data class to nominal
            if (testData.classAttribute().isNumeric()) {
                NumericToNominal filter = new NumericToNominal();
                filter.setAttributeIndices("last");
                filter.setInputFormat(testData);
                testData = Filter.useFilter(testData, filter);
            }

            // Print test class distribution
            System.out.println("Test class distribution: " + testData.attributeStats(testData.classIndex()));

            // Initialize J48 classifier
            J48 j48 = new J48();
            j48.setSeed((int) seed);
            String[] options = new String[] { "-C", "0.25", "-M", "2" };
            j48.setOptions(options);

            // Train the classifier
            j48.buildClassifier(trainData);

            // Evaluate on training data
            Evaluation trainEval = new Evaluation(trainData);
            trainEval.evaluateModel(j48, trainData);
            double trainAccuracy = trainEval.pctCorrect() / 100.0;
            double trainF1 = trainEval.weightedFMeasure();

            // Evaluate on test data
            Evaluation testEval = new Evaluation(trainData);
            testEval.evaluateModel(j48, testData);
            double testAccuracy = testEval.pctCorrect() / 100.0;
            double testF1 = testEval.weightedFMeasure();

            // Print results
            System.out.println("\nDecision Tree (J48) Results:");
            System.out.println("Seed Value: " + seed);
            System.out.printf("Training Accuracy: %.4f\n", trainAccuracy);
            System.out.printf("Training F1 Score: %.4f\n", trainF1);
            System.out.printf("Test Accuracy: %.4f\n", testAccuracy);
            System.out.printf("Test F1 Score: %.4f\n", testF1);

            // Print confusion matrix for debugging
            System.out.println("Training Confusion Matrix:\n" + trainEval.toMatrixString());
            System.out.println("Test Confusion Matrix:\n" + testEval.toMatrixString());

            // Print the decision tree
            System.out.println("\nDecision Tree Structure:");
            System.out.println(j48.toString());

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}