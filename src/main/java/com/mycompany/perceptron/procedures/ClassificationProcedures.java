package com.mycompany.perceptron.procedures;


import com.mycompany.perceptron.network.ConnectedNeuron;

/**
 * Created by Piotrek on 02.05.2016.
 */
public class ClassificationProcedures {

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

    public static String inputFile = "classification_train.txt";
    public static String testFile = "classification_test.txt";

    public static void generateReports() {
        int hiddenNeurons;
        int epochs;
        String outputFile = "_classification";
        double expectedError;

        ConnectedNeuron.BETA = 1.0d;
        ConnectedNeuron.STEP = 0.1d;
        ConnectedNeuron.MOMENTUM = 0.2d;
        ConnectedNeuron.BIAS_ENABLED = true;
        epochs = 3000;
        //dla powyzszych danych liczy i rysuje wykresy dla sieci z neuronami od 1 do 20
        //mozna sprawdzic co sie dzieje z innymi parametrami powyzej
        for (hiddenNeurons = 1; hiddenNeurons <= 20; hiddenNeurons++) {
            String outputFile1 = outputFile + hiddenNeurons + "epochs";
            ClassificationProcedures.performClassification(epochs, hiddenNeurons, outputFile1 + "a");
            ClassificationProcedures.performClassification(epochs, hiddenNeurons, outputFile1 + "b");
        }

        //tylko jeden przypadek, mozna dodac inne
        hiddenNeurons = 15;
        String outputFile1 = outputFile + hiddenNeurons + "error";
        expectedError = 0.1d;
        ClassificationProcedures.performClassification(expectedError, hiddenNeurons, outputFile1 + "a");
        ClassificationProcedures.performClassification(expectedError, hiddenNeurons, outputFile1 + "b");
    }

    /**
     * @param epochs        - number of epochs network will be train
     * @param hiddenNeurons - number of neurons on hidden layer
     * @param outputFile    - reports will start with this file name
     */
    public static void performClassification(int epochs, int hiddenNeurons, String outputFile) {
        String errorsFilePathLearnSet = "_classification_learn_error.txt";
        String errorsFilePathTestSet = "_classification_test_error.txt";

        String plotFilePath = "_classificationPlot";
        /*
        //ConnectedNeuralNetwork network = new ConnectedNeuralNetwork(1, 1, hiddenNeurons, 1);
        ConnectedNeuralNetwork network = new NeuralNetworkApproximation(hiddenNeurons);
        double[][] learningSet;
        if (learningSet1) {
            learningSet = FileUtils.loadDataArrays(ApproximationProcedures.inputFile1);
        } else {
            learningSet = FileUtils.loadDataArrays(ApproximationProcedures.inputFile2);
        }

        double[][] testingSet = FileUtils.loadDataArrays(testFile);

        File f = new File(errorsFilePathLearnSet);
        f.delete();
        File f2 = new File(errorsFilePathTestSet);
        f2.delete();
        double err = 0;

        for (int i = 0; i < epochs; i++) {
            //epoka nauki
            err = 0;
            double[][] mixedSet = Utils.shake(learningSet);
            for (double[] data : mixedSet) {
                network.learn(new double[]{data[0]}, new double[]{data[1]});
                err = err + Utils.countError(network.output(new double[]{data[0]})[0], data[1]);
            }
            err = err / mixedSet.length;
            FileUtils.addPoint(errorsFilePathLearnSet, new double[]{i, err});

            //liczenie bledu ze zbioru testowego
            err = 0;
            mixedSet = testingSet;//Utils.shake(testingSet);
            for (double[] data : mixedSet) {
                err = err + Utils.countError(network.output(new double[]{data[0]})[0], data[1]);
            }
            err = err / mixedSet.length;
            FileUtils.addPoint(errorsFilePathTestSet, new double[]{i, err});
        }

        //network.print();

        DecimalFormat df = new DecimalFormat("0.00000");
        String header = "Epoki: " + epochs + ", ostatni blad: " + df.format(err) +
                ",\\n step: " + ConnectedNeuron.STEP + ", " +
                "momentum: " + ConnectedNeuron.MOMENTUM + ", bias: " + ConnectedNeuron.BIAS_ENABLED +
                ",  " + hiddenNeurons + " neurony ukryte.";

        ApproximationProcedures.drawFunction(network, outputFile + "Functions", header, learningSet1);


        //generowanie raportu z błędami wyliczonymi z danych testowych
        ApproximationProcedures.saveErrorPlotCommand(plotFilePath,
                outputFile + "TLError.png",
                errorsFilePathLearnSet,
                errorsFilePathTestSet,
                "Blad sredniokwadratowy. \\n" + header);

        try {
            Utils.runGnuplotScript(plotFilePath);
            System.out.println("Wygenerowano pliki raportu: " + outputFile);
        } catch (IOException ex) {
            System.out.println("Wystapil blad przy rysowaniu wykresu " + outputFile);
        }

        f = new File(errorsFilePathLearnSet);
        f.delete();
        f = new File(errorsFilePathTestSet);
        f.delete();
        f = new File(plotFilePath);
        f.delete();
        f = new File(outputFile + "FunctionsPlot");
        f.delete();
        f = new File(outputFile + "FunctionsTmp.txt");
        f.delete();
        */
    }

