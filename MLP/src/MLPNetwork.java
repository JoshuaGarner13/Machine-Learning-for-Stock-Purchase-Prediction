import java.util.*;

public class MLPNetwork {
    private int inputSize, hiddenSize, outputSize;
    private double[][] weightsInputHidden, weightsHiddenOutput;
    private double[] hiddenBiases, outputBiases;
    private Random random;

    public MLPNetwork(int inputSize, int hiddenSize, int outputSize, long seed) {
        this.inputSize = inputSize;
        this.hiddenSize = hiddenSize;
        this.outputSize = outputSize;
        this.random = new Random(seed);
        initWeights();
    }

    private void initWeights() {
        weightsInputHidden = new double[inputSize][hiddenSize];
        weightsHiddenOutput = new double[hiddenSize][outputSize];
        hiddenBiases = new double[hiddenSize];
        outputBiases = new double[outputSize];

        for (int i = 0; i < inputSize; i++)
            for (int j = 0; j < hiddenSize; j++)
                weightsInputHidden[i][j] = random.nextGaussian() * 0.01;

        for (int i = 0; i < hiddenSize; i++)
            for (int j = 0; j < outputSize; j++)
                weightsHiddenOutput[i][j] = random.nextGaussian() * 0.01;

        for (int i = 0; i < hiddenSize; i++)
            hiddenBiases[i] = 0.0;

        for (int i = 0; i < outputSize; i++)
            outputBiases[i] = 0.0;
    }

    public void train(double[][] X, int[] y, int epochs, double learningRate) {
        for (int epoch = 0; epoch < epochs; epoch++) {
            for (int i = 0; i < X.length; i++) {
                double[] hidden = new double[hiddenSize];
                double[] output = new double[outputSize];

                // Forward
                for (int j = 0; j < hiddenSize; j++) {
                    for (int k = 0; k < inputSize; k++)
                        hidden[j] += X[i][k] * weightsInputHidden[k][j];
                    hidden[j] += hiddenBiases[j];
                    hidden[j] = Utils.sigmoid(hidden[j]);
                }

                for (int j = 0; j < outputSize; j++) {
                    for (int k = 0; k < hiddenSize; k++)
                        output[j] += hidden[k] * weightsHiddenOutput[k][j];
                    output[j] += outputBiases[j];
                    output[j] = Utils.sigmoid(output[j]);
                }

                // Backward
                double[] target = new double[outputSize];
                if (y[i] < 0 || y[i] >= outputSize) {
                    throw new IllegalArgumentException("Label y[" + i + "] = " + y[i] + " is out of bounds for output size " + outputSize);
                }
                target[y[i]] = 1.0;

                double[] outputErrors = new double[outputSize];
                for (int j = 0; j < outputSize; j++)
                    outputErrors[j] = (target[j] - output[j]) * output[j] * (1 - output[j]);

                double[] hiddenErrors = new double[hiddenSize];
                for (int j = 0; j < hiddenSize; j++) {
                    for (int k = 0; k < outputSize; k++)
                        hiddenErrors[j] += outputErrors[k] * weightsHiddenOutput[j][k];
                    hiddenErrors[j] *= hidden[j] * (1 - hidden[j]);
                }

                // Update weights
                for (int j = 0; j < hiddenSize; j++)
                    for (int k = 0; k < outputSize; k++)
                        weightsHiddenOutput[j][k] += learningRate * outputErrors[k] * hidden[j];

                for (int j = 0; j < inputSize; j++)
                    for (int k = 0; k < hiddenSize; k++)
                        weightsInputHidden[j][k] += learningRate * hiddenErrors[k] * X[i][j];

                for (int j = 0; j < outputSize; j++)
                    outputBiases[j] += learningRate * outputErrors[j];

                for (int j = 0; j < hiddenSize; j++)
                    hiddenBiases[j] += learningRate * hiddenErrors[j];
            }
        }
    }

    public int predict(double[] input) {
        double[] hidden = new double[hiddenSize];
        double[] output = new double[outputSize];

        for (int j = 0; j < hiddenSize; j++) {
            for (int k = 0; k < inputSize; k++)
                hidden[j] += input[k] * weightsInputHidden[k][j];
            hidden[j] += hiddenBiases[j];
            hidden[j] = Utils.sigmoid(hidden[j]);
        }

        for (int j = 0; j < outputSize; j++) {
            for (int k = 0; k < hiddenSize; k++)
                output[j] += hidden[k] * weightsHiddenOutput[k][j];
            output[j] += outputBiases[j];
            output[j] = Utils.sigmoid(output[j]);
        }

        return Utils.argmax(output);
    }

    public int[] predictAll(double[][] X) {
        int[] preds = new int[X.length];
        for (int i = 0; i < X.length; i++) {
            preds[i] = predict(X[i]);
        }
        return preds;
    }
}
