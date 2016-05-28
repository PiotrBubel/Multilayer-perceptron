package com.mycompany.perceptron.procedures;

import com.mycompany.perceptron.ConnectedNeuralNetwork;
import com.mycompany.perceptron.ConnectedNeuron;
import com.mycompany.perceptron.FileUtils;
import com.mycompany.perceptron.NeuralNetworkApproximation;
import com.mycompany.perceptron.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;

/**
 * Created by Piotrek on 02.05.2016.
 */
public class ApproximationProcedures {

    /**
     * Aproksymacja (maksymalna ocena 4)
     *
     * Stworzyć sieć neuronową (MLP) z jednym wejściem i jednym wyjściem. Sieć powinna mieć jedną
     * warstwę ukrytą z neuronami o sigmoidalnej funkcji aktywacji oraz warstwę wyjściową z neuronem
     * z identycznościową funkcją aktywacji (neuron liniowy). Korzystając z poniższych danych
     * treningowych (wszystkie eksperymenty należy przeprowadzić dla obu zbiorów):
     *
     * approximation_train_1.txt approximation_train_2.txt
     *
     * należy nauczyć sieci dla liczby neuronów w wartwie ukrytej od 1 do 20. Należy przetestować
     * sieci z biasem. Nauczona sieć powinna aproksymować funkcję (przybliżać jej wartości) dla
     * danych, które nie były w zbiorze treningowym. W celu sprawdzenia jakości aproksymacji należy
     * każdorazowo skorzystać z poniższego zbioru testowego:
     *
     * approximation_test.txt
     *
     * Zarówno pliki zawierające zbiory treningowe, jak i plik zawierający dane testowe mają ten sam
     * format - w każdej linii zawarte są wejście i odpowiadającej jej wyjście (oddzielone spacją).
     * Jako ocenę jakości aproksymacji należy rozważyć błąd średniokwadratowy na zbiorze testowym.
     *
     * W sprawozdaniu należy zwrócić uwagę na następujące rzeczy:
     *
     * Jak zmienia się błąd średniokwadratowy po każdej epoce nauki na zbiorze treningowym i zbiorze
     * testowym? Jaka liczba neuronów w warstwie ukrytej jest potrzebna, aby sieć dokonywała
     * poprawnej aproksymacji? Kiedy można uznać, że sieć jest nauczona? Jak wpływają parametry
     * nauki (współczynnik nauki i momentum) na szybkość nauki? Jak wyglądają wykresy funkcji
     * aproksymowanej przez sieć w porównaniu z rozkładem punktów treningowych?
     */

    public static String inputFile1 = "approximation_train_1.txt";
    public static String inputFile2 = "approximation_train_2.txt";
    public static String testFile = "approximation_test.txt";

    public static void generateReports() {
        int hiddenNeurons;
        int epochs;
        String outputFile = "_approximation";
        double expectedError;
        //kazdy blok to przypadek testowy ktory wygeneruje raport
        //raport sklada sie z pliku png z wykresem bledu sredniokwadratowego i opisem parametrow
        //  oraz pliku txt ze szczegolowym raportem

        //przy dodawaniu przypadkow testowych trzeba zmieniac nazwe pliku, zeby nie nadpisalo poprzedniego reportu
        //mozna dodawac dowolnie duzo przypadkow testowych z rownymi danymi, dla kazdego wygenerowany zostanie raport

        //TODO ze zbioru testowego wydzielic maly zbior walidacyjny, zeby sprawdzac czy nie dochodzi do
        // TODO przeuczenia (tzn blad na zbiorze do nauki sie zmniejsza, ale zwieksza sie blad na zbiorze walidacyjnym)

        hiddenNeurons = 20;
        outputFile = outputFile + "1";
        ConnectedNeuron.BETA = 0.8d;
        ConnectedNeuron.STEP = 0.2d;
        ConnectedNeuron.MOMENTUM = 0.7d;
        ConnectedNeuron.BIAS_ENABLED = true;
        epochs = 75;
        ApproximationProcedures.performApproximation(epochs, hiddenNeurons, outputFile + "a", ApproximationProcedures.inputFile1);
        //ApproximationProcedures.performApproximation(epochs, hiddenNeurons, outputFile + "b", ApproximationProcedures.inputFile2);


    }


