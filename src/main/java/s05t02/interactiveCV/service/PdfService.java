package s05t02.interactiveCV.service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import s05t02.interactiveCV.model.documents.InteractiveDocument;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
        String html = generateHtml(document.projectDocument());  // Get rendered HTML

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.withHtmlContent(html, null); // HTML content
        builder.toStream(out);               // Output stream (PDF goes here)
        builder.run();                       // Convert HTML → PDF
        return out.toByteArray();  // return the PDF as byte array
    }

    public byte[] printDocumentToPdf(InteractiveDocument document) throws IOException {
        byte[] pdfBytes = generatePdf(document);
        Files.createDirectories(Paths.get("test-output"));
        Path path = Paths.get("test-output/generated-cv.pdf");
        Files.write(path, pdfBytes);
        // Act: Generate HTML
        String html = generateHtml(document);
        // Save to file
        File outputFile = new File("test-output/generated-cv.html");
        try (FileWriter writer = new FileWriter(outputFile)) {
            writer.write(html);
        }
        return pdfBytes;
    }
}

