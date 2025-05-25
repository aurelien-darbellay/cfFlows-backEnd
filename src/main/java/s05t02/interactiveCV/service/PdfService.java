package s05t02.interactiveCV.service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import s05t02.interactiveCV.exception.IllegalDocumentTypeException;
import s05t02.interactiveCV.model.documents.InteractiveDocument;
import s05t02.interactiveCV.model.documents.cv.InteractiveCv;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PdfService {

    private final TemplateEngine templateEngine;

    private String generateHtml(InteractiveDocument document, Class<? extends InteractiveDocument> clazz) throws IllegalDocumentTypeException {
        return switch (clazz.getSimpleName()) {
            case "InteractiveCv" -> generateCvHtml((InteractiveCv) document);
            default -> throw new IllegalDocumentTypeException();
        };
    }

    public String generateCvHtml(InteractiveCv interactiveCv) {
        Context context = new Context();
        context.setVariable("cv", interactiveCv);
        return templateEngine.process("cv-template", context); // renders cv-template.html
    }

    public byte[] generatePdf(InteractiveDocument document, Class<? extends InteractiveDocument> clazz) throws IllegalDocumentTypeException, IOException {
        String html = generateHtml(document, clazz);  // Get rendered HTML

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.withHtmlContent(html, null); // HTML content
        builder.toStream(out);               // Output stream (PDF goes here)
        builder.run();                       // Convert HTML → PDF
        return out.toByteArray();  // return the PDF as byte array
    }
}

