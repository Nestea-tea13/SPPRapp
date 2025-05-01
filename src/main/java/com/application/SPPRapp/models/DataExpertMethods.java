package com.application.SPPRapp.models;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class DataExpertMethods {
    
    // Количество критериев
    private int numCriteria;

    private List<String> criteria;

    private List<double[][]> evaluations;

    // A - собственный вектор матрицы
    private double[][] eigenvectorOfMatrix;

    // X - вектор приоритетов
    private double[][] priorityVector;

    private double[][] evaluationsSum;

    private double[] eigenvectorOfMatrixSum;

    private double[] priorityVectorSum;

    private double[] lambda;

    // ИС=(лямбда-n)/(n-1)
    private double[] IS;

    // Значение показателя случайной согласованности из таблицы
    private double[] SS;

    // ОС=ИС/СС
    private double[] OS;

    // Общее мнение по показателям
    private double[] OM;

    private double OMsum;

    // Эксперты, чье мнение можно учитывать в общем, если ПОС <= 0.2
    private List<String> OMExperts;


    public DataExpertMethods() {
        criteria = new ArrayList<>();
        evaluations = new ArrayList<>();
        OMExperts = new ArrayList<>();
    }

    public int getNumCriteria() {
        return numCriteria;
    }

    public void setNumCriteria(int numCriteria) {
        this.numCriteria = numCriteria;
    }

    public List<String> getCriteria() {
        return criteria;
    }

    public void setCriteria(List<String> criteria) {
        this.criteria = criteria;
    }

    public List<double[][]> getEvaluations() {
        return evaluations;
    }

    public void setEvaluations(List<double[][]> evaluations) {
        this.evaluations = evaluations;
    }

    public double[][] getEigenvectorOfMatrix() {
        return eigenvectorOfMatrix;
    }

    public void setEigenvectorOfMatrix(double[][] eigenvectorOfMatrix) {
        this.eigenvectorOfMatrix = eigenvectorOfMatrix;
    }

    public double[][] getPriorityVector() {
        return priorityVector;
    }

    public void setPriorityVector(double[][] priorityVector) {
        this.priorityVector = priorityVector;
    }

    public double[][] getEvaluationsSum() {
        return evaluationsSum;
    }

    public void setEvaluationsSum(double[][] evaluationsSum) {
        this.evaluationsSum = evaluationsSum;
    }

    public double[] getEigenvectorOfMatrixSum() {
        return eigenvectorOfMatrixSum;
    }

    public void setEigenvectorOfMatrixSum(double[] eigenvectorOfMatrixSum) {
        this.eigenvectorOfMatrixSum = eigenvectorOfMatrixSum;
    }

    public double[] getPriorityVectorSum() {
        return priorityVectorSum;
    }

    public void setPriorityVectorSum(double[] priorityVectorSum) {
        this.priorityVectorSum = priorityVectorSum;
    }

    public double[] getLambda() {
        return lambda;
    }

    public void setLambda(double[] lambda) {
        this.lambda = lambda;
    }

    public double[] getIS() {
        return IS;
    }

    public void setIS(double[] iS) {
        IS = iS;
    }

    public double[] getSS() {
        return SS;
    }

    public void setSS(double[] sS) {
        SS = sS;
    }

    public double[] getOS() {
        return OS;
    }

    public void setOS(double[] oS) {
        OS = oS;
    }

    public double[] getOM() {
        return OM;
    }

    public void setOM(double[] oM) {
        OM = oM;
    }

    public double getOMsum() {
        return OMsum;
    }

    public void setOMsum(double oMsum) {
        OMsum = oMsum;
    }

    public List<String> getOMExperts() {
        return OMExperts;
    }

    public void setOMExperts(List<String> oMExperts) {
        OMExperts = oMExperts;
    }


    public void ReadData(Sheet[] sheets) {
        for (Row row : sheets[0]) {
            Cell cell = row.getCell(0);
            if (row.getCell(0) == null) continue;
            criteria.add(cell.getStringCellValue());
        }

        numCriteria = criteria.size();

        for (int sheetId = 1; sheetId < sheets.length; sheetId++) {
            double[][] evaluation = new double[numCriteria][numCriteria];

            for (int i = 0; i < numCriteria; i++) {
                Row row = sheets[sheetId].getRow(i);
                for (int j = 0; j < numCriteria; j++) {
                    if (i <= j) evaluation[i][j] = (double) row.getCell(j).getNumericCellValue();
                    else evaluation[i][j] = 1 / evaluation[j][i];
                }
            }
            evaluations.add(evaluation);
        }

        int numExperts = sheets.length - 1;
        eigenvectorOfMatrix = new double[numExperts][numCriteria]; // кол-во строк - кол-во экспертов, кол-во столбцов - кол-во критериев
        eigenvectorOfMatrixSum = new double[numExperts];
        priorityVector = new double[numExperts][numCriteria];
        priorityVectorSum = new double[numExperts];
        calculateEigenvectorOfMatrixAndPriorityVectorParameters();

        evaluationsSum = new double[numExperts][numCriteria];
        calculateEvaluationsSum();

        lambda = new double[numExperts];
        IS = new double[numExperts];
        calculateLambdasAndIS(numExperts);

        SS = new double[numExperts];
        OS = new double[numExperts];
        calculateSSandOS(numExperts);

        calculateOM(numExperts);
    }

    public void calculateEigenvectorOfMatrixAndPriorityVectorParameters() {
        int index = 0;
        for(double[][] evaluation : evaluations) {
            double[] geometricMeans = new double[numCriteria];
            for(int i = 0; i < numCriteria; i++) {
                double geometricMean = 1;
                for(double num : evaluation[i]) geometricMean *= num;
                geometricMeans[i] = Math.pow(geometricMean, 1.0 / numCriteria);
            }
            eigenvectorOfMatrix[index] = geometricMeans;

            for(double value : geometricMeans) eigenvectorOfMatrixSum[index] += value;

            for(int i = 0; i < numCriteria; i++) {
                priorityVector[index][i] = eigenvectorOfMatrix[index][i] / eigenvectorOfMatrixSum[index];
                priorityVectorSum[index] += priorityVector[index][i];
            }
            index++;
        }
    }

    public void calculateEvaluationsSum() {
        int index = 0;
        for(double[][] evaluation : evaluations) {
            double[] sums = new double[numCriteria];
            for(int col = 0; col < numCriteria; col++) {
                sums[col] = 0;
                for(int row = 0; row < numCriteria; row++) {
                    sums[col] += evaluation[row][col];
                }
            }
            evaluationsSum[index] = sums;
            index++;
        }
    }

    public void calculateLambdasAndIS(int numExperts) {
        for(int i = 0; i < numExperts; i++) {
            for(int j = 0; j < numCriteria; j++) {
                lambda[i] += evaluationsSum[i][j] * priorityVector[i][j];
            }
        }

        // ИС=(лямбда-n)/(n-1)
        for(int i = 0; i < numExperts; i++) {
            IS[i] = (lambda[i] - numCriteria) / (numCriteria - 1);
        }
    }

    public void calculateSSandOS(int numExperts) {
        double[] PSS = new double[]{0, 0, 0.58, 0.9, 1.12, 1.24, 1.32, 1.41, 1.45, 1.49};
        for(int i = 0; i < numExperts; i++) {
            SS[i] = (numCriteria < 10) ? PSS[numCriteria - 1] : PSS[9];
            OS[i] = IS[i] / SS[i];
        }
    }

    public void calculateOM(int numExperts) {
        OMsum = 0;
        int OMnumExperts = 0;
        OM = new double[numCriteria];

        for(int i = 0; i < numCriteria; i++) {
            double _OM = 0;
            for(int j = 0; j < numExperts; j++) {
                if (OS[j] <= 0.2) {
                    _OM += priorityVector[j][i];
                    if (i == 0) {
                        OMnumExperts++;
                        OMExperts.add("Эксперт " + (j + 1));
                    }
                }   
            }
            OM[i] = _OM / OMnumExperts;
            OMsum += OM[i];
        }
    }

}