    /**
     * Method learns network until it reaches expected error or 10000 epochs. May cause performance
     * issues.
     *
     * @param expectedError - error you want to end with after learning, if it is impossible,
     *                      learning will stop after 250 000 epochs
     * @param hiddenNeurons - number of neurons on hidden layer
     * @param outputFile    - reports will start with this file name
     */
    public static void performClassification(double expectedError, int hiddenNeurons, String outputFile) {

        int maxEpochs = 10000;
        int epochs = 0;
        String errorsFilePathLearnSet = "_classification_learn_error.txt";
        String errorsFilePathTestSet = "_classification_test_error.txt";

        String plotFilePath = "_classificationPlot";
        /*
        //ConnectedNeuralNetwork network = new ConnectedNeuralNetwork(1, 1, hiddenNeurons, 1);
        ConnectedNeuralNetwork network = new NeuralNetworkApproximation(hiddenNeurons);
        double[][] learningSet;
        if (learningSet1) {
            learningSet = FileUtils.loadDataArrays(ApproximationProcedures.inputFile1);
        } else {
            learningSet = FileUtils.loadDataArrays(ApproximationProcedures.inputFile2);
        }

        double[][] testingSet = FileUtils.loadDataArrays(testFile);

        File f = new File(errorsFilePathLearnSet);
        f.delete();
        File f2 = new File(errorsFilePathTestSet);
        f2.delete();
        double err = Double.MAX_VALUE;

        while (err >= expectedError && epochs < maxEpochs) {
            //epoka nauki
            double[][] mixedSet = Utils.shake(learningSet);
            err = 0d;
            for (double[] data : mixedSet) {
                network.learn(new double[]{data[0]}, new double[]{data[1]});
                err = err + Utils.countError(network.output(new double[]{data[0]})[0], data[1]);
            }
            err = err / mixedSet.length;
            FileUtils.addPoint(errorsFilePathLearnSet, new double[]{epochs, err});

            //liczenie bledu ze zbioru testowego
            err = 0d;
            mixedSet = testingSet;//Utils.shake(testingSet);
            for (double[] data : mixedSet) {
                err = err + Utils.countError(network.output(new double[]{data[0]})[0], data[1]);
            }
            err = err / mixedSet.length;
            FileUtils.addPoint(errorsFilePathTestSet, new double[]{epochs, err});
            epochs++;

            if (epochs % 1000 == 0) {
                System.out.println("Siec przeszla " + epochs + " epok nauki");
            }
        }

        DecimalFormat df = new DecimalFormat("0.00000");
        String header = "Oczekiwany blad: " + expectedError + ", ostatni blad: " + df.format(err * 0.25) +
                ", epoki: " + epochs + ", \\n step: " + ConnectedNeuron.STEP + ", " +
                "momentum: " + ConnectedNeuron.MOMENTUM + ", bias: " + ConnectedNeuron.BIAS_ENABLED +
                ",  " + hiddenNeurons + " neurony ukryte.";


        ApproximationProcedures.drawFunction(network, outputFile + "Functions", header, learningSet1);


        //generowanie raportu z błędami wyliczonymi z danych testowych
        ApproximationProcedures.saveErrorPlotCommand(plotFilePath,
                outputFile + "TLError.png",
                errorsFilePathLearnSet,
                errorsFilePathTestSet,
                "Blad sredniokwadratowy. \\n" + header);

        try {
            Utils.runGnuplotScript(plotFilePath);
            System.out.println("Wygenerowano pliki raportu: " + outputFile);
        } catch (IOException ex) {
            System.out.println("Wystapil blad przy rysowaniu wykresu " + outputFile);
        }

        f = new File(errorsFilePathLearnSet);
        f.delete();
        f = new File(errorsFilePathTestSet);
        f.delete();
        f = new File(plotFilePath);
        f.delete();
        f = new File(outputFile + "FunctionsPlot");
        f.delete();
        f = new File(outputFile + "FunctionsTmp.txt");
        f.delete();
        */
    }

}
