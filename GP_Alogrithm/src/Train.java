import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Train {
    static List<Data> trainData = new ArrayList<>();
    static List<Tree> currentTreePopulation = new ArrayList<>();
    static List<Tree> treesSelectedForCrossover = new ArrayList<>();
    static List<Tree> offspringFromCrossover = new ArrayList<>();
    static List<Tree> offspringFromMutation = new ArrayList<>();
    static List<Tree> treesSelectedForMutation = new ArrayList<>();
    static Tree bestSolution;

    static void print() {
        Train.bestSolution.applyTreeToTrainData();
        System.out.println("*******Results For Training Data*******");
        System.out.println("Accuracy: " + Train.bestSolution.computeAccuracy(Train.bestSolution.trainScore) + "%");
        System.out.println("F1: " + Train.bestSolution.computeF1(Train.bestSolution.trainScore));
    }

    static void setBestSolution() {
        Tree bestTree = new Tree();
        Double bestFitness = Double.NEGATIVE_INFINITY;
        for (Tree tree : currentTreePopulation) {
            if (tree.fitness > bestFitness) {
                bestTree = tree;
                bestFitness = tree.fitness;
            }
        }
        bestSolution = bestTree;
    }

    static void run() {
        currentTreePopulation = generatePopulation(Parameters.populationSize);
        // System.out.println(currentTreePopulation.get(0).finaltree);
        // System.out.println(currentTreePopulation.get(1).finaltree);
        // System.out.println(currentTreePopulation.get(2).finaltree);
        for (int i = 0; i < Parameters.numberOfIterations; i++) {
            evaluateFitnessOfEveryTree();
            // System.out.println(currentTreePopulation.get(0).fitness);
            // System.out.println(currentTreePopulation.get(1).fitness);
            // System.out.println(currentTreePopulation.get(2).fitness);

            setBestSolution();
            // System.out.println(bestSolution.finaltree);
            // System.out.println(bestSolution.fitness);
            selectTreesForReproduction();
            // System.out.println(treesSelectedForCrossover.get(0).fitness);
            // System.out.println(treesSelectedForMutation.get(0).fitness);

            applyCrossover();
            applyMutation();
            replacePopulation();
            updateBestSolution();
        }
    }

    static void updateBestSolution() {
        Double maxFitness = Double.NEGATIVE_INFINITY;
        for (Tree tree : currentTreePopulation) {
            if (tree.fitness > maxFitness) {
                maxFitness = tree.fitness;
                bestSolution = tree;
            }
            // System.out.println("current fitness: " + tree.fitness);
        }
        // System.out.println("max fitness: " + bestSolution.fitness);
    }

    static List<Tree> generatePopulation(int size) {
        List<Tree> trees = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            trees.add(new Tree());
            trees.get(i).generateRandomTree();
        }
        return trees;
    }

    static void evaluateFitnessOfEveryTree() {
        for (int i = 0; i < Parameters.populationSize; i++) {
            currentTreePopulation.get(i).applyTreeToTrainData();
        }
    }

    static Tree findTreeWithBestFitness(List<Integer> selected) {
        Map<Integer, Double> selectedValues = new HashMap<>();
        for (int i = 0; i < selected.size(); i++) {
            selectedValues.put(selected.get(i), currentTreePopulation.get(selected.get(i)).fitness);
        }

        Double maxFitness = Double.NEGATIVE_INFINITY;
        Integer maxIndex = null;
        for (Map.Entry<Integer, Double> entry : selectedValues.entrySet()) {
            if (entry.getValue() > maxFitness) {
                maxFitness = entry.getValue();
                maxIndex = entry.getKey();
            }
        }

        return currentTreePopulation.get(maxIndex);
    }

    static List<Integer> selectRandomTrees(int k) {
        List<Integer> indexArrayOfTreesSelected = new ArrayList<>();

        for (int i = 0; i < k; i++) {
            int selectedIndex = Parameters.rand.nextInt(Parameters.populationSize);
            indexArrayOfTreesSelected.add(selectedIndex);
        }

        return indexArrayOfTreesSelected;
    }

    static void selectTreesForReproduction() {
        int c = Parameters.numberOfIndividualsForCrossover;
        for (int i = 0; i < c; i++) {
            int k = Parameters.tournamentSelectionRandomK;
            List<Integer> indexArrayOfTreesSelected = selectRandomTrees(k);
            Tree treeWithBestFitness = findTreeWithBestFitness(indexArrayOfTreesSelected);
            treesSelectedForCrossover.add(treeWithBestFitness);
        }
        int m = Parameters.numberOfIndividualsForMutation;
        for (int i = 0; i < m; i++) {
            int k = Parameters.tournamentSelectionRandomK;
            List<Integer> indexArrayOfTreesSelected = selectRandomTrees(k);
            Tree treeWithBestFitness = findTreeWithBestFitness(indexArrayOfTreesSelected);
            treesSelectedForMutation.add(treeWithBestFitness);
        }
    }

    static void applyCrossover() {
        for (int i = 0; i < Parameters.numberOfIndividualsForCrossover; i += 2) {
            Tree parent1 = treesSelectedForCrossover.get(i);
            Tree parent2 = treesSelectedForCrossover.get(i + 1);

            Tree child1 = new Tree();
            child1.root = Node.deepCopy(parent1.root);
            // for simplicity only swapping the first left branches
            // expand on this logic for more cases
            if (child1.root.left != null && parent2.root.left != null) {
                if (child1.root.left.type == parent2.root.left.type) {
                    child1.root.left = Node.deepCopy(parent2.root.left);
                } else if (child1.root.left.left != null && child1.root.left.left.type == parent2.root.left.type) {
                    child1.root.left.left = Node.deepCopy(parent2.root.left);
                } else if (parent2.root.left.left != null && child1.root.left.type == parent2.root.left.left.type) {
                    child1.root.left = Node.deepCopy(parent2.root.left.left);
                }
            }

            Tree child2 = new Tree();
            child2.root = Node.deepCopy(parent2.root);
            if (child2.root.left != null && parent1.root.left != null) {
                if (child2.root.left.type == parent1.root.left.type) {
                    child2.root.left = Node.deepCopy(parent1.root.left);
                } else if (child2.root.left.left != null && child2.root.left.left.type == parent1.root.left.type) {
                    child2.root.left.left = Node.deepCopy(parent1.root.left);
                } else if (parent1.root.left.left != null && child2.root.left.type == parent1.root.left.left.type) {
                    child2.root.left = Node.deepCopy(parent1.root.left.left);
                }

            }

            child1.finaltree = Tree.setFinalTree(child1.root);
            child2.finaltree = Tree.setFinalTree(child2.root);

            child1.applyTreeToTrainData();// fitness is set here
            child2.applyTreeToTrainData();// fitness is set here

            // System.out.println("Parent 1: " + parent1.finaltree);
            // System.out.println("Parent 2: " + parent2.finaltree);
            // System.out.println("Child 1: " + child1.finaltree);
            // System.out.println("Child 2: " + child2.finaltree);

            offspringFromCrossover.add(child1);
            offspringFromCrossover.add(child2);

        }
    }

    static void applyMutation() {
        String chosen = "Shrink";
        if (Parameters.rand.nextBoolean()) {
            chosen = "Grow";
        }
        // System.out.println(chosen);
        for (Tree tree : treesSelectedForMutation) {
            if (chosen == "Shrink") {
                // System.out.println("Tree before mutation: " + tree.finaltree);
                List<Node> list = new ArrayList<>();
                getListOfNodes(tree.root, list);
                int index = Parameters.rand.nextInt(list.size());

                Node selected = list.get(index);
                // System.out.println("before: " + selected.data);
                Shrink(selected);
                // System.out.println("after: " + selected.data);
                // Node testIsListChanging = list.get(index);
                // System.out.println("test: " + testIsListChanging.data);
                tree.finaltree = Tree.setFinalTree(tree.root);
                tree.applyTreeToTrainData();
                // System.out.println(tree.fitness);
                offspringFromMutation.add(tree);
                // System.out.println("Tree after mutation: " + tree.finaltree);
            } else {
                // System.out.println("Tree before mutation: " + tree.finaltree);
                List<Node> list = new ArrayList<>();
                getListOfTerminalNodes(tree.root, list);
                int index = Parameters.rand.nextInt(list.size());

                Node selected = list.get(index);
                // System.out.println("before: " + selected.data);
                Grow(selected);
                // System.out.println("after: " + selected.data);
                // Node testIsListChanging = list.get(index);
                // System.out.println("test: " + testIsListChanging.data);
                tree.finaltree = Tree.setFinalTree(tree.root);
                tree.applyTreeToTrainData();
                // System.out.println(tree.fitness);
                offspringFromMutation.add(tree);
                // System.out.println("Tree after mutation: " + tree.finaltree);
            }
        }
    }

    static void Shrink(Node node) {
        Node newSubtree = Node.generateLeaf(node.depth);
        node.data = newSubtree.data;
        node.left = newSubtree.left;
        node.right = newSubtree.right;
        node.type = newSubtree.type;
        node.depth = newSubtree.depth;

    }

    static void Grow(Node node) {
        // make sure that you are replacing the terminal node!
        int allowedHeight = Parameters.maxDepth - node.depth;
        Node newSubtree = Node.generateRandomTree(allowedHeight, Parameters.maxDepth);
        node.data = newSubtree.data;
        node.left = newSubtree.left;
        node.right = newSubtree.right;
        node.type = newSubtree.type;
        node.depth = newSubtree.depth;
    }

    static public void getListOfNodes(Node node, List<Node> list) {
        if (node == null) {
            return;
        }

        getListOfNodes(node.left, list);
        list.add(node);
        getListOfNodes(node.right, list);

    }

    static public void getListOfTerminalNodes(Node node, List<Node> list) {
        if (node == null) {
            return;
        }

        getListOfTerminalNodes(node.left, list);
        if (node.left == null && node.right == null) {
            list.add(node);
        }
        getListOfTerminalNodes(node.right, list);
    }

    static void replacePopulation() {
        // generational replacement
        // System.out.println("Size of population" + currentTreePopulation.size());
        // System.out.println("Size of crossover" + treesSelectedForCrossover.size());
        // System.out.println("Size of mutation" + treesSelectedForMutation.size());

        currentTreePopulation.clear();
        // System.out.println("Size of population" + currentTreePopulation.size());
        for (Tree tree : treesSelectedForCrossover) {
            currentTreePopulation.add(tree);
        }
        for (Tree tree : treesSelectedForMutation) {
            currentTreePopulation.add(tree);
        }
        // System.out.println("Size of population" + currentTreePopulation.size());

    }
}
