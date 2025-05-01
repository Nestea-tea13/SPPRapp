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
    public String handleFileUploadAVONA(MultipartFile file, Model model) {
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

    @PostMapping("/upload-data-risk-theory")
    public String handleFileUploadRiskTheory(MultipartFile file, Model model) {
        if (file.isEmpty()) {
            model.addAttribute("message", "Пожалуйста, выберите файл для загрузки.");
            return "main";
        }

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            SppRappApplication.dataRiskTheory.ReadDataSheet1(workbook.getSheetAt(0));
            SppRappApplication.dataRiskTheory.ReadDataSheet2(workbook.getSheetAt(1));
        } catch (IOException e) {
        model.addAttribute("message", "Ошибка при чтении файла: " + e.getMessage());
        }

        return "redirect:/upload-result-risk-theory";
    }

    @PostMapping("/upload-data-expert-methods")
    public String handleFileUploadExpertMethods(MultipartFile file, Model model) {
        if (file.isEmpty()) {
            model.addAttribute("message", "Пожалуйста, выберите файл для загрузки.");
            return "main";
        }

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            int sheetCount = workbook.getNumberOfSheets();
            Sheet[] sheets = new Sheet[sheetCount];
            
            for (int i = 0; i < sheetCount; i++) {
                sheets[i] = workbook.getSheetAt(i);
            }
            
            SppRappApplication.dataExpertMethods.ReadData(sheets);
        } catch (IOException e) {
        model.addAttribute("message", "Ошибка при чтении файла: " + e.getMessage());
        }

        return "redirect:/upload-result-expert-methods";
    }

}
