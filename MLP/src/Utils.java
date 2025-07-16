public class Utils {
    public static double sigmoid(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    }

    public static int argmax(double[] output) {
        int maxIdx = 0;
        for (int i = 1; i < output.length; i++) {
            if (output[i] > output[maxIdx]) maxIdx = i;
        }
        return maxIdx;
    }

    public static double accuracy(int[] pred, int[] trueLabels) {
        int correct = 0;
        for (int i = 0; i < pred.length; i++) {
            if (pred[i] == trueLabels[i]) correct++;
        }
        return 100.0 * correct / pred.length;
    }

    public static double f1Score(int[] pred, int[] trueLabels) {
        int tp = 0, fp = 0, fn = 0;
        for (int i = 0; i < pred.length; i++) {
            if (pred[i] == 1 && trueLabels[i] == 1) tp++;
            if (pred[i] == 1 && trueLabels[i] == 0) fp++;
            if (pred[i] == 0 && trueLabels[i] == 1) fn++;
        }
        double precision = tp / (double)(tp + fp + 1e-6);
        double recall = tp / (double)(tp + fn + 1e-6);
        return 2 * (precision * recall) / (precision + recall + 1e-6);
    }
}
