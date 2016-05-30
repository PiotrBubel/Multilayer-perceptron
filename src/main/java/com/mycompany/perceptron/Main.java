package com.mycompany.perceptron;

import com.mycompany.perceptron.network.ConnectedNeuralNetwork;
import com.mycompany.perceptron.network.ConnectedNeuron;
import com.mycompany.perceptron.procedures.ApproximationProcedures;
import com.mycompany.perceptron.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        //epoka uczenia
        //nie pokazujemy danych testowych sieci wtedy gdy sie uczy
        //w kazdej epoce losowe podzbiory zbioru uczącego, ale nigdy zboiru testowego
        //sluzy to sprawdzeniu jak zachowuje sie siec przy danych na ktorych sie nigdy nie uczyla
        //w tym zadaniu pokazujemy wszystkie dane
        //zadanie to nie interpolacja ani aproksymacja, bo siec nigdy nie zwroci dokladnie 1 albo 0 wiec wykres nie bedzie przechodzil w punktach
        //wykres bledu - suma bledow kwadratowych miedzy punktem a tym co zwraca siec
        //wykres: pionowa oś - wartość błędu, pozioma oś - kolejne epoki nauki


        //TransformationProcedures.generateReports();
        ApproximationProcedures.generateReports();
    }

    public static void perceptron() {
        ConnectedNeuralNetwork network = new ConnectedNeuralNetwork(4, 4, 2, 1);
        //(int inputNeurons, int outputNeurons, int hiddenNeurons, int hiddenLayers)
        ConnectedNeuron.BETA = 1.0d;
        ConnectedNeuron.STEP = 0.9d;
        ConnectedNeuron.MOMENTUM = 0.9d;
        ConnectedNeuron.BIAS_ENABLED = true;

        double[] learn0 = new double[]{1d, 0d, 0d, 0d};//, 0d};
        double[] learn1 = new double[]{0d, 1d, 0d, 0d};//, 0d};
        double[] learn2 = new double[]{0d, 0d, 1d, 0d};//, 0d};
        double[] learn3 = new double[]{0d, 0d, 0d, 1d};//, 0d};
        //double[] learn4 = new double[]{0d, 0d, 0d, 0d, 1d};

        double[][] learnSet = {learn0, learn1, learn2, learn3};//, learn4};
        ArrayList<double[]> learningSet = new ArrayList<>();
        for (double[] d : learnSet) {
            learningSet.add(d);
        }

        double[] test0 = new double[]{1d, 0d, 0d, 1d};
        double[] test1 = new double[]{1d, 0d, 1d, 0d};
        double[] test2 = new double[]{1d, 1d, 0d, 0d};
        double[] test3 = new double[]{0d, 0d, 1d, 1d};
        double[] test4 = new double[]{0d, 1d, 1d, 0d};
        double[] test5 = new double[]{0d, 0d, 0d, 0d};
        //printNeuronWages(network.getLayers().get(1).get(0));
        //double err = 0;
        for (int i = 0; i < 25000; i++) {
            //epoka nauki
            //err = 0;
            List<double[]> mixedSet = Utils.shake(learningSet);
            for (double[] data : mixedSet) {
                //network.learn(learningSet.get(0), learningSet.get(0));
                network.learn(data, data);
                //printNeuronWages(network.getLayers().get(1).get(0));
                //System.out.println(countError(network.output(data), data));

                //err = err + countError(network.output(data), data);
            }
            //FileUtils.addPoint("punkty.txt", new double[]{i, err * 0.25});
            //System.out.println("Error dla wszystkich wzorcow: " + err * 0.25);
        }
        //printNetworkStatus(network);
        //printWages(network);

        /*
        List<double[]> outputs = new ArrayList<>();
        outputs.add(network.output(learnSet[0]));
        outputs.add(network.output(learnSet[1]));
        outputs.add(network.output(learnSet[2]));
        outputs.add(network.output(learnSet[3]));

        double[][] outputsArray = {
                network.output(learnSet[0]),
                network.output(learnSet[1]),
                network.output(learnSet[2]),
                network.output(learnSet[3])
        };
    */
        //FileUtils.saveOutput("learningSet.txt", learningSet);
        //FileUtils.saveOutput("outputs.txt", outputs);
        //FileUtils.saveOutput("outputsArray.txt", outputsArray);
        //double[][] x1 = FileUtils.loadDataArrays("outputsArray.txt");
        //List<List<Double>> x2 = FileUtils.loadDataLists("outputsArray.txt");
        //FileUtils.saveOutput("outputsArray2.txt", x1);
        //FileUtils.saveOutput("outputsArray3.txt", x2);


        if (true) {
            Utils.print(network.output(learnSet[0]));
            Utils.print(network.output(learnSet[1]));
            Utils.print(network.output(learnSet[2]));
            Utils.print(network.output(learnSet[3]));
            //print(network.output(learnSet[4]));

            if (true) {
                System.out.println();
                System.out.println("---T-E-S-T---");
                Utils.print(network.output(test0));
                Utils.print(network.output(test1));
                Utils.print(network.output(test2));
                Utils.print(network.output(test3));
                Utils.print(network.output(test4));
                Utils.print(network.output(test5));
            }
        }
    }

}

//PROJEKT
//projekt 1 a: autoasocjacja - podajemy wejscie i kazemy sieci odtworzyc to wejscie
//back propagation
//czesc b i sprawozdanie jest praktycznie oceniane
//trzy przypadki uzycia
//b1 - regresja (aproksymacja) - zbior uczacy ktory polega na podaniu wartosci funkcji w roznych punktach 0 siec powinna nauczyc sie tej funkcji jak najlepiej
//b2 - klasyfikacja (np. binarna) - rozni sie tym od regresji, ze oczekujemy 'etykiety', przyporzoakowujemy jedna z dwoch etykiet (0/1)
//gdy simgoida odpowie <0.5 to zwracamy 0, jeśli >0.5 to zwracamy 1
//mozna wprowadzic odpowiedz 'nie wiem', dzielimy wtedy nie na wieksze od 0.5 i mniejsze, tylko np. <0.3 to 0, >0.7 to 1, a pomiedzy 'nie wiem'
//na podstawie 1 mozna wykonac 2 na piątkę
//autoasocjacja - oczekujemy na wyjsciu tego samego co na wyjsciu
//tyle samo wejsc i wyjsc
//warstwa ukryta ma dwa neurony, kazdy z nich ma 4 wejscia (z i bez wyrazu wolnego) i 4 wyjscia, od kazdego wejscie i do kazdego wyjscia
//4 liczby -> 2 liczby -> 4 liczby    czyli de facto wykonujemy kompresje (stratną dla dowolnych wartosci, ale tutaj bezstratna, bo mamy tylko 0 i 1)
// 4 wzorce
//wejście   nr wzorca
// 1 0 0 0  0  00       -> to samo co na wyjściu
// 0 1 0 0  1  01
// 0 0 1 0  2  10
// 0 0 0 1  3  11
//najpierw czesc 1, siec ze wsteczna propagacja bledu, ma działać, bez sprawozdania - szybki deadline - trzy tygodnie od dzisiaj, tj.
//27 kwiecień - termin oddania
//potem dopiero mamy to zastosować, narysować wykresy, pozmieniać parametry, napisać sprawozdanie -
//koniec maja, poczatek czerwca

