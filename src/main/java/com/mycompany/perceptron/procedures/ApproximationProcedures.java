package com.mycompany.perceptron.procedures;


import com.mycompany.perceptron.network.ConnectedNeuralNetwork;
import com.mycompany.perceptron.network.ConnectedNeuron;
import com.mycompany.perceptron.network.NeuralNetworkApproximation;
import com.mycompany.perceptron.utils.FileUtils;
import com.mycompany.perceptron.utils.Utils;

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

        ConnectedNeuron.BETA = 1.0d;
        ConnectedNeuron.STEP = 0.1d;
        ConnectedNeuron.MOMENTUM = 0.2d;
        ConnectedNeuron.BIAS_ENABLED = false;
        epochs = 3000;
        //dla powyzszych danych liczy i rysuje wykresy dla sieci z neuronami od 1 do 20
        //mozna sprawdzic co sie dzieje z innymi parametrami powyzej
        for (hiddenNeurons = 999; hiddenNeurons <= 20; hiddenNeurons++) {
            if (hiddenNeurons == 5 || hiddenNeurons == 15) {
                String outputFile1 = outputFile + hiddenNeurons + "epochs";
                ApproximationProcedures.performApproximation(epochs, hiddenNeurons, outputFile1 + "a", true);
                ApproximationProcedures.performApproximation(epochs, hiddenNeurons, outputFile1 + "b", false);
            }
        }

        //tylko jeden przypadek, mozna dodac inne
        hiddenNeurons = 15;
        String outputFile1 = outputFile + hiddenNeurons + "error";
        expectedError = 0.3d;
        ApproximationProcedures.performApproximation(expectedError, hiddenNeurons, outputFile1 + "a", true);
        //ApproximationProcedures.performApproximation(expectedError, hiddenNeurons, outputFile1 + "b", false);
    }

    /**
     * @param epochs        - number of epochs network will be train
     * @param hiddenNeurons - number of neurons on hidden layer
     * @param outputFile    - reports will start with this file name
     */
    public static void performApproximation(int epochs, int hiddenNeurons, String outputFile, boolean learningSet1) {
        String errorsFilePathLearnSet = "_approximation_learn_error.txt";
        String errorsFilePathTestSet = "_approximation_test_error.txt";

        String plotFilePath = "_approximationPlot";
        //ConnectedNeuralNetwork network = new ConnectedNeuralNetwork(1, 1, hiddenNeurons, 1);
        //(int inputNeurons, int hiddenNeurons, int hiddenLayers)
        ConnectedNeuralNetwork network = new NeuralNetworkApproximation(1, hiddenNeurons, 1);
        double[][] learningSet;
        if (learningSet1) {
            learningSet = FileUtils.loadDataArrays(ApproximationProcedures.inputFile1);
        } else {
            learningSet = FileUtils.loadDataArrays(ApproximationProcedures.inputFile2);
        }

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
            mixedSet = testingSet;//Utils.shake(testingSet);
            for (double[] data : mixedSet) {
                err = err + Utils.countError(network.output(new double[]{data[0]})[0], data[1]);
            }
            err = err / mixedSet.length;
            FileUtils.addPoint(errorsFilePathTestSet, new double[]{i, err});
        }

        //network.print();

        DecimalFormat df = new DecimalFormat("0.00000");
        String header = "Epoki: " + epochs + ", ostatni blad: " + df.format(err) +
                ",\\n step: " + ConnectedNeuron.STEP + ", " +
                "momentum: " + ConnectedNeuron.MOMENTUM + ", bias: " + ConnectedNeuron.BIAS_ENABLED +
                ",  " + hiddenNeurons + " neurony ukryte.";

        ApproximationProcedures.drawFunction(network, outputFile + "Functions", header, learningSet1);


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

        f = new File(errorsFilePathLearnSet);
        f.delete();
        f = new File(errorsFilePathTestSet);
        f.delete();
        f = new File(plotFilePath);
        f.delete();
        f = new File(outputFile + "FunctionsPlot");
        f.delete();
        f = new File(outputFile + "FunctionsTmp.txt");
        f.delete();
    }

    /**
     * Method learns network until it reaches expected error or 10000 epochs. May cause performance
     * issues.
     *
     * @param expectedError - error you want to end with after learning, if it is impossible,
     *                      learning will stop after 250 000 epochs
     * @param hiddenNeurons - number of neurons on hidden layer
     * @param outputFile    - reports will start with this file name
     */
    public static void performApproximation(double expectedError, int hiddenNeurons, String outputFile, boolean learningSet1) {
        int maxEpochs = 10000;
        int epochs = 0;
        String errorsFilePathLearnSet = "_approximation_learn_error.txt";
        String errorsFilePathTestSet = "_approximation_test_error.txt";

        String plotFilePath = "_approximationPlot";
        //ConnectedNeuralNetwork network = new ConnectedNeuralNetwork(1, 1, hiddenNeurons, 1);
        ConnectedNeuralNetwork network = new NeuralNetworkApproximation(1, hiddenNeurons, 1);
        double[][] learningSet;
        if (learningSet1) {
            learningSet = FileUtils.loadDataArrays(ApproximationProcedures.inputFile1);
        } else {
            learningSet = FileUtils.loadDataArrays(ApproximationProcedures.inputFile2);
        }

        double[][] testingSet = FileUtils.loadDataArrays(testFile);

        File f = new File(errorsFilePathLearnSet);
        f.delete();
        File f2 = new File(errorsFilePathTestSet);
        f2.delete();
        double err = Double.MAX_VALUE;

        while (err > expectedError && epochs < maxEpochs) {
            //epoka nauki
            double[][] mixedSet = Utils.shake(learningSet);
            err = 0d;
            for (double[] data : mixedSet) {
                network.learn(new double[]{data[0]}, new double[]{data[1]});
                err = err + Utils.countError(network.output(new double[]{data[0]})[0], data[1]);
            }
            err = err / mixedSet.length;
            FileUtils.addPoint(errorsFilePathLearnSet, new double[]{epochs, err});

            //liczenie bledu ze zbioru testowego
            err = 0d;
            mixedSet = testingSet;//Utils.shake(testingSet);
            for (double[] data : mixedSet) {
                err = err + Utils.countError(network.output(new double[]{data[0]})[0], data[1]);
            }
            err = err / mixedSet.length;
            FileUtils.addPoint(errorsFilePathTestSet, new double[]{epochs, err});
            epochs++;

            if (epochs % 1000 == 0) {
                System.out.println("Siec przeszla " + epochs + " epok nauki");
            }
        }

        DecimalFormat df = new DecimalFormat("0.00000");
        String header = "Oczekiwany blad: " + expectedError + ", ostatni blad: " + df.format(err * 0.25) +
                ", epoki: " + epochs + ", \\n step: " + ConnectedNeuron.STEP + ", " +
                "momentum: " + ConnectedNeuron.MOMENTUM + ", bias: " + ConnectedNeuron.BIAS_ENABLED +
                ",  " + hiddenNeurons + " neurony ukryte.";


        ApproximationProcedures.drawFunction(network, outputFile + "Functions", header, learningSet1);


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

        f = new File(errorsFilePathLearnSet);
        f.delete();
        f = new File(errorsFilePathTestSet);
        f.delete();
        f = new File(plotFilePath);
        f.delete();
        f = new File(outputFile + "FunctionsPlot");
        f.delete();
        f = new File(outputFile + "FunctionsTmp.txt");
        f.delete();
    }

    private static void drawFunction(ConnectedNeuralNetwork network, String fileName, String header, boolean learningSet1) {
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
        ApproximationProcedures.saveFunctionsPlotCommand(fileName + "Plot", fileName + ".png", fileName + "Tmp.txt", "Dane testowe i wyjscia z sieci. " + header, learningSet1);
        try {
            Utils.runGnuplotScript(fileName + "Plot");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void saveFunctionsPlotCommand(String plotFilePath, String outputFilePath, String networkPointsPath, String plotTitle, boolean learningSet1) {
        try (PrintStream out = new PrintStream(new FileOutputStream(plotFilePath))) {
            out.println("set terminal png size 800,600");
            out.println("set output '" + outputFilePath + "'");
            out.println("set title \"" + plotTitle + "\"");
            //out.println("set style data lines");
            out.println("plot \"" + networkPointsPath + "\" title \"Outputs\", \\");
            out.println("\"" + ApproximationProcedures.testFile + "\" title \"Test\", \\");
            if (learningSet1) {
                out.println("\"" + ApproximationProcedures.inputFile1 + "\" title \"LearningSet1\", \\");
            } else {
                out.println("\"" + ApproximationProcedures.inputFile2 + "\" title \"LearningSet2\"");
            }

            out.println();
            out.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    private static void saveErrorPlotCommand(String plotFilePath, String outputFilePath, String pointsPathL, String pointsPathT, String plotTitle) {
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
