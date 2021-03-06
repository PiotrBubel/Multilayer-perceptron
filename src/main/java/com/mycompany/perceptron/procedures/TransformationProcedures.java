package com.mycompany.perceptron.procedures;


import com.mycompany.perceptron.network.ConnectedNeuralNetwork;
import com.mycompany.perceptron.network.ConnectedNeuron;
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
public class TransformationProcedures {

    /**
     * Stworzyć sieć neuronową (MLP) o 4 wejściach i 4 wyjściach oraz jedną warstwą ukrytą. Należy
     * nauczyć sieć z wykorzystaniem poniższych danych wejściowych: transformation.txt
     *
     * zakładając, że na wyjściu sieci te same dane powinny zostać odtworzone. Wszystkie
     * eksperymenty należy powtórzyć dla 1, 2 oraz 3 neuronów w warstwie ukrytej. Wszystkie neurony
     * powinny posiadać sigmoidalną funkcję aktywacji. Należy przetestować sieci z biasem i bez
     * biasu. Format pliku z danymi wejściowymi zawiera kolejne dane wejściowe zawarte w kolejnych
     * liniach (oddzielone spacją).
     *
     * W sprawozdaniu należy zwrócić uwagę na następujące rzeczy:
     *
     * Jak zmienia się błąd średniokwadratowy  po każdej epoce nauki na zbiorze treningowym? Jak
     * wpływają parametry nauki (współczynnik nauki i momentum) na szybkość nauki? Jak wpływa
     * obecność lub brak obecności biasu na proces nauki? Jak można interpretować wyjścia z warstwy
     * ukrytej w tego rodzaju sieci?
     */
    public static void generateReports() {
        int hiddenNeurons;
        int epochs;
        String outputFile = "_transformation";
        double expectedError;
        //kazdy blok to przypadek testowy ktory wygeneruje raport
        //raport sklada sie z pliku png z wykresem bledu sredniokwadratowego i opisem parametrow
        //  oraz pliku txt ze szczegolowym raportem

        //przy dodawaniu przypadkow testowych trzeba zmieniac nazwe pliku, zeby nie nadpisalo poprzedniego reportu
        //mozna dodawac dowolnie duzo przypadkow testowych z rownymi danymi, dla kazdego wygenerowany zostanie raport

        hiddenNeurons = 2;
        outputFile = outputFile + "1";
        ConnectedNeuron.BETA = 1.0d;
        ConnectedNeuron.STEP = 0.2d;
        ConnectedNeuron.MOMENTUM = 0.0d;
        ConnectedNeuron.BIAS_ENABLED = true;
        epochs = 1000;
        TransformationProcedures.performTransformation(epochs, hiddenNeurons, outputFile);
/*
        hiddenNeurons = 2;
        outputFile = outputFile.replace('1', '2');
        ConnectedNeuron.BETA = 1.0d;
        ConnectedNeuron.STEP = 0.2d;
        ConnectedNeuron.MOMENTUM = 0.0d;
        ConnectedNeuron.BIAS_ENABLED = false;
        epochs = 100000;
        TransformationProcedures.performTransformation(epochs, hiddenNeurons, outputFile);

        hiddenNeurons = 1;
        outputFile = outputFile.replace('2', '3');
        ConnectedNeuron.BETA = 1.0d;
        ConnectedNeuron.STEP = 0.9d;
        ConnectedNeuron.MOMENTUM = 0.9d;
        ConnectedNeuron.BIAS_ENABLED = true;
        //epochs = 500;
        expectedError = 0.01;
        TransformationProcedures.performTransformation(expectedError, hiddenNeurons, outputFile);


        hiddenNeurons = 2;
        outputFile = outputFile.replace('3', '4');
        ConnectedNeuron.BETA = 1.0d;
        ConnectedNeuron.STEP = 0.9d;
        ConnectedNeuron.MOMENTUM = 0.9d;
        ConnectedNeuron.BIAS_ENABLED = false;
        epochs = 500;
        TransformationProcedures.performTransformation(epochs, hiddenNeurons, outputFile);

        hiddenNeurons = 2;
        outputFile = outputFile.replace('4', '5');
        ConnectedNeuron.BETA = 1.0d;
        ConnectedNeuron.STEP = 0.9d;
        ConnectedNeuron.MOMENTUM = 0.9d;
        ConnectedNeuron.BIAS_ENABLED = true;
        epochs = 500;
        TransformationProcedures.performTransformation(epochs, hiddenNeurons, outputFile);

        hiddenNeurons = 2;
        outputFile = outputFile.replace('5', '6');
        ConnectedNeuron.BETA = 1.0d;
        ConnectedNeuron.STEP = 0.9d;
        ConnectedNeuron.MOMENTUM = 0.9d;
        ConnectedNeuron.BIAS_ENABLED = true;
        //epochs = 500;
        expectedError = 0.01;
        TransformationProcedures.performTransformation(expectedError, hiddenNeurons, outputFile);


        hiddenNeurons = 3;
        outputFile = outputFile.replace('6', '7');
        ConnectedNeuron.BETA = 1.0d;
        ConnectedNeuron.STEP = 0.9d;
        ConnectedNeuron.MOMENTUM = 0.9d;
        ConnectedNeuron.BIAS_ENABLED = false;
        epochs = 500;
        TransformationProcedures.performTransformation(epochs, hiddenNeurons, outputFile);

        hiddenNeurons = 3;
        outputFile = outputFile.replace('7', '8');
        ConnectedNeuron.BETA = 1.0d;
        ConnectedNeuron.STEP = 0.9d;
        ConnectedNeuron.MOMENTUM = 0.9d;
        ConnectedNeuron.BIAS_ENABLED = true;
        epochs = 500;
        TransformationProcedures.performTransformation(epochs, hiddenNeurons, outputFile);

        hiddenNeurons = 3;
        outputFile = outputFile.replace('8', '9');
        ConnectedNeuron.BETA = 1.0d;
        ConnectedNeuron.STEP = 0.9d;
        ConnectedNeuron.MOMENTUM = 0.9d;
        ConnectedNeuron.BIAS_ENABLED = true;
        //epochs = 500;
        expectedError = 0.01;
        TransformationProcedures.performTransformation(expectedError, hiddenNeurons, outputFile);
*/
    }

