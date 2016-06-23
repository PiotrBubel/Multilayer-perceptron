package com.mycompany.perceptron;

import com.mycompany.perceptron.network.ConnectedNeuron;
import com.mycompany.perceptron.odp.ApproximationProceduresOdp;
import com.mycompany.perceptron.odp.ClassificationProceduresOdp;
import com.mycompany.perceptron.procedures.ApproximationProcedures;
import com.mycompany.perceptron.procedures.ClassificationProcedures;
import com.mycompany.perceptron.procedures.TransformationProcedures;

public class Main {

    public static void main(String[] args) {
        String fileWhite = "winequality-white.csv";
        String fileRed = "winequality-red.csv";

        //TransformationProcedures.generateReports();
        ApproximationProcedures.generateReports();
        ClassificationProcedures.generateReports();

        ConnectedNeuron.BETA = 0.01d;
        ConnectedNeuron.STEP = 0.01d;
        ConnectedNeuron.MOMENTUM = 0.1d;
        ConnectedNeuron.BIAS_ENABLED = true;
        int epochs = 200;

        for (int i = 1; i < 25; i++) {
            ApproximationProceduresOdp.performApproximaOdpowiedz(epochs, i, "_ODPOWIEDZ_W" + i, fileWhite);
        }

        ApproximationProceduresOdp.performApproximaOdpowiedz(200, 13, "_ODPOWIEDZ_R", fileRed);

        //trzeci parametr nie moze byc wiekszy niz ilosc wejsc
        ClassificationProceduresOdp.performClassificationODP(epochs, 10, 11, "_ODPOWIEDZ_KLASYFIKACJA11", fileRed, fileWhite);
        //ClassificationProceduresOdp.performClassificationODP(epochs, 10, 9, "_ODPOWIEDZ_KLASYFIKACJA9", fileRed, fileWhite);
        //ClassificationProceduresOdp.performClassificationODP(epochs, 10, 8, "_ODPOWIEDZ_KLASYFIKACJA8", fileRed, fileWhite);
        //ClassificationProceduresOdp.performClassificationODP(epochs, 10, 7, "_ODPOWIEDZ_KLASYFIKACJA7", fileRed, fileWhite);
        //ClassificationProceduresOdp.performClassificationODP(epochs, 10, 6, "_ODPOWIEDZ_KLASYFIKACJA6", fileRed, fileWhite);
        //ClassificationProceduresOdp.performClassificationODP(epochs, 10, 5, "_ODPOWIEDZ_KLASYFIKACJA5", fileRed, fileWhite);
        //ClassificationProceduresOdp.performClassificationODP(epochs, 10, 4, "_ODPOWIEDZ_KLASYFIKACJA4", fileRed, fileWhite);
        //ClassificationProceduresOdp.performClassificationODP(epochs, 10, 3, "_ODPOWIEDZ_KLASYFIKACJA3", fileRed, fileWhite);
        //ClassificationProceduresOdp.performClassificationODP(epochs, 10, 2, "_ODPOWIEDZ_KLASYFIKACJA2", fileRed, fileWhite);
    }
}

