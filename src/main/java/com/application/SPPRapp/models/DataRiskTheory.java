package com.application.SPPRapp.models;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.*;

public class DataRiskTheory {

    private List<String> codes;

    private List<String> risks;

    // Ущербы
    private int[] damages;

    // Вероятности
    private int[] probabilities;

    // Опасности
    private int[] dangers;

    private double[][] borders;

    public DataRiskTheory() {
        codes = new ArrayList<>();
        risks = new ArrayList<>();
    }

    public List<String> getCodes() {
        return codes;
    }

    public void setCodes(List<String> codes) {
        this.codes = codes;
    }

    public List<String> getRisks() {
        return risks;
    }

    public void setRisks(List<String> risks) {
        this.risks = risks;
    }

    public int[] getDamages() {
        return damages;
    }

    public void setDamages(int[] damages) {
        this.damages = damages;
    }

    public int[] getProbabilities() {
        return probabilities;
    }

    public void setProbabilities(int[] probabilities) {
        this.probabilities = probabilities;
    }

    public int[] getDangers() {
        return dangers;
    }

    public void setDangers(int[] dangers) {
        this.dangers = dangers;
    }

    public double[][] getBorders() {
        return borders;
    }

    public void setBorders(double[][] borders) {
        this.borders = borders;
    }

    public void ReadDataSheet1(Sheet sheet) {
        int rowNum = sheet.getLastRowNum() + 1;
        damages = new int[rowNum];
        probabilities = new int[rowNum];
        dangers = new int[rowNum];

        for (int rowId = 0; rowId < rowNum; rowId++) {
            Row row = sheet.getRow(rowId);
            if (row == null) continue;

            codes.add(row.getCell(0).getStringCellValue());
            risks.add(row.getCell(1).getStringCellValue());

            damages[rowId] = (row.getCell(2) != null) ? (int) row.getCell(2).getNumericCellValue() : 0;
            probabilities[rowId] = (row.getCell(3) != null) ? (int) row.getCell(3).getNumericCellValue() : 0;
            dangers[rowId] = damages[rowId] * probabilities[rowId];
        }
    }

    public void ReadDataSheet2(Sheet sheet) {
        int rowNum = sheet.getLastRowNum() + 1;
        borders = new double[rowNum][2];

        for (int rowId = 0; rowId < rowNum; rowId++) {
            Row row = sheet.getRow(rowId);
            borders[rowId][0] = (row.getCell(0) != null) ? (double) row.getCell(0).getNumericCellValue() : 0;
            borders[rowId][1] = (row.getCell(1) != null) ? (double) row.getCell(1).getNumericCellValue() : 0;
        }
    }

}
