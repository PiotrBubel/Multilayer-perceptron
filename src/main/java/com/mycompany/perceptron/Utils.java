package com.mycompany.perceptron;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Piotrek on 16.04.2016.
 */
public class Utils {

    public static double countError(double[] out, double[] exp) {
        double agregatedErrors = 0;
        double[] errors = new double[out.length];
        for (double d : errors) {
            d = 0d;
        }

        for (int i = 0; i < out.length; i++) {
            errors[i] = errors[i] + (out[i] - exp[i]);
            agregatedErrors = agregatedErrors + countError(out[i], exp[i]);
        }
        return agregatedErrors * (1 / out.length + 1);  //bo srednia z czterech liczb
    }

    public static double countError(double out, double exp) {
        return (out - exp) * (out - exp);
    }

    public static List<double[]> shake(List<double[]> list) {
        List<double[]> copy = new ArrayList<>();
        copy.addAll(list);
        ArrayList<double[]> newList = new ArrayList<>();
        while (copy.size() > 0) {
            Random r = new Random();
            int i = r.nextInt(copy.size());
            newList.add(copy.get(i));
            copy.remove(i);
        }
        return newList;
    }

    public static double[][] shake(double[][] array) {
        List<double[]> doubleList = new ArrayList<>();
        for (double[] d : array) {
            doubleList.add(d);
        }

        return Utils.shake(doubleList).toArray(array);
    }

    public static List<double[]> createNoise(double[][] tests, int size) {
        List<double[]> tests2 = new ArrayList<>();

        for (double[] d : tests) {
            tests2.add(d);
        }

        Random n = new Random();
        Random r1 = new Random();
        double noiseMin = 0.01, noiseMax = 0.2;
        for (int i = 0; i < size; i++) {

            int t = r1.nextInt(4);
            double noise0 = noiseMin + (noiseMax - noiseMin) * n.nextDouble();
            double noise1 = noiseMin + (noiseMax - noiseMin) * n.nextDouble();
            double noise2 = noiseMin + (noiseMax - noiseMin) * n.nextDouble();
            double noise3 = noiseMin + (noiseMax - noiseMin) * n.nextDouble();

            tests2.add(new double[]{
                    tests[t][0] + noise0,
                    tests[t][1] + noise1,
                    tests[t][2] + noise2,
                    tests[t][3] + noise3
            });
        }
        return tests2;
    }

    public static void print(double[] last) {
        DecimalFormat df = new DecimalFormat("0.00");
        System.out.println("");
        System.out.println("-------------------");
        for (double d : last) {
            System.out.print(df.format(d) + "  ");
        }
    }

    public static void runGnuplotScript(String scriptName) throws IOException {
        Process gnuplot;
        gnuplot = Runtime.getRuntime().exec("gnuplot " + scriptName);
        try {
            gnuplot.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
