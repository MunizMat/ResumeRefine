package com.resumeassist.services;

import com.resumeassist.dtos.SaveResumeResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StorageService {
    byte[] getResume(String resumeId) throws IOException;
    SaveResumeResponseDTO saveResume(MultipartFile resume) throws IOException;
}
