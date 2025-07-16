import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;

public class Tree {

    public Node root;

    public String finaltree;

    public Double fitness;

    public Score trainScore = new Score();

    public Score testScore = new Score();

    public Double computeAccuracy(Score s) {
        double denominator = s.TP + s.TN + s.FP + s.FN;

        if (denominator == 0.0) {
            return 0.0;
        }
        double accuracy = ((s.TP + s.TN) / denominator) * 100;
        return Math.round(accuracy * 100.0) / 100.0;
    }

    public Double computeF1(Score s) {
        double precision;

        if (s.TP + s.FP == 0) {
            precision = 0.0;
        } else {
            precision = (double) s.TP / (s.TP + s.FP);
        }
        double recall;
        if (s.TP + s.FN == 0) {
            recall = 0.0;
        } else {
            recall = (double) s.TP / (s.TP + s.FN);
        }
        if (precision + recall == 0.0) {
            return 0.0;
        }
        double beforeRounded = 2 * (precision * recall) / (precision + recall);
        return (Math.round(beforeRounded * 100.0)) / 100.0;
    }

    static public String setFinalTree(Node node) {
        if (node == null) {
            return "";
        }
        String left = "";
        if (node.left != null) {
            left = setFinalTree(node.left);
        }
        String data = node.data;
        String right = "";
        if (node.right != null) {
            right = setFinalTree(node.right);
        }
        return left + data + right;
    }

    public void generateRandomTree() {
        root = Node.generateRandomTree(0, Parameters.maxDepth);
        finaltree = setFinalTree(root);
    }

    public Double parseExpression(String str, int i) {
        int operatorIndex = findIndexOfFirstOperator(str);
        if (operatorIndex == -1) {// base case 1
            switch (str) {
                case "Open":
                    return Train.trainData.get(i).Open;
                case "High":
                    return Train.trainData.get(i).High;
                case "Low":
                    return Train.trainData.get(i).Low;
                case "Close":
                    return Train.trainData.get(i).Close;
                case "AdjClose":
                    return Train.trainData.get(i).AdjClose;

                default:
                    return Double.parseDouble(str);// This shouldn't run
            }
        }

        String beforeOperator = str.substring(0, operatorIndex);
        Double beforeOperatorValue;
        switch (beforeOperator) {
            case "Open":
                beforeOperatorValue = Train.trainData.get(i).Open;
                break;
            case "High":
                beforeOperatorValue = Train.trainData.get(i).High;
                break;
            case "Low":
                beforeOperatorValue = Train.trainData.get(i).Low;
                break;
            case "Close":
                beforeOperatorValue = Train.trainData.get(i).Close;
                break;
            case "AdjClose":
                beforeOperatorValue = Train.trainData.get(i).AdjClose;
                break;

            default:
                // it will be a constant we added
                beforeOperatorValue = Double.parseDouble(beforeOperator);
                break;
        }

        str = str.substring(operatorIndex);// Remove before operator
        String operatorString = str.substring(0, 1);
        // System.out.println("operator string: " + operatorString);
        // System.out.println("str before remove operator: " + str);
        str = str.substring(1);// Remove operator
        // System.out.println("str after remove operator: " + str);

        int nextOperatorIndex = findIndexOfFirstOperator(str);
        // System.out.println("nextOperatorIndex: " + nextOperatorIndex);

        if (nextOperatorIndex == -1) {// base case 2

            Double strValue = 0.;
            switch (str) {
                case "Open":
                    strValue = Train.trainData.get(i).Open;
                    break;
                case "High":
                    strValue = Train.trainData.get(i).High;
                    break;
                case "Low":
                    strValue = Train.trainData.get(i).Low;
                    break;
                case "Close":
                    strValue = Train.trainData.get(i).Close;
                    break;
                case "AdjClose":
                    strValue = Train.trainData.get(i).AdjClose;
                    break;

                default:
                    break;
            }

            switch (operatorString) {
                case "+":
                    return beforeOperatorValue + strValue;
                case "−":
                    return beforeOperatorValue - strValue;
                case "/":
                    if (strValue == 0) {
                        return 0.;
                    }
                    return beforeOperatorValue / strValue;
                case "*":
                    return beforeOperatorValue * strValue;

                default:
                    break;
            }
        }

        String afterOperator = str.substring(0, nextOperatorIndex);
        // System.out.println("afterOperator: " + afterOperator);
        Double afterOperatorValue;
        switch (afterOperator) {
            case "Open":
                afterOperatorValue = Train.trainData.get(i).Open;
                break;
            case "High":
                afterOperatorValue = Train.trainData.get(i).High;
                break;
            case "Low":
                afterOperatorValue = Train.trainData.get(i).Low;
                break;
            case "Close":
                afterOperatorValue = Train.trainData.get(i).Close;
                break;
            case "AdjClose":
                afterOperatorValue = Train.trainData.get(i).AdjClose;
                break;

            default:
                afterOperatorValue = Double.parseDouble(afterOperator);// This shouldn't run
        }

        Double combinedValue = 0.;

        // System.out.println("operator string: " + operatorString);
        switch (operatorString) {
            case "+":
                combinedValue = beforeOperatorValue + afterOperatorValue;
                break;
            case "−":
                combinedValue = beforeOperatorValue - afterOperatorValue;
                break;
            case "/":
                if (afterOperatorValue == 0) {
                    combinedValue = 0.;
                }
                combinedValue = beforeOperatorValue / afterOperatorValue;
                break;
            case "*":
                combinedValue = beforeOperatorValue * afterOperatorValue;
                break;

            default:
                break;
        }
        String combinedString = combinedValue.toString();
        // System.out.println("combinedString: " + combinedString);
        str = str.substring(nextOperatorIndex);// Remove before operator
        // System.out.println("Input: " + combinedString + str);
        return parseExpression(combinedString + str, i);

    }

