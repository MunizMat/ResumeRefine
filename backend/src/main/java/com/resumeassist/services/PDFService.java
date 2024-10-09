package com.resumeassist.services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface PDFService {
    String parsePDFText(byte[] pdfBytes) throws IOException;
    byte[] getPdfImage(InputStream inputStream) throws IOException;
}
