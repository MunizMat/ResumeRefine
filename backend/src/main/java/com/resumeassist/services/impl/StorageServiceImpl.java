package com.resumeassist.services.impl;

import com.resumeassist.dtos.SaveResumeResponseDTO;
import com.resumeassist.services.PDFService;
import com.resumeassist.services.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
class StorageServiceImpl implements StorageService  {
    private final S3Client s3Client;
    private final PDFService pdfService;

    @Value("${bucket.name}")
    private String bucketName;

    public StorageServiceImpl(PDFService pdfService){
        this.pdfService = pdfService;

        this.s3Client = S3Client.builder()
                .region(Region.US_EAST_1)
                .build();
    }

    @Override
    public byte[] getResume(String resumeId) throws IOException {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucketName)
                .key("%s/resume.pdf".formatted(resumeId))
                .build();

        var result = s3Client.getObject(request);

        return result.readAllBytes();
    }


    @Override
    public SaveResumeResponseDTO saveResume(MultipartFile resume) throws IOException{
        Map<String, String> metadata = new HashMap<>();
        String resumeId = UUID.randomUUID().toString();
        String objectKey = "%s/resume.pdf".formatted(resumeId);

        metadata.put("filename", resume.getOriginalFilename());

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .metadata(metadata)
                .build();

        s3Client.putObject(request, RequestBody.fromInputStream(resume.getInputStream(), resume.getSize()));

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key("%s/preview.jpg".formatted(resumeId))
                .build();

        byte[] fileContent = pdfService.getPdfImage(resume.getInputStream());

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(fileContent));

        String url = "https://%s.s3.amazonaws.com/%s/preview.jpg".formatted(bucketName, resumeId);

        return new SaveResumeResponseDTO(url, resumeId ,resume.getOriginalFilename());
    }
}
