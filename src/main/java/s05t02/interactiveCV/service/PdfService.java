package s05t02.interactiveCV.service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import s05t02.interactiveCV.model.documents.InteractiveDocument;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PdfService {

    private final TemplateEngine templateEngine;

    public String generateHtml(InteractiveDocument document) {
        Context context = new Context();
        context.setVariable(document.getHtmlTargetCoordinate().getVariableName(), document);
        return templateEngine.process(document.getHtmlTargetCoordinate().getTemplateName(), context); // renders cv-template.html
    }

    public byte[] generatePdf(InteractiveDocument document) throws IOException {
        String html = generateHtml(document);  // Get rendered HTML

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.withHtmlContent(html, null); // HTML content
        builder.toStream(out);               // Output stream (PDF goes here)
        builder.run();                       // Convert HTML → PDF
        return out.toByteArray();  // return the PDF as byte array
    }
}

