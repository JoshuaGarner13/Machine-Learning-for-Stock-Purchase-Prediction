import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class InputParser {
    public static final Scanner input = new Scanner(System.in);

    static public void parse() {
        readFiles();
        askUserForSeed();
    }

    static void readFiles() {
        try {
            Scanner testfile = new Scanner(new File("Euro USD Stock/BTC_test.csv"));

            testfile.nextLine();
            while (testfile.hasNextLine()) {
                String cur = testfile.nextLine();
                String[] curSplit = cur.split(",");
                Data data = new Data();
                data.Open = Double.parseDouble(curSplit[0]);
                data.High = Double.parseDouble(curSplit[1]);
                data.Low = Double.parseDouble(curSplit[2]);
                data.Close = Double.parseDouble(curSplit[3]);
                data.AdjClose = Double.parseDouble(curSplit[4]);
                data.Output = Integer.parseInt(curSplit[5]);
                Test.testData.add(data);
            }

            testfile.close();

            Scanner trainfile = new Scanner(new File("Euro USD Stock/BTC_train.csv"));

            trainfile.nextLine();
            while (trainfile.hasNextLine()) {
                String cur = trainfile.nextLine();
                String[] curSplit = cur.split(",");
                Data data = new Data();
                data.Open = Double.parseDouble(curSplit[0]);
                data.High = Double.parseDouble(curSplit[1]);
                data.Low = Double.parseDouble(curSplit[2]);
                data.Close = Double.parseDouble(curSplit[3]);
                data.AdjClose = Double.parseDouble(curSplit[4]);
                data.Output = Integer.parseInt(curSplit[5]);
                Train.trainData.add(data);
            }

            trainfile.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    static void askUserForSeed() {
        System.out.println("Seed value: ");
        String seedGiven = input.nextLine();
        Parameters.setSeed(Integer.parseInt(seedGiven));
    }

}