    /**
     * @param epochs        - number of epochs network will be train
     * @param hiddenNeurons - number of neurons on hidden layer
     * @param outputFile    - reports will start with this file name
     */
    public static void performTransformation(int epochs, int hiddenNeurons, String outputFile) {
        String errorsFilePath = "_transformation_error.txt";
        String plotFilePath = "_transformationPlot";
        ConnectedNeuralNetwork network = new ConnectedNeuralNetwork(4, 4, hiddenNeurons, 1);
        double[][] learningSet = FileUtils.loadDataArrays("transformation.txt");

        File f = new File(errorsFilePath);
        f.delete();
        double err = 0;

        for (int i = 0; i < epochs; i++) {
            //epoka nauki
            err = 0;
            double[][] mixedSet = Utils.shake(learningSet);
            for (double[] data : mixedSet) {
                network.learn(data, data);
                err = err + Utils.countError(network.output(data), data);
            }
            err = err * 0.25;
            FileUtils.addPoint(errorsFilePath, new double[]{i, err});
        }

        DecimalFormat df = new DecimalFormat("0.00000");
        String header = "Epoki: " + epochs + ", ostatni blad: " + df.format(err) +
                ",\\n step: " + ConnectedNeuron.STEP + ", " +
                "momentum: " + ConnectedNeuron.MOMENTUM + ", bias: " + ConnectedNeuron.BIAS_ENABLED +
                ",  " + hiddenNeurons + " neurony ukryte.";

        TransformationProcedures.generateNetworkReport(outputFile + "_report.txt", header, network, learningSet, learningSet);
        TransformationProcedures.saveSinglePlotCommand(plotFilePath,
                outputFile + ".png",
                errorsFilePath,
                "Blad sredniokwadratowy. \\n" + header);

        try {
            Utils.runGnuplotScript(plotFilePath);
            System.out.println("Wygenerowano pliki raportu: " + outputFile);
        } catch (IOException ex) {
            System.out.println("Wystapil blad przy generowaniu raportu " + outputFile);
        }

        f = new File(errorsFilePath);
        f.delete();
        f = new File(plotFilePath);
        f.delete();

    }

