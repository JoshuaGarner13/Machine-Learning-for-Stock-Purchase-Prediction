import java.util.List;
import java.util.ArrayList;

public class Test {
    static List<Data> testData = new ArrayList<>();

    static void print() {
        Train.bestSolution.applyTreeToTestData();
        System.out.println("*******Results For Testing Data*******");
        System.out.println("Accuracy: " + Train.bestSolution.computeAccuracy(Train.bestSolution.testScore) + "%");
        System.out.println("F1: " + Train.bestSolution.computeF1(Train.bestSolution.testScore));
    }
}
