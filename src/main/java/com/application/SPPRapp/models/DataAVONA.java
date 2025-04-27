package com.application.SPPRapp.models;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.stat.inference.OneWayAnova;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class DataAVONA {
    
    private List<String> platformNames;

    private List<double[]> numberOfSales;
    
    private double[] averageValues;

    private double[] sumOfSquares;

    private ANOVAResult anovaResult;

    public DataAVONA() {
        platformNames = new ArrayList<>();
        numberOfSales = new ArrayList<>();
    }

    public List<String> getPlatformNames() {
        return platformNames;
    }

    public void setPlatformNames(List<String> platformNames) {
        this.platformNames = platformNames;
    }

    public List<double[]> getNumberOfSales() {
        return numberOfSales;
    }

    public void setNumberOfSales(List<double[]> numberOfSales) {
        this.numberOfSales = numberOfSales;
    }
    
    public double[] getAverageValues() {
        return averageValues;
    }

    public void setAverageValues(double[] averageValues) {
        this.averageValues = averageValues;
    }

    public double[] getSumOfSquares() {
        return sumOfSquares;
    }

    public void setSumOfSquares(double[] sumOfSquares) {
        this.sumOfSquares = sumOfSquares;
    }


    public void ReadData(Sheet sheet) {
        List<String> names = new ArrayList<>();
        Row rowNames = sheet.getRow(0);

        for (Cell cell : rowNames) {
            names.add(cell.getStringCellValue());
        }
        setPlatformNames(names);

        List<double[]> sales = new ArrayList<>();
        int n = names.size();

        // Начинаем со второй строки (индекс 1), пропуская заголовок
        for (int rowId = 1; rowId <= sheet.getLastRowNum(); rowId++) {
            Row row = sheet.getRow(rowId);
            if (row == null) continue;

            double[] rowData = new double[n];
            for (int i = 0; i < n; i++) {
                Cell cell = row.getCell(i);
                rowData[i] = (cell != null) ? (double) cell.getNumericCellValue() : 0;
            }
            sales.add(rowData);
        }
        setNumberOfSales(sales);

        // Вычисляем средние значения и сумму квадратов значений по столбцам
        calculateAverageValuesAndSumOfSquares();

        // Вычисляем ANOVA сразу после загрузки данных
        calculateANOVA();
    }

    private void calculateAverageValuesAndSumOfSquares() {
        int columns = platformNames.size();
        int rows = numberOfSales.size();

        double[] _averageValues = new double[columns];
        double[] _sumOfSquares = new double[columns];

        for (double[] row : numberOfSales) {
            for (int i = 0; i < columns; i++) {
                _averageValues[i] += row[i];
                _sumOfSquares[i] += row[i]*row[i];
            }
        }

        for (int i = 0; i < columns; i++) _averageValues[i] = _averageValues[i] / rows;

        setAverageValues(_averageValues);
        setSumOfSquares(_sumOfSquares);
    }

    private void calculateANOVA() {
        OneWayAnova anova = new OneWayAnova();
        List<double[]> groups = new ArrayList<>();
        
        // Транспонируем данные для группировки по платформам
        for (int i = 0; i < platformNames.size(); i++) {
            double[] platformData = new double[numberOfSales.size()];
            for (int j = 0; j < numberOfSales.size(); j++) {
                platformData[j] = numberOfSales.get(j)[i];
            }
            groups.add(platformData);
        }

        this.anovaResult = new ANOVAResult(anova, groups, this.averageValues, this.sumOfSquares);
    }

    private static double getFCriticalValue(double alpha, int k1, int k2) {
        FDistribution fDist = new FDistribution(k1, k2);
        return fDist.inverseCumulativeProbability(1 - alpha);
    }
    
    public ANOVAResult getAnovaResult() {
        return anovaResult;
    }
    
    
    public static class ANOVAResult {

        /**
         * Значение F-статистики, вычисленное в ходе дисперсионного анализа (ANOVA)
         * Показывает отношение межгрупповой изменчивости к внутригрупповой
         * Чем больше значение, тем более значимы различия между группами
         */
        public final double fValue;

        /**
         * p-значение (уровень значимости)
         * Вероятность получить такие или более крайние результаты при условии, что нулевая гипотеза верна
         * - p < 0.05: различия статистически значимы
         * - p >= 0.05: различия незначимы
         */
        public final double pValue;

        /**
         * Критическое значение F-распределения для заданного уровня значимости (обычно α=0.05)
         * Если F-статистика превышает это значение, нулевая гипотеза отвергается
         * Зависит от степеней свободы
         */
        public final double criticalValue;

        /**
         * Исходные данные по группам
         * Каждый элемент списка - массив значений одной группы (платформы)
         * Например: groups.get(0) - массив продаж 1-й платформы
         */
        public final List<double[]> groups;

        /** Количество групп */ 
        public int k;

        /** Количество измерений */ 
        public int n;

        public double Qa;

        public double Qe;

        public double Q;

        /** Va = k - 1 */
        public int Va;

        /** Ve = k(n - 1) */
        public int Ve;

        /** V = nk - 1 */
        public int V;

        public double Sa2;

        public double Se2;

        public double S2;

        public double f;
    
        /**
         * Конструктор для хранения результатов ANOVA.
         * @param fValue        вычисленная F-статистика
         * @param pValue        p-значение
         * @param criticalValue критическое значение F-распределения
         * @param groups        исходные данные по группам
         */
        public ANOVAResult(OneWayAnova anova, List<double[]> _groups, double[] _averageValues, double[] _sumOfSquares) {
            groups = _groups;
            fValue = anova.anovaFValue(groups);
            pValue = anova.anovaPValue(groups);

            k = groups.size();
            n = _groups.get(0).length;

            Qa = calculateQa(_averageValues);
            Q = calculateQ(_averageValues, _sumOfSquares);
            Qe = Q - Qa;

            Va = k - 1;
            Ve = k*(n - 1);
            V = n*k - 1;

            Sa2 = Qa/Va;
            Se2 = Qe/Ve;
            S2 = Q/V;

            f = Sa2/Se2;

            criticalValue = getFCriticalValue(0.05, Va, Ve);
        }

        private double calculateQa(double[] _averageValues) {
            double averageX = 0, averageSum2 = 0, Qa = 0;

            for(int i = 0; i < k; i++) averageX += _averageValues[i];
            averageX /= k;

            for(int i = 0; i < k; i++) averageSum2 += _averageValues[i]*_averageValues[i];

            Qa = n * averageSum2 - n * k * averageX * averageX;
            return Qa;
        }

        private double calculateQ(double[] _averageValues, double[] _sumOfSquares) {
            double averageX = 0, sum = 0, Q = 0;

            for(int i = 0; i < k; i++) averageX += _averageValues[i];
            averageX /= k;

            for(int i = 0; i < k; i++) sum += _sumOfSquares[i];

            Q = sum - n * k * averageX * averageX;
            return Q;
        }
    }
}
