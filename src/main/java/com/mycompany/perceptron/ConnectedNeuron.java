package com.mycompany.perceptron;

import java.util.Random;

/**
 * @author Piotrek
 */
public class ConnectedNeuron {

    public static double BETA = 1.0d;
    public static double STEP = 0.9d;
    public static double MOMENTUM = 0.6d;
    public static boolean BIAS_ENABLED = false;

    private final int indexInLayer;
    private double[] w;
    private double error;
    private double lastOut;
    private double lastSum;
    private double[] lastW;

    private double biasWage = 0d;
    private double lastBiasWage = 0d;

    private ConnectedNeuron[] nextLayer;
    private ConnectedNeuron[] previousLayer;


    public ConnectedNeuron(double[] wages, int index) {
        this.w = wages.clone();
        this.previousLayer = null;
        this.nextLayer = null;
        this.indexInLayer = index;
        lastW = new double[w.length];
        error = Double.NaN;
        lastSum = Double.NaN;
        lastOut = Double.NaN;
        if (BIAS_ENABLED) {
            Random r = new Random();
            biasWage = lastBiasWage =
                    ConnectedNeuralNetwork.MIN_FIRST_WAGE + (ConnectedNeuralNetwork.MAX_FIRST_WAGE - ConnectedNeuralNetwork.MIN_FIRST_WAGE) * r.nextDouble();
        }
    }

    public double getError() {
        return this.error;
    }

    public double getLastOutput() {
        return this.lastOut;
    }

    public double getWage(int index) {
        return this.w[index];
    }

    public void setNextLayer(ConnectedNeuron[] out) {
        this.nextLayer = out;
    }

    public void setPreviousLayer(ConnectedNeuron[] in) {
        this.previousLayer = in;
    }

    public double output(double[] x) {
        lastSum = 0.0d;
        for (int i = 0; i < x.length; i++) {
            lastSum = lastSum + (x[i] * w[i]);
        }
        if (BIAS_ENABLED) {
            lastSum = lastSum + biasWage * 1;
        }
        lastOut = sigmoidFunction(lastSum);
        return lastOut;
    }

    public double outputWithoutSigmoid(double x) {
        lastSum = 0.0d;
        //for (int i = 0; i < x.length; i++) {
        lastSum = (x * w[0]);
        //}
        if (BIAS_ENABLED) {
            lastSum = lastSum + biasWage * 1;
        }
        lastOut = lastSum;
        return lastOut;
    }

    //expectedOutput wykorzystywane jest tylko wtedy kiedy jest to warstwa ostatnia, w innych jest pomijane
    public void calculateError() {
        double errorSum = 0.0d;
        for (int j = 0; j < nextLayer.length; j++) {
            errorSum = errorSum + (nextLayer[j].getError() * nextLayer[j].getWage(indexInLayer));
        }
        error = sigmoidFunctionDerivative(lastSum) * errorSum;
    }

    public void calculateError(double expectedOutput) {
        //wzór dla ostatniej warstwy
        error = sigmoidFunctionDerivative(lastSum) * (expectedOutput - lastOut);
    }

    public void changeWages() {
        double[] lastWClone = cloneArray(lastW);
        lastW = cloneArray(w);
        for (int i = 0; i < w.length; i++) {
            w[i] = w[i] + STEP * error * previousLayer[i].getLastOutput() + MOMENTUM * (w[i] - lastWClone[i]);
        }
        if (BIAS_ENABLED) {
            double lastBiasTemp = biasWage;
            biasWage = biasWage + STEP * error * 1 + MOMENTUM * (biasWage - lastBiasWage);
            lastBiasWage = lastBiasTemp;
        }
    }

    private double[] cloneArray(double[] toClone) {
        double[] clone = new double[toClone.length];
        for (int i = 0; i < toClone.length; i++) {
            clone[i] = toClone[i];
        }
        return clone;
    }

    //sigmoida
    private double sigmoidFunction(double x) {
        return 1d / (1d + Math.exp(-1d * BETA * x));
    }

    //pochodna sigmoidy
    private double sigmoidFunctionDerivative(double x) {
        double sig = sigmoidFunction(x);
        return sig * (1d - sig); //wynika to z definicji pochodnej funkcji sigmoidalnej
    }
}
