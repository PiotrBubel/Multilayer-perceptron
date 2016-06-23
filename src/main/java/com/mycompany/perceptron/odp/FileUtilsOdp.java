package com.mycompany.perceptron.odp;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Piotrek on 23.06.2016.
 */
public class FileUtilsOdp {
    public static List<List<Double>> loadCSV(String filePath) {
        List<List<Double>> listOfLists = new ArrayList<>();
        try (Scanner sc = new Scanner(new FileReader(filePath))) {
            int l = 0;
            sc.nextLine();  //pomijamy pierwsza linie - komentarze
            while (sc.hasNext()) {
                String line = sc.nextLine();
                String[] values = line.split(";");
                listOfLists.add(new ArrayList<Double>());
                for (String v : values) {
                    listOfLists.get(l).add(Double.parseDouble(v.replaceAll(",", ".")));
                }
                l++;
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        return listOfLists;
    }

    public static List<List<Double>> loadCSVAndAddGroup(String filePath, double group) {
        List<List<Double>> listOfLists = new ArrayList<>();
        try (Scanner sc = new Scanner(new FileReader(filePath))) {
            int l = 0;
            sc.nextLine();  //pomijamy pierwsza linie - komentarze
            while (sc.hasNext()) {
                String line = sc.nextLine();
                String[] values = line.split(";");
                listOfLists.add(new ArrayList<Double>());
                for (String v : values) {
                    listOfLists.get(l).add(Double.parseDouble(v.replaceAll(",", ".")));
                }
                listOfLists.get(l).add(group);
                l++;
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        return listOfLists;
    }

    public static double[][] loadCSVArrays(String filePath) {
        List<List<Double>> listOfLists = loadCSV(filePath);
        int x = listOfLists.size();
        int y = listOfLists.get(0).size();
        double[][] arrayOfArrays = new double[x][y];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                arrayOfArrays[i][j] = listOfLists.get(i).get(j);
            }
        }
        return arrayOfArrays;
    }

    public static double[][] loadCSVforClassification(String file1, String file2) {
        List<List<Double>> f1 = loadCSVAndAddGroup(file1, 1d); //czerwone
        List<List<Double>> f2 = loadCSVAndAddGroup(file2, 2d); //biale
        List<List<Double>> listOfLists = new ArrayList<>();
        listOfLists.addAll(f1);
        listOfLists.addAll(f2);
        int x = listOfLists.size();
        int y = listOfLists.get(0).size();
        double[][] arrayOfArrays = new double[x][y];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                arrayOfArrays[i][j] = listOfLists.get(i).get(j);
            }
        }
        return arrayOfArrays;
    }


}
