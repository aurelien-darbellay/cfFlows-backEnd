package s05t02.interactiveCV.service.pdfService;

import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UtilsTest {
    @Test
    void fetchImageFromUrl_withValidUrl_returnsImage() throws Exception {
        // Example: Use a known small public PNG
        String testUrl = "https://res.cloudinary.com/dhll1igfz/image/upload/v1752067184/robert/WIN_20240919_11_45_22_Pro.jpg.jpg";

        BufferedImage image = Utils.fetchImageFromUrl(testUrl);

        assertNotNull(image, "Image should be loaded");
        assertTrue(image.getWidth() > 0);
        assertTrue(image.getHeight() > 0);
    }
}