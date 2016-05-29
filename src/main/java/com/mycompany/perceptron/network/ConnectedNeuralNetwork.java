package com.mycompany.perceptron.network;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Piotrek
 */
public class ConnectedNeuralNetwork {

    //przedział z którego losowane są wagi początkowe neuronów
    public static double MIN_FIRST_WAGE = -0.5d;
    public static double MAX_FIRST_WAGE = 0.5d;
    protected List<ConnectedNeuron[]> layers;

    public ConnectedNeuralNetwork() {

    }

    public ConnectedNeuralNetwork(int inputNeurons, int outputNeurons, int hiddenNeurons, int hiddenLayers) {

        //tworzenie warstw
        layers = new ArrayList<>();
        layers.add(0, new ConnectedNeuron[inputNeurons]);
        for (int i = 1; i < hiddenLayers + 1; i++) {
            layers.add(i, new ConnectedNeuron[hiddenNeurons]);
        }
        layers.add(hiddenLayers + 1, new ConnectedNeuron[outputNeurons]);

        //wypełnianie pierwszej warstwy (input)
        for (int i = 0; i < inputNeurons; i++) {
            layers.get(0)[i] = new ConnectedNeuron(new double[]{1.0}, i);
        }

        //wypełnianie warstw ukrytych
        for (int i = 0; i < hiddenLayers; i++) {
            for (int j = 0; j < hiddenNeurons; j++) {
                //neurony w każdej kolejnej warstwie mają tyle wejść ile jest neuronów w warstwie poprzedniej
                layers.get(i + 1)[j] = new ConnectedNeuron(randWages(layers.get(i).length), j);
            }
        }

        //wypełnianie ostatniej warstwy (output)
        for (int i = 0; i < outputNeurons; i++) {
            layers.get(hiddenLayers + 1)[i] = new ConnectedNeuron(randWages(layers.get(hiddenLayers).length), i);
        }
        connectNeurons();
    }

    public double[] lastHiddenLayerOutput() {
        List<Double> outputsList = new ArrayList<>();

        for (ConnectedNeuron n : this.layers.get(layers.size() - 2)) {
            outputsList.add(n.getLastOutput());
        }

        return listToArray(outputsList);
    }

    protected void connectNeurons() {
        for (int i = 0; i < layers.size(); i++) {
            for (int j = 0; j < layers.get(i).length; j++) {
                if (i == 0) {
                    layers.get(i)[j].setNextLayer(layers.get(i + 1));
                } else if (i == layers.size() - 1) {
                    layers.get(i)[j].setPreviousLayer(layers.get(i - 1));
                } else {
                    layers.get(i)[j].setNextLayer(layers.get(i + 1));
                    layers.get(i)[j].setPreviousLayer(layers.get(i - 1));
                }
            }
        }
    }

    protected double[] randWages(int length) {
        double[] wages = new double[length];
        Random r = new Random();
        for (int i = 0; i < length; i++) {
            wages[i] = MIN_FIRST_WAGE + (MAX_FIRST_WAGE - MIN_FIRST_WAGE) * r.nextDouble();
        }
        return wages;
    }

    public double[] output(double[] x) {

        List<Double> outputsList = new ArrayList<>();
        //double[] outputsArray = new double[x.length];
        for (int i = 0; i < x.length; i++) {
            outputsList.add(i, layers.get(0)[i].outputWithoutSigmoid(x[i]));
        }

/*
        List<Double> outputsList2 = new ArrayList<>();
        for (int i = 0; i < layers.get(1).length; i++) {
            outputsList2.add(layers.get(1)[i].output(listToArray(outputsList)));
        }
        List<Double> outputsList3 = new ArrayList<>();
        for (int i = 0; i < layers.get(2).length; i++) {
            outputsList3.add(layers.get(2)[i].output(listToArray(outputsList2)));
        }
        return listToArray(outputsList3);
*/
        List<Double> secondList = new ArrayList<>();
        for (int l = 1; l < layers.size(); l++) {
            for (int n = 0; n < layers.get(l).length; n++) {
                if (l % 2 == 1) {
                    secondList.add(n, layers.get(l)[n].output(listToArray(outputsList)));
                } else {
                    outputsList.add(n, layers.get(l)[n].output(listToArray(secondList)));
                }
            }
            if (l % 2 == 1) {
                outputsList.clear();
            } else {
                secondList.clear();
            }
        }
        if (secondList.isEmpty()) {
            return listToArray(outputsList);
        } else {
            return listToArray(secondList);
        }
    }

    //wsteczna propagacja błędu
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

    //wsteczna propagacja błędu
    public void learn(double[] input, double[] expected) {
        output(input);
        learn(expected);
    }

    public void learn(List<Double> input, List<Double> expected) {
        output(input);
        learn(listToArray(expected));
    }

    public double[] output(List<Double> x) {
        return output(listToArray(x));
    }

    protected double[] listToArray(List<Double> x) {
        double[] target = new double[x.size()];
        for (int i = 0; i < target.length; i++) {
            target[i] = x.get(i);
        }
        return target;
    }

    public void print() {
        for (int i = 0; i < this.layers.size(); i++) {
            System.out.println(" --- Layer " + i + " --- ");
            for (ConnectedNeuron n : layers.get(i)) {
                n.print();
            }
            System.out.println(" --- End of layer " + i + " --- ");
        }
    }
}
