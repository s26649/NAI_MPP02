import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// wczytuje dane z plików CSV i konwertuje je na listę obiektów Data
public class CSVLoader {

    public static List<Data> loadDataFromCsv(String filePath) throws IOException {
        List<Data> dataList = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists() || !file.isFile()) {
            throw new IOException("Plik nie istnieje lub podana sciezka nie jest plikiem: " + filePath);
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            Integer expectedFeatureCount = null;

            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                String[] values = line.split(";");

                // oczekiwana liczba cech, ustalana na podstawie pierwszego wiersza
                if (lineNumber == 1) {
                    expectedFeatureCount = values.length - 1;
                }

                // sprawdza czy liczba cech w aktualnym wierszu jest zgodna z oczekiwaną
                if (values.length - 1 != expectedFeatureCount) {
                    throw new IOException("Nieprawidlowy format danych w wierszu " + lineNumber + ". Oczekiwano " + expectedFeatureCount + " cech, otrzymano " + (values.length - 1) + ".");
                }

                // konwertuje cechy na typ double i dodaje je do tablicy features
                double[] features = new double[values.length - 1];
                for (int i = 0; i < values.length - 1; i++) {
                    try {
                        features[i] = Double.parseDouble(values[i]);
                    } catch (NumberFormatException e) {
                        throw new IOException("Nieprawidlowa wartosc numeryczna w wierszu " + lineNumber + ", kolumna " + (i+1) + ".");
                    }
                }
                // przypisuje ostatni element wiersza jako etykieta
                String label = values[values.length - 1];

                dataList.add(new Data(features, label));
            }

            if (lineNumber == 0) {
                throw new IOException("Plik jest pusty: " + filePath);
            }
        }
        return dataList;
    }

}
