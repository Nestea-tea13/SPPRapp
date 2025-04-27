package com.application.SPPRapp.controllers.auxiliary;

import java.io.FileOutputStream;
import java.util.Random;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GeneratePlatformSales {

    @GetMapping("/analysis-of-variance-generate")
    public String generateSalesData() {
        try {
            // 1. Создаем книгу Excel
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Sales Data");

            // 2. Заголовки столбцов
            String[] platforms = {"Платформа 1", "Платформа 2", "Платформа 3", "Платформа 4", "Платформа 5"};
            Row headerRow = sheet.createRow(0);
            for (int i = 1; i < platforms.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(platforms[i]);
            }

            // 3. Генерация случайных данных за 60 дней
            Random random = new Random();
            for (int day = 1; day <= 60; day++) {
                Row row = sheet.createRow(day);
                row.createCell(0).setCellValue(day); // Номер дня

                for (int platform = 1; platform <= 5; platform++) {
                    int sales = 20 + random.nextInt(281); // Случайное число от 20 до 300
                    row.createCell(platform).setCellValue(sales);
                }
            }

            // 4. Сохраняем файл в папку static для доступа из веб-интерфейса
            String filePath = "src/main/resources/static/platform_sales.xlsx";
            try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
                workbook.write(outputStream);
            }
            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/error"; // В случае ошибки - на страницу ошибки
        }
            return "redirect:/analysis-of-variance";
    }
    
}
