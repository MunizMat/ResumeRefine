package com.resumeassist.services.impl;

import com.resumeassist.services.PDFService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class PDFServiceImpl implements PDFService {

    @Override
    public String parsePDFText(byte[] pdfBytes) throws IOException {
        PDDocument document = PDDocument.load(pdfBytes);
        PDFTextStripper textStripper = new PDFTextStripper();

        String text = textStripper.getText(document);

        document.close();

        return text;
    }

    @Override
    public byte[] getPdfImage(InputStream inputStream) throws IOException {
        PDDocument document = PDDocument.load(inputStream);
        PDFRenderer renderer = new PDFRenderer(document);
        BufferedImage image = renderer.renderImageWithDPI (0, 300);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", outputStream);

        document.close();
        return outputStream.toByteArray();
    }
}