    /**
     * @param epochs        - number of epochs network will be train
     * @param hiddenNeurons - number of neurons on hidden layer
     * @param outputFile    - reports will start with this file name
     */
    public static void performApproximation(int epochs, int hiddenNeurons, String outputFile, String inputFile) {
        String errorsFilePathLearnSet = "_approximation_learn_error.txt";
        String errorsFilePathTestSet = "_approximation_test_error.txt";

        String plotFilePath = "_approximationPlot";
        ConnectedNeuralNetwork network = new NeuralNetworkApproximation(1, 1, hiddenNeurons);//new ConnectedNeuralNetwork(1, 1, hiddenNeurons, 1);
        double[][] learningSet = FileUtils.loadDataArrays(inputFile);
        double[][] testingSet = FileUtils.loadDataArrays(testFile);

        File f = new File(errorsFilePathLearnSet);
        f.delete();
        File f2 = new File(errorsFilePathTestSet);
        f2.delete();
        double err = 0;

        for (int i = 0; i < epochs; i++) {
            //epoka nauki
            err = 0;
            double[][] mixedSet = Utils.shake(learningSet);
            for (double[] data : mixedSet) {
                network.learn(new double[]{data[0]}, new double[]{data[1]});
                err = err + Utils.countError(network.output(new double[]{data[0]})[0], data[1]);
            }
            err = err / mixedSet.length;
            FileUtils.addPoint(errorsFilePathLearnSet, new double[]{i, err});

            //liczenie bledu ze zbioru testowego
            err = 0;
            mixedSet = Utils.shake(testingSet);
            for (double[] data : mixedSet) {
                err = err + Utils.countError(network.output(new double[]{data[0]})[0], data[1]);
            }
            err = err / mixedSet.length;
            FileUtils.addPoint(errorsFilePathTestSet, new double[]{i, err});
        }

        DecimalFormat df = new DecimalFormat("0.00000");
        String header = "Epoki: " + epochs + ", ostatni blad: " + df.format(err) +
                ",\\n step: " + ConnectedNeuron.STEP + ", " +
                "momentum: " + ConnectedNeuron.MOMENTUM + ", bias: " + ConnectedNeuron.BIAS_ENABLED +
                ",  " + hiddenNeurons + " neurony ukryte.";

        ApproximationProcedures.drawFunction(network, outputFile + "Functions", header);


        //network.print();

        //generowanie raportu z błędami wyliczonymi z danych testowych
        ApproximationProcedures.saveErrorPlotCommand(plotFilePath,
                outputFile + "TLError.png",
                errorsFilePathLearnSet,
                errorsFilePathTestSet,
                "Blad sredniokwadratowy. \\n" + header);

        try {
            Utils.runGnuplotScript(plotFilePath);
            System.out.println("Wygenerowano pliki raportu: " + outputFile);
        } catch (IOException ex) {
            System.out.println("Wystapil blad przy rysowaniu wykresu " + outputFile);
        }


    }

