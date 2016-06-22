package com.mycompany.perceptron.network;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Piotrek on 21.05.2016.
 */
public class NeuralNetworkApproximation extends ConnectedNeuralNetwork {

    public NeuralNetworkApproximation(int inputNeurons, int hiddenNeurons, int hiddenLayers) { //(int inputNeurons, int outputNeurons, int hiddenNeurons, int hiddenLayers)
        super(inputNeurons, 1, hiddenNeurons, hiddenLayers);
    }


    public double[] output(double[] x) {
        List<Double> outputsList = new ArrayList<>();
        //double[] outputsArray = new double[x.length];
        for (int i = 0; i < x.length; i++) {
            outputsList.add(i, layers.get(0)[i].outputWithoutSigmoid(x[i]));
        }

        List<Double> secondList = new ArrayList<>();
        for (int n = 0; n < layers.get(1).length; n++) {
            secondList.add(n, layers.get(1)[n].output(listToArray(outputsList)));
        }

        outputsList.clear();
        for (int n = 0; n < layers.get(2).length; n++) {
            outputsList.add(n, layers.get(2)[n].outputWithoutSigmoid(listToArray(secondList)));
        }
        return listToArray(outputsList);

    }


    protected void learn(double[] expected) {
        //System.out.println("NAUKA--------------------------");
        for (int j = 0; j < layers.get(2).length; j++) {
            layers.get(2)[j].calculateNonSigmoidError(expected[j]);
        }

        for (int j = 0; j < layers.get(1).length; j++) {
            layers.get(1)[j].calculateError();
        }


        //najpierw wyliczamy błędy, potem zmieniamy wagi
        for (int i = layers.size() - 1; i > 0; i--) {//przechodzimy warstwa po warstwie od outputu w dół
            for (int j = 0; j < layers.get(i).length; j++) { //for (int j = layers.get(i).size() - 1; j >= 0; j--) {
                //connectNeurons();
                layers.get(i)[j].changeWages();
            }
        }
    }
}