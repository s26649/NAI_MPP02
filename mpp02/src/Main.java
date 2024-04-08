import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length < 3) {
            System.out.println("Musisz podac wartosc a, sciezke do pliku treningowego i testowego jako argumenty.");
            return;
        }

        double learningRate = Double.parseDouble(args[0]);
        String trainFile = args[1];
        String testFile = args[2];

        List<Data> trainingData = CSVLoader.loadDataFromCsv(trainFile);
        List<Data> testData = CSVLoader.loadDataFromCsv(testFile);

        Trainer trainer = new Trainer(trainingData, testData, learningRate);
        System.out.println("Trenowanie perceptronu.");
        trainer.train();

        System.out.println("Aktualne wagi perceptronu: ");
        for (double weight : trainer.perceptron.getWeights()) {
            System.out.print(weight + " ");
        }
        System.out.println("\nAktualny bias perceptronu: " + trainer.perceptron.getBias());

        Scanner scanner = new Scanner(System.in);
        PrintWriter writer = new PrintWriter("accuracy.csv");

        while (true) {
            System.out.println("\nWybierz jedna z opcji:");
            System.out.println("1. Sklasyfikuj zestaw testowy");
            System.out.println("2. Recznie wprowadz wektory do klasyfikacji");
            System.out.println("3. Zakoncz program");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    classifyTestSet(trainer, writer);
                    break;
                case "2":
                    manuallyEnterVectors(trainer, scanner);
                    break;
                case "3":
                    System.out.println("Zakończenie programu.");
                    writer.close();
                    scanner.close();
                    return;
                default:
                    System.out.println("Nieznana opcja, spróbuj ponownie.");
                    break;
            }
            writer.flush();

            System.out.println("\nCzy chcesz trenowac perceptron?");
            System.out.println("1. Tak");
            System.out.println("2. Nie, zakoncz program");

            choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    System.out.println("Trenuje perceptron.");
                    trainer.train();
                    System.out.println("Perceptron został ponownie wytrenowany.");
                    System.out.println("Aktualne wagi perceptronu: ");
                    for (double weight : trainer.perceptron.getWeights()) {
                        System.out.print(weight + " ");
                    }
                    System.out.println("\nAktualny bias perceptronu: " + trainer.perceptron.getBias());
                    break;
                case "2":
                    System.out.println("Zakończenie programu.");
                    writer.close();
                    scanner.close();
                    return;
                default:
                    System.out.println("Nieznana opcja, spróbuj ponownie.");
                    break;
            }
        }
    }

    private static void classifyTestSet(Trainer trainer, PrintWriter writer) {
        trainer.test();

        double accuracy = trainer.accuracy;
        double accuracyClassZero = trainer.accuracyClassZero;
        double accuracyClassOne = trainer.accuracyClassOne;

        writer.println("Wyniki klasyfikacji:");
        writer.println("Etykieta rzeczywista; Przewidywana etykieta");

        for (Data data : trainer.testData) {
            int predictedLabel = trainer.perceptron.compute(data.getFeatures());
            String predictedLabelString = predictedLabel == 0 ? trainer.classZeroLabel : trainer.classOneLabel;
            writer.printf("%s; %s\n", data.getLabel(), predictedLabelString);
        }

        writer.println();
        writer.printf("Ogolna dokladnosc: %.2f%%\n", accuracy);
        writer.printf("Dokladnosc dla klasy '%s': %.2f%%\n", trainer.classZeroLabel, accuracyClassZero);
        writer.printf("Dokladnosc dla klasy '%s': %.2f%%\n", trainer.classOneLabel, accuracyClassOne);
        writer.println();
        writer.println();
        writer.flush();
    }

    private static void manuallyEnterVectors(Trainer trainer, Scanner scanner) {
        System.out.println("Wprowadz wektor cech oddzielony srednikami lub wpisz 'exit' aby wyjsc:");
        while (true) {
            String line = scanner.nextLine();
            if ("exit".equalsIgnoreCase(line)) {
                break;
            }
            String[] inputs = line.split(";");
            double[] features = new double[inputs.length];
            for (int i = 0; i < inputs.length; i++) {
                try {
                    features[i] = Double.parseDouble(inputs[i]);
                } catch (NumberFormatException e) {
                    System.out.println("Nieprawidlowa wartosc: " + inputs[i] + ". Wprowadz wektor ponownie.");
                    return;
                }
            }
            int predictedLabel = trainer.perceptron.compute(features);
            System.out.println("Wektor: " + line);
            System.out.println("Przewidywana klasa: " + (predictedLabel == 0 ? trainer.classZeroLabel : trainer.classOneLabel));
        }
    }
}
