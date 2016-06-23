package com.mycompany.perceptron.odp;

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
 * Created by Piotrek on 23.06.2016.
 */
public class ApproximationProceduresOdp {

    /**
     * @param epochs        - number of epochs network will be train
     * @param hiddenNeurons - number of neurons on hidden layer
     * @param outputFile    - reports will start with this file name
     */
    public static void performApproximaOdpowiedz(int epochs, int hiddenNeurons, String outputFile, String inputFile) {
        String errorsFilePathLearnSet = "_approximation_learn_error_ODP.txt";

        String plotFilePath = "_approximationPlot_ODP";
        ConnectedNeuralNetwork network = new NeuralNetworkApproximation(11, hiddenNeurons, 1);//(int inputNeurons, int outputNeurons, int hiddenNeurons, int hiddenLayers)

        double[][] learningSet = FileUtilsOdp.loadCSVArrays(inputFile);

        //learningSet = Utils.normalizeData(learningSet);
        //TODO mamy normalizowac dane?

        //double[][] testingSet = FileUtilsOdp.loadDataArrays(testFile);
        //TODO mamy wydzielic zbior testowy ze zbioru uczacego?

        File f = new File(errorsFilePathLearnSet);
        f.delete();

        double err = 0;

        for (int i = 0; i < epochs; i++) {
            //epoka nauki
            err = 0;
            double[][] mixedSet = Utils.shake(learningSet);
            for (double[] data : mixedSet) {
                double[] input = new double[]{data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8], data[9], data[10]};
                network.learn(input, new double[]{data[11]});
                err = err + Utils.countError(network.output(input)[0], data[11]);
            }
            err = err / mixedSet.length;
            FileUtils.addPoint(errorsFilePathLearnSet, new double[]{i, err});
            if (i % 10 == 0) {
                System.out.println("epoka: " + i);
            }
        }

        //network.print();
        DecimalFormat df = new DecimalFormat("0.00000");

        generateHistogram(network, learningSet,
                "Histogram dla sieci z " + hiddenNeurons + " ukrytymi neuronami. Sredni blad: " + df.format(err) + " ",
                outputFile);

        String wineColor = "Nieznany";
        if (inputFile.contains("white")) {
            wineColor = "Biale wino";
        }
        if (inputFile.contains("red")) {
            wineColor = "Czerwone wino";
        }

        String header = "Epoki: " + epochs + ", ostatni blad: " + df.format(err) +
                ",\\n step: " + ConnectedNeuron.STEP + ", " +
                "momentum: " + ConnectedNeuron.MOMENTUM + ", bias: " + ConnectedNeuron.BIAS_ENABLED +
                ",  " + hiddenNeurons + " neurony ukryte. ";

        //generowanie raportu z błędami wyliczonymi z danych testowych
        saveErrorPlotCommand(plotFilePath,
                outputFile + "TLError.png",
                errorsFilePathLearnSet,
                "brak",
                "Blad sredniokwadratowy. " + wineColor + "\\n" + header);

        try {
            Utils.runGnuplotScript(plotFilePath);
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
    }

    private static void generateHistogram(ConnectedNeuralNetwork network, double[][] learningSet, String header, String outputFile) {
        String dataFile = "_his.dat";

        File f3 = new File(dataFile);
        f3.delete();

        int[] ile = new int[9];

        for (int i = 0; i < learningSet.length; i++) {
            double[] data = learningSet[i];
            double[] input = new double[]{data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8], data[9], data[10]};
            double err1 = Utils.countError(network.output(input)[0], data[11]);

            if (err1 < 0.1) {
                ile[0]++;
            } else if (err1 < 0.2) {
                ile[1]++;
            } else if (err1 < 0.3) {
                ile[2]++;
            } else if (err1 < 0.4) {
                ile[3]++;
            } else if (err1 < 0.5) {
                ile[4]++;
            } else if (err1 < 0.6) {
                ile[5]++;
            } else if (err1 < 0.7) {
                ile[6]++;
            } else if (err1 < 0.8) {
                ile[7]++;
            } else if (err1 < 0.9) {
                ile[8]++;
            }
        }

        double x = 0.1d;
        for (int i = 0; i < ile.length; i++) {
            FileUtils.addPoint(dataFile, new double[]{x, ile[i]});
            x = x + 0.1d;
        }

        savePlotHistogram("_plot", outputFile + "_histogram.png", dataFile, header);
        //"Histogram dla sieci z " + hiddenNeurons + " ukrytymi neuronami. Sredni blad: " + df.format(err) + " "        );

        try {
            Utils.runGnuplotScript("_plot");
            System.out.println("Wygenerowano plik histogramu: " + outputFile);
        } catch (IOException ex) {
            System.out.println("Wystapil blad przy rysowaniu wykresu " + outputFile);
        }

        f3 = new File(dataFile);
        f3.delete();
    }

    private static void savePlotHistogram(String plotFilePath, String outputFilePath, String networkPointsPath, String plotTitle) {
        try (PrintStream out = new PrintStream(new FileOutputStream(plotFilePath))) {
            out.println("set terminal png size 800,600");
            out.println("set output '" + outputFilePath + "'");
            out.println("set title \"" + plotTitle + "\"");
            out.println("set yrange[0:2000]");
            out.println("set xrange[0:10]");
            out.println("set style data histogram");
            out.println("set ytics rotate out");
            out.println("set style fill solid border");
            out.println("plot \"" + networkPointsPath + "\" using 2 title 'Blad'");
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
