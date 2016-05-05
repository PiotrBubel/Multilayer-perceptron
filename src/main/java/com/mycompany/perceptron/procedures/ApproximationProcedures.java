package com.mycompany.perceptron.procedures;

import com.mycompany.perceptron.ConnectedNeuralNetwork;
import com.mycompany.perceptron.ConnectedNeuron;
import com.mycompany.perceptron.FileUtils;
import com.mycompany.perceptron.Utils;

import java.io.File;
import java.io.IOException;
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
    public static void generateRaports() {
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

        hiddenNeurons = 1;
        outputFile = outputFile + "1";
        ConnectedNeuron.BETA = 1.0d;
        ConnectedNeuron.STEP = 0.9d;
        ConnectedNeuron.MOMENTUM = 0.9d;
        ConnectedNeuron.BIAS_ENABLED = false;
        epochs = 500;
        ApproximationProcedures.performApproximation(epochs, hiddenNeurons, outputFile);

        hiddenNeurons = 1;
        outputFile = outputFile.replace('1', '2');
        ConnectedNeuron.BETA = 1.0d;
        ConnectedNeuron.STEP = 0.9d;
        ConnectedNeuron.MOMENTUM = 0.9d;
        ConnectedNeuron.BIAS_ENABLED = true;
        epochs = 500;
        ApproximationProcedures.performApproximation(epochs, hiddenNeurons, outputFile);

        hiddenNeurons = 1;
        outputFile = outputFile.replace('2', '3');
        ConnectedNeuron.BETA = 1.0d;
        ConnectedNeuron.STEP = 0.9d;
        ConnectedNeuron.MOMENTUM = 0.9d;
        ConnectedNeuron.BIAS_ENABLED = true;
        //epochs = 500;
        expectedError = 0.01;
        ApproximationProcedures.performApproximation(expectedError, hiddenNeurons, outputFile);


        hiddenNeurons = 2;
        outputFile = outputFile.replace('3', '4');
        ConnectedNeuron.BETA = 1.0d;
        ConnectedNeuron.STEP = 0.9d;
        ConnectedNeuron.MOMENTUM = 0.9d;
        ConnectedNeuron.BIAS_ENABLED = false;
        epochs = 500;
        ApproximationProcedures.performApproximation(epochs, hiddenNeurons, outputFile);

        hiddenNeurons = 2;
        outputFile = outputFile.replace('4', '5');
        ConnectedNeuron.BETA = 1.0d;
        ConnectedNeuron.STEP = 0.9d;
        ConnectedNeuron.MOMENTUM = 0.9d;
        ConnectedNeuron.BIAS_ENABLED = true;
        epochs = 500;
        ApproximationProcedures.performApproximation(epochs, hiddenNeurons, outputFile);

        hiddenNeurons = 2;
        outputFile = outputFile.replace('5', '6');
        ConnectedNeuron.BETA = 1.0d;
        ConnectedNeuron.STEP = 0.9d;
        ConnectedNeuron.MOMENTUM = 0.9d;
        ConnectedNeuron.BIAS_ENABLED = true;
        //epochs = 500;
        expectedError = 0.01;
        ApproximationProcedures.performApproximation(expectedError, hiddenNeurons, outputFile);


        hiddenNeurons = 3;
        outputFile = outputFile.replace('6', '7');
        ConnectedNeuron.BETA = 1.0d;
        ConnectedNeuron.STEP = 0.9d;
        ConnectedNeuron.MOMENTUM = 0.9d;
        ConnectedNeuron.BIAS_ENABLED = false;
        epochs = 500;
        ApproximationProcedures.performApproximation(epochs, hiddenNeurons, outputFile);

        hiddenNeurons = 3;
        outputFile = outputFile.replace('7', '8');
        ConnectedNeuron.BETA = 1.0d;
        ConnectedNeuron.STEP = 0.9d;
        ConnectedNeuron.MOMENTUM = 0.9d;
        ConnectedNeuron.BIAS_ENABLED = true;
        epochs = 500;
        ApproximationProcedures.performApproximation(epochs, hiddenNeurons, outputFile);

        hiddenNeurons = 3;
        outputFile = outputFile.replace('8', '9');
        ConnectedNeuron.BETA = 1.0d;
        ConnectedNeuron.STEP = 0.9d;
        ConnectedNeuron.MOMENTUM = 0.9d;
        ConnectedNeuron.BIAS_ENABLED = true;
        //epochs = 500;
        expectedError = 0.01;
        ApproximationProcedures.performApproximation(expectedError, hiddenNeurons, outputFile);
    }

    /**
     * @param epochs        - number of epochs network will be train
     * @param hiddenNeurons - number of neurons on hidden layer
     * @param outputFile    - reports will start with this file name
     */
    public static void performApproximation(int epochs, int hiddenNeurons, String outputFile) {
        //TODO
        String errorsFilePath = "_approximation_error.txt";
        String plotFilePath = "_approximationPlot";
        //ConnectedNeuralNetwork network = new ConnectedNeuralNetwork(4, 4, hiddenNeurons, 1);
        //double[][] learningSet = FileUtils.loadDataArrays("transformation.txt");

        File f = new File(errorsFilePath);
        f.delete();
        double err = 0;

        /*for (int i = 0; i < epochs; i++) {
            //epoka nauki
            err = 0;
            double[][] mixedSet = Utils.shake(learningSet);
            for (double[] data : mixedSet) {
                network.learn(data, data);
                err = err + Utils.countError(network.output(data), data);
            }
            FileUtils.addPoint(errorsFilePath, new double[]{i, err * 0.25});
        }

        DecimalFormat df = new DecimalFormat("0.00000");
        String header = "Epoki: " + epochs + ", ostatni blad: " + df.format(err * 0.25) +
                ",\\n step: " + ConnectedNeuron.STEP + ", " +
                "momentum: " + ConnectedNeuron.MOMENTUM + ", bias: " + ConnectedNeuron.BIAS_ENABLED +
                ",  " + hiddenNeurons + " neurony ukryte.";

        FileUtils.generateNetworkReport(outputFile + "_report.txt", header, network, learningSet, learningSet);
        FileUtils.saveSinglePlotCommand(plotFilePath,
                outputFile + ".png",
                errorsFilePath,
                "Blad sredniokwadratowy. \\n" + header);

        try {
            Utils.runGnuplotScript(plotFilePath);
            System.out.println("Wygenerowano pliki raportu: " + outputFile);
        } catch (IOException ex) {
            System.out.println("Wystapil blad przy rysowaniu wykresu " + outputFile);
        }
*/
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
    public static void performApproximation(double expectedError, int hiddenNeurons, String outputFile) {
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
        FileUtils.saveSinglePlotCommand(plotFilePath,
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
}
