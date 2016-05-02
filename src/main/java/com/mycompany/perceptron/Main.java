package com.mycompany.perceptron;

import com.mycompany.perceptron.procedures.TransformationProcedures;

import java.util.*;

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


        TransformationProcedures.generateRaports();
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

    /**
     * Klasyfikacja (maksymalna ocena 5)
     *
     * Stworzyć sieć neuronową (MPL) o 1, 2, 3 oraz 4 wejściach i 3 wyjściach. Sieć powinna posiadać
     * od 1 do 20 neuronów w jednej warstwie ukrytej. Wszystkie neurony powinny posiadać sigmoidalną
     * funkcję aktywacji. Należy nauczyć sieci z wykorzystaniem poniższych danych:
     *
     * classification_train.txt
     *
     * gdzie w każdej linicje opisany jest jeden obiekt (pierwsze 4 liczby oznaczają cechy tego
     * obiektu) ostatnia liczba oznacza rodzaj obiektu (oddzielone spacją). Za wyjście sieci należy
     * uznać odpowiednio zakodowany rodzaj obiektu: 1 - (1,0,0), 2 - (0,1,0), 3 - (0,0,1). Na
     * wejście sieci należy podawać wybrane cechy obiektu w zależoności od liczby wejść (należy
     * rozważyć 4 sieci z 1 wejściem, 6 sieci z 2 wejściami, 4 sieci z 3 wejściami i 1 sieć z 4
     * wejściami). Należy przetestować sieci z biasem. Nauczona sieć powinna klasyfikować obiekty
     * (określać prawidłowo ich rodzaj), których nie widziała podczas nauki. W celu sprawdzenia
     * jakości nauczonej sieci należy skorzystać z następujących danych testowych:
     *
     * classification_test.txt
     *
     * Format tego pliku jest identyczny z formatem pliku z danymi treningowymi. Jako ocenę jakości
     * należy rozważyć procent poprawnie sklasyfikowanych obiektów (odpowiednio odkodowany rodzaj
     * obiektu) ze zbioru testowego.
     *
     * W sprawozdaniu należy zwrócić uwagę na następujące rzeczy:
     *
     * Jak zmienia się błąd średniokwadratowy oraz procent poprawnie sklasyfikowanych obiektów po
     * każdej epoce nauki na zbiorze treningowym i zbiorze testowym? Jaka liczba neuronów w warstwie
     * ukrytej jest potrzebna, aby sieć dokonywała poprawnej klasyfikiacji? Kiedy można uznać, że
     * sieć jest nauczona? Jak wpływają parametry nauki (współczynnik nauki i momentum) na szybkość
     * nauki? Jak wpływa wybór liczby i rodzaju cech obiektów na możlwości nauki sieci?
     */
    public static void classification() {

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

