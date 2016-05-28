package com.mycompany.perceptron;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Piotrek on 21.05.2016.
 */
public class NeuralNetworkApproximation extends ConnectedNeuralNetwork {

    public NeuralNetworkApproximation(int inputNeurons, int outputNeurons, int hiddenNeurons) {
        super(inputNeurons, outputNeurons, hiddenNeurons, 1);
    }

    public double[] output(double[] x) {

        List<Double> outputsList = new ArrayList<>();
        for (int i = 0; i < x.length; i++) {
            outputsList.add(i, layers.get(0)[i].output(new double[]{x[i]}));
        }

        List<Double> secondList = new ArrayList<>();

        for (int n = 0; n < layers.get(1).length; n++) {
            secondList.add(n, layers.get(1)[n].output(listToArray(outputsList)));
        }
        outputsList.clear();
        for (int n = 0; n < layers.get(2).length; n++) {
            outputsList.add(n, layers.get(2)[n].outputWithoutSigmoid(listToArray(outputsList)));
        }

        return listToArray(outputsList);
    }

    public void learn(double[] input, double[] expected) {
        output(input);
        learn(expected);
    }

    protected void learn(double[] expected) {
        //System.out.println("NAUKA--------------------------");
        for (int i = layers.size() - 1; i > 0; i--) {//przechodzimy warstwa po warstwie od outputu w dół
            for (int j = 0; j < layers.get(i).length; j++) {
                if (i == layers.size() - 1) {
                    layers.get(i)[j].calculateError(expected[j]);
                } else {
                    layers.get(i)[j].calculateError();
                }
            }
        }
        //najpierw wyliczamy błędy, potem zmieniamy wagi
        for (int i = layers.size() - 1; i > 0; i--) {//przechodzimy warstwa po warstwie od outputu w dół
            for (int j = 0; j < layers.get(i).length; j++) { //for (int j = layers.get(i).size() - 1; j >= 0; j--) {
                //connectNeurons();
                layers.get(i)[j].changeWages();
            }
        }
    }

    public void learn(List<Double> input, List<Double> expected) {
        output(input);
        learn(listToArray(expected));
    }

    public double[] output(List<Double> x) {
        return output(listToArray(x));
    }
}
