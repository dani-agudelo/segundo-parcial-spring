package com.example.pal.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class PDFService {
    public byte[] generateSimplePdf(String title, String content) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // Inicializar PDF writer
        PdfWriter writer = new PdfWriter(baos);

        // Inicializar PDF document
        PdfDocument pdf = new PdfDocument(writer);

        // Inicializar document
        Document document = new Document(pdf);

        try {
            // Añadir título
            document.add(new Paragraph(title).setBold().setFontSize(16)
                    .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER));

            // Añadir fecha y hora
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            document.add(new Paragraph("Generado: " + now.format(formatter))
                    .setFontSize(10)
                    .setItalic())
                    .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT);
            // Añadir contenido principal
            document.add(new Paragraph(content)
                    .setMarginTop(20)
                    .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER));

        } finally {
            document.close();
        }

        return baos.toByteArray();
    }
}