    /**
     * Method learns network until it reaches expeted error or 100000 epochs. May cause performance
     * issues.
     *
     * @param expectedError - error you want to end with after learning, if it is impossible,
     *                      learning will stop after 250 000 epochs
     * @param hiddenNeurons - number of neurons on hidden layer
     * @param outputFile    - reports will start with this file name
     */
    public static void performTransformation(double expectedError, int hiddenNeurons, String outputFile) {

        int maxEpochs = 100000;
        String errorsFilePath = "_transformation_error.txt";
        String plotFilePath = "_transformationPlot";
        ConnectedNeuralNetwork network = new ConnectedNeuralNetwork(4, 4, hiddenNeurons, 1);
        double[][] learningSet = FileUtils.loadDataArrays("transformation.txt");

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
            err = err * 0.25;
            FileUtils.addPoint(errorsFilePath, new double[]{epochs, err});
            epochs++;

            if (epochs % 10000 == 0) {
                System.out.println("Siec przeszla " + epochs + " epok nauki");
            }
        }
        DecimalFormat df = new DecimalFormat("0.00000");
        String header = "Oczekiwany blad: " + expectedError + ", ostatni blad: " + df.format(err) +
                ", epoki: " + epochs + ", \\n step: " + ConnectedNeuron.STEP + ", " +
                "momentum: " + ConnectedNeuron.MOMENTUM + ", bias: " + ConnectedNeuron.BIAS_ENABLED +
                ",  " + hiddenNeurons + " neurony ukryte.";

        TransformationProcedures.generateNetworkReport(outputFile + "_report.txt", header, network, learningSet, learningSet);
        TransformationProcedures.saveSinglePlotCommand(plotFilePath,
                outputFile + ".png",
                errorsFilePath,
                "Blad sredniokwadratowy. \\n" + header);

        try {
            Utils.runGnuplotScript(plotFilePath);
            System.out.println("Wygenerowano pliki raportu: " + outputFile);
        } catch (IOException ex) {
            System.out.println("Wystapil blad przy generowaniu raportu " + outputFile);
        }

        f = new File(errorsFilePath);
        f.delete();
        f = new File(plotFilePath);
        f.delete();
    }

    public static void generateNetworkReport(String reportFilePath, String reportHeader, ConnectedNeuralNetwork network, double[][] testSet, double[][] expectedOutputs) {
        try (PrintStream out = new PrintStream(new FileOutputStream(reportFilePath))) {
            DecimalFormat df = new DecimalFormat("0.0000");
            out.println(reportHeader);
            out.println();
            for (int i = 0; i < expectedOutputs.length; i++) {

                out.print("Network inputs \t\t");
                for (double in : testSet[i]) {
                    out.print(df.format(in) + ", \t");
                }
                out.println();
                out.print("Network outputs \t");
                double[] outputs = network.output(testSet[i]);
                for (double outs : outputs) {
                    out.print(df.format(outs) + ", \t");
                }
                out.println();
                out.print("Expected outputs \t");
                for (double outs : expectedOutputs[i]) {
                    out.print(df.format(outs) + ", \t");
                }

                out.println();
                out.print("Error \t");
                double error = Utils.countError(outputs, expectedOutputs[i]);
                out.print(error);

                out.println();
                out.print("Hidden layer outputs \t");
                double[] hiddenOutputs = network.lastHiddenLayerOutput();
                for (double outs : hiddenOutputs) {
                    out.print(df.format(outs) + ", \t");
                }
                out.println();
                out.println();
            }
            out.println();
            out.println("--end of report--");

        } catch (FileNotFoundException ex) {
        }
    }

    public static void saveSinglePlotCommand(String plotFilePath, String outputFilePath, String pointsPath, String plotTitle) {
        try (PrintStream out = new PrintStream(new FileOutputStream(plotFilePath))) {
            out.println("set terminal png size 800,600");
            out.println("set output '" + outputFilePath + "'");
            out.println("set title \"" + plotTitle + "\"");
            out.println("plot \"" + pointsPath + "\" title \"Points\" as line");
            out.println();
        } catch (FileNotFoundException ex) {
        }
    }
}