    public void applyTreeToTrainData() {
        trainScore.TP = 0;
        trainScore.FP = 0;
        trainScore.FN = 0;
        trainScore.TN = 0;
        for (int i = 0; i < Train.trainData.size(); i++) {
            String copyOfFinalTree = finaltree;
            Double result = parseExpression(copyOfFinalTree, i);
            // System.out.println("result: " + result);
            Double sigmoid = 1.0 / (1.0 + Math.exp(-result));

            int classification = 0;
            if (sigmoid >= 0.5) {
                classification = 1;
            }

            if (Train.trainData.get(i).Output == 1 && classification == 1) {
                trainScore.TP++;
            } else if (Train.trainData.get(i).Output == 0 && classification == 1) {
                trainScore.FP++;
            } else if (Train.trainData.get(i).Output == 1 && classification == 0) {
                trainScore.FN++;
            } else if (Train.trainData.get(i).Output == 0 && classification == 0) {
                trainScore.TN++;
            }
        }
        // System.out.println("trainScore.TP :" + trainScore.TP);
        // System.out.println("trainScore.FP :" + trainScore.FP);
        // System.out.println("trainScore.FN :" + trainScore.FN);
        // System.out.println("trainScore.TN :" + trainScore.TN);
        fitness = computeF1(trainScore);
    }

    public int findIndexOfFirstOperator(String s) {
        int plus = s.indexOf("+");
        int minus = s.indexOf("−");
        int divide = s.indexOf("/");
        int multiply = s.indexOf("*");

        PriorityQueue<Integer> operatorsQueue = new PriorityQueue<>();
        operatorsQueue.add(plus);
        operatorsQueue.add(minus);
        operatorsQueue.add(divide);
        operatorsQueue.add(multiply);

        Map<Integer, String> operators = new TreeMap<>();
        operators.put(plus, "+");
        operators.put(minus, "−");
        operators.put(divide, "/");
        operators.put(multiply, "*");

        String firstOperator;
        int first = operatorsQueue.poll();
        int second = operatorsQueue.poll();
        int third = operatorsQueue.poll();
        int fourth = operatorsQueue.poll();

        if (first != -1) {
            firstOperator = operators.get(first);
        } else if (second != -1) {
            firstOperator = operators.get(second);
        } else if (third != -1) {
            firstOperator = operators.get(third);
        } else if (fourth != -1) {
            firstOperator = operators.get(fourth);
        } else {
            firstOperator = "";
        }

        switch (firstOperator) {
            case "+":
                return s.indexOf("+");
            case "−":
                return s.indexOf("−");
            case "/":
                return s.indexOf("/");
            case "*":
                return s.indexOf("*");

            default:
                return -1;
        }
    }

    public void applyTreeToTestData() {
        testScore.TP = 0;
        testScore.FP = 0;
        testScore.FN = 0;
        testScore.TN = 0;
        for (int i = 0; i < Test.testData.size(); i++) {
            String copyOfFinalTree = finaltree;
            Double result = parseExpression(copyOfFinalTree, i);
            // System.out.println("result: " + result);
            Double sigmoid = 1.0 / (1.0 + Math.exp(-result));

            int classification = 0;
            if (sigmoid >= 0.5) {
                classification = 1;
            }

            if (Test.testData.get(i).Output == 1 && classification == 1) {
                testScore.TP++;
            } else if (Train.trainData.get(i).Output == 0 && classification == 1) {
                testScore.FP++;
            } else if (Train.trainData.get(i).Output == 1 && classification == 0) {
                testScore.FN++;
            } else if (Train.trainData.get(i).Output == 0 && classification == 0) {
                testScore.TN++;
            }
        }
        // System.out.println("testScore.TP :" + testScore.TP);
        // System.out.println("testScore.FP :" + testScore.FP);
        // System.out.println("testScore.FN :" + testScore.FN);
        // System.out.println("testScore.TN :" + testScore.TN);

    }

}