    /**
     * Method learns network until it reaches expected error or 100000 epochs. May cause performance
     * issues.
     *
     * @param expectedError - error you want to end with after learning, if it is impossible,
     *                      learning will stop after 250 000 epochs
     * @param hiddenNeurons - number of neurons on hidden layer
     * @param outputFile    - reports will start with this file name
     */
    public static void performApproximation(double expectedError, int hiddenNeurons, String outputFile, String inputFile) {
        //TODO
        int maxEpochs = 100000;
        String errorsFilePath = "_approximation_error.txt";
        String plotFilePath = "_approximationPlot";
        //ConnectedNeuralNetwork network = new ConnectedNeuralNetwork(4, 4, hiddenNeurons, 1);
        //double[][] learningSet = FileUtils.loadDataArrays("transformation.txt");
/*
        File f = new File(errorsFilePath);
        f.delete();
        double err = Double.MAX_VALUE;
        int epochs = 0;

        while (err >= expectedError && epochs < maxEpochs) {
            //epoka nauki
            double[][] mixedSet = Utils.shake(learningSet);
            err = 0;
            for (double[] data : mixedSet) {
                network.learn(data, data);
                err = err + Utils.countError(network.output(data), data);
            }
            FileUtils.addPoint(errorsFilePath, new double[]{epochs, err * 0.25});
            epochs++;

            if (epochs % 10000 == 0) {
                System.out.println("Siec przeszla " + epochs + " epok nauki");
            }
        }
        DecimalFormat df = new DecimalFormat("0.00000");
        String header = "Oczekiwany blad: " + expectedError + ", ostatni blad: " + df.format(err * 0.25) +
                ", epoki: " + epochs + ", \\n step: " + ConnectedNeuron.STEP + ", " +
                "momentum: " + ConnectedNeuron.MOMENTUM + ", bias: " + ConnectedNeuron.BIAS_ENABLED +
                ",  " + hiddenNeurons + " neurony ukryte.";

        FileUtils.generateNetworkReport(outputFile + "_report.txt", header, network, learningSet, learningSet);
        FileUtils.saveErrorPlotCommand(plotFilePath,
                outputFile + ".png",
                errorsFilePath,
                "Blad sredniokwadratowy. \\n" + header);

        try {
            Utils.runGnuplotScript(plotFilePath);
            System.out.println("Wygenerowano pliki raportu: " + outputFile);
        } catch (IOException ex) {
            System.out.println("Wystapil blad przy rysowaniu wykresu " + outputFile);
        }
    }*/
    }

    public static void drawFunction(ConnectedNeuralNetwork network, String fileName, String header) {
        double[][] data = new double[160][2];
        int i = 0;
        double x = -4.0;
        while (i < 160) {
            data[i][0] = x;
            data[i][1] = network.output(new double[]{x})[0];
            i++;
            x = x + 0.05d;
        }
        FileUtils.saveArray(fileName + "Tmp.txt", data);
        ApproximationProcedures.saveFunctionsPlotCommand(fileName + "Plot", fileName + ".png", fileName + "Tmp.txt", "Dane testowe i wyjscia z sieci. " + header);
        try {
            Utils.runGnuplotScript(fileName + "Plot");
        } catch (Exception e) {

        }
    }

    public static void saveFunctionsPlotCommand(String plotFilePath, String outputFilePath, String networkPointsPath, String plotTitle) {
        try (PrintStream out = new PrintStream(new FileOutputStream(plotFilePath))) {
            out.println("set terminal png size 800,600");
            out.println("set output '" + outputFilePath + "'");
            out.println("set title \"" + plotTitle + "\"");
            //out.println("set style data lines");
            out.println("plot \"" + networkPointsPath + "\" title \"Outputs\", \\");
            out.print("\"" + ApproximationProcedures.testFile + "\" title \"Test\"");
            out.println();
            out.close();
        } catch (FileNotFoundException ex) {
        }

    }

    public static void saveErrorPlotCommand(String plotFilePath, String outputFilePath, String pointsPathL, String pointsPathT, String plotTitle) {
        try (PrintStream out = new PrintStream(new FileOutputStream(plotFilePath))) {
            out.println("set terminal png size 800,600");
            //out.println("set xrange [-0.5:12.5]");
            //out.println("set yrange [-0.5:12.5]");
            out.println("set output '" + outputFilePath + "'");
            out.println("set title \"" + plotTitle + "\"");
            out.println("set style data lines");
            out.println("plot \"" + pointsPathT + "\" title \"Testing set\", \\");
            out.println("\"" + pointsPathL + "\" title \"Learning set\"");
            out.println();
        } catch (FileNotFoundException ex) {
        }
    }
}
