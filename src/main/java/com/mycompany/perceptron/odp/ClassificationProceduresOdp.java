package com.mycompany.perceptron.odp;

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
 * Created by Piotrek on 23.06.2016.
 */
public class ClassificationProceduresOdp {

    public static double min = 0.5d;
    public static double max = 0.5d;

    /**
     * @param epochs        - number of epochs network will be train
     * @param hiddenNeurons - number of neurons on hidden layer
     * @param outputFile    - reports will start with this file name
     */
    public static void performClassificationODP(int epochs, int hiddenNeurons, int inputNeurons, String outputFile, String file1, String file2) {
        String errorsFilePathLearnSet = "_classification_learn_error.txt";
        String percentOfCorrectFilePathLearnSet = "_classification_percent_of_correct.txt";

        String plotFilePath = "_classificationPlot";

        ConnectedNeuralNetwork network = new ConnectedNeuralNetwork(inputNeurons, 2, hiddenNeurons, 1);
        //(int inputNeurons, int outputNeurons, int hiddenNeurons, int hiddenLayers)

        double[][] learningSet = FileUtilsOdp.loadCSVforClassification(file1, file2);

        File f = new File(errorsFilePathLearnSet);
        f.delete();
        f = new File(percentOfCorrectFilePathLearnSet);
        f.delete();


        double err = 0;
        double percent = 0;
        double correct = 0d;
        for (int i = 0; i < epochs; i++) {
            //epoka nauki
            err = 0;
            correct = 0d;
            double[][] mixedSet = Utils.shake(learningSet);
            for (double[] data : mixedSet) {
                double[] expectedResult = interpretInputODP(data[12]);
                double[] input = new double[]{data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8], data[9], data[10], data[11]};
                network.learn(input, expectedResult);
                double output = interpretOutputODP(network.output(input));

                err = err + Utils.countError(output, data[12]);
                if (output == data[12]) {
                    correct++;
                }
            }
            percent = correct / (double) mixedSet.length * 100d;
            FileUtils.addPoint(percentOfCorrectFilePathLearnSet, new double[]{i, percent});

            err = err / mixedSet.length;
            FileUtils.addPoint(errorsFilePathLearnSet, new double[]{i, err});

            if (i % 10 == 0) {
                System.out.println("epoka: " + i);
            }
        }

        //network.print();

        DecimalFormat df = new DecimalFormat("0.00000");
        String header = "Epoki: " + epochs + ", ostatni blad: " + df.format(err) +
                ",\\n step: " + ConnectedNeuron.STEP + ", " +
                "momentum: " + ConnectedNeuron.MOMENTUM + ", bias: " + ConnectedNeuron.BIAS_ENABLED +
                ",  " + hiddenNeurons + " neurony ukryte, " + inputNeurons + "podane wejscia";


        //generowanie raportu z błędami wyliczonymi z danych testowych
        saveErrorPlotCommandODP(plotFilePath,
                outputFile + "TLError.png",
                errorsFilePathLearnSet,
                "Blad sredniokwadratowy. \\n" + header, true);

        saveErrorPlotCommandODP(plotFilePath + "percent",
                outputFile + "Percent.png",
                percentOfCorrectFilePathLearnSet,
                "Procent poprawnych odpowiedzi sieci. \\n" + header, false);

        try {
            Utils.runGnuplotScript(plotFilePath);
            Utils.runGnuplotScript(plotFilePath + "percent");
            System.out.println("Wygenerowano pliki raportu: " + outputFile);
        } catch (IOException ex) {
            System.out.println("Wystapil blad przy rysowaniu wykresu " + outputFile);
        }


        f = new File(errorsFilePathLearnSet);
        f.delete();
        f = new File(plotFilePath);
        f.delete();
        f = new File(outputFile + "FunctionsPlot");
        f.delete();
        f = new File(outputFile + "FunctionsTmp.txt");
        f.delete();
        f = new File(plotFilePath + "percent");
        f.delete();
        f = new File(percentOfCorrectFilePathLearnSet);
        f.delete();

    }

    private static void saveErrorPlotCommandODP(String plotFilePath, String outputFilePath, String pointsPathL, String plotTitle, boolean lines) {
        try (PrintStream out = new PrintStream(new FileOutputStream(plotFilePath))) {
            out.println("set terminal png size 1000,600");
            out.println("set output '" + outputFilePath + "'");
            out.println("set title \"" + plotTitle + "\"");
            out.println("set key outside");
            if (lines) {
                out.println("set style data lines");
            }
            out.println("plot \"" + pointsPathL + "\" title \"Learning set\", \\");
            //out.println("\"" + pointsPathL + "\" title \"Learning set\"");
            out.println();
        } catch (FileNotFoundException ex) {
        }
    }


    private static double[] interpretInputODP(double v) {
        if (v == 1d) {
            return new double[]{0d, 1d};
        }
        if (v == 2d) {
            return new double[]{1d, 0d};
        }

        System.out.println("blad podczas interpretowania oczekiwanego wyjscia z sieci");
        return null;
    }

    private static double interpretOutputODP(double[] v) {
        if (v[0] > max && v[1] < min) {
            return 2d;
        }
        if (v[0] < min && v[1] > max) {
            return 1d;
        }

        return 0d;
    }
}
