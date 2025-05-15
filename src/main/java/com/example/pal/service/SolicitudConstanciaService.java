package com.example.pal.service;

import com.example.pal.dto.SolicitudConstanciaDTO;
import com.example.pal.model.SolicitudConstancia;
import com.example.pal.repository.SolicitudConstanciaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SolicitudConstanciaService {

    private final SolicitudConstanciaRepository repository;
    private final PDFService pdfService;
    private final EmailService emailService;

    @Transactional
    public SolicitudConstancia registrarSolicitud(SolicitudConstanciaDTO dto) throws Exception {
        SolicitudConstancia solicitud = new SolicitudConstancia();
        solicitud.setNombre(dto.getNombre());
        solicitud.setMatricula(dto.getMatricula());
        solicitud.setCorreo(dto.getCorreo());
        solicitud.setTipoConstancia(dto.getTipoConstancia());
        solicitud.setFechaSolicitud(LocalDateTime.now());
        solicitud.setEstado("ENVIADA");

        SolicitudConstancia saved = repository.save(solicitud);

        // Generar PDF personalizado
        String contenido = "Constancia para: " + saved.getNombre() + "\nMatrícula: " + saved.getMatricula() + "\nTipo: " + saved.getTipoConstancia();
        byte[] pdf = pdfService.generateSimplePdf("Constancia Académica", contenido);

        // Enviar correo con PDF adjunto
        String cuerpo = "Hola " + saved.getNombre() + ",\nAdjuntamos tu constancia académica solicitada.";
        emailService.sendEmailWithAttachment(saved.getCorreo(), "Tu constancia académica", cuerpo, pdf, "constancia.pdf");

        return saved;
    }

    public List<SolicitudConstancia> obtenerSolicitudes() {
        return repository.findAll();
    }
}