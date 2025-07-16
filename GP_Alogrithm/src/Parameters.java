import java.util.Random;

public class Parameters {
    static int numberOfIterations = 3;
    static int populationSize = 10;
    static Random rand;

    static void setSeed(int seed) {
        rand = new Random(seed);
    }

    static Double chanceToGenerateLeafEarly = 0.1; // 10%
    static int tournamentSelectionRandomK = 2;// note this is dependent on populationSize
    // Note numberOfIndividualsForCrossover+numberOfIndividualsForMutation =
    // populationSize
    static int numberOfIndividualsForCrossover = 4;// note this is dependent on populationSize
    // Ensure numberOfIndividualsForCrossover even so that their are even parents
    static int numberOfIndividualsForMutation = 6;// note this is dependent on populationSize
    static int maxDepth = 5;// 14 seems to break it, 5 returns in decent time
}
