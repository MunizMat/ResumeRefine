package com.resumeassist.controllers;

import com.resumeassist.dtos.SaveResumeResponseDTO;
import com.resumeassist.services.AIService;
import com.resumeassist.services.PDFService;
import com.resumeassist.services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping(path = "/resume")
public class ResumeController {
    private  final StorageService storageService;
    private final AIService aiService;
    private final PDFService pdfService;

    @Autowired
    public ResumeController(StorageService storageService, AIService aiService, PDFService pdfService){
        this.storageService = storageService;
        this.aiService = aiService;
        this.pdfService = pdfService;
    }

    @PostMapping
    public ResponseEntity<SaveResumeResponseDTO> saveResume(@RequestParam MultipartFile resume){

        try {
            var response = storageService.saveResume(resume);

            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            ex.printStackTrace();

            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(path = "/{resumeId}/analysis")
    public ResponseEntity<String> getResumeAnalysis(@PathVariable String resumeId){
        try {
            byte[] pdfContent = storageService.getResume(resumeId);
            String resumeParsed = pdfService.parsePDFText(pdfContent);
            var result = aiService.getResumeRecommendations(resumeParsed);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
 }
