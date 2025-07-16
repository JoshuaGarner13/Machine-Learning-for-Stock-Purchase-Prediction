# Machine Learning for Stock Purchase Prediction

This project, developed for the COS314 Artificial Intelligence module at the University of Pretoria, explores the use of machine learning models to predict whether a financial stock should be purchased based on historical data. We implemented and evaluated three different classification algorithms in Java.

## 🤖 Models Implemented

1.  **Genetic Programming (GP):** An evolutionary algorithm designed to evolve a population of computer programs (in this case, mathematical expressions) to find an optimal classification rule.
    * **Functions:** `+`, `-`, `*`, `/`
    * **Terminals:** `Open`, `High`, `Low`, `Close`, `AdjClose` stock features.
    * **Fitness:** F1-Score.
    * **Selection:** Tournament Selection.
    * **Mutation:** Grow and Shrink mutation.

2.  **Multi-Layer Perceptron (MLP):** A feedforward artificial neural network.
    * **Architecture:** 5 input neurons, 10 hidden neurons, 2 output neurons.
    * **Activation:** Sigmoid function for hidden and output layers.
    * **Training:** Backpropagation with stochastic gradient descent.
    * **Implementation:** Built from scratch in Java.

3.  **J48 Decision Tree:** An implementation of the C4.5 algorithm available in the Weka library.
    * **Configuration:** Confidence factor of 0.25 for pruning and a minimum of 2 instances per leaf.
    * **Preprocessing:** Used Weka's `ReplaceMissingValues` and `NumericToNominal` filters. The training data was balanced using the `Resample` filter.

---

## 📊 Key Results

The models were evaluated on their accuracy and F1 scores over 10 runs with different random seeds. The **Multi-Layer Perceptron (MLP)** was the standout performer.

| Model | Mean Test Accuracy | Mean Test F1-Score |
| :--- | :--- | :--- |
| **Multi-Layer Perceptron** | **96.40%** | **0.9628** |
| Genetic Programming | 66.12% | 0.7670 |
| J48 Decision Tree | 57.38% | 0.5917* |

_*Excludes 4 runs with NaN F1 scores due to degenerate predictions._

A **Wilcoxon signed-rank test** confirmed that the MLP's superior performance over the GP was statistically significant ($p < 0.05$), highlighting its robustness and effectiveness for this classification task.

---

## 🚀 How to Run

The programs are designed to be run from a compiled JAR file without an IDE.

### J48 Decision tree
1. Compile:

    javac -cp "lib/weka.jar" src/DecisionTreeClassifier.java    

2. Run:

    java --add-opens java.base/java.lang=ALL-UNNAMED -jar DecisionTree.jar

3. Inputs (Example):

    Enter seed: 1234
    Enter train file path: Euro_USD Stock\BTC_train.csv 
    Enter test file path: Euro_USD Stock\BTC_test.csv 

### Genetic Programming Algorithm
1. Ensure that you are in the directory with the `bin, src, Euro USD Stock` directories and the `Assignment3.jar` file. 

2. Run `java -jar Assignment3.jar` in the terminal.

3. Enter the seed number. 

### Multi-Layer Perceptron
1. COMPILE:

    javac *.java

2. RUN:
    
    java -jar MLP.jar

3. Inputs (Example):

    Enter seed: 1234
    Enter train file path: Euro_USD Stock\BTC_train.csv 
    Enter test file path: Euro_USD Stock\BTC_test.csv 

---

.
├── DecisionTree/ # J48 Decision Tree model
│ ├── src/
│ │ └── DecisionTreeClassifier.java # Model implementation
│ ├── bin/
│ ├── Euro_USD Stock/ # Data files
│ │ ├── BTC_test.csv # Test dataset
│ │ └── BTC_train.csv # Training dataset
│ ├── lib/
│ │ └── weka.jar # Weka library
│ ├── META-INF/
│ │ └── MANIFEST.MF
│ ├── Decision.jar # Executable JAR file
│ └── README.md

├── GP_Algorithm/ # Genetic Programming model
│ ├── bin/
│ ├── Euro_USD Stock/
│ │ ├── BTC_test.csv
│ │ └── BTC_train.csv
│ ├── src/
│ │ ├── Data.java
│ │ ├── InputParser.java
│ │ ├── Main.java
│ │ ├── Node.java
│ │ ├── Parameters.java
│ │ ├── Score.java
│ │ ├── Test.java
│ │ ├── Train.java
│ │ └── Tree.java
│ ├── Assignment3.jar # Executable JAR file
│ └── README.md

├── MLP/ # Multi-Layer Perceptron model
│ ├── bin/
│ ├── Euro_USD Stock/
│ │ ├── BTC_test.csv
│ │ └── BTC_train.csv
│ ├── src/
│ │ ├── DataLoader.java
│ │ ├── Main.java
│ │ ├── MLPNetwork.java
│ │ └── Utils.java
│ ├── MLP.jar # Executable JAR file
│ └── README.md

└── Assignment_3_Report_FINAL.pdf # Project report

---

## 👥 Contributors

This project was a collaborative effort by:

* **Joshua Garner** ([@joshuagarner](https://github.com/joshuagarner)) - Multi-Layer Perceptron, Statistical Analysis, Report
* **Christopher Xides** ([@christopherxides](https://github.com/christopherxides)) - Genetic Programming, Report
* **Shravan Seebran** ([@shravanseebran](https://github.com/shravanseebran)) - Genetic Programming, Report
* **Kgosi Segale** ([@kgosisegale](https://github.com/kgosisegale)) - J48 Decision Tree, Report

---

## 🎓 Course Information

* **Institution:** University of Pretoria
* **Course:** COS314 - Artificial Intelligence
* **Date:** May 2025
