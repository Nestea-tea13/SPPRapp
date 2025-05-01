package com.application.SPPRapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.application.SPPRapp.SppRappApplication;

@Controller
public class MainController {

    @GetMapping("/")
    public String mainPage() {
        return "main";
    }

    @GetMapping("/analysis-of-variance")
    public String analysisOfVariancePage() {
        return "AnalysisOfVariance";
    }

    @GetMapping("/expert-methods")
    public String expertMethodsPage() {
        return "ExpertMethods";
    }

    @GetMapping("/risk-theory")
    public String riskTheoryPage() {
        return "RiskTheory";
    }

    @GetMapping("/upload-result-AVONA")
    public String uploadResultAVONAPage(Model model) {
        model.addAttribute("names", SppRappApplication.dataAVONA.getPlatformNames());
        model.addAttribute("sales", SppRappApplication.dataAVONA.getNumberOfSales());
        model.addAttribute("averageValues", SppRappApplication.dataAVONA.getAverageValues());
        model.addAttribute("sumSquares", SppRappApplication.dataAVONA.getSumOfSquares());
        model.addAttribute("anova", SppRappApplication.dataAVONA.getAnovaResult());
        return "uploadResultAVONA";
    }

    @GetMapping("/upload-result-risk-theory")
    public String uploadResultRiskTheoryPage(Model model) {
        model.addAttribute("codes", SppRappApplication.dataRiskTheory.getCodes());
        model.addAttribute("risks", SppRappApplication.dataRiskTheory.getRisks());
        model.addAttribute("damages", SppRappApplication.dataRiskTheory.getDamages());
        model.addAttribute("probabilities", SppRappApplication.dataRiskTheory.getProbabilities());
        model.addAttribute("dangers", SppRappApplication.dataRiskTheory.getDangers());
        model.addAttribute("borders", SppRappApplication.dataRiskTheory.getBorders());
        return "uploadResultRiskTheory";
    }

    @GetMapping("/upload-result-expert-methods")
    public String uploadResultExpertMethodsPage(Model model) {
        model.addAttribute("numCriteria", SppRappApplication.dataExpertMethods.getNumCriteria());
        model.addAttribute("criteria", SppRappApplication.dataExpertMethods.getCriteria());
        model.addAttribute("evaluations", SppRappApplication.dataExpertMethods.getEvaluations());
        model.addAttribute("eigenvectorOfMatrix", SppRappApplication.dataExpertMethods.getEigenvectorOfMatrix());
        model.addAttribute("eigenvectorOfMatrixSum", SppRappApplication.dataExpertMethods.getEigenvectorOfMatrixSum());
        model.addAttribute("priorityVector", SppRappApplication.dataExpertMethods.getPriorityVector());
        model.addAttribute("priorityVectorSum", SppRappApplication.dataExpertMethods.getPriorityVectorSum());
        model.addAttribute("evaluationsSum", SppRappApplication.dataExpertMethods.getEvaluationsSum());
        model.addAttribute("lambdas", SppRappApplication.dataExpertMethods.getLambda());
        model.addAttribute("ISs", SppRappApplication.dataExpertMethods.getIS());
        model.addAttribute("SSs", SppRappApplication.dataExpertMethods.getSS());
        model.addAttribute("OSs", SppRappApplication.dataExpertMethods.getOS());
        model.addAttribute("OMs", SppRappApplication.dataExpertMethods.getOM());
        model.addAttribute("OMExperts", SppRappApplication.dataExpertMethods.getOMExperts());
        model.addAttribute("OMsum", SppRappApplication.dataExpertMethods.getOMsum());
        return "uploadResultExpertMethods";
    }

}
