import java.util.Random;

public class Perceptron {
    double[] weights; //wektory wag
    double bias; //prog

    public Perceptron(int inputLength) {
        Random random = new Random();
        weights = new double[inputLength];
        for (int i = 0; i < weights.length; i++) {
            weights[i] = -5 + 10 * random.nextDouble(); //wartosc losowa z zakresu (-5 , 5) dla wagi i-tego atrybutu
        }
        bias = random.nextDouble() + 0.1; //wartosc losowa z zakresu (0 , 1)
    }

    public int compute(double[] input) {
        double sum = -bias;
        for (int i = 0; i < input.length; i++) {
            sum += weights[i] * input[i];
        }
        return sum >= 0 ? 1 : 0;
    }

    public void learn(double learningRate, int actual, int prediction, double[] input) {
        int error = actual - prediction;
        for (int i = 0; i < weights.length; i++) {
            weights[i] += error * learningRate * input[i]; //w' = w - (d - y) * stala uczenia * wektor wejsciowy
        }
        bias -= error * learningRate; //t' = t - (d - y) * stala uczenia
    }

    public double[] getWeights() {
        return weights;
    }

    public double getBias() {
        return bias;
    }
}
