package com.mycompany.perceptron;

import com.mycompany.perceptron.network.ConnectedNeuron;
import com.mycompany.perceptron.procedures.ApproximationProcedures;
import com.mycompany.perceptron.procedures.ClassificationProcedures;

public class Main {

    public static void main(String[] args) {
        String fileWhite = "winequality-white.csv";
        String fileRed = "winequality-red.csv";

        //TransformationProcedures.generateReports();
        //ApproximationProcedures.generateReports();
        //ClassificationProcedures.generateReports();

        ConnectedNeuron.BETA = 0.01d;
        ConnectedNeuron.STEP = 0.01d;
        ConnectedNeuron.MOMENTUM = 0.1d;
        ConnectedNeuron.BIAS_ENABLED = true;
        int epochs = 200;
        ApproximationProcedures.performApproximaOdpowiedz(epochs, 15, "_ODPOWIEDZ_W", fileWhite);
        ApproximationProcedures.performApproximaOdpowiedz(epochs, 15, "_ODPOWIEDZ_R", fileRed);

        //trzeci parametr nie moze byc wiekszy niz ilosc wejsc
        ClassificationProcedures.performClassificationODP(epochs, 15, 11, "_ODPOWIEDZ_KLASYFIKACJA", fileRed, fileWhite);

    }
}

