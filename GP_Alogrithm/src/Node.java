import java.util.Arrays;
import java.util.List;

public class Node {
    public Node right;
    public Node left;
    public String data;
    public String type;
    public int depth;

    static public Node deepCopy(Node node) {
        if (node == null) {
            return null;
        }
        Node newNode = new Node();
        newNode.data = node.data;
        newNode.type = node.type;
        newNode.depth = node.depth;
        if (node.left != null) {
            newNode.left = deepCopy(node.left);
        }
        if (node.right != null) {
            newNode.right = deepCopy(node.right);
        }
        return newNode;
    }

    static public Node generateRandomTree(int currentDepth, int maxDepth) {
        if (currentDepth >= maxDepth) {
            return generateLeaf(currentDepth);
        }

        if (Parameters.rand.nextDouble() < Parameters.chanceToGenerateLeafEarly) {
            return generateLeaf(currentDepth);
        }
        return generateChildNode(currentDepth + 1, maxDepth);
    }

    static public Node generateChildNode(int currentDepth, int MaxDepth) {
        Node child = new Node();
        child.type = "Function";
        child.depth = currentDepth;
        List<String> functions = Arrays.asList("−", "+", "*", "/");
        child.data = functions.get(Parameters.rand.nextInt(functions.size()));

        child.right = generateRandomTree(currentDepth, MaxDepth);
        child.left = generateRandomTree(currentDepth, MaxDepth);
        return child;
    };

    static public Node generateLeaf(int currentDepth) {
        Node leaf = new Node();
        leaf.type = "Leaf";
        leaf.depth = currentDepth;
        List<String> terminals = Arrays.asList("Open", "High", "Low", "Close", "AdjClose");
        leaf.data = terminals.get(Parameters.rand.nextInt(terminals.size()));

        return leaf;
    };
}
