package com.example.pal.controller;

import com.example.pal.service.EmailService;
import com.example.pal.service.PDFService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private PDFService pdfService;

    public EmailController() {
    }

    @GetMapping("/send-email")
    public String sendEmail(@RequestParam String to, @RequestParam String subject, @RequestParam String text) {
        try {
            this.emailService.sendSimpleEmail(to, subject, text);
            return "Correo enviado exitosamente a: " + to;
        } catch (Exception e) {
            return "Error al enviar el correo: " + e.getMessage();
        }
    }

    @GetMapping("/send-email-pdf")
    public String sendPdfEmail(@RequestParam String to, @RequestParam String subject,
            @RequestParam String emailText, @RequestParam String titlePdf,
            @RequestParam String contentPdf) {

        try {
            // Generar PDF
            byte[] pdfBytes = pdfService.generateSimplePdf(titlePdf, contentPdf);

            // Enviar email con PDF adjunto
            emailService.sendEmailWithAttachment(to, subject, emailText, pdfBytes, "documento.pdf");

            return "Correo con PDF adjunto enviado exitosamente a: " + to;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al enviar el correo con PDF: " + e.getMessage();
        }
    }

    // Endpoint adicional para descargar el PDF directamente (para pruebas)
    @GetMapping("/generate-pdf")
    public ResponseEntity<byte[]> generatePdf(@RequestParam String title, @RequestParam String content) {
        try {
            byte[] pdfBytes = pdfService.generateSimplePdf(title, content);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=documento.pdf");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
