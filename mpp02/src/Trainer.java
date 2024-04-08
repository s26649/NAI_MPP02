import java.util.Collections;
import java.util.List;

public class Trainer {
    protected Perceptron perceptron;
    protected final List<Data> trainData;
    protected final List<Data> testData;
    protected String classZeroLabel; // etykieta dla klasy 0
    protected String classOneLabel; // etykieta dla klasy 1
    protected double learningRate;

    protected double accuracy = 0;
    protected double accuracyClassZero = 0;
    protected double accuracyClassOne = 0;

    public Trainer(List<Data> trainData, List<Data> testData, double learningRate) {
        perceptron = new Perceptron(trainData.get(0).getFeatures().length);
        this.trainData = trainData;
        this.testData = testData;
        this.learningRate = learningRate;
        this.classZeroLabel = trainData.get(0).getLabel(); // inicjalizacja etykiety klasy 0
        this.classOneLabel = trainData.stream().filter(data -> !data.getLabel().equals(classZeroLabel)).findFirst().get().getLabel();
        Collections.shuffle(this.trainData);
        Collections.shuffle(this.testData);
    }

    public void train() {
        for (Data data : trainData) {
            double[] features = data.getFeatures();
            String actualLabel = data.getLabel();

            int predicted = perceptron.compute(features);
            int actual = actualLabel.equals(classZeroLabel) ? 0 : 1;
            perceptron.learn(learningRate, actual, predicted, features);
        }
    }

    public void test() {
        int correct = 0;
        int totalClassZero = 0;
        int correctClassZero = 0;
        int totalClassOne = 0;
        int correctClassOne = 0;

        for (Data data : testData) {
            double[] features = data.getFeatures();
            String actualLabel = data.getLabel();
            int predicted = perceptron.compute(features); // 0 dla klasy 0, 1 dla klasy 1
            boolean isCorrect = (predicted == 0 && actualLabel.equals(classZeroLabel)) || (predicted == 1 && actualLabel.equals(classOneLabel));

            if (isCorrect) {
                correct++;
                if (actualLabel.equals(classZeroLabel)) {
                    correctClassZero++;
                } else {
                    correctClassOne++;
                }
            }

            if (actualLabel.equals(classZeroLabel)) {
                totalClassZero++;
            } else if (actualLabel.equals(classOneLabel)) {
                totalClassOne++;
            }
        }

        accuracy = (double) correct / testData.size() * 100.0;
        accuracyClassZero = (double) correctClassZero / totalClassZero * 100.0;
        accuracyClassOne = (double) correctClassOne / totalClassOne * 100.0;
    }
}
