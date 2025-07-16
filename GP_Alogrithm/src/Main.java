public class Main {
    public static void main(String[] args) {
        InputParser.parse();

        System.out.println();

        Train.run();

        Train.print();

        System.out.println();

        Test.print();

        InputParser.input.close();
    }
}
