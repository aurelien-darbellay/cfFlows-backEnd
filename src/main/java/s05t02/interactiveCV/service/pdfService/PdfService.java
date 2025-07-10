package s05t02.interactiveCV.service.pdfService;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import s05t02.interactiveCV.model.documents.InteractiveDocument;
import s05t02.interactiveCV.model.documents.WithProfilePicture;

import java.io.ByteArrayOutputStream;

@Service
@RequiredArgsConstructor
public class PdfService {

    private final TemplateEngine templateEngine;

    public String generateHtml(InteractiveDocument document) throws Exception {
        Context context = new Context();
        InteractiveDocument processedDoc;
        if (document instanceof WithProfilePicture)
            processedDoc = Utils.processImageInDoc((WithProfilePicture) document);
        else processedDoc = document;
        context.setVariable(processedDoc.getHtmlTargetCoordinate().getVariableName(), processedDoc);
        return templateEngine.process(processedDoc.getHtmlTargetCoordinate().getTemplateName(), context); // renders cv-template.html
    }

    public byte[] generatePdf(InteractiveDocument document) throws Exception {
        String html = generateHtml(document.projectDocument());  // Get rendered HTML

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfRendererBuilder builder = new PdfRendererBuilder();
        Utils.registerPoppinsFonts(builder);
        builder.withHtmlContent(html, null); // HTML content
        builder.toStream(out);               // Output stream (PDF goes here)
        builder.run();                       // Convert HTML → PDF
        return out.toByteArray();  // return the PDF as byte array
    }

    public byte[] printDocumentToPdf(InteractiveDocument document) throws Exception {
        /*Files.createDirectories(Paths.get("test-output"));
        Path path = Paths.get("test-output/generated-cv.pdf");
        Files.write(path, pdfBytes);*/

        return generatePdf(document);
    }
}

