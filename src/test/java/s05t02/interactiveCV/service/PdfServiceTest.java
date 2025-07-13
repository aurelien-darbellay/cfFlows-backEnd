package s05t02.interactiveCV.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import s05t02.interactiveCV.model.documents.cv.InteractiveCv;
import s05t02.interactiveCV.service.pdfService.PdfService;
import s05t02.interactiveCV.testClasses.DocumentFactory;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PdfServiceTest {

    private PdfService pdfService;

    @BeforeEach
    void setup() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/"); // assumes templates are in src/test/resources/templates/
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML");
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setCacheable(false); // disable cache for test visibility

        // Create the template engine manually
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        // Inject into PdfService (assuming constructor injection)
        pdfService = new PdfService(templateEngine);
    }

    @Test
    void testGenerateHtmlAndSaveToFile() throws Exception {
        // Arrange: Build a sample InteractiveCv (you can improve this with real test data)
        InteractiveCv sampleCv = DocumentFactory.populatedInteractiveCv("test");

        // Act: Generate HTML
        String html = pdfService.generateHtml(sampleCv.projectDocument());

        // Ensure output folder exists
        Files.createDirectories(Paths.get("test-output"));

        // Save to file
        File outputFile = new File("test-output/generated-cv.html");
        try (FileWriter writer = new FileWriter(outputFile)) {
            writer.write(html);
        }

        // Assert: File exists and is not empty
        assertTrue(outputFile.exists());
        assertTrue(outputFile.length() > 0);
    }

    @Test
    void generatePdfAndSaveToFile() throws Exception {
        InteractiveCv sampleCv = DocumentFactory.populatedInteractiveCv("test");
        byte[] pdfBytes = pdfService.generatePdf(sampleCv);
        Files.createDirectories(Paths.get("test-output"));
        Path path = Paths.get("test-output/generated-cv.pdf");
        Files.write(path, pdfBytes);
        assertTrue(Files.exists(path));
    }
}
