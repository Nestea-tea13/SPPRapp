package com.application.SPPRapp.controllers;

import java.io.IOException;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import com.application.SPPRapp.SppRappApplication;

@Controller
public class ExcelWorkController {

    @PostMapping("/upload-data-AVONA")
    public String handleFileUpload(MultipartFile file, Model model) {
        if (file.isEmpty()) {
            model.addAttribute("message", "Пожалуйста, выберите файл для загрузки.");
            return "main";
        }

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            SppRappApplication.dataAVONA.ReadData(sheet);
        } catch (IOException e) {
            model.addAttribute("message", "Ошибка при чтении файла: " + e.getMessage());
        }

        return "redirect:/upload-result-AVONA";
    }

    
}